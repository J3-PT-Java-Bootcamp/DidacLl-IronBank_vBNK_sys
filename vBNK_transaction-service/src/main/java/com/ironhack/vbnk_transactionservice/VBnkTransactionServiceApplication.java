package com.ironhack.vbnk_transactionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class VBnkTransactionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VBnkTransactionServiceApplication.class, args);
    }

}
