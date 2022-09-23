package com.ironhack.vbnk_dataservice.controllers;

import com.ironhack.vbnk_dataservice.data.dto.accounts.AccountDTO;
import com.ironhack.vbnk_dataservice.data.dto.accounts.CheckingDTO;
import com.ironhack.vbnk_dataservice.data.dto.accounts.CreditDTO;
import com.ironhack.vbnk_dataservice.data.dto.accounts.SavingsDTO;
import com.ironhack.vbnk_dataservice.data.http.request.NewCheckingAccountRequest;
import com.ironhack.vbnk_dataservice.data.http.request.NewCreditAccountRequest;
import com.ironhack.vbnk_dataservice.data.http.request.NewSavingsAccountRequest;
import com.ironhack.vbnk_dataservice.data.http.views.AccountView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.http.HttpException;
import org.apache.http.client.HttpResponseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.naming.ServiceUnavailableException;
import java.util.List;
@Tag(name = "Account Services",description = "Accounts Data manipulation")
public interface AccountController {

    //------------------------------------------------------------------------------GET END POINTS

    @Tag(name = "Admin operations")
    @Operation(summary = "GET ACCOUNT: Get Any account by it's ID or AccountNumber")
    ResponseEntity<AccountDTO> getAccount(String id) throws HttpException, HttpResponseException;

    //------------------------------------------------------------------------------GET ALL END POINTS

    @Tag(name = "Main operations")
    @Operation(summary = "GET USER ACCOUNTS: Get all user accounts by it's ID or AccountNumber")
    ResponseEntity<List<AccountDTO>> getAllUserAccounts(String userId);

    @Tag(name = "Admin operations")
    @Operation(summary = "TOGGLE FROZEN ACCOUNT: Toggle account state between frozen and active")
    @PostMapping("/auth/state")
    String toggleFreezeAccount(@RequestBody String accountRef) throws HttpResponseException;

    //------------------------------------------------------------------------------CREATE END POINTS
    @Tag(name = "Admin operations")
    @Operation(summary = "CREATE SAVINGS ACCOUNT: Create new SavingsAccount")
    String createSavingsAccount(Authentication auth, @RequestBody NewSavingsAccountRequest request) throws HttpResponseException;
    @Tag(name = "Admin operations")
    @Operation(summary = "CREATE CHECKING ACCOUNT: Create new CheckingAccount")
    String createChecking(Authentication auth, @RequestBody NewCheckingAccountRequest request) throws HttpResponseException;
    @Tag(name = "Admin operations")
    @Operation(summary = "CREATE CREDIT ACCOUNT: Create new CreditAccount")
    String createCreditAccount(Authentication auth, @RequestBody NewCreditAccountRequest request) throws HttpResponseException;

    //------------------------------------------------------------------------------UPDATE END POINTS
    @Tag(name = "Admin operations")
    @Operation(summary = "UPDATE SAVINGS ACCOUNT: Update any SavingAccount attribute")// TODO: 22/09/2022 change request to only allow modify secure fields
    void updateSavingsAccount(@RequestBody SavingsDTO dto, String id) throws HttpResponseException;

    @Tag(name = "Admin operations")
    @Operation(summary = "UPDATE CHECKING ACCOUNT: Update any CheckingAccount attribute")// TODO: 22/09/2022 change request to only allow modify secure fields
    void updateChecking(@RequestBody CheckingDTO dto, String id) throws HttpResponseException;

    @Tag(name = "Admin operations")
    @Operation(summary = "UPDATE CREDIT ACCOUNT: Update any CreditAccount attribute")// TODO: 22/09/2022 change request to only allow modify secure fields
    void updateCreditAccount(@RequestBody CreditDTO dto, String id) throws HttpResponseException;

    //------------------------------------------------------------------------------DELETE END POINTS
    @Tag(name = "Admin operations")
    @Operation(summary = "DELETE ACCOUNT: Delete any Account")
    void delete(String id) throws HttpResponseException;

    @Tag(name = "Main operations")
    @Operation(summary = "GET ACCOUNT DETAILS: Get public view from any account, includes 10 last statements")// TODO: 22/09/2022 change request to only allow modify secure fields
    @GetMapping("main/accounts/view")
    AccountView getAccountDetails(@RequestParam String accountRef, Authentication auth) throws HttpResponseException, ServiceUnavailableException;
}
