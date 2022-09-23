package com.ironhack.vbnk_dataservice.services;

import com.ironhack.vbnk_dataservice.data.Address;
import com.ironhack.vbnk_dataservice.data.NotificationState;
import com.ironhack.vbnk_dataservice.data.NotificationType;
import com.ironhack.vbnk_dataservice.data.dao.Notification;
import com.ironhack.vbnk_dataservice.data.dao.users.AccountHolder;
import com.ironhack.vbnk_dataservice.data.http.request.NotificationRequest;
import com.ironhack.vbnk_dataservice.repositories.NotificationRepository;
import com.ironhack.vbnk_dataservice.repositories.users.AccountHolderRepository;
import org.apache.http.client.HttpResponseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static com.ironhack.vbnk_dataservice.data.dao.users.AccountHolder.newAccountHolder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class NotificationServiceImplTest {
    @Autowired
    NotificationService service;
    @Autowired
    AccountHolderRepository userRepository;
    @Autowired
    NotificationRepository repository;
    AccountHolder user;

    @BeforeEach
    void setUp() {
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
    @DisplayName("Get All User Pending Notifications")
    void getAllPending_test() {
        assertEquals(6, service.getAllPending("aaa").size());
    }

    @Test
    @DisplayName("Get Incoming Pending Notifications")
    void getIncomingNotifications_test() {
        assertEquals(2, service.getIncomingNotifications("aaa").size());
    }

    @Test
    @DisplayName("Get Fraud Pending Notifications")
    void getFraudNotifications_test() {
        assertEquals(3, service.getFraudNotifications("aaa").size());
    }

    @Test
    @DisplayName("Get Payment Pending Notifications")
    void getPaymentNotifications_test() {
        assertEquals(1, service.getPaymentNotifications("aaa").size());
    }

    @Test
    @DisplayName("Create new (OK)")
    void create_test() throws HttpResponseException {
        service.create(new NotificationRequest("CHECK ME!", "TEST", NotificationType.INCOMING.name(), "bbb","fd"));
        assertEquals("CHECK ME!", service.getIncomingNotifications("bbb").get(0).getTitle());
    }

    @Test
    @DisplayName("Create new NOK wrong user")
    void create_test_NOK() {
        assertThrows(HttpResponseException.class, () -> service.create(new NotificationRequest("CHECK ME!", "TEST", NotificationType.INCOMING.name(), "","")));
    }

    @Test
    void delete_test() throws HttpResponseException {
        var note = service.create(new NotificationRequest("CHECK ME!", "TEST", NotificationType.INCOMING.name(), "bbb",""));
        service.delete(note.getId());
        assertEquals(0, service.getIncomingNotifications("bbb").size());

    }
}