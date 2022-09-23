package com.ironhack.vbnk_dataservice.data.http.response;

import com.ironhack.vbnk_dataservice.data.http.request.TransferRequest;
import com.ironhack.vbnk_dataservice.utils.VBError;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Hidden
@Schema(hidden = true)
public class TransferResponse {

    private TransferRequest request;
    private boolean source, destination;
    private BigDecimal srcBalance,dstBalance;
    private List<VBError> errors;

}
