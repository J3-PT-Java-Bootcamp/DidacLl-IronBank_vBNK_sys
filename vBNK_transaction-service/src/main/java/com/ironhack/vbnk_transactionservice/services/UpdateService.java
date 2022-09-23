package com.ironhack.vbnk_transactionservice.services;

import javax.naming.ServiceUnavailableException;

public interface UpdateService {


    void checkLog() throws ServiceUnavailableException;
    void saveLog();

}
