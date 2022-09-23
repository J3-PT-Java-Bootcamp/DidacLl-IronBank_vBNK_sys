package com.ironhack.vbnk_antifraudservice.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AFTransactionTest {

    @Test
    void compare_test() {
        var aft = new AFTransaction()
                .setTransactionDate(Instant.now().minus(5, ChronoUnit.HOURS))
                .setAmount(BigDecimal.TEN)
                .setSenderId("patata")
                .setSrcAccountNumber("frita");

        var aft2 = new AFRequest()
                .setAmount(BigDecimal.TEN)
                .setSenderId("patata")
                .setSrcAccountNumber("frita");

        assertTrue(aft.compareSimilarity(aft2)>50);
    }
}