package com.ironhack.vbnk_transactionservice.data.http.request;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;

@Getter @Hidden
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionRequest {
    private Currency currency;
    private boolean isCharge;
    private BigDecimal amount,balance;
    private String concept;
    private String accountId;




}
