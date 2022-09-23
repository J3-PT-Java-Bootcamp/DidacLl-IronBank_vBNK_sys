package com.ironhack.vbnk_dataservice.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

public class MoneyDeserializer extends StdDeserializer<Money> {

    public MoneyDeserializer() {
        this(null);
    }

    public MoneyDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Money deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        BigDecimal amount = new BigDecimal((int)( node.get("amount")).numberValue());
        return new Money(amount);
    }
}