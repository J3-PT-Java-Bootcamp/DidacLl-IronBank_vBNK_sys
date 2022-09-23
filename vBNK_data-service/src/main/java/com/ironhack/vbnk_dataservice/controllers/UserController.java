package com.ironhack.vbnk_dataservice.controllers;

import com.ironhack.vbnk_dataservice.data.dto.users.AccountHolderDTO;
import com.ironhack.vbnk_dataservice.data.dto.users.AdminDTO;
import com.ironhack.vbnk_dataservice.data.dto.users.ThirdPartyDTO;
import com.ironhack.vbnk_dataservice.data.dto.users.VBUserDTO;
import com.ironhack.vbnk_dataservice.data.http.request.NewAccountHolderRequest;
import com.ironhack.vbnk_dataservice.data.http.request.NewAdminRequest;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.http.client.HttpResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface UserController {


    //------------------------------------------------------------------------------GET END POINTS
    @Operation(summary = "ping...")
    @GetMapping("/client/test/{ping}") @ResponseStatus(HttpStatus.OK)
    String ping(@PathVariable("ping") String ping);

    @Operation(summary = "GET USER: Get any type of User by it's ID")
    @GetMapping("/auth/users")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<VBUserDTO> get(@RequestParam String id) throws HttpResponseException;

    @Operation(summary = "GET ADMIN: Get admin user by it's ID")
    @GetMapping("/dev/users/admin")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<AdminDTO> getAdmin(@RequestParam String id);

    @Operation(summary = "GET ACCOUNT HOLDER: Get AccountHolder User by it's ID")
    @GetMapping("/auth/users/account-holder")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<AccountHolderDTO> getAccountHolder(@RequestParam String id);

    @Tag(name = "Admin operations")
    @Operation(summary = "GET OWN USER: Get User by it's Authentication")
    @GetMapping("/auth/users/own")   @ResponseStatus(HttpStatus.OK)
    ResponseEntity<AccountHolderDTO> getOwnUser(Authentication auth);

    @Operation(summary = "GET THIRD PARTY: Get ThirdParty User by it's ID")
    @GetMapping("/auth/users/third-party")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ThirdPartyDTO> getThirdParty(@RequestParam String id);

    @Operation(summary = "GET ALL ADMIN: Get a list with all Admin users")
    @GetMapping("/dev/users/admin/all")    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<AdminDTO>> getAllAdmin();

    @Operation(summary = "GET ALL ACCOUNT HOLDERS: Get a list with all AccountHolder users")
    @GetMapping("/dev/users/account-holder/all")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<AccountHolderDTO>> getAllAccountHolder();

    @Operation(summary = "GET ALL THIRD PARTY: Get a list with all thirdParty users")
    @GetMapping("/auth/users/third-party/all")    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<ThirdPartyDTO>> getThirdParty();

    //------------------------------------------------------------------------------CREATE END POINTS
    @Hidden
    @ResponseStatus(HttpStatus.CREATED)
    String createAccountHolder(@RequestBody NewAccountHolderRequest request) throws HttpResponseException;

    @Hidden
    @ResponseStatus(HttpStatus.CREATED)
    String createAdmin(@RequestBody NewAdminRequest request) throws HttpResponseException;

    @Operation(summary = "CREATE THIRD PARTY: Create new ThirdParty user")
    @ResponseStatus(HttpStatus.CREATED)
    void createThirdParty(@RequestBody ThirdPartyDTO dto, @RequestParam String id) throws HttpResponseException;

    @ResponseStatus(HttpStatus.OK)
    void updateAdmin(@RequestParam String id, @RequestBody AdminDTO dto) throws HttpResponseException;

    @PatchMapping("/auth/users/update/account-holder")
    @ResponseStatus(HttpStatus.CREATED)
    void updateAccountHolder(@RequestParam String id, @RequestBody AccountHolderDTO dto) throws HttpResponseException;

    @PatchMapping("/auth/users/update/third-party")
    @ResponseStatus(HttpStatus.CREATED)
    void updateThirdParty(@RequestParam String id, @RequestBody ThirdPartyDTO dto) throws HttpResponseException;

    //------------------------------------------------------------------------------DELETE END POINTS
    @DeleteMapping("/dev/users/delete")
    @ResponseStatus(HttpStatus.OK)
    void delete(@RequestParam String id) throws HttpResponseException;
}
