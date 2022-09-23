package com.ironhack.vbnk_dataservice.utils;

import org.junit.jupiter.api.Test;

class CryptoConverterTest {
    CryptoConverter conv = new CryptoConverter();

    @Test
    void convertToDatabaseColumn_test() {
        System.out.println(conv.convertToDatabaseColumn("dlb12345"));
    }

    @Test
    void convertToEntityAttribute_test() {
        System.out.println(conv.convertToEntityAttribute("WZ5M/5CD4ARyBx4r/+JhPQ=="));
    }
}