package com.ironhack.vbnk_dataservice.data;

import lombok.Getter;

@Getter
public enum NotificationType {
    INCOMING(2),
    FRAUD(0),
    PAYMENT_CONFIRM(2),
    BANK_CHARGES_INTERESTS(30);
    private final int expirationDays;

    NotificationType(int expirationDays) {
        this.expirationDays = expirationDays;
    }
}
