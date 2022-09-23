package com.ironhack.vbnk_dataservice.services.impl;


import com.ironhack.vbnk_dataservice.data.AccountState;
import com.ironhack.vbnk_dataservice.data.dao.accounts.CheckingAccount;
import com.ironhack.vbnk_dataservice.data.dao.accounts.CreditAccount;
import com.ironhack.vbnk_dataservice.data.dao.accounts.SavingsAccount;
import com.ironhack.vbnk_dataservice.data.dao.accounts.StudentCheckingAccount;
import com.ironhack.vbnk_dataservice.data.dao.users.AccountHolder;
import com.ironhack.vbnk_dataservice.data.dao.users.VBAdmin;
import com.ironhack.vbnk_dataservice.data.dto.accounts.*;
import com.ironhack.vbnk_dataservice.data.dto.users.AccountHolderDTO;
import com.ironhack.vbnk_dataservice.data.dto.users.AdminDTO;
import com.ironhack.vbnk_dataservice.data.http.request.*;
import com.ironhack.vbnk_dataservice.data.http.views.StatementView;
import com.ironhack.vbnk_dataservice.repositories.accounts.CheckingAccountRepository;
import com.ironhack.vbnk_dataservice.repositories.accounts.CreditAccountRepository;
import com.ironhack.vbnk_dataservice.repositories.accounts.SavingsAccountRepository;
import com.ironhack.vbnk_dataservice.repositories.accounts.StudentCheckingAccountRepository;
import com.ironhack.vbnk_dataservice.services.VBAccountService;
import com.ironhack.vbnk_dataservice.services.VBUserService;
import com.ironhack.vbnk_dataservice.utils.VBNKConfig;
import org.apache.http.client.HttpResponseException;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.naming.ServiceUnavailableException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.ironhack.vbnk_dataservice.utils.VBNKConfig.*;
import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT;

@Service
@EnableEurekaClient
public class VBAccountServiceImpl implements VBAccountService {

    private static final String[] TRANSACTION_SERVICE = new String[]{"vbnk-transaction-service", "/v1/trans/main/ping"};
    final private SavingsAccountRepository savingsAccountRepository;
    final private CheckingAccountRepository checkingRepository;
    final private CreditAccountRepository creditRepository;
    final private StudentCheckingAccountRepository studentRepository;
    final private VBUserService userService;
    final private DiscoveryClient discoveryClient;
    private WebClient client;

    public VBAccountServiceImpl(
            SavingsAccountRepository savingsAccountRepository,
            CheckingAccountRepository checkingRepository,
            CreditAccountRepository creditRepository,
            StudentCheckingAccountRepository studentRepository, VBUserService userService, DiscoveryClient discoveryClient) {
//        this.repository = repository;
        this.savingsAccountRepository = savingsAccountRepository;
        this.checkingRepository = checkingRepository;
        this.creditRepository = creditRepository;
        this.studentRepository = studentRepository;
        this.userService = userService;
        this.discoveryClient = discoveryClient;
    }

    @Override
    public AccountDTO getAccount(String ref) throws HttpResponseException {
        if (ref.contains(VBNK_INTERNATIONAL_CODE + VBNK_ENTITY_CODE)) {
            if (checkingRepository.existsByAccountNumber(ref))
                return CheckingDTO.fromEntity(checkingRepository.findByAccountNumber(ref)
                        .orElseThrow(() -> new HttpResponseException(404, "FATAL ERR")));
            if (savingsAccountRepository.existsByAccountNumber(ref))
                return SavingsDTO.fromEntity(savingsAccountRepository.findByAccountNumber(ref)
                        .orElseThrow(() -> new HttpResponseException(404, "FATAL ERR")));
            if (creditRepository.existsByAccountNumber(ref))
                return CreditDTO.fromEntity(creditRepository.findByAccountNumber(ref)
                        .orElseThrow(() -> new HttpResponseException(404, "FATAL ERR")));
            if (studentRepository.existsByAccountNumber(ref))
                return StudentCheckingDTO.fromEntity(studentRepository.findByAccountNumber(ref)
                        .orElseThrow(() -> new HttpResponseException(404, "FATAL ERR")));
            else throw new HttpResponseException(404, "ID NOK");
        }
//        return AccountDTO.fromAnyAccountEntity(repository.findById(ref).orElseThrow());
        if (checkingRepository.existsById(ref)) return CheckingDTO.fromEntity(checkingRepository.findById(ref)
                .orElseThrow(() -> new HttpResponseException(404, "FATAL ERR")));
        if (savingsAccountRepository.existsById(ref))
            return SavingsDTO.fromEntity(savingsAccountRepository.findById(ref)
                    .orElseThrow(() -> new HttpResponseException(404, "FATAL ERR")));
        if (creditRepository.existsById(ref)) return CreditDTO.fromEntity(creditRepository.findById(ref)
                .orElseThrow(() -> new HttpResponseException(404, "FATAL ERR")));
        if (studentRepository.existsById(ref)) return StudentCheckingDTO.fromEntity(studentRepository.findById(ref)
                .orElseThrow(() -> new HttpResponseException(404, "FATAL ERR")));
        else throw new HttpResponseException(404, "ID NOK");
    }

