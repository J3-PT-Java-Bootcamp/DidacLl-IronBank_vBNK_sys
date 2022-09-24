package com.ironhack.vbnk_transactionservice.services.impl;

import com.ironhack.vbnk_transactionservice.data.TransactionType;
import com.ironhack.vbnk_transactionservice.data.dao.VBTransaction;
import com.ironhack.vbnk_transactionservice.data.dto.TransactionDTO;
import com.ironhack.vbnk_transactionservice.data.http.request.AFRequest;
import com.ironhack.vbnk_transactionservice.data.http.request.NotificationRequest;
import com.ironhack.vbnk_transactionservice.data.http.request.TransferRequest;
import com.ironhack.vbnk_transactionservice.data.http.responses.AFResponse;
import com.ironhack.vbnk_transactionservice.data.http.responses.DataTransferResponse;
import com.ironhack.vbnk_transactionservice.data.http.responses.TransferResponse;
import com.ironhack.vbnk_transactionservice.data.http.views.StatementView;
import com.ironhack.vbnk_transactionservice.repositories.TransactionRepository;
import com.ironhack.vbnk_transactionservice.services.TransactionService;
import com.ironhack.vbnk_transactionservice.utils.Money;
import com.ironhack.vbnk_transactionservice.utils.VBError;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.naming.ServiceUnavailableException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ironhack.vbnk_transactionservice.data.TransactionState.*;
import static com.ironhack.vbnk_transactionservice.data.TransactionType.*;
import static com.ironhack.vbnk_transactionservice.utils.NotificationType.*;
import static com.ironhack.vbnk_transactionservice.utils.VBError.*;
import static com.ironhack.vbnk_transactionservice.utils.VBNKConfig.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

@Service
public class TransactionServiceImpl implements TransactionService {

    private WebClient dataClient;
    private WebClient antiFraudClient;
    private static final String[] DATA_SERVICE = new String[]{"vbnk-data-service", "/v1/data/client/test/ping"};
    private static final String[] ANTI_FRAUD_SERVICE = new String[]{"vbnk-anti-fraud-service", "/v1/af/client/test/ping"};
    private final DiscoveryClient discoveryClient;
    private final TransactionRepository repository;

    public TransactionServiceImpl(DiscoveryClient discoveryClient, TransactionRepository repository) {
        this.discoveryClient = discoveryClient;
        this.repository = repository;
    }


    @Override
    public ResponseEntity<TransactionDTO> getTransaction(String id) {
        return ResponseEntity.ok(TransactionDTO.fromEntity(repository.findById(id).orElseThrow()));
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    //-----------------------------------------------------------------------------------------------------INITIATE REQUESTS
    @Override
    public ResponseEntity<TransferResponse> initiateTransferRequest(Authentication authentication, TransferRequest request) throws ServiceUnavailableException {
        this.dataClient = checkClientAvailable(DATA_SERVICE, dataClient);
        this.antiFraudClient = checkClientAvailable(ANTI_FRAUD_SERVICE, antiFraudClient);
        var idToken = getIdTokenFromAuth(authentication);
        var accessToken = getTokenStringFromAuth(authentication);
        AFResponse afResponse = antiFraudValidation(request, accessToken);// antiFraudValidation(request, accessToken);
        var userId = getUserIdFromAuth(authentication);
        request.setOrderingUserId(userId);
        if (afResponse != null && afResponse.isAllValidated()) {
            if (afResponse.isAllOK()) {
                DataTransferResponse res = null;
                try {
                    res = dataClient.post()
                            .uri("/v1/data/main/tf/send")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken)
                            .body(Mono.just(request), TransferRequest.class)
                            .retrieve().bodyToMono(DataTransferResponse.class)
                            .block();
                } catch (Throwable err) {
                    res = null;
                }
                if (res == null) throw new HttpClientErrorException(BAD_REQUEST);
                createTransactionsFromTransfer(res);
                return ResponseEntity.ok(new TransferResponse().setState(OK));
            }
            return ResponseEntity.status(CONFLICT).body(fraudFail(authentication, request.getFromAccount(), afResponse.getFraudLevel()));
        }
        return ResponseEntity.status(BAD_REQUEST).body(new TransferResponse().setState(NOK).addErrors(FATAL_ERROR));
    }


