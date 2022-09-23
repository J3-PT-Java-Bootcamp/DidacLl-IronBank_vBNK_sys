package com.ironhack.vbnk_dataservice.services;

import com.ironhack.vbnk_dataservice.data.http.request.NotificationRequest;
import com.ironhack.vbnk_dataservice.data.http.request.TransferRequest;
import com.ironhack.vbnk_dataservice.data.http.response.DataResponse;
import com.ironhack.vbnk_dataservice.data.http.response.TransferResponse;
import org.apache.http.client.HttpResponseException;
import org.springframework.http.ResponseEntity;

import javax.naming.ServiceUnavailableException;

public interface VBNKService {


    ResponseEntity<TransferResponse> transferFunds(TransferRequest request) throws HttpResponseException;

    ResponseEntity<TransferResponse> receiveTransfer(TransferRequest request) throws HttpResponseException;
    ResponseEntity<TransferResponse> sendBlindTransfer(TransferRequest request) throws HttpResponseException;


    String bankUpdateUsers() throws HttpResponseException, ServiceUnavailableException;

    ResponseEntity<DataResponse> sendNotification(NotificationRequest request);

}
