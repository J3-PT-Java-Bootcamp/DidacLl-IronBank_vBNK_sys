package com.ironhack.vbnk_dataservice.data.dao.accounts;

import com.ironhack.vbnk_dataservice.utils.Money;
import com.ironhack.vbnk_dataservice.data.dto.accounts.CheckingDTO;
import com.ironhack.vbnk_dataservice.utils.MoneyConverter;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Convert;
import javax.persistence.Entity;
import java.math.BigDecimal;

import static com.ironhack.vbnk_dataservice.utils.VBNKConfig.*;

@Entity
@Getter
@Setter
@Hidden
public class CheckingAccount extends VBAccount {

    @Convert(converter = MoneyConverter.class)
    private final Money minimumBalance= VBNK_CHECKING_MIN_BALANCE;
    private final BigDecimal penaltyFee= VBNK_PENALTY_FEE;
    private final BigDecimal monthlyMaintenanceFee= VBNK_MONTH_MAINTENANCE_FEE;

    public static CheckingAccount fromDTO(CheckingDTO dto) {
        var entity = new CheckingAccount();//.setMinimumBalance(new Money(dto.getMinimumBalance(),dto.getCurrency()))
//                .setMonthlyMaintenanceFee(dto.getMonthlyMaintenanceFee())
//                .setPenaltyFee(dto.getPenaltyFee());
        entity.setId(dto.getId())
                .setAccountNumber(dto.getAccountNumber())
                .setBalance(new Money(dto.getAmount(),dto.getCurrency()))                .setState(dto.getState())
                .setSecretKey(dto.getSecretKey())
                .setPrimaryOwner(dto.getPrimaryOwner())
                .setSecondaryOwner(dto.getSecondaryOwner())
                .setAdministratedBy(dto.getAdministratedBy());
        return entity;
    }

}
