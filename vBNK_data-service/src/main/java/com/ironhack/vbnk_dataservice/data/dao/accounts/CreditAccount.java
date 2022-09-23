package com.ironhack.vbnk_dataservice.data.dao.accounts;

import com.ironhack.vbnk_dataservice.utils.Money;
import com.ironhack.vbnk_dataservice.data.dto.accounts.CreditDTO;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

import static com.ironhack.vbnk_dataservice.utils.VBNKConfig.*;

@Entity
//@NoArgsConstructor
@Getter
@Setter
@Hidden
public class CreditAccount extends VBAccount {
//    @Convert(converter = MoneyConverter.class)
    @ColumnDefault(VBNK_MIN_CREDIT_LIMIT)
    private BigDecimal creditLimit;

    @ColumnDefault(VBNK_MAX_INTEREST_RATE)
    @DecimalMax(VBNK_MAX_INTEREST_RATE) @DecimalMin(VBNK_MIN_INTEREST_RATE)
    private BigDecimal interestRate;

    public static CreditAccount fromDTO(CreditDTO dto) {
        var retEntity = new CreditAccount().setCreditLimit(dto.getCreditLimit())
                .setInterestRate(dto.getInterestRate());
        retEntity.setId(dto.getId());
                retEntity.setBalance(new Money(dto.getAmount(),dto.getCurrency()))
                .setState(dto.getState())
                .setSecretKey(dto.getSecretKey())
                .setPrimaryOwner(dto.getPrimaryOwner())
                .setSecondaryOwner(dto.getSecondaryOwner())
                .setAdministratedBy(dto.getAdministratedBy())
                .setAccountNumber(dto.getAccountNumber());
        return retEntity;
    }
}
