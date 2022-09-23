package com.ironhack.vbnk_transactionservice.services;

import com.ironhack.vbnk_transactionservice.data.TransactionType;
import com.ironhack.vbnk_transactionservice.data.http.request.TransferRequest;
import com.ironhack.vbnk_transactionservice.data.dto.TransactionDTO;
import com.ironhack.vbnk_transactionservice.data.http.responses.DataTransferResponse;
import com.ironhack.vbnk_transactionservice.data.http.responses.TransferResponse;
import com.ironhack.vbnk_transactionservice.data.http.views.StatementView;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import javax.naming.ServiceUnavailableException;
import java.util.List;

public interface TransactionService {


    ResponseEntity<TransferResponse> receiveBlindTransfer(Authentication authentication,TransferRequest request) throws ServiceUnavailableException;


    ResponseEntity<TransactionDTO> getTransaction(String id);

//    TransactionDTO createTransaction(DataTransferResponse request);

    void delete(String id);

    ResponseEntity<TransferResponse> initiateTransferRequest(Authentication authentication,TransferRequest request) throws ServiceUnavailableException;


    ResponseEntity<TransferResponse> initiatePaymentRequest(TransferRequest request) throws ServiceUnavailableException;

    List<StatementView> getAccountStatements(int pag, String account);

    TransactionDTO createTransaction(DataTransferResponse response, TransactionType type) throws ServiceUnavailableException;

    void checkPendingTransactions(String accountId);
}
