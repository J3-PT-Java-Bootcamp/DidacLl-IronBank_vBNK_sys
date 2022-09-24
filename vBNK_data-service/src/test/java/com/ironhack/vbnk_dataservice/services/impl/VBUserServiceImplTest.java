package com.ironhack.vbnk_dataservice.services.impl;

import com.ironhack.vbnk_dataservice.services.VBUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class VBUserServiceImplTest {
@Autowired
    VBUserService service;
    @Test
    void getAdmin_test() {
        assertThrows(Throwable.class,()->service.getAdmin("a418ae70-a1b0-47f8-9ea2-b92ef27d0aa8"));
    }
}