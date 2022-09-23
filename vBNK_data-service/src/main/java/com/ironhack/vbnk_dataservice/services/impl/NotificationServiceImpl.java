package com.ironhack.vbnk_dataservice.services.impl;

import com.ironhack.vbnk_dataservice.data.NotificationState;
import com.ironhack.vbnk_dataservice.data.NotificationType;
import com.ironhack.vbnk_dataservice.data.dao.Notification;
import com.ironhack.vbnk_dataservice.data.dao.users.VBUser;
import com.ironhack.vbnk_dataservice.data.dto.NotificationDTO;
import com.ironhack.vbnk_dataservice.data.http.request.NotificationRequest;
import com.ironhack.vbnk_dataservice.repositories.NotificationRepository;
import com.ironhack.vbnk_dataservice.services.NotificationService;
import com.ironhack.vbnk_dataservice.services.VBAccountService;
import com.ironhack.vbnk_dataservice.services.VBUserService;
import org.apache.http.client.HttpResponseException;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.naming.ServiceUnavailableException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository repository;
    private final VBUserService userService;
    private final VBAccountService accountService;

    public NotificationServiceImpl(NotificationRepository repository, VBUserService userService, VBAccountService accountService) {
        this.repository = repository;
        this.userService = userService;
        this.accountService = accountService;
    }

    @Override
    public List<NotificationDTO> getAllPending(String userId) {
        return repository.findAllByOwnerIdAndState(userId, NotificationState.PENDING)
                .stream().map(NotificationDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<NotificationDTO> getIncomingNotifications(String userId) {
        return repository.findAllByOwnerIdAndTypeAndState(userId,
                        NotificationType.INCOMING,
                        NotificationState.PENDING)
                .stream().map(NotificationDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<NotificationDTO> getFraudNotifications(String userId) {
        List<Notification> all = repository.findAllByOwnerIdAndTypeAndState(userId,
                NotificationType.FRAUD,
                NotificationState.PENDING);
        return all.stream().map(NotificationDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<NotificationDTO> getPaymentNotifications(String userId) {
        return repository.findAllByOwnerIdAndTypeAndState(userId,
                        NotificationType.PAYMENT_CONFIRM,
                        NotificationState.PENDING)
                .stream().map(NotificationDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public NotificationDTO create(NotificationRequest request) throws HttpResponseException {
        String ref = request.getAccountRef();
        if(request.getType()==NotificationType.FRAUD.name()){
               ref=(accountService.getAccount(ref).getAdministratedBy().getId());
        }else {
            if (!userService.existsById(ref))
               ref=(accountService.getAccount(ref).getPrimaryOwner().getId());
        }
        Notification entity = new Notification().setType(NotificationType.valueOf(request.getType()))
                .setMessage(request.getMessage()).setTitle(request.getTitle())
                .setState(NotificationState.PENDING)
                .setOwner(VBUser.fromUnknownDTO(userService.getUnknown(ref)))
                .setTransactionId(request.getTransactionId());

        return NotificationDTO.fromEntity(repository.save(
                entity));
    }
    @Override
    public void bankUpdateNotification(String userId){
        var notifications= getAllPending(userId);
        for (NotificationDTO note: notifications){
            var exp= note.getType().getExpirationDays();
            if(exp==0)break;
            if(note.getCreationDate().plus(exp, ChronoUnit.DAYS).isBefore(Instant.now()))
                delete(note.getId());
        }

    }
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void confirmNotification(Authentication auth, Long id) throws ServiceUnavailableException {
        var notif= repository.findById(id).orElseThrow();
        RefreshableKeycloakSecurityContext context = (RefreshableKeycloakSecurityContext) auth.getCredentials();
        AccessToken accessToken = context.getToken();
        var owner = userService.getOwnerFromToken(accessToken,true);
        if(notif.getOwner().getId().equalsIgnoreCase(owner.getId())){
            accountService.getTransactionClient().post().uri("/v1/trans/main/cnf")
                    .header("Authorization","Bearer "+ context.getTokenString())
                    .body(Mono.just(notif.getTransactionId()),String.class)
                    .retrieve().toBodilessEntity()
                    .block();

            repository.deleteById(notif.getId());
        }

    }
}
