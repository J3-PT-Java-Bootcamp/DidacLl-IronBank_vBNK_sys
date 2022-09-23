package com.ironhack.vbnk_antifraudservice.controllers;

import com.ironhack.vbnk_antifraudservice.model.AFRequest;
import com.ironhack.vbnk_antifraudservice.model.AFResponse;

public interface AntiFraudController {
    AFResponse validateTransaction(AFRequest request);

    String ping(String ping);
}
