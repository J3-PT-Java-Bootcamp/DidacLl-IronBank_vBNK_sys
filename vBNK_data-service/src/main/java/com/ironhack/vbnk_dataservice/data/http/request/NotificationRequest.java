package com.ironhack.vbnk_dataservice.data.http.request;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Hidden
@Getter
@Setter
@AllArgsConstructor
public class NotificationRequest {
    private String title;
    private  String message;
    private String type;
    private  String accountRef;
    private String transactionId;
}
