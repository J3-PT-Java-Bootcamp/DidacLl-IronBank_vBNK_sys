package com.ironhack.vbnk_transactionservice.data.dto;

import com.ironhack.vbnk_transactionservice.data.TransactionState;
import com.ironhack.vbnk_transactionservice.data.TransactionType;
import com.ironhack.vbnk_transactionservice.data.dao.VBTransaction;
import com.ironhack.vbnk_transactionservice.data.http.request.TransferRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@NoArgsConstructor
@Getter @Setter
public class TransactionDTO {
    private String id;
    private TransactionType type;
    private TransactionState state;
    private Instant expirationDate;
    private Instant updateDate;
    private String subjAccount;
    private Currency balanceCurrency;
    private BigDecimal balanceAmount;
    private TransferRequest request;




    public static TransactionDTO fromEntity(VBTransaction entity){
        TransactionDTO transactionDTO = new TransactionDTO().setId(entity.getId())
                .setType(entity.getType())
                .setUpdateDate(entity.getModification())
                .setState(entity.getState())
                .setExpirationDate(entity.getExpirationDate())
                .setSubjAccount(entity.getSubjAccount())
                .setRequest(entity.getRequest());
        if(entity.getCurrentAccountBalance()!=null)
            transactionDTO.setBalanceAmount(entity.getCurrentAccountBalance().getAmount())
                .setBalanceCurrency(entity.getCurrentAccountBalance().getCurrency());
        return transactionDTO;
    }
}