    @Override
    public List<AccountDTO> getAllUserAccounts(String userId) {
        List<AccountDTO> primary = checkingRepository.findAllByPrimaryOwnerId(userId)
                .stream().map(AccountDTO::fromAnyAccountEntity).collect(Collectors.toCollection(ArrayList::new));
        primary.addAll(checkingRepository.findAllBySecondaryOwnerId(userId)
                .stream().map(AccountDTO::fromAnyAccountEntity).collect(Collectors.toCollection(ArrayList::new)));
        primary.addAll(savingsAccountRepository.findAllByPrimaryOwnerId(userId)
                .stream().map(AccountDTO::fromAnyAccountEntity).collect(Collectors.toCollection(ArrayList::new)));
        primary.addAll(savingsAccountRepository.findAllBySecondaryOwnerId(userId)
                .stream().map(AccountDTO::fromAnyAccountEntity).collect(Collectors.toCollection(ArrayList::new)));
        primary.addAll(creditRepository.findAllByPrimaryOwnerId(userId)
                .stream().map(AccountDTO::fromAnyAccountEntity).collect(Collectors.toCollection(ArrayList::new)));
        primary.addAll(creditRepository.findAllBySecondaryOwnerId(userId)
                .stream().map(AccountDTO::fromAnyAccountEntity).collect(Collectors.toCollection(ArrayList::new)));
        primary.addAll(studentRepository.findAllByPrimaryOwnerId(userId)
                .stream().map(AccountDTO::fromAnyAccountEntity).collect(Collectors.toCollection(ArrayList::new)));
        primary.addAll(studentRepository.findAllBySecondaryOwnerId(userId)
                .stream().map(AccountDTO::fromAnyAccountEntity).collect(Collectors.toCollection(ArrayList::new)));
        return primary;
    }

    @Override
    public AccountDTO create(NewAccountRequest request) throws HttpResponseException {
        var owner = userService.getAccountHolder(request.getPrimaryOwner());
        if (owner == null) throw new HttpResponseException(404, "Wrong User Id");
        if (request.getCurrency() == null) request.setCurrency(VBNK_CURRENCY_DEF);
        var admin = (request.getAdministratedBy() == null
                || request.getAdministratedBy().equals("")
                || request.getAdministratedBy().equals(" ")) ?
                userService.getRandomAdmin()
                : userService.getAdmin(request.getAdministratedBy());
        if (admin == null) throw new HttpResponseException(404, "Wrong Admin Id");
        AccountHolderDTO sOwner = null;
        try {
            sOwner = userService.getAccountHolder(request.getSecondaryOwner());
        } catch (Throwable ignored) {
        }
        request.setPrimaryOwner(owner.getId()).setAdministratedBy(admin.getId());
        switch (request.getClass().getSimpleName()) {
            case "NewCheckingAccountRequest" -> {
                return (owner.getDateOfBirth().plusYears(24).isAfter(LocalDate.now())) ?
                        StudentCheckingDTO.fromEntity(
                                studentRepository.save(
                                        StudentCheckingAccount.fromDTO(
                                                StudentCheckingDTO.fromRequest((NewCheckingAccountRequest) request,
                                                        AccountHolder.fromDTO(owner),
                                                        sOwner == null ? null : AccountHolder.fromDTO(sOwner),
                                                        VBAdmin.fromDTO(admin),
                                                        createAccountNumber(owner, admin)))))
                        :
                        CheckingDTO.fromEntity(checkingRepository.save(
                                CheckingAccount.fromDTO(
                                        CheckingDTO.fromRequest((NewCheckingAccountRequest) request,
                                                AccountHolder.fromDTO(owner),
                                                sOwner == null ? null : AccountHolder.fromDTO(sOwner),
                                                VBAdmin.fromDTO(admin),
                                                createAccountNumber(owner, admin)))));
            }

            case "NewSavingsAccountRequest" -> {
                return SavingsDTO.fromEntity(
                        savingsAccountRepository.save(SavingsAccount.fromDTO(
                                SavingsDTO.fromRequest((NewSavingsAccountRequest) request,
                                        AccountHolder.fromDTO(owner),
                                        sOwner == null ? null : AccountHolder.fromDTO(sOwner),
                                        VBAdmin.fromDTO(admin),
                                        createAccountNumber(owner, admin)))));
            }
            case "NewCreditAccountRequest" -> {
                request.setInitialAmount(((NewCreditAccountRequest)request).getCreditLimit());
                return CreditDTO.fromEntity(
                    creditRepository.save(CreditAccount.fromDTO(
                            CreditDTO.fromRequest((NewCreditAccountRequest) request,
                                    AccountHolder.fromDTO(owner),
                                    sOwner == null ? null : AccountHolder.fromDTO(sOwner),
                                    VBAdmin.fromDTO(admin),
                                    createAccountNumber(owner, admin)))));
            }
            default -> throw new HttpResponseException(I_AM_A_TEAPOT.value(), I_AM_A_TEAPOT.getReasonPhrase());
        }
    }

