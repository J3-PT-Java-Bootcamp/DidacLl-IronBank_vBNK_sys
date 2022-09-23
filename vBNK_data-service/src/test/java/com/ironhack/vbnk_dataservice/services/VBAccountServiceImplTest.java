package com.ironhack.vbnk_dataservice.services;

import com.ironhack.vbnk_dataservice.data.AccountState;
import com.ironhack.vbnk_dataservice.data.Address;
import com.ironhack.vbnk_dataservice.utils.Money;
import com.ironhack.vbnk_dataservice.data.dao.accounts.CheckingAccount;
import com.ironhack.vbnk_dataservice.data.dao.accounts.CreditAccount;
import com.ironhack.vbnk_dataservice.data.dao.accounts.SavingsAccount;
import com.ironhack.vbnk_dataservice.data.dao.accounts.StudentCheckingAccount;
import com.ironhack.vbnk_dataservice.data.dao.users.AccountHolder;
import com.ironhack.vbnk_dataservice.data.dao.users.VBAdmin;
import com.ironhack.vbnk_dataservice.data.dto.accounts.StudentCheckingDTO;
import com.ironhack.vbnk_dataservice.data.dto.users.AccountHolderDTO;
import com.ironhack.vbnk_dataservice.data.dto.users.AdminDTO;
import com.ironhack.vbnk_dataservice.repositories.accounts.CheckingAccountRepository;
import com.ironhack.vbnk_dataservice.repositories.accounts.CreditAccountRepository;
import com.ironhack.vbnk_dataservice.repositories.accounts.SavingsAccountRepository;
import com.ironhack.vbnk_dataservice.repositories.accounts.StudentCheckingAccountRepository;
import org.apache.http.client.HttpResponseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class VBAccountServiceImplTest {
    @Autowired
    VBAccountService accountService;
    @Autowired
    VBUserService userService;
    @Autowired
    CheckingAccountRepository checkingRepository;
    @Autowired
    StudentCheckingAccountRepository studentRepository;
    @Autowired
    SavingsAccountRepository savingsRepository;
    @Autowired
    CreditAccountRepository creditRepository;

    CreditAccount credit;
    CheckingAccount checking;
    SavingsAccount savings;
    StudentCheckingAccount student;

    VBAdmin admin;
    AccountHolder user;

    @BeforeEach
    void setUp() throws HttpResponseException {
        admin = VBAdmin.fromDTO((AdminDTO) userService.create(new AdminDTO().setUsername("Super Admin").setId("bbb")));
        user = AccountHolder.fromDTO((AccountHolderDTO) userService.create(
                AccountHolderDTO.newAccountHolderDTO("Antonio", "aaa","antonio","antoniez")
                        .setDateOfBirth(LocalDate.of(1990, 5, 3))
                        .setPrimaryAddress(new Address().setAdditionalInfo("KJSGD").setCity("Oklahoma").setCountry("India")
                                .setStreet("Main street").setStreetNumber(45).setZipCode(8080)))
        );
        Money money = new Money(BigDecimal.valueOf(10));

        credit = new CreditAccount().setInterestRate(BigDecimal.TEN);
        credit.setBalance(money).setState(AccountState.ACTIVE)
                .setPrimaryOwner(user).setAdministratedBy(admin).setSecretKey("patatas");
        savings = new SavingsAccount().setInterestRate(BigDecimal.TEN)
                .setMinimumBalance(money);
        savings.setBalance(money).setState(AccountState.ACTIVE)
                .setPrimaryOwner(user).setAdministratedBy(admin).setSecretKey("patatas");
        checking = new CheckingAccount();
        checking.setBalance(money).setState(AccountState.ACTIVE)
                .setPrimaryOwner(user).setAdministratedBy(admin).setSecretKey("patatas");
        student = new StudentCheckingAccount();
        student.setBalance(money).setState(AccountState.ACTIVE)
                .setPrimaryOwner(user).setAdministratedBy(admin).setSecretKey("patatas");
////        repository.save(checking);
//        credit = CreditAccount.fromDTO((CreditDTO) accountService.create(CreditDTO.fromEntity(credit), "aaa"));
//        checking = CheckingAccount.fromDTO((CheckingDTO) accountService.create(CheckingDTO.fromEntity(checking), "aaa"));
//        savings = SavingsAccount.fromDTO((SavingsDTO) accountService.create(SavingsDTO.fromEntity(savings), "aaa"));
//        student = StudentCheckingAccount.fromDTO((StudentCheckingDTO) accountService.create(StudentCheckingDTO.fromEntity(student), "aaa"));
    }

    @AfterEach
    void tearDown() throws HttpResponseException {
        checkingRepository.deleteAll();
        savingsRepository.deleteAll();
        studentRepository.deleteAll();
        creditRepository.deleteAll();
        userService.delete("bbb");
        userService.delete("aaa");
    }

    @Test
    void getAccount_test() throws HttpResponseException {
        assertEquals("Antonio", accountService.getAccount(student.getId()).getPrimaryOwner().getUsername());
    }

    @Test
    void getAllUserAccounts_test() {
        assertEquals(4, accountService.getAllUserAccounts("aaa").size());
    }

    @Test
    void create_test() throws HttpResponseException {
//        accountService.create(CheckingDTO.fromEntity(checking), "aaa");
        assertEquals(5, accountService.getAllUserAccounts("aaa").size());
    }

    @Test
    void update_test() throws HttpResponseException {
        var dto = new StudentCheckingDTO();
        dto.setAmount(new BigDecimal(10000));
        accountService.update(dto, student.getId());
        assertEquals(new Money(new BigDecimal(10000)).getAmount(), accountService.getAccount(student.getId()).getAmount());
    }

    @Test
    void delete_test() {
        accountService.delete(student.getId());
        assertEquals(3, accountService.getAllUserAccounts("aaa").size());

    }
}