package com.ironhack.vbnk_antifraudservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class AFTransaction {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;
    private BigDecimal amount;
    private String srcAccountNumber, senderId;
    @Embedded
    private AFResponse result;
    @CreationTimestamp
    @Column(updatable = false)
    private Instant transactionDate;

    public static AFTransaction fromDTO(AFTransactionDTO dto) {
        return new AFTransaction()
                .setTransactionDate(dto.getTransactionDate())
                .setAmount(dto.getAmount())
                .setSenderId(dto.getSenderId())
                .setSrcAccountNumber(dto.getSrcAccountNumber());
    }

    public int compareSimilarity(AFRequest request) {
        int val = 0;
        if (this.amount.equals(request.getAmount())) val++;
        if (this.srcAccountNumber.equals(request.getSrcAccountNumber())) val++;
        if (this.senderId.equals(request.getSenderId())) val++;
        if (this.transactionDate.plus(6, ChronoUnit.HOURS).isAfter(Instant.now())) val += 2;
        else if (this.transactionDate.plus(1, ChronoUnit.DAYS).isAfter(Instant.now())) val++;
        return (int) (val / 5F * 100);
    }

    public boolean compare(AFRequest request) {
        return this.amount.equals(request.getAmount())
                && this.srcAccountNumber.equals(request.getSrcAccountNumber())
                && this.senderId.equals(request.getSenderId())
                && this.transactionDate.plus(1, ChronoUnit.HOURS).isAfter(Instant.now());

    }
}