    @Override
    public AccountDTO update(AccountDTO dto, String id) throws HttpResponseException {
        AccountDTO original = getAccount(id);
        if (dto.getAmount() != null) original.setAmount(dto.getAmount());
        if (dto.getPrimaryOwner() != null) original.setPrimaryOwner(dto.getPrimaryOwner());
        if (dto.getSecondaryOwner() != null) original.setSecondaryOwner(dto.getSecondaryOwner());
        if (dto.getAdministratedBy() != null) original.setAdministratedBy(dto.getAdministratedBy());
        if (dto.getState() != null) original.setState(dto.getState());
        if (dto.getSecretKey() != null) original.setSecretKey(dto.getSecretKey());
        if (dto instanceof CheckingDTO espDto) {
            var save = (CheckingDTO) original;
            if (espDto.getMinimumBalance() != null) (save).setMinimumBalance(espDto.getMinimumBalance());
            if (espDto.getPenaltyFee() != null) (save).setPenaltyFee(espDto.getPenaltyFee());
            if (espDto.getMonthlyMaintenanceFee() != null)
                (save).setMonthlyMaintenanceFee(espDto.getMonthlyMaintenanceFee());
            return AccountDTO.fromAnyAccountEntity(checkingRepository.save(CheckingAccount.fromDTO(save)));
        }
        if (dto instanceof SavingsDTO espDto) {
            var save = (SavingsDTO) original;
            if (espDto.getMinimumBalance() != null) (save).setMinimumBalance(espDto.getMinimumBalance());
            if (espDto.getPenaltyFee() != null) (save).setPenaltyFee(espDto.getPenaltyFee());
            if (espDto.getInterestRate() != null) (save).setInterestRate(espDto.getInterestRate());
            return AccountDTO.fromAnyAccountEntity(savingsAccountRepository.save(SavingsAccount.fromDTO(save)));
        }
        if (dto instanceof CreditDTO espDto) {
            var save = (CreditDTO) original;
            if (espDto.getCreditLimit() != null) (save).setCreditLimit(espDto.getCreditLimit());
            if (espDto.getInterestRate() != null) (save).setInterestRate(espDto.getInterestRate());
            return AccountDTO.fromAnyAccountEntity(creditRepository.save(CreditAccount.fromDTO(save)));
        }
        if (dto instanceof StudentCheckingDTO) {
            var save = (StudentCheckingDTO) original;
            return AccountDTO.fromAnyAccountEntity(studentRepository.save(StudentCheckingAccount.fromDTO(save)));
        }
        throw new HttpResponseException(I_AM_A_TEAPOT.value(), I_AM_A_TEAPOT.getReasonPhrase());
    }

    @Override
    public void delete(String id) {
        if (checkingRepository.existsById(id)) checkingRepository.deleteById(id);
        else if (savingsAccountRepository.existsById(id)) savingsAccountRepository.deleteById(id);
        else if (creditRepository.existsById(id)) creditRepository.deleteById(id);
        else if (studentRepository.existsById(id)) studentRepository.deleteById(id);
    }

