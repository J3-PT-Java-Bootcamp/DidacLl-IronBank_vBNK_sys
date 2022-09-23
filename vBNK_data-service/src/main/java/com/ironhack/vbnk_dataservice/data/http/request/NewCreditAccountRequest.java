package com.ironhack.vbnk_dataservice.data.http.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@Schema(name = "New Credit Account Request")
@Tag(name = "HTTP Requests")
public class NewCreditAccountRequest extends NewAccountRequest {
    @DecimalMax(value = "100000.00",message = "Credit Limit must be between 100,000.00 and 100.00")
    @DecimalMin(value = "100",message = "Credit Limit must be between 100,000.00 and 100.00")
    private  BigDecimal creditLimit;
    @DecimalMax(value = "0.2",message = "Interest Rare must be between 0.2 and 0.1")
    @DecimalMin(value = "0.1",message = "Interest Rare must be between 0.2 and 0.1")
    private  BigDecimal interestRate;


}