    @Override
    public ResponseEntity<TransferResponse> initiatePaymentRequest(TransferRequest request) throws ServiceUnavailableException {

        try {
            var trans = createTransaction(new DataTransferResponse(request,
                    false,
                    false,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    null), PAYMENT_ORDER);
            sendNotificationToOwner(new NotificationRequest(
                    "Payment authorization",
                    "You've received a Payment Order from" + request.getSenderDisplayName()
                            + "for " + request.getAmount() + request.getCurrency().getDisplayName()
                            + ". Concept: " + request.getConcept() + ".\n\n Do you confirm to pay it?",
                    PAYMENT_CONFIRM.name(),
                    request.getFromAccount(),
                    trans.getId()
            ));
            return ResponseEntity.ok(new TransferResponse().setState(PENDING));
        } catch (Throwable err) {
            return ResponseEntity.badRequest().body(new TransferResponse().setState(NOK).addErrors(USER_NOT_FOUND));
        }
    }

    @Override
    public List<StatementView> getAccountStatements(int pag, String account) {
        try {
            var page = repository.findAllBySubjAccountOrderByModificationDesc(account, PageRequest.of(pag, 10));
            return page.stream().map(StatementView::fromEntity).collect(Collectors.toCollection(ArrayList::new));
        } catch (Throwable err) {
            return new ArrayList<>();
        }
    }

    @Override
    public ResponseEntity<TransferResponse> receiveBlindTransfer(Authentication authentication, TransferRequest request) throws ServiceUnavailableException {
        checkClientAvailable(DATA_SERVICE, dataClient);
        checkClientAvailable(ANTI_FRAUD_SERVICE, antiFraudClient);
        RefreshableKeycloakSecurityContext context = (RefreshableKeycloakSecurityContext) authentication.getCredentials();
        var accessToken = context.getTokenString();
        AFResponse afResponse = antiFraudValidation(request, accessToken);
        if (afResponse != null && afResponse.isAllValidated()) {
            if (afResponse.isAllOK()) {
                var res = dataClient.post()
                        .uri("/v1/data/client/tf/receive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getTokenStringFromAuth(authentication))
                        .body(Mono.just(request), TransferRequest.class)
                        .retrieve().bodyToMono(DataTransferResponse.class)
                        .block();
                if (res != null && res.isDestination() && (res.getErrors() == null || res.getErrors().isEmpty()))
                    createTransaction(res, CHARGE);
                return ResponseEntity.ok(new TransferResponse().setState(OK));
            }
            return ResponseEntity.status(CONFLICT).body(fraudFail(authentication, request.getFromAccount(), afResponse.getFraudLevel()));
        }
        return ResponseEntity.status(BAD_REQUEST).body(new TransferResponse().setState(NOK).addErrors(FATAL_ERROR));
    }

    //-------------------------------------------------------------------------------------------------------PRIVATE METHODS
    private void createTransactionsFromTransfer(DataTransferResponse res) throws ServiceUnavailableException {
        if (res != null && (res.getErrors() == null || res.getErrors().isEmpty())) {
            if (res.isSource() && res.getSrcBalance() != null) {
                createTransaction(res, INCOME);
            }
            if (res.isDestination()) {
                createTransaction(res, CHARGE);
            }
        }
    }

    @Override
    public TransactionDTO createTransaction(DataTransferResponse response, TransactionType type) throws ServiceUnavailableException {
        var request = response.getRequest();
        NotificationRequest notRequest = new NotificationRequest();
        switch (type) {

            case BANK_CHARGE: {
                notRequest.setTitle(VBNK_ENTITY_NAME + " charge in your account")
                        .setMessage("You have a new charge from " + VBNK_ENTITY_NAME + " in your account")
                        .setType(BANK_CHARGES_INTERESTS.name());
            }
            case CHARGE: {

                VBTransaction entity = new VBTransaction()
                        .setState(OK)
                        .setType(type)
                        .setRequest(request)
                        .setSubjAccount(request.getFromAccount())
                        .setCurrentAccountBalance(new Money(response.getSrcBalance(), request.getCurrency()))
                        .setExpirationDate(null);
                TransactionDTO transactionDTO = TransactionDTO.fromEntity(repository.save(entity));
                if (notRequest.getType() != null)
                    sendNotificationToOwner(notRequest.setTransactionId(transactionDTO.getId())
                            .setAccountRef(transactionDTO.getSubjAccount()));
                return transactionDTO;
            }
            case BANK_INCOME: {
                notRequest.setTitle(VBNK_ENTITY_NAME + " income in your account")
                        .setMessage("You have a new income from " + VBNK_ENTITY_NAME + " in your account")
                        .setType(BANK_CHARGES_INTERESTS.name());
            }
            case INCOME: {
                if (notRequest.getType() == null) notRequest.setTitle("New income in your account")
                        .setMessage("You have a new income from " + request.getSenderDisplayName() + " in your account")
                        .setType(INCOMING.name());

                TransactionDTO transactionDTO = TransactionDTO.fromEntity(repository.save(new VBTransaction()
                        .setState(OK)
                        .setType(type)
                        .setSubjAccount(request.getToAccount())
                        .setCurrentAccountBalance(new Money(response.getDstBalance(), request.getCurrency()))
                        .setRequest(request)
                        .setExpirationDate(null)
                ));
                sendNotificationToOwner(notRequest.setTransactionId(transactionDTO.getId())
                        .setAccountRef(transactionDTO.getSubjAccount()));
                return transactionDTO;
            }
            case PAYMENT_ORDER: {
                notRequest.setTitle(VBNK_ENTITY_NAME + " charge in your account")
                        .setMessage("You have a new charge from " + VBNK_ENTITY_NAME + " in your account")
                        .setType(PAYMENT_CONFIRM.name());

                TransactionDTO transactionDTO = TransactionDTO.fromEntity(repository.save(new VBTransaction()
                        .setState(PENDING)
                        .setType(type)
                        .setSubjAccount(request.getFromAccount())
                        .setCurrentAccountBalance(null)
                        .setRequest(request)
                        .setExpirationDate(Instant.now().plus(48L, ChronoUnit.HOURS))
                ));
                sendNotificationToOwner(notRequest.setTransactionId(transactionDTO.getId())
                        .setAccountRef(transactionDTO.getSubjAccount()));
                return transactionDTO;
            }

        }
        return null;
    }

    @Override
    public void checkPendingTransactions(String accountId) {
        var list = repository.findAllBySubjAccountAndState(accountId, PENDING);
        for (var trans : list) {
            if (trans.getExpirationDate().isAfter(Instant.now())) {
                repository.delete(trans);
            }
        }
    }

    private void sendNotificationToOwner(NotificationRequest request) throws ServiceUnavailableException {
        dataClient = checkClientAvailable(DATA_SERVICE, dataClient);
        try {
            var res = dataClient.post().uri("/v1/data/client/notifications")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(request), NotificationRequest.class)
                    .retrieve().bodyToMono(String.class)
                    .block();
        } catch (Throwable err) {
            System.err.println(err);
        }
    }

