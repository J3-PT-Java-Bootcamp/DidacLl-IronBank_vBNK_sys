package com.ironhack.vbnk_dataservice.data.dao.accounts;

import com.ironhack.vbnk_dataservice.utils.Money;
import com.ironhack.vbnk_dataservice.data.dto.accounts.SavingsDTO;
import com.ironhack.vbnk_dataservice.utils.MoneyConverter;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Convert;
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
public class SavingsAccount extends VBAccount {

    @ColumnDefault(VBNK_MIN_SAVINGS_INTEREST_RATE)
    @DecimalMax(VBNK_MAX_SAVINGS_INTEREST_RATE) @DecimalMin(VBNK_MIN_SAVINGS_INTEREST_RATE)
    private BigDecimal interestRate;
    private final BigDecimal penaltyFee=VBNK_PENALTY_FEE;
    @Convert(converter = MoneyConverter.class)
    @ColumnDefault(VBNK_MAX_SAVINGS_MINIMUM_BALANCE)
    @DecimalMin(VBNK_MIN_SAVINGS_MINIMUM_BALANCE) @DecimalMax(VBNK_MAX_SAVINGS_MINIMUM_BALANCE)
    private Money minimumBalance;

    public static SavingsAccount fromDTO(SavingsDTO dto) {
        var retEntity = new SavingsAccount().setMinimumBalance(new Money(dto.getMinimumBalance(),dto.getCurrency()))
                .setInterestRate(dto.getInterestRate());
        retEntity.setId(dto.getId())
                .setBalance(new Money(dto.getAmount(),dto.getCurrency()))                .setState(dto.getState())
                .setSecretKey(dto.getSecretKey())
                .setPrimaryOwner(dto.getPrimaryOwner())
                .setSecondaryOwner(dto.getSecondaryOwner())
                .setAdministratedBy(dto.getAdministratedBy())
                .setAccountNumber(dto.getAccountNumber());
        return retEntity;
    }
}
