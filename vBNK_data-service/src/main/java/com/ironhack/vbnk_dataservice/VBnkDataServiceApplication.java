package com.ironhack.vbnk_dataservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class VBnkDataServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VBnkDataServiceApplication.class, args);
    }

}
