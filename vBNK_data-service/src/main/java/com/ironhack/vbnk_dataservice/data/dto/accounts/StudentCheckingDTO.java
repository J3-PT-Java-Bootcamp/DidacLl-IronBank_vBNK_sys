package com.ironhack.vbnk_dataservice.data.dto.accounts;

import com.ironhack.vbnk_dataservice.data.AccountState;
import com.ironhack.vbnk_dataservice.data.dao.accounts.StudentCheckingAccount;
import com.ironhack.vbnk_dataservice.data.dao.users.AccountHolder;
import com.ironhack.vbnk_dataservice.data.dao.users.VBAdmin;
import com.ironhack.vbnk_dataservice.data.http.request.NewCheckingAccountRequest;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Currency;

@NoArgsConstructor
@Getter @Setter
@Hidden
public class StudentCheckingDTO extends AccountDTO {
    private static final String  display_name = "Student Checking Account";


    public static StudentCheckingDTO fromEntity(StudentCheckingAccount entity) {
        StudentCheckingDTO dto = new StudentCheckingDTO();
        dto.setId(entity.getId())
                .setAmount(entity.getBalance().getAmount())
                .setCurrency(entity.getBalance().getCurrency())
                .setState(entity.getState())
                .setSecretKey(entity.getSecretKey())
                .setPrimaryOwner(entity.getPrimaryOwner())
                .setSecondaryOwner(entity.getSecondaryOwner())
                .setAdministratedBy(entity.getAdministratedBy())
                .setAccountNumber(entity.getAccountNumber())
                .setDisplayName(display_name)
                .setLastBankUpdate(entity.getLastBankUpdate());
        return dto;
    }

    public static StudentCheckingDTO fromRequest(NewCheckingAccountRequest request, AccountHolder pOwner, AccountHolder sOwner, VBAdmin admin, String accountNumber) {
        StudentCheckingDTO dto = new StudentCheckingDTO();
        dto
                .setAmount(request.getInitialAmount())
                .setCurrency(Currency.getInstance(request.getCurrency()))
                .setState(AccountState.ACTIVE)
                .setSecretKey(request.getSecretKey())
                .setAccountNumber(accountNumber)
                .setAdministratedBy(admin)
                .setPrimaryOwner(pOwner)
                .setLastBankUpdate(Instant.now());
        if(sOwner!=null)dto.setSecondaryOwner(sOwner);
        return dto;
    }
}
