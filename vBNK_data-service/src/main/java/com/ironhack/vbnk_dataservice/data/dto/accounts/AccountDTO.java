package com.ironhack.vbnk_dataservice.data.dto.accounts;

import com.ironhack.vbnk_dataservice.data.AccountState;
import com.ironhack.vbnk_dataservice.data.dao.accounts.*;
import com.ironhack.vbnk_dataservice.data.dao.users.AccountHolder;
import com.ironhack.vbnk_dataservice.data.dao.users.VBAdmin;
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
public class AccountDTO {
    private String displayName;
    private String id;
    private String accountNumber;
    private BigDecimal amount;
    private Currency currency;
    private String secretKey;
    private AccountHolder primaryOwner;
    private AccountHolder secondaryOwner;
    private AccountState state;
    private VBAdmin administratedBy;
    Instant lastBankUpdate;
    public static AccountDTO fromAnyAccountEntity(VBAccount entity) {
        AccountDTO retVal = null;
        if(entity instanceof CheckingAccount)retVal=new CheckingDTO().setDisplayName("Checking Account");
        else if(entity instanceof CreditAccount)retVal=new CreditDTO().setDisplayName("Credit Account");
        else if(entity instanceof SavingsAccount)retVal=new SavingsDTO().setDisplayName("Savings Account");
        else if(entity instanceof StudentCheckingAccount)retVal=new StudentCheckingDTO().setDisplayName("Student Checking Account");
        assert (retVal!=null);
        retVal.setId(entity.getId())
                .setAccountNumber(entity.getAccountNumber())
                .setAmount(entity.getBalance().getAmount())
                .setCurrency(entity.getBalance().getCurrency())
                .setState(entity.getState())
                .setSecretKey(entity.getSecretKey())
                .setPrimaryOwner(entity.getPrimaryOwner())
                .setSecondaryOwner(entity.getSecondaryOwner())
                .setAdministratedBy(entity.getAdministratedBy())
                .setLastBankUpdate(entity.getLastBankUpdate());
        return retVal;
    }

    public static CreditDTO convertToCreditDTO(AccountDTO accDTO) {
        return (CreditDTO) copyBaseValues(accDTO, new CreditDTO());
    }

    public static CheckingDTO convertToCheckingDTO(AccountDTO accDTO) {
        return (CheckingDTO) copyBaseValues(accDTO, new CheckingDTO());
    }

    public static SavingsDTO convertToSavingsDTO(AccountDTO accDTO) {
        return (SavingsDTO) copyBaseValues(accDTO, new SavingsDTO());
    }

    public static StudentCheckingDTO convertToStudentDTO(AccountDTO accDTO) {
        return (StudentCheckingDTO) copyBaseValues(accDTO, new StudentCheckingDTO());
    }


    private static AccountDTO copyBaseValues(AccountDTO src, AccountDTO dest) {
        return dest.setAccountNumber(src.getAccountNumber())
                .setAmount(src.getAmount()).setCurrency(src.getCurrency())
                .setAdministratedBy(src.administratedBy)
                .setState(src.getState())
                .setPrimaryOwner(src.getPrimaryOwner())
                .setSecondaryOwner(src.getSecondaryOwner())
                .setSecretKey(src.getSecretKey())
                .setId(src.getId())
                .setLastBankUpdate(src.lastBankUpdate);
    }

}
