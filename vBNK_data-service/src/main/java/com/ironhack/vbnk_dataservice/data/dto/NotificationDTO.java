package com.ironhack.vbnk_dataservice.data.dto;

import com.ironhack.vbnk_dataservice.data.NotificationState;
import com.ironhack.vbnk_dataservice.data.NotificationType;
import com.ironhack.vbnk_dataservice.data.dao.Notification;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
@Hidden
public class NotificationDTO {
    private Long id;
    private String title;
    private  String message;
    private NotificationType type;
    private  NotificationState state;
    private String ownerID;
    private Instant creationDate;
    String transactionId;


    public static NotificationDTO fromEntity(Notification entity) {
        return new NotificationDTO().setId(entity.getId())
                .setType(entity.getType())
                .setOwnerID(entity.getOwner().getId())
                .setState(entity.getState())
                .setMessage(entity.getMessage())
                .setTitle(entity.getTitle())
                .setTransactionId(entity.getTransactionId())
                .setCreationDate(entity.getCreationDate());
    }
}
