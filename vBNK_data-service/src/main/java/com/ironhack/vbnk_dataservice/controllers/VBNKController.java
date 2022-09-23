package com.ironhack.vbnk_dataservice.controllers;

import com.ironhack.vbnk_dataservice.data.http.request.NotificationRequest;
import com.ironhack.vbnk_dataservice.data.http.request.ThirdPartyTransferRequest;
import com.ironhack.vbnk_dataservice.data.http.request.TransferRequest;
import com.ironhack.vbnk_dataservice.data.http.response.DataResponse;
import com.ironhack.vbnk_dataservice.data.http.response.TransferResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.apache.http.client.HttpResponseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.naming.ServiceUnavailableException;

public interface VBNKController {

    ResponseEntity<TransferResponse> transferFunds(Authentication auth,TransferRequest request) throws HttpResponseException;

    ResponseEntity<TransferResponse> transferFunds_destinationLevel(ThirdPartyTransferRequest request) throws HttpResponseException;

    @PostMapping("/client/in")
    ResponseEntity<TransferResponse> transferFundsFromBank(@RequestBody TransferRequest request) throws HttpResponseException;

    @PostMapping("/auth/out")
    ResponseEntity<TransferResponse> transferFundsToBank(@RequestBody TransferRequest request) throws HttpResponseException;

    ResponseEntity<DataResponse> sendNotification(NotificationRequest request);

    @Hidden
    String startBankUpdate() throws HttpResponseException, ServiceUnavailableException;
}