    @Override
    public boolean exist(String destinationAccountRef) {
        return checkingRepository.existsById(destinationAccountRef)
                || checkingRepository.existsByAccountNumber(destinationAccountRef)
                || savingsAccountRepository.existsById(destinationAccountRef)
                || savingsAccountRepository.existsByAccountNumber(destinationAccountRef)
                || creditRepository.existsById(destinationAccountRef)
                || creditRepository.existsByAccountNumber(destinationAccountRef)
                || studentRepository.existsById(destinationAccountRef)
                || studentRepository.existsByAccountNumber(destinationAccountRef);

    }

    @Override
    public boolean isOwnedBy(AccountDTO acc, String userID) {
        return acc.getPrimaryOwner().getId().equalsIgnoreCase(userID)
                || (acc.getSecondaryOwner()!=null&&acc.getSecondaryOwner().getId().equalsIgnoreCase(userID));
    }

    @Override
    public boolean isOwnedBy(String accID, String userID) throws HttpResponseException {
        var acc = getAccount(accID);
        return acc.getPrimaryOwner().getId().equalsIgnoreCase(userID)
                || acc.getSecondaryOwner().getId().equalsIgnoreCase(userID);
    }

    @Override
    public StatementView[] getStatements(int i, String accountRef, Authentication auth) throws ServiceUnavailableException, HttpResponseException {
        try {
            client = checkClientAvailable(TRANSACTION_SERVICE, client);
        } catch (ServiceUnavailableException e) {
            throw new ServiceUnavailableException();
        }
        String accID="";
        if (accountRef.substring(0, 8).equalsIgnoreCase(VBNK_INTERNATIONAL_CODE + VBNK_ENTITY_CODE))
            accID = getAccount(accountRef).getId();
        else{
            accID=accountRef;
            accountRef=getAccount(accountRef).getAccountNumber();
        }
        StatementView[] res,res2;
        res = client.post()
                .uri("/v1/trans/main/statements/0")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + VBNKConfig.getTokenFromAuth(auth))
                .body(Mono.just(accountRef), String.class)
                .retrieve().bodyToMono(StatementView[].class)
                .block();
        res2 = client.post()
                .uri("/v1/trans/main/statements/0")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + VBNKConfig.getTokenFromAuth(auth))
                .body(Mono.just(accID), String.class)
                .retrieve().bodyToMono(StatementView[].class)
                .block();
        var list= List.of(res);
        Collections.addAll(list, res2);
        return list.toArray(new StatementView[0]);
    }

    @Override
    public void bankUpdateAccounts(String userID) throws HttpResponseException, ServiceUnavailableException {
        client = checkClientAvailable(TRANSACTION_SERVICE, client);
        var accList = getAllUserAccounts(userID);
        for (var acc : accList) {
            if (acc instanceof CreditDTO) {
                //---------------------------------------Apply Monthly interests
                if (acc.getLastBankUpdate().plus(1, ChronoUnit.MONTHS).isBefore(Instant.now()))
                    if (acc.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                        BigDecimal interest = (((CreditDTO) acc).getInterestRate()
                                .divide(BigDecimal.valueOf(12), RoundingMode.UP))
                                .multiply(((CreditDTO) acc).getCreditLimit()
                                        .subtract(acc.getAmount()));
                        creditRepository.save(
                                CreditAccount.fromDTO((CreditDTO) acc.setLastBankUpdate(Instant.now())
                                        .setAmount(acc.getAmount().subtract(
                                                interest))));
                        var res= client.post()
                                .uri("/v1/trans/client/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(Mono.just(new UpdateTransactionRequest(
                                        true,
                                        interest,
                                        acc.getAmount(),
                                        "Monthly used credit interests",
                                        acc.getId(),
                                        acc.getCurrency()
                                )), UpdateTransactionRequest.class).retrieve().toBodilessEntity();
                    }

            } else if (acc instanceof CheckingDTO) {
                //------------------------------------------------------Apply penaltyFee
                if (acc.getAmount().compareTo(VBNK_CHECKING_MIN_BALANCE.getAmount()) < 0) {
                    BigDecimal fee = ((CheckingDTO) acc).getPenaltyFee();
                    checkingRepository.save(
                            CheckingAccount.fromDTO((CheckingDTO) acc.setAmount(
                                    acc.getAmount().subtract(fee))));
                    var res= client.post()
                            .uri("/v1/trans/client/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(Mono.just(new UpdateTransactionRequest(
                                    true,
                                    fee,
                                    acc.getAmount(),
                                    "Monthly used credit interests",
                                    acc.getId(),
                                    acc.getCurrency()
                            )), UpdateTransactionRequest.class).retrieve().toBodilessEntity();
                }

            } else if (acc instanceof SavingsDTO) {
                //----------------------------------------------------Apply penaltyFee
                if(((SavingsDTO) acc).getMinimumBalance()==null)
                    ((SavingsDTO) acc).setMinimumBalance(new BigDecimal(VBNK_MAX_SAVINGS_MINIMUM_BALANCE));

                if (acc.getAmount().compareTo(((SavingsDTO) acc).getMinimumBalance()) < 0) {
                    BigDecimal fee = ((SavingsDTO) acc).getPenaltyFee();
                    savingsAccountRepository.save(
                            SavingsAccount.fromDTO((SavingsDTO) acc.setAmount(
                                    acc.getAmount().subtract(fee))));
                    var res=client.post()
                            .uri("/v1/trans/client/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(Mono.just(new UpdateTransactionRequest(
                                    true,
                                    fee,
                                    acc.getAmount(),
                                    "Monthly used credit interests",
                                    acc.getId(),
                                    acc.getCurrency()
                            )), UpdateTransactionRequest.class).retrieve().toBodilessEntity();
                }
                //-------------------------------------------------------Apply annual interests
                if (acc.getLastBankUpdate().plus(1, ChronoUnit.YEARS).isBefore(Instant.now())) {
                    BigDecimal interest = acc.getAmount()
                            .multiply(((SavingsDTO) acc).getInterestRate());
                    savingsAccountRepository.save(
                            SavingsAccount.fromDTO((SavingsDTO) acc.setLastBankUpdate(Instant.now())
                                    .setAmount(acc.getAmount()
                                            .add(interest))));
                    var res= client.post()
                            .uri("/v1/trans/client/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(Mono.just(new UpdateTransactionRequest(
                                    false,
                                    interest,
                                    acc.getAmount(),
                                    "Annual savings interests",
                                    acc.getId(),
                                    acc.getCurrency()

                            )), UpdateTransactionRequest.class).retrieve().toBodilessEntity();
                }
            }
        }
    }

    private String createAccountNumber(AccountHolderDTO pOwner, AdminDTO admin) {
        Random rand = new Random();
        StringBuilder userNumbers = new StringBuilder();
        for (Character num : getNumbersFromId(pOwner.getId())) userNumbers.append(num);
        int val = rand.nextInt(6, userNumbers.toString().length());
        var accountNumber = userNumbers.toString().substring(val - 6, val);
        userNumbers = new StringBuilder();
        for (Character num : getNumbersFromId(admin.getId())) userNumbers.append(num);
        val = rand.nextInt(4, userNumbers.toString().length());
        accountNumber += userNumbers.substring(val - 4, val);
        String securityCode = ((pOwner.getDateOfBirth().getDayOfYear() + 10) + " ").substring(0, 2);
        String IBANNumber = VBNK_INTERNATIONAL_CODE + VBNK_ENTITY_CODE + securityCode + accountNumber;
        if (exist(IBANNumber)) return createAccountNumber(pOwner, admin);
        return IBANNumber;
    }
    @Override
    public WebClient getTransactionClient() throws ServiceUnavailableException {
        return client=checkClientAvailable(TRANSACTION_SERVICE,client);
    }

    @Override
    public String toggleFreezeAccount(String accountRef) throws HttpResponseException {
        var acc= getAccount(accountRef);
        if(acc!=null) {
            if(acc.getState().equals(AccountState.ACTIVE)) acc.setState(AccountState.FROZEN);
            else if(acc.getState().equals(AccountState.FROZEN)) acc.setState(AccountState.ACTIVE);
            else throw new HttpResponseException(I_AM_A_TEAPOT.value(), I_AM_A_TEAPOT.getReasonPhrase());
            if(acc instanceof CheckingDTO) return checkingRepository.save(CheckingAccount.fromDTO((CheckingDTO)acc)).getState().name();
            else if(acc instanceof SavingsDTO) savingsAccountRepository.save(SavingsAccount.fromDTO((SavingsDTO)acc)).getState().name();
            else if(acc instanceof CreditDTO) creditRepository.save(CreditAccount.fromDTO((CreditDTO)acc)).getState().name();
            else if(acc instanceof StudentCheckingDTO) studentRepository.save(StudentCheckingAccount.fromDTO((StudentCheckingDTO)acc)).getState().name();
        }
        throw new HttpResponseException(I_AM_A_TEAPOT.value(), I_AM_A_TEAPOT.getReasonPhrase());

    }

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
