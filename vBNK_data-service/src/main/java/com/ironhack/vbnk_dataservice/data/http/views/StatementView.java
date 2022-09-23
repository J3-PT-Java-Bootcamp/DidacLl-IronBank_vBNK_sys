package com.ironhack.vbnk_dataservice.data.http.views;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
@Getter
@NoArgsConstructor

@Schema(name = "Statement View")
@Tag(name = "HTTP Views")
public class StatementView {
    BigDecimal amount;
    BigDecimal balance;
    Currency currency;
    Instant date;
    String type;
    String senderDisplayName;
    String concept;
}
