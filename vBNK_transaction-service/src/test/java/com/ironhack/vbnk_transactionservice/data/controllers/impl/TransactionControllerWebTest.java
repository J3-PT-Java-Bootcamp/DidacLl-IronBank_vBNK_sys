package com.ironhack.vbnk_transactionservice.data.controllers.impl;

import com.ironhack.vbnk_transactionservice.data.controllers.TransactionController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TransactionControllerWebTest {
    @Autowired
    TransactionController controller;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void ping_test() {
        assertEquals("pong",controller.ping(null,"ping"));
    }

    @Test
    void transferTo_test() {
    }

    @Test
    void orderPaymentTo_test() {
    }

    @Test
    void confirmPendingTransaction_test() {
    }

    @Test
    void getStatements_test() {
    }

    @Test
    void registerBankUpdate_test() {
    }
}