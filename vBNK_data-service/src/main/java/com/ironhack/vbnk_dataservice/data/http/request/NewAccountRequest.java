package com.ironhack.vbnk_dataservice.data.http.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public abstract class NewAccountRequest {
    @NotNull(message = "Initial Amount must not be null")
    private BigDecimal initialAmount;
    private String currency;
    @NotBlank(message = "Secret Key must not be null")
    private String secretKey;
    @NotBlank(message = "Primary Owner ID must not be null")
    private String primaryOwner;
    private String secondaryOwner;
    private String administratedBy;

}
