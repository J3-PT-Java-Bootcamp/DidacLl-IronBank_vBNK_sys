package com.ironhack.vbnk_transactionservice.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class TransactionDetails {
    String senderDisplayName;
    String concept;
}
