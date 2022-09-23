package com.ironhack.vbnk_dataservice.data.http.request;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Hidden
public class TransferRequest {

    private String fromAccount, orderingUserId, toAccount;
    @DecimalMin("0.01")
    private BigDecimal amount;
    private Currency currency;
    String senderDisplayName;
    String concept;
}
