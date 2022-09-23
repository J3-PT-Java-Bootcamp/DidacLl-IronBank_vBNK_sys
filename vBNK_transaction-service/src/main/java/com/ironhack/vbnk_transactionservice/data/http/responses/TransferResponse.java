package com.ironhack.vbnk_transactionservice.data.http.responses;

import com.ironhack.vbnk_transactionservice.data.TransactionState;
import com.ironhack.vbnk_transactionservice.utils.VBError;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class TransferResponse {
    @Setter
    private TransactionState state;
    private List<VBError> errors;

    public TransferResponse addErrors(VBError... errors){
        if(this.errors==null)this.errors=List.of(errors);
        else this.errors.addAll(List.of(errors));
        return this;
    }

}
