package com.ironhack.vbnk_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class VBnkGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(VBnkGatewayApplication.class, args);
    }

}
