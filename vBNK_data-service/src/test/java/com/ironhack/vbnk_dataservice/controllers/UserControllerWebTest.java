package com.ironhack.vbnk_dataservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.vbnk_dataservice.data.Address;
import com.ironhack.vbnk_dataservice.repositories.users.AccountHolderRepository;
import com.ironhack.vbnk_dataservice.repositories.users.AdminRepository;
import com.ironhack.vbnk_dataservice.repositories.users.ThirdPartyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static com.ironhack.vbnk_dataservice.data.dao.users.AccountHolder.newAccountHolder;
import static com.ironhack.vbnk_dataservice.data.dao.users.ThirdParty.newThirdParty;
import static com.ironhack.vbnk_dataservice.data.dao.users.VBAdmin.newVBAdmin;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerWebTest {

    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    ThirdPartyRepository thirdPartyRepository;
    @Autowired
    WebApplicationContext webApplicationContext;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
        var address = new Address().setAdditionalInfo("KJSGD").setCity("Oklahoma").setCountry("India")
                .setStreet("Main street").setStreetNumber(45).setZipCode(8080);

        accountHolderRepository.saveAll(List.of(
                newAccountHolder("Antonio", "aaa","Antonio","Antoniez").setDateOfBirth(LocalDate.now()).setPrimaryAddress(address),
                newAccountHolder("Antonia", "aab","Antonio","Antoniez").setDateOfBirth(LocalDate.now()).setPrimaryAddress(address),
                newAccountHolder("Antonino", "aac","Antonio","Antoniez").setDateOfBirth(LocalDate.now()).setPrimaryAddress(address),
                newAccountHolder("Antoine", "aad","Antonio","Antoniez").setDateOfBirth(LocalDate.now()).setPrimaryAddress(address)
        ));
        adminRepository.saveAll(List.of(
                newVBAdmin("Antonio", "bbb","Antonio","Antoniez"),
                newVBAdmin("Antonia", "bba","Antonio","Antoniez"),
                newVBAdmin("Antonino", "bbc","Antonio","Antoniez"),
                newVBAdmin("Antoine", "bbd","Antonio","Antoniez")
        ));
        thirdPartyRepository.saveAll(List.of(
                newThirdParty("Antonio", "abb","Antonio","Antoniez"),
                newThirdParty("Antonia", "aba","Antonio","Antoniez"),
                newThirdParty("Antonino", "abc","Antonio","Antoniez"),
                newThirdParty("Antoine", "abd","Antonio","Antoniez")
        ));
    }

    @AfterEach
    void tearDown() {
        accountHolderRepository.deleteAll();
        thirdPartyRepository.deleteAll();
        adminRepository.deleteAll();
    }

    @Test
    void get_test() throws Exception {
        var result = mockMvc
                .perform(get("/v1/data/auth/users?id=aaa"))
                .andExpect(status().isFound()) // check status code 200
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Antonio"));
    }

    @Test
    void getAdmin_test() {
    }

    @Test
    void getAccountHolder_test() {
    }

    @Test
    void getThirdParty_test() {
    }

    @Test
    void getAllAdmin_test() {
    }

    @Test
    void getAllAccountHolder_test() {
    }

    @Test
    void testGetThirdParty_test() {
    }

    @Test
    void createAccountHolder_test() {
    }

    @Test
    void createAdmin_test() {
    }

    @Test
    void createThirdParty_test() {
    }

    @Test
    void updateAdmin_test() {
    }

    @Test
    void updateAccountHolder_test() {
    }

    @Test
    void updateThirdParty_test() {
    }

    @Test
    void delete_test() {
    }
//    @Test
//    void test_findAll_okOne() throws Exception {
//
//        // we perform the call and check the response code
//        var result = mockMvc
//                .perform(get("/v1/vegetables"))
//                .andExpect(status().isOk()) // check status code 200
//                .andReturn();
//
//        assertTrue(result.getResponse().getContentAsString().contains("Onion")); // check the json
//    }
//
//    @Test
//    void test_create_ok() throws Exception {
//        var broccoli = new Vegetable("Broccoli", new BigDecimal("1.50"), Quality.ORGANIC); // create the object
//        var vegString = objectMapper.writeValueAsString(broccoli); // transform the object to string
//        var result = mockMvc
//                .perform(post("/v1/vegetables")
//                        .content(vegString) // inject the string/object into the requestbody
//                        .contentType(MediaType.APPLICATION_JSON) // tell the client to expect a JSON
//                )
//                .andExpect(status().isCreated())
//                .andReturn();
//        var response = result.getResponse().getContentAsString();
//        assertTrue(response.contains("broccoli"));
//    }
}