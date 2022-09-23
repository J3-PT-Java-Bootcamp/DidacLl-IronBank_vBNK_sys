package com.ironhack.vbnk_dataservice.controllers.impl;

import com.ironhack.vbnk_dataservice.controllers.VBNKController;
import com.ironhack.vbnk_dataservice.data.http.request.NotificationRequest;
import com.ironhack.vbnk_dataservice.data.http.request.ThirdPartyTransferRequest;
import com.ironhack.vbnk_dataservice.data.http.request.TransferRequest;
import com.ironhack.vbnk_dataservice.data.http.response.DataResponse;
import com.ironhack.vbnk_dataservice.data.http.response.TransferResponse;
import com.ironhack.vbnk_dataservice.services.VBNKService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.http.client.HttpResponseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.naming.ServiceUnavailableException;

@RestController
@RequestMapping("/v1/data")
public class VBNKControllerOpen implements VBNKController {
    private final VBNKService service;

    public VBNKControllerOpen(VBNKService service) {
        this.service = service;
    }

    @Hidden @Override
    @PostMapping("/main/tf/send")
    public ResponseEntity<TransferResponse> transferFunds(Authentication auth, @RequestBody TransferRequest request) throws HttpResponseException {
        return service.transferFunds(request);
    }

    @Hidden    @Override
    @PostMapping("/client/tf/receive")
    public ResponseEntity<TransferResponse> transferFunds_destinationLevel(@RequestBody ThirdPartyTransferRequest request) throws HttpResponseException {
        return service.receiveTransfer(request);
    }


    @PostMapping("/auth/in")
    @Override @Tag(name = "Admin operations")
    public ResponseEntity<TransferResponse> transferFundsFromBank(@RequestBody TransferRequest request) throws HttpResponseException {
        return service.receiveTransfer(request);
    }
    @Override @Tag(name = "Admin operations")
    @PostMapping("/auth/out")
    public ResponseEntity<TransferResponse> transferFundsToBank(@RequestBody TransferRequest request) throws HttpResponseException {
        return service.sendBlindTransfer(request);
    }

    @Hidden @Override
    @PostMapping("/client/notif")
    public ResponseEntity<DataResponse> sendNotification(@RequestBody NotificationRequest request) {
        return service.sendNotification(request);
    }
    @Hidden @Override
    @GetMapping("/client/update")
    public String startBankUpdate() throws HttpResponseException, ServiceUnavailableException {
        return service.bankUpdateUsers();
    }


}
