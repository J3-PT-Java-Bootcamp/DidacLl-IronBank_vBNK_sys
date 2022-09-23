package com.ironhack.vbnk_dataservice.data.dto.accounts;

import com.ironhack.vbnk_dataservice.data.AccountState;
import com.ironhack.vbnk_dataservice.data.dao.accounts.CheckingAccount;
import com.ironhack.vbnk_dataservice.data.dao.users.AccountHolder;
import com.ironhack.vbnk_dataservice.data.dao.users.VBAdmin;
import com.ironhack.vbnk_dataservice.data.http.request.NewCheckingAccountRequest;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@NoArgsConstructor
@Getter @Setter
@Hidden
public class CheckingDTO extends AccountDTO {
    private static final String  display_name = "Checking Account";
    private BigDecimal minimumBalance;
    private BigDecimal penaltyFee;
    private BigDecimal monthlyMaintenanceFee;

    public static CheckingDTO fromEntity(CheckingAccount entity) {
        CheckingDTO dto = new CheckingDTO().setMinimumBalance(entity.getMinimumBalance().getAmount())
                .setMonthlyMaintenanceFee(entity.getMonthlyMaintenanceFee())
                .setPenaltyFee(entity.getPenaltyFee());
        dto.setId(entity.getId())
                .setAccountNumber(entity.getAccountNumber())
                .setAmount(entity.getBalance().getAmount())
                .setCurrency(entity.getBalance().getCurrency())
                .setState(entity.getState())
                .setSecretKey(entity.getSecretKey())
                .setPrimaryOwner(entity.getPrimaryOwner())
                .setSecondaryOwner(entity.getSecondaryOwner())
                .setAdministratedBy(entity.getAdministratedBy())
                .setDisplayName(display_name)
                .setLastBankUpdate(entity.getLastBankUpdate());
        return dto;
    }

    public static CheckingDTO fromRequest(NewCheckingAccountRequest request, AccountHolder pOwner, AccountHolder sOwner, VBAdmin admin, String accountNumber) {

        CheckingDTO dto = new CheckingDTO();
        dto
                .setAccountNumber(accountNumber)
                .setAmount(request.getInitialAmount())
                .setCurrency(Currency.getInstance(request.getCurrency()))
                .setState(AccountState.ACTIVE)
                .setSecretKey(request.getSecretKey())
                .setPrimaryOwner(pOwner)
                .setSecondaryOwner(sOwner)
                .setAdministratedBy(admin)
                .setLastBankUpdate(Instant.now());
        return dto;
    }
}
