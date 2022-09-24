package com.ironhack.vbnk_dataservice.controllers.impl;

import com.ironhack.vbnk_dataservice.controllers.AccountController;
import com.ironhack.vbnk_dataservice.data.dto.accounts.AccountDTO;
import com.ironhack.vbnk_dataservice.data.dto.accounts.CheckingDTO;
import com.ironhack.vbnk_dataservice.data.dto.accounts.CreditDTO;
import com.ironhack.vbnk_dataservice.data.dto.accounts.SavingsDTO;
import com.ironhack.vbnk_dataservice.data.http.request.NewAccountRequest;
import com.ironhack.vbnk_dataservice.data.http.request.NewCheckingAccountRequest;
import com.ironhack.vbnk_dataservice.data.http.request.NewCreditAccountRequest;
import com.ironhack.vbnk_dataservice.data.http.request.NewSavingsAccountRequest;
import com.ironhack.vbnk_dataservice.data.http.views.AccountView;
import com.ironhack.vbnk_dataservice.services.VBAccountService;
import com.ironhack.vbnk_dataservice.services.VBUserService;
import com.ironhack.vbnk_dataservice.utils.VBNKConfig;
import io.swagger.v3.oas.annotations.Hidden;
import org.apache.http.client.HttpResponseException;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.naming.ServiceUnavailableException;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("v1/data")
@Validated
public class AccountControllerWeb implements AccountController {

    private final VBAccountService service;
    private final VBUserService userService;

    public AccountControllerWeb(VBAccountService service, VBUserService userService) {
        this.service = service;
        this.userService = userService;
    }


    @Override @GetMapping("/auth/accounts")
    public ResponseEntity<AccountDTO> getAccount(@RequestParam String id) throws HttpResponseException {
        return ResponseEntity.ok(service.getAccount(id));
    }
    @Override @GetMapping("/main/accounts/all")
    public ResponseEntity<List<AccountDTO>> getAllUserAccounts(@RequestParam String userId) {
        return ResponseEntity.ok(service.getAllUserAccounts(userId));
    }

    @Override @PatchMapping("/auth/accounts/savings")
    public void updateSavingsAccount(@RequestBody SavingsDTO dto, @RequestParam String id) throws HttpResponseException {
        service.update(dto, id);
    }
    @Override @PatchMapping("/auth/accounts/checking")
    public void updateChecking(@RequestBody CheckingDTO dto,@RequestParam  String id) throws HttpResponseException {
        service.update(dto, id);
    }
    @Override @PatchMapping("/auth/accounts/credit")
    public void updateCreditAccount(@RequestBody CreditDTO dto,@RequestParam  String id) throws HttpResponseException {
        service.update(dto, id);
    }
    @Override @DeleteMapping("/auth/accounts")
    public void delete(@RequestParam String id) throws HttpResponseException {
        if(service.getAccount(id).getAmount().compareTo(BigDecimal.ZERO)!=0)throw new HttpResponseException(HttpStatus.CONFLICT.value(), "Account must be at 0 to delete it");
        service.delete(id);
    }

    @Override @GetMapping("/auth/state")
    public String toggleFreezeAccount(@RequestParam(name = "ref") String accountRef) throws HttpResponseException {
     return service.toggleFreezeAccount(accountRef);
    }
    //------------------------------------------------------------------------------------------------NEW METHODS


    @Override @PatchMapping("/auth/accounts/savings/new")
    public String createSavingsAccount(Authentication auth,@Valid  @RequestBody NewSavingsAccountRequest request) throws HttpResponseException {
        checkOwnerAndAdmin(auth, request);
        return service.create(request).getAccountNumber();
    }
    @Override @PatchMapping("/auth/accounts/checking/new")
    public String createChecking(Authentication auth,@Valid  @RequestBody NewCheckingAccountRequest request) throws HttpResponseException {
        checkOwnerAndAdmin(auth,request);
        return service.create(request).getAccountNumber();
    }
    @Override @PatchMapping("/auth/accounts/credit/new")
    public String createCreditAccount(Authentication auth, @Valid @RequestBody NewCreditAccountRequest request) throws HttpResponseException {
        checkOwnerAndAdmin(auth,request);
        return service.create(request).getAccountNumber();
    }

   @Override @GetMapping("main/accounts/view")
   public AccountView getAccountDetails(@RequestParam(name = "ref") String accountRef, Authentication auth) throws HttpResponseException, ServiceUnavailableException {
        var acc= service.getAccount(accountRef);
        if(!(VBNKConfig.isAdmin(auth)
                ||acc.getPrimaryOwner().getId().equalsIgnoreCase(VBNKConfig.getUserIdFromAuth(auth))
                ||(acc.getSecondaryOwner()!=null&&acc.getSecondaryOwner().getId().equalsIgnoreCase(VBNKConfig.getUserIdFromAuth(auth)))))
            throw new HttpResponseException(HttpStatus.FORBIDDEN.value(), "User not authorized for this account");
        return AccountView.fromDTO(acc,service.getStatements(0,accountRef,auth ));
   }

    //------------------------------------------------------------------------------------------------UTILS
    private void checkOwnerAndAdmin(Authentication auth, NewAccountRequest request) throws HttpResponseException {
        try {
            RefreshableKeycloakSecurityContext context = (RefreshableKeycloakSecurityContext) auth.getCredentials();
            AccessToken accessToken = context.getToken();
            request.setAdministratedBy(userService.getAdminFromToken(accessToken).getId());
        }catch (Throwable ignored){
        }
        if(!(userService.existsById(request.getPrimaryOwner())
                && userService.existsById(request.getAdministratedBy())))
            throw new HttpResponseException(404,"USERS NOT FOUND");
    }

    @Hidden
    @GetMapping("/auth/accounts/{ping}")
    public String authPing(Authentication auth, @PathVariable(name = "ping") String ping)   {
        return ping.replace('i','o');
    }

}
