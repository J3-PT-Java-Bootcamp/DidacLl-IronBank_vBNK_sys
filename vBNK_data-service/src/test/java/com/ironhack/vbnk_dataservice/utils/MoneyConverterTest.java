package com.ironhack.vbnk_dataservice.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyConverterTest {
    MoneyConverter converter = new MoneyConverter();

    @Test
    @DisplayName("Convert Money to String")
    void convertToDatabaseColumn_test() {
        assertEquals("30.00//EUR",
                converter.convertToDatabaseColumn(new Money(BigDecimal.valueOf(30), Currency.getInstance("EUR"))));
    }

    @Test
    @DisplayName("Parse String as Money")
    void convertToEntityAttribute_test() {
        var mon = new Money(BigDecimal.valueOf(30), Currency.getInstance("EUR"));
        assertEquals(mon.getCurrency().getDisplayName() + mon.getAmount(),
                converter.convertToEntityAttribute("30.00//EUR").getCurrency().getDisplayName() + mon.getAmount());
    }
}