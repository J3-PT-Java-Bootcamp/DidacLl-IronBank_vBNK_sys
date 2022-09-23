package com.ironhack.vbnk_antifraudservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class AFTransactionDTO {
    private String id;
    private BigDecimal amount;
    private String srcAccountNumber, senderId;
    private Instant transactionDate;
    private AFResponse result;

    public static AFTransactionDTO fromEntity(AFTransaction entity) {
        return new AFTransactionDTO().setId(entity.getId())
                .setTransactionDate(entity.getTransactionDate())
                .setAmount(entity.getAmount())
                .setSenderId(entity.getSenderId())
                .setSrcAccountNumber(entity.getSrcAccountNumber())
                .setResult(entity.getResult());
    }
}
