package com.ironhack.vbnk_antifraudservice.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AntiFraudRepositoryTest {
    @Autowired
    AntiFraudRepository repository;
    @Test
    void findAllBySrcAccountNumberOrderByTransactionDateDesc_test() {
        var list = repository.findAllBySrcAccountNumberOrderByTransactionDateDesc("9b0ce13a-4e8f-4568-b935-c8e34ac1f219");
        assertFalse(list.isEmpty());
    }

    @Test
    void findAllBySenderIdOrderByTransactionDateDesc_test() {
        var list = repository.findAllBySenderIdOrderByTransactionDateDesc("2f0d1f40-4074-4844-b845-a949523b2065");
        assertFalse(list.isEmpty());
    }

    @Test
    void findAllByTransactionDateBefore_test() {
        var list = repository.findAllByTransactionDateBefore(Instant.now());
        assertFalse(list.isEmpty());
    }
}