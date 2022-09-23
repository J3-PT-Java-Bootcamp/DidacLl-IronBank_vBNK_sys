package com.ironhack.vbnk_dataservice.data.http.request;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;

@Setter @Getter @Hidden
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionRequest {
    private boolean isCharge;
    private BigDecimal amount,balance;
    private String concept;
    private String accountId;
    private Currency currency;

}
