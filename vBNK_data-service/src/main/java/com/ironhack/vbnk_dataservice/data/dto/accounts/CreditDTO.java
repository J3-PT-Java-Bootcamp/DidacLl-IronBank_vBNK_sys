package com.ironhack.vbnk_dataservice.data.dto.accounts;

import com.ironhack.vbnk_dataservice.data.AccountState;
import com.ironhack.vbnk_dataservice.data.dao.accounts.CreditAccount;
import com.ironhack.vbnk_dataservice.data.dao.users.AccountHolder;
import com.ironhack.vbnk_dataservice.data.dao.users.VBAdmin;
import com.ironhack.vbnk_dataservice.data.http.request.NewCreditAccountRequest;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

import static com.ironhack.vbnk_dataservice.utils.VBNKConfig.*;

@NoArgsConstructor
@Getter @Setter
@Hidden
public class CreditDTO extends AccountDTO {
    private static final String  display_name = "Credit Account";

    private BigDecimal creditLimit;
    private  BigDecimal interestRate;

    public static CreditDTO fromEntity(CreditAccount entity) {
        CreditDTO dto = new CreditDTO().setCreditLimit(entity.getCreditLimit())
                .setInterestRate(entity.getInterestRate());
        dto.setId(entity.getId())
                .setAmount(entity.getBalance().getAmount())
                .setCurrency(entity.getBalance().getCurrency())
                .setState(entity.getState())
                .setSecretKey(entity.getSecretKey())
                .setPrimaryOwner(entity.getPrimaryOwner())
                .setSecondaryOwner(entity.getSecondaryOwner())
                .setAccountNumber(entity.getAccountNumber())
                .setAdministratedBy(entity.getAdministratedBy())
                .setDisplayName(display_name)
                .setLastBankUpdate(entity.getLastBankUpdate());
        return dto;
    }

    public static CreditDTO fromRequest(NewCreditAccountRequest request, AccountHolder pOwner, AccountHolder sOwner, VBAdmin admin, String accountNumber) {
        CreditDTO dto = new CreditDTO();
        var cLimit= request.getCreditLimit();
        var intRate= request.getInterestRate();
        if(cLimit!=null&&cLimit.compareTo(new BigDecimal(VBNK_MIN_CREDIT_LIMIT))>=0&&
                cLimit.compareTo(new BigDecimal(VBNK_MAX_CREDIT_LIMIT))<1)dto.setCreditLimit(request.getCreditLimit());
        if(intRate!=null&&intRate.compareTo(new BigDecimal(VBNK_MIN_INTEREST_RATE))>=0&&
        intRate.compareTo(new BigDecimal(VBNK_MAX_INTEREST_RATE))<=1)dto.setInterestRate(request.getInterestRate());
        dto
                .setAmount(request.getInitialAmount())
                .setCurrency(Currency.getInstance(request.getCurrency()))
                .setState(AccountState.ACTIVE)
                .setSecretKey(request.getSecretKey())
                .setPrimaryOwner(pOwner)
                .setSecondaryOwner(sOwner)
                .setAccountNumber(accountNumber)
                .setAdministratedBy(admin)
                .setLastBankUpdate(Instant.now());
        return dto;
    }
}