    private TransferResponse fraudFail(Authentication auth, String accRef, int fraudLevel) throws ServiceUnavailableException {
        var res = new TransferResponse().setState(NOK);
        VBError err = null;
        switch (fraudLevel) {
            case (3): {
                err = AF_3;
                toggleFreezeAccount(auth, accRef);
            }
            case (2): {
                if (err == null) err = AF_2;
                sendNotificationToOwner(new NotificationRequest().setType(FRAUD.name()).setTitle("Fraud Alert")
                        .setMessage("Following transaction needs to be confirmed by an admin")
                        .setTransactionId(null)
                        .setAccountRef(accRef));
            }
            case (1): {
                if (err == null) err = AF_1;
                return res.addErrors(err);
            }
            default: {
                return res;
            }
        }
    }

    private void toggleFreezeAccount(Authentication auth, String accRef) throws ServiceUnavailableException {
        dataClient = checkClientAvailable(DATA_SERVICE, dataClient);
        RefreshableKeycloakSecurityContext context = (RefreshableKeycloakSecurityContext) auth.getCredentials();
        var accessToken = context.getTokenString();

        try {
            var res = dataClient.get().uri("/v1/data/auth/state?ref=" + accRef)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve().bodyToMono(String.class)
                    .block();
        } catch (Throwable err) {
            System.err.println(err);
        }
    }

    private AFResponse antiFraudValidation(TransferRequest request, String accessToken) {
        var afResponse = antiFraudClient.patch()
                .uri("/v1/af/client/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(Mono.just(AFRequest.fromTransferRequest(request)), AFRequest.class)
                .retrieve().bodyToMono(AFResponse.class)
                .block();
        return afResponse;
    }

    //---------------------------------------------------------------------------------------------------------CLIENT CONFIG
    private WebClient checkClientAvailable(String[] service, WebClient webClient) throws ServiceUnavailableException {
        try {
            try {
                if (webClient == null) createClient(service[0]);
                if (webClient.get()
                        .uri(service[1])
                        .retrieve()
                        .bodyToMono(String.class)
                        .block()
                        != "pong") return createClient(service[0]);
            } catch (Exception e) {
                return createClient(service[0]);
            }
        } catch (Throwable err) {
            if (err instanceof ServiceUnavailableException) throw err;
        }
        return webClient;
    }

    private WebClient createClient(String service) throws ServiceUnavailableException {
        for (int i = 0; i < 3; i++) {
            try {
                var serviceInstanceList = discoveryClient.getInstances(service);
                String clientURI = serviceInstanceList.get(0).getUri().toString();
                return WebClient.create(clientURI);
            } catch (Throwable ignored) {
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {
            }
        }
        throw new ServiceUnavailableException();
    }

}
