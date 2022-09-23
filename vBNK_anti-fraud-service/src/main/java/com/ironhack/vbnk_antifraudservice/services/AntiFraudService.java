package com.ironhack.vbnk_antifraudservice.services;

import com.ironhack.vbnk_antifraudservice.model.AFRequest;
import com.ironhack.vbnk_antifraudservice.model.AFResponse;

public interface AntiFraudService {

    AFResponse registerTransaction(AFRequest request, AFResponse res);

    AFResponse validateByAccount(AFRequest request);

    AFResponse validateByUser(AFRequest request);

    AFResponse mainValidation(AFRequest request, String ref);
//    List<AFTransactionDTO> getLast48HoursTransactions(String userId, String accountRef);
//    List<AFTransactionDTO> getLast24HoursUserTransactions


}
