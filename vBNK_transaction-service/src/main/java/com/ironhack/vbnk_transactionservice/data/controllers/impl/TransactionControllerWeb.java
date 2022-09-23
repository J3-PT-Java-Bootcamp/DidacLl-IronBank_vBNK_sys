package com.ironhack.vbnk_transactionservice.data.controllers.impl;

import com.ironhack.vbnk_transactionservice.data.TransactionState;
import com.ironhack.vbnk_transactionservice.data.TransactionType;
import com.ironhack.vbnk_transactionservice.data.controllers.TransactionController;
import com.ironhack.vbnk_transactionservice.data.dto.TransactionDTO;
import com.ironhack.vbnk_transactionservice.data.http.request.TransferRequest;
import com.ironhack.vbnk_transactionservice.data.http.request.UpdateTransactionRequest;
import com.ironhack.vbnk_transactionservice.data.http.responses.DataTransferResponse;
import com.ironhack.vbnk_transactionservice.data.http.responses.TransferResponse;
import com.ironhack.vbnk_transactionservice.data.http.views.StatementView;
import com.ironhack.vbnk_transactionservice.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.naming.ServiceUnavailableException;
import java.util.ArrayList;
import java.util.List;

import static com.ironhack.vbnk_transactionservice.data.TransactionState.NOK;
import static com.ironhack.vbnk_transactionservice.data.TransactionType.BANK_CHARGE;
import static com.ironhack.vbnk_transactionservice.data.TransactionType.BANK_INCOME;

@RestController

@RequestMapping("/v1/trans")
public class TransactionControllerWeb implements TransactionController {

    final
    TransactionService service;

    public TransactionControllerWeb(TransactionService service) {
        this.service = service;
    }
@Override
    @GetMapping("/public/{ping}")
    public String ping(Authentication auth, @PathVariable(name = "ping") String ping)   {
        return ping.replace('i','o');
    }

    @Override
    @PostMapping( "/main/trf")
    public ResponseEntity<TransferResponse> transferTo( Authentication auth,@RequestBody TransferRequest request) throws ServiceUnavailableException {
        return service.initiateTransferRequest(auth,request );
    }

    @Override
    @PostMapping("/main/pmt")
    public ResponseEntity<TransferResponse> orderPaymentTo(Authentication auth, @RequestBody TransferRequest request) throws ServiceUnavailableException {
        return service.initiatePaymentRequest(request);
    }

    @Override
    @PostMapping("/main/cnf")
    public void confirmPendingTransaction(Authentication auth, @RequestBody String transactionId) throws ServiceUnavailableException {
        TransactionDTO trans=null;
        try {
            trans = service.getTransaction(transactionId).getBody();
        }catch (Throwable ignored){}
        if(trans!=null&& trans.getState()== TransactionState.PENDING){
            transferTo(auth,trans.getRequest());
            try {
                if (trans.getType() == TransactionType.PAYMENT_ORDER) transferTo(auth, trans.getRequest());
                trans.setState(TransactionState.OK);
            }catch (Throwable err){
                trans.setState(NOK);
            }
        }
    }
    @Override
    @PostMapping("/main/statements/{pag}")
    public List<StatementView> getStatements(Authentication auth,
                                             @PathVariable(name = "pag") int pag,
                                             @RequestBody String account) {
        try {
            return service.getAccountStatements(pag, account);
        }catch (Throwable err){
            return new ArrayList<StatementView>();
        }
    }

    @Override
    @PostMapping("/client/update")
    public void registerBankUpdate(UpdateTransactionRequest request) throws ServiceUnavailableException {
        service.createTransaction(DataTransferResponse.fromUpdateRequest(request),request.isCharge()?BANK_CHARGE:BANK_INCOME);
        service.checkPendingTransactions(request.getAccountId());
    }
}
