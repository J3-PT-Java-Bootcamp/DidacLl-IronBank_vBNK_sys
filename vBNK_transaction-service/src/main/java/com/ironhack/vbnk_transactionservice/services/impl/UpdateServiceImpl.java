package com.ironhack.vbnk_transactionservice.services.impl;

import com.ironhack.vbnk_transactionservice.data.dao.DailyLog;
import com.ironhack.vbnk_transactionservice.repositories.LogRepository;
import com.ironhack.vbnk_transactionservice.services.UpdateService;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.naming.ServiceUnavailableException;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Service
public class UpdateServiceImpl implements UpdateService {
    private WebClient dataClient;
    private static final String[] DATA_SERVICE = new String[]{"vbnk-data-service","/v1/data/client/test/ping"};
    final
    DiscoveryClient discoveryClient;
    final
    LogRepository logRepository;

    public UpdateServiceImpl(DiscoveryClient discoveryClient, LogRepository logRepository) {
        this.discoveryClient = discoveryClient;
        this.logRepository = logRepository;
    }


    @Override
    @Scheduled(fixedRate = 1,timeUnit = TimeUnit.HOURS)
    public void checkLog() throws ServiceUnavailableException {
        var now = LocalDate.now();
        var logId= now.getYear()+""+now.getMonthValue()+""+now.getDayOfMonth();
        if(!logRepository.existsById(logId))startUpdateTask();
    }

    private void startUpdateTask() throws ServiceUnavailableException {
        dataClient=checkClientAvailable(DATA_SERVICE,dataClient);
        var res =dataClient.get()
                .uri("/v1/data/client/update")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        saveLog();
    }

    @Override
    public void saveLog() {
        var now = LocalDate.now();

        String str=   (now.getYear()*10000+now.getMonthValue()*100+now.getDayOfMonth())+"";
        if(!logRepository.existsById(str))logRepository.save(new DailyLog());
    }

    //---------------------------------------------------------------------------------------------------------CLIENT CONFIG
    private WebClient checkClientAvailable(String[] service, WebClient webClient) throws ServiceUnavailableException {
        try {
            try {
                if (webClient == null) createClient(service[0]);
                if (webClient.get()
                        .uri(service[1])
                        .retrieve()
                        .bodyToMono(String.class)
                        .block()
                        != "pong")return createClient(service[0]);
            } catch (Exception e) {
                return createClient(service[0]);
            }
        }catch (Throwable err){
            if(err instanceof ServiceUnavailableException)throw err;
        }
        return webClient;
    }
    private WebClient  createClient(String service) throws ServiceUnavailableException{
        for (int i = 0; i < 3; i++) {
            try {
                var serviceInstanceList = discoveryClient.getInstances(service);
                String clientURI = serviceInstanceList.get(0).getUri().toString();
                return WebClient.create(clientURI);
            } catch (Throwable ignored) {}
            try {Thread.sleep(5000);} catch (InterruptedException ignored) {}
        }
        throw new ServiceUnavailableException();
    }

}
