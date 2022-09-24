//package com.ironhack.vbnk_dataservice.controllers;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.ironhack.vbnk_dataservice.data.AccountState;
//import com.ironhack.vbnk_dataservice.data.Address;
//import com.ironhack.vbnk_dataservice.utils.Money;
//import com.ironhack.vbnk_dataservice.data.dao.accounts.CheckingAccount;
//import com.ironhack.vbnk_dataservice.data.dao.accounts.CreditAccount;
//import com.ironhack.vbnk_dataservice.data.dao.accounts.SavingsAccount;
//import com.ironhack.vbnk_dataservice.data.dao.accounts.StudentCheckingAccount;
//import com.ironhack.vbnk_dataservice.data.dao.users.AccountHolder;
//import com.ironhack.vbnk_dataservice.data.dao.users.VBAdmin;
//import com.ironhack.vbnk_dataservice.data.dto.accounts.CheckingDTO;
//import com.ironhack.vbnk_dataservice.data.dto.accounts.CreditDTO;
//import com.ironhack.vbnk_dataservice.data.dto.accounts.SavingsDTO;
//import com.ironhack.vbnk_dataservice.data.dto.users.AccountHolderDTO;
//import com.ironhack.vbnk_dataservice.data.dto.users.AdminDTO;
//import com.ironhack.vbnk_dataservice.repositories.accounts.CheckingAccountRepository;
//import com.ironhack.vbnk_dataservice.repositories.accounts.CreditAccountRepository;
//import com.ironhack.vbnk_dataservice.repositories.accounts.SavingsAccountRepository;
//import com.ironhack.vbnk_dataservice.repositories.accounts.StudentCheckingAccountRepository;
//import com.ironhack.vbnk_dataservice.services.VBAccountService;
//import com.ironhack.vbnk_dataservice.services.VBUserService;
//import com.ironhack.vbnk_dataservice.utils.MoneyDeserializer;
//import org.apache.http.client.HttpResponseException;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//@SpringBootTest
//class AccountControllerWebTest {
//
//    @Autowired
//    VBAccountService accountService;
//    @Autowired
//    VBUserService userService;
//    @Autowired
//    CheckingAccountRepository checkingRepository;
//    @Autowired
//    StudentCheckingAccountRepository studentRepository;
//    @Autowired
//    SavingsAccountRepository savingsRepository;
//    @Autowired
//    CreditAccountRepository creditRepository;
//    @Autowired
//    WebApplicationContext webApplicationContext;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private MockMvc mockMvc;
//
//    CreditAccount credit;
//    CheckingAccount checking;
//    SavingsAccount savings;
//    StudentCheckingAccount student;
//
//    VBAdmin admin;
//    AccountHolder user;
//
//    @BeforeEach
//    void setUp() throws HttpResponseException {
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        SimpleModule module = new SimpleModule();
//        module.addDeserializer(Money.class, new MoneyDeserializer());
//        objectMapper.registerModule(module);
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(webApplicationContext)
//                .build();
//        admin = VBAdmin.fromDTO((AdminDTO) userService.create(new AdminDTO().setUsername("Super Admin").setId("bbb")));
//        user = AccountHolder.fromDTO((AccountHolderDTO) userService.create(
//                AccountHolderDTO.newAccountHolderDTO("Antonio", "aaa","antonio","antoniez")
//                        .setDateOfBirth(LocalDate.of(1990, 5, 3))
//                        .setPrimaryAddress(new Address().setAdditionalInfo("KJSGD").setCity("Oklahoma").setCountry("India")
//                                .setStreet("Main street").setStreetNumber(45).setZipCode(8080)))
//        );
//        Money money = new Money(BigDecimal.valueOf(10));
//
//        credit = new CreditAccount().setInterestRate(BigDecimal.TEN);
//        credit.setBalance(money).setState(AccountState.ACTIVE)
//                .setPrimaryOwner(user).setAdministratedBy(admin).setSecretKey("superSecretKey");
//        savings = new SavingsAccount().setInterestRate(BigDecimal.TEN)
//                .setMinimumBalance(money);
//        savings.setBalance(money).setState(AccountState.ACTIVE)
//                .setPrimaryOwner(user).setAdministratedBy(admin).setSecretKey("patatas");
//        checking = new CheckingAccount();
//        checking.setBalance(money).setState(AccountState.ACTIVE)
//                .setPrimaryOwner(user).setAdministratedBy(admin).setSecretKey("patatas");
//        student = new StudentCheckingAccount();
//        student.setBalance(money).setState(AccountState.ACTIVE)
//                .setPrimaryOwner(user).setAdministratedBy(admin).setSecretKey("patatas");
////        credit = CreditAccount.fromDTO((CreditDTO) accountService.create(CreditDTO.fromEntity(credit), "aaa"));
////        checking = CheckingAccount.fromDTO((CheckingDTO) accountService.create(CheckingDTO.fromEntity(checking), "aaa"));
////        savings = SavingsAccount.fromDTO((SavingsDTO) accountService.create(SavingsDTO.fromEntity(savings), "aaa"));
////        student = StudentCheckingAccount.fromDTO((StudentCheckingDTO) accountService.create(StudentCheckingDTO.fromEntity(student), "aaa"));
//    }
//
//    @AfterEach
//    void tearDown() throws HttpResponseException {
//        checkingRepository.deleteAll();
//        savingsRepository.deleteAll();
//        studentRepository.deleteAll();
//        creditRepository.deleteAll();
//        userService.delete("bbb");
//        userService.delete("aaa");
//    }
//
//    @Test
//    void getAccount_test() throws Exception {
//        var result = mockMvc
//                .perform(get("/v1/data/auth/accounts?id="+credit.getId()))
//                .andExpect(status().isOk()) // check status code 200
//                .andReturn();
//        assertTrue(result.getResponse().getContentAsString().contains("superSecretKey"));
//    }
//
//    @Test
//    void getAllUserAccounts_test() throws Exception {
//        var result = mockMvc
//                .perform(get("/v1/data/auth/accounts/all?userId=aaa"))
//                .andExpect(status().isOk()) // check status code 200
//                .andReturn();
//        assertTrue(result.getResponse().getContentAsString().contains("superSecretKey"));
//    }
//
//    @Test
//    void createSavingsAccount_test() throws Exception {
//        int count = accountService.getAllUserAccounts("aaa").size();
//
//        var result = mockMvc
//                .perform(post("/v1/data/auth/accounts/savings?userId=aaa").contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(SavingsDTO.fromEntity(savings))))
//                .andExpect(status().isOk()) // check status code 200
//                .andReturn();
//        assertTrue(count<accountService.getAllUserAccounts("aaa").size());
//    }
//
//    @Test
//    void createChecking_test() throws Exception {
//        int count = accountService.getAllUserAccounts("aaa").size();
//        var result = mockMvc
//                .perform(post("/v1/data/auth/accounts/checking?userId=aaa").contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(CheckingDTO.fromEntity(checking))))
//                .andExpect(status().isOk()) // check status code 200
//                .andReturn();
//        assertTrue(count<accountService.getAllUserAccounts("aaa").size());
//    }
//
//    @Test
//    void createCreditAccount_test() throws Exception {
//        int count = accountService.getAllUserAccounts("aaa").size();
//        var result = mockMvc
//                .perform(post("/v1/data/auth/accounts/credit?userId=aaa").contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(CreditDTO.fromEntity(credit))))
//                .andExpect(status().isOk()) // check status code 200
//                .andReturn();
//        assertTrue(count<accountService.getAllUserAccounts("aaa").size());
//
//    }
//
//    @Test
//    void updateSavingsAccount_test() throws Exception {
//        var result = mockMvc
//                .perform(patch("/v1/data/auth/accounts/savings?id="+savings.getId()).contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new SavingsDTO().setState(AccountState.FROZEN))))
//                .andExpect(status().isOk()) // check status code 200
//                .andReturn();
//        assertSame(accountService.getAccount(savings.getId()).getState(), AccountState.FROZEN);
//    }
//
//    @Test
//    void updateChecking_test() throws Exception {
//        var result = mockMvc
//            .perform(patch("/v1/data/auth/accounts/checking?id="+checking.getId()).contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(new CheckingDTO().setState(AccountState.FROZEN))))
//            .andExpect(status().isOk()) // check status code 200
//            .andReturn();
//        assertSame(accountService.getAccount(checking.getId()).getState(), AccountState.FROZEN);
//    }
//
//    @Test
//    void updateCreditAccount_test() throws Exception {
//        var result = mockMvc
//                .perform(patch("/v1/data/auth/accounts/credit?id="+credit.getId()).contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new CreditDTO().setState(AccountState.FROZEN))))
//                .andExpect(status().isOk()) // check status code 200
//                .andReturn();
//        assertSame(accountService.getAccount(credit.getId()).getState(), AccountState.FROZEN);
//    }
//
//    @Test
//    void delete_test() throws Exception {
//        int count = accountService.getAllUserAccounts("aaa").size();
//
//        var result = mockMvc
//                .perform(delete("/v1/data/auth/accounts?id="+credit.getId()))
//                .andExpect(status().isOk()) // check status code 200
//                .andReturn();
//        assertTrue(count>accountService.getAllUserAccounts("aaa").size());
//    }
//}