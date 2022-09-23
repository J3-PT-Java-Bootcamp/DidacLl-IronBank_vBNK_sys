package com.ironhack.vbnk_dataservice.data.dao.accounts;

import com.ironhack.vbnk_dataservice.data.AccountState;
import com.ironhack.vbnk_dataservice.utils.Money;
import com.ironhack.vbnk_dataservice.data.dao.users.AccountHolder;
import com.ironhack.vbnk_dataservice.data.dao.users.VBAdmin;
import com.ironhack.vbnk_dataservice.utils.CryptoConverter;
import com.ironhack.vbnk_dataservice.utils.MoneyConverter;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@Getter
@Setter
@Hidden
public abstract class VBAccount {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
//    @Type(type = "uuid-char")
    private  String id;
    @Column(updatable = false,unique = true,nullable = false)
    private  String accountNumber;
    @Convert(converter = MoneyConverter.class)
    private  Money balance;

    //    @NotNull
    @Convert(converter = CryptoConverter.class)
    private  String secretKey;

    @ManyToOne
    @JoinColumn(name = "primary_owner_id")
    private  AccountHolder primaryOwner;

    @ManyToOne
    @JoinColumn(name = "secondary_owner_id")
    AccountHolder secondaryOwner;

    @CreationTimestamp
    @Column(updatable = false)
    Instant creationDate;
    Instant lastBankUpdate;
    @UpdateTimestamp
    Instant updateDate;

    @Enumerated(EnumType.STRING)
    AccountState state;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    VBAdmin administratedBy;
}
