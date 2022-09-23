package com.ironhack.vbnk_dataservice.data.dao;

import com.ironhack.vbnk_dataservice.data.NotificationState;
import com.ironhack.vbnk_dataservice.data.NotificationType;
import com.ironhack.vbnk_dataservice.data.dao.users.VBUser;
import com.ironhack.vbnk_dataservice.data.dto.NotificationDTO;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity @Hidden
@NoArgsConstructor
@Getter @Setter
public class Notification {
    @Id
    @GeneratedValue
    Long id;
    String title;
    String message;
    @Enumerated(EnumType.STRING)
    NotificationType type;
    @Enumerated(EnumType.STRING)
    NotificationState state;
    @ManyToOne(fetch = FetchType.LAZY)
    VBUser owner;
    @CreationTimestamp
    @Column(updatable = false)
    Instant creationDate;
    String transactionId;

    public final Notification fromDTO(NotificationDTO entity,VBUser owner) {
        return new Notification().setId(entity.getId())
                .setType(entity.getType())
                .setOwner(owner)
                .setState(entity.getState())
                .setMessage(entity.getMessage())
                .setTransactionId(entity.getTransactionId())
                .setTitle(entity.getTitle());
    }
}
