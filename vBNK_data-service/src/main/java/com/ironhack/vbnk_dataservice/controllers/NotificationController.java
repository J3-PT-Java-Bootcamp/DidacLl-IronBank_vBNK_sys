package com.ironhack.vbnk_dataservice.controllers;

import com.ironhack.vbnk_dataservice.data.dto.NotificationDTO;
import com.ironhack.vbnk_dataservice.data.http.request.NotificationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.http.HttpException;
import org.apache.http.client.HttpResponseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

import javax.naming.ServiceUnavailableException;
import java.util.List;
@Tag(name = "Notification Services",description = "Notifications data manipulation")
public interface NotificationController {

    //------------------------------------------------------------------------------GET END POINTS
    @Tag(name = "Main operations")
    @Operation(summary = "GET ALL NOTIFICATIONS: Get all current authenticated user notifications")
    ResponseEntity<List<NotificationDTO>> getAll(Authentication auth) throws HttpException;

    ResponseEntity<List<NotificationDTO>> getIncoming(String userId);

    ResponseEntity<List<NotificationDTO>> getFraud(String userId);

    ResponseEntity<List<NotificationDTO>> getPaymentConfirm(String id);

    //------------------------------------------------------------------------------CREATE END POINTS
    String createNotification(@RequestBody NotificationRequest dto) throws HttpResponseException;

    //------------------------------------------------------------------------------DELETE END POINTS
    @Tag(name = "Main operations")
    @Operation(summary = "DELETE NOTIFICATION: delete notification by id")
    void delete(Long id) throws HttpResponseException;

    @Tag(name = "Main operations")
    @Operation(summary = "CONFIRM NOTIFICATION")
    void confirmNotification(Authentication auth,Long id) throws HttpResponseException, ServiceUnavailableException;
}
