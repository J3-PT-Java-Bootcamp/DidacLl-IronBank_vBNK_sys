package com.ironhack.vbnk_transactionservice.data.dao;

import com.ironhack.vbnk_transactionservice.data.TransactionState;
import com.ironhack.vbnk_transactionservice.data.TransactionType;
import com.ironhack.vbnk_transactionservice.data.dto.TransactionDTO;
import com.ironhack.vbnk_transactionservice.data.http.request.TransferRequest;
import com.ironhack.vbnk_transactionservice.utils.Money;
import com.ironhack.vbnk_transactionservice.utils.MoneyConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter @Setter
@NoArgsConstructor
public class VBTransaction {
    @Id @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @Enumerated(EnumType.STRING) private TransactionType type;
    
    @Enumerated(EnumType.STRING) private TransactionState state;
    @CreationTimestamp @Column(updatable = false)
    private Instant creationDate;
    @UpdateTimestamp
    private Instant modification;
    private Instant expirationDate;

    private String subjAccount;
    @Convert(converter = MoneyConverter.class)
    private Money currentAccountBalance;

    @Embedded
    TransferRequest request;
    public static VBTransaction fromDTO(TransactionDTO dto){
        return new VBTransaction().setId(dto.getId())
                .setType(dto.getType())
                .setState(dto.getState())
                .setExpirationDate(dto.getExpirationDate())
                .setSubjAccount(dto.getSubjAccount())
                .setCurrentAccountBalance(new Money(dto.getBalanceAmount(),dto.getBalanceCurrency()))
                .setRequest(dto.getRequest());
    }




}
