package com.ironhack.vbnk_dataservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.vbnk_dataservice.data.Address;
import com.ironhack.vbnk_dataservice.data.NotificationState;
import com.ironhack.vbnk_dataservice.data.NotificationType;
import com.ironhack.vbnk_dataservice.data.dao.Notification;
import com.ironhack.vbnk_dataservice.data.dao.users.AccountHolder;
import com.ironhack.vbnk_dataservice.data.http.request.NotificationRequest;
import com.ironhack.vbnk_dataservice.repositories.NotificationRepository;
import com.ironhack.vbnk_dataservice.repositories.users.AccountHolderRepository;
import com.ironhack.vbnk_dataservice.services.NotificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static com.ironhack.vbnk_dataservice.data.dao.users.AccountHolder.newAccountHolder;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
class NotificationControllerWebTest {
    @Autowired
    NotificationController controller;
    @Autowired
    NotificationService service;
    @Autowired
    AccountHolderRepository userRepository;
    @Autowired
    NotificationRepository repository;
    @Autowired
    WebApplicationContext webApplicationContext;
    AccountHolder user;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
        user = userRepository.save(newAccountHolder("Antonio", "aaa","Antonio","Antoniez")
                .setDateOfBirth(LocalDate.now()).
                setPrimaryAddress(new Address().setAdditionalInfo("KJSGD")
                        .setCity("Oklahoma").setCountry("India").setStreet("Main street")
                        .setStreetNumber(45).setZipCode(8080)));
        var otherUser = userRepository.save(newAccountHolder("Antonia", "bbb","Antonio","Antoniez")
                .setDateOfBirth(LocalDate.now()).
                setPrimaryAddress(new Address().setAdditionalInfo("KJSGD")
                        .setCity("Oklahoma").setCountry("India").setStreet("Main street")
                        .setStreetNumber(45).setZipCode(8080)));


        repository.saveAll(List.of(
                new Notification().setMessage("This is a message").setType(NotificationType.INCOMING).setOwner(user).setTitle("test1").setState(NotificationState.PENDING),
                new Notification().setMessage("This is a message").setType(NotificationType.PAYMENT_CONFIRM).setOwner(user).setTitle("test2").setState(NotificationState.PENDING),
                new Notification().setMessage("This is a message").setType(NotificationType.FRAUD).setOwner(user).setTitle("test3").setState(NotificationState.PENDING),
                new Notification().setMessage("This is a message").setType(NotificationType.INCOMING).setOwner(user).setTitle("test4").setState(NotificationState.PENDING),
                new Notification().setMessage("This is a message").setType(NotificationType.FRAUD).setOwner(user).setTitle("test5").setState(NotificationState.PENDING),
                new Notification().setMessage("This is a message").setType(NotificationType.FRAUD).setOwner(otherUser).setTitle("test6").setState(NotificationState.PENDING),
                new Notification().setMessage("This is a message").setType(NotificationType.FRAUD).setOwner(user).setTitle("test7").setState(NotificationState.PENDING)
        ));
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getAll_test() throws Exception {

    }

    @Test
    void getIncoming_test() throws Exception {
        var result = mockMvc
                .perform(get("/v1/data/dev/notifications/incoming?userId=aaa"))
                .andExpect(status().isOk()) // check status code 200
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("test1"));

    }

    @Test
    void getFraud_test() throws Exception {
        var result = mockMvc
                .perform(get("/v1/data/dev/notifications/fraud?userId=aaa"))
                .andExpect(status().isOk()) // check status code 200
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("test3"));
    }

    @Test
    void getPaymentConfirm_test() throws Exception {
        var result = mockMvc
                .perform(get("/v1/data/dev/notifications/payment?userId=aaa"))
                .andExpect(status().isOk()) // check status code 200
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("test2"));
    }

    @Test
    void createNotification_test() throws Exception {
//        var objString = objectMapper.writeValueAsString(new NotificationRequest("Testing", "This is a test", NotificationType.INCOMING.name(), "aaa","bbb")); // transform the object to string
//        int count = service.getAllPending("aaa").size();
//        mockMvc
//                .perform(post("/v1/data/dev/notifications").content(objString).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk()) // check status code 200
//                .andReturn();
//        assertTrue(service.getAllPending("aaa").size()>count);
    }

    @Test
    void delete_test() throws Exception {
//        int count= service.getAllPending("aaa").size();
//        Long id= service.getIncomingNotifications("aaa").get(0).getId();
//        var result = mockMvc
//                .perform(delete("/v1/data/dev/notifications?id="+id))
//                .andExpect(status().isOk()) // check status code 200
//                .andReturn();
//        assertTrue(service.getAllPending("aaa").size()<count);
    }
}