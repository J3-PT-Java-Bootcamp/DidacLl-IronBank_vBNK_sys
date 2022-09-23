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
@Schema(name = "New Savings Account Request")
@Tag(name = "HTTP Requests")
public class NewSavingsAccountRequest extends NewAccountRequest {
    @DecimalMax(value = "1000",message = "Minimum balance must be between 100 and 1000 ")
    @DecimalMin(value = "100",message = "Minimum balance must be between 100 and 1000 ")
    private  BigDecimal minimumBalance;
    @DecimalMax(value = "0.5",message = "Interest Rate must be less than 0.5")
    private  BigDecimal interestRate;
    
}
