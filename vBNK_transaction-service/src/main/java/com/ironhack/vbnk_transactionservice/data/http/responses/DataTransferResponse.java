package com.ironhack.vbnk_transactionservice.data.http.responses;

import com.ironhack.vbnk_transactionservice.data.http.request.TransferRequest;
import com.ironhack.vbnk_transactionservice.data.http.request.UpdateTransactionRequest;
import com.ironhack.vbnk_transactionservice.utils.VBError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

import static com.ironhack.vbnk_transactionservice.utils.VBNKConfig.*;


@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataTransferResponse {

    private TransferRequest request;
    private boolean source, destination;
    private BigDecimal srcBalance,dstBalance;
    private List<VBError> errors;


    public static DataTransferResponse fromUpdateRequest(UpdateTransactionRequest request){
        return new DataTransferResponse(
                new TransferRequest(
                        request.isCharge()?request.getAccountId():VBNK_MAIN_ACCOUNT,
                        VBNK_ENTITY_CODE,
                        request.isCharge()?VBNK_MAIN_ACCOUNT:request.getAccountId(),
                        request.getAmount(),
                        request.getCurrency(),
                        VBNK_ENTITY_NAME,
                        request.getConcept()
                ),
                request.isCharge(),
                !request.isCharge(),
                request.isCharge()?request.getBalance():BigDecimal.ZERO,
                request.isCharge()?BigDecimal.ZERO:request.getBalance(),
                null
        );
    }

}
