package com.ironhack.vbnk_transactionservice.data.http.views;

import com.ironhack.vbnk_transactionservice.data.TransactionType;
import com.ironhack.vbnk_transactionservice.data.dao.VBTransaction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
@Getter @Setter
@NoArgsConstructor
public class StatementView {
    BigDecimal amount;
    BigDecimal balance;
    Currency currency;
    Instant date;
    TransactionType type;
    String senderDisplayName;
    String concept;


    public static StatementView fromEntity(VBTransaction entity){
        return new StatementView()
                .setAmount(entity.getRequest().getAmount())
                .setBalance(entity.getCurrentAccountBalance().getAmount())
                .setCurrency(entity.getCurrentAccountBalance().getCurrency())
                .setDate(entity.getModification())
                .setType(entity.getType())
                .setSenderDisplayName(entity.getRequest().getSenderDisplayName())
                .setConcept(entity.getRequest().getConcept());

    }
}
