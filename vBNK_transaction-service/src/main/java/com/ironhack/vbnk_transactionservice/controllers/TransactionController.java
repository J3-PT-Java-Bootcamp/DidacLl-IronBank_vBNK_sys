package com.ironhack.vbnk_transactionservice.controllers;

import com.ironhack.vbnk_transactionservice.data.http.request.TransferRequest;
import com.ironhack.vbnk_transactionservice.data.http.request.UpdateTransactionRequest;
import com.ironhack.vbnk_transactionservice.data.http.responses.TransferResponse;
import com.ironhack.vbnk_transactionservice.data.http.views.StatementView;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.naming.ServiceUnavailableException;
import java.util.List;

public interface TransactionController {
    @Tag(name = "ping..")
    @GetMapping("/public/{ping}")
    String ping(Authentication auth, @PathVariable(name = "ping") String ping);

    @SecurityRequirement(name = "customer")
    @Tag(name = "Send Transfer",description = "Send a complete found transfer ")
    ResponseEntity<TransferResponse> transferTo(Authentication auth, TransferRequest request) throws ServiceUnavailableException;

    @Tag(name = "Send Payment Order",description = "Send a request for a payment order ")
    ResponseEntity<TransferResponse> orderPaymentTo(Authentication auth, TransferRequest request) throws ServiceUnavailableException;

    @Tag(name = "Confirm Transaction",description = "Confirm a pending transaction ")
    void confirmPendingTransaction(Authentication auth, String id) throws ServiceUnavailableException;

    @Tag(name = "Get Statements",description = "Get 10 account statements, pageable")
    @PostMapping("/main/statements/{pag}")
    List<StatementView> getStatements(Authentication auth, @PathVariable(name = "pag") int pag, @RequestBody String account);

    @PostMapping("/client/update") @Hidden
    void registerBankUpdate(UpdateTransactionRequest request) throws ServiceUnavailableException;
}
