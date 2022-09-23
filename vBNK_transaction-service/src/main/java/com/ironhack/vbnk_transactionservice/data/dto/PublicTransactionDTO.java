package com.ironhack.vbnk_transactionservice.data.dto;

import com.ironhack.vbnk_transactionservice.data.TransactionState;
import com.ironhack.vbnk_transactionservice.data.TransactionType;
import com.ironhack.vbnk_transactionservice.data.dao.VBTransaction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@NoArgsConstructor
@Getter @Setter
public class PublicTransactionDTO {
//    String id;
    private TransactionType type;
    private TransactionState state;
    private String subjAccount;
    private BigDecimal amount;
    private BigDecimal balance;
    private Currency currency;
    private Instant date;
    private String senderDisplayName;
    private String concept;





    public static PublicTransactionDTO fromEntity(VBTransaction entity){
        return new PublicTransactionDTO()
                .setType(entity.getType())
                .setState(entity.getState())
                .setSubjAccount(entity.getSubjAccount())
                .setAmount(entity.getRequest().getAmount())
                .setBalance(entity.getCurrentAccountBalance().getAmount())
                .setCurrency(entity.getCurrentAccountBalance().getCurrency())
                .setDate(entity.getModification())
                .setSenderDisplayName(entity.getRequest().getSenderDisplayName())
                .setConcept(entity.getRequest().getConcept());

    }
}
