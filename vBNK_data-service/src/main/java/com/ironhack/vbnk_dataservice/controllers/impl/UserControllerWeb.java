package com.ironhack.vbnk_dataservice.controllers.impl;

import com.ironhack.vbnk_dataservice.controllers.UserController;
import com.ironhack.vbnk_dataservice.data.dto.users.*;
import com.ironhack.vbnk_dataservice.data.http.request.*;
import com.ironhack.vbnk_dataservice.repositories.users.AccountHolderRepository;
import com.ironhack.vbnk_dataservice.services.VBUserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.http.client.HttpResponseException;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController @RequestMapping(path = "/v1/data")
@Validated @Tag(name = "User Services",description = "User Data manipulation")
public class UserControllerWeb implements UserController {
    private final VBUserService service;
    private final AccountHolderRepository repo;

    public UserControllerWeb(VBUserService service, AccountHolderRepository repo) {
        this.service = service;
        this.repo = repo;
    }


    //------------------------------------------------------------------------------GET END POINTS
    @Override @Operation(summary = "ping...")
    @GetMapping("/client/test/{ping}") @ResponseStatus(HttpStatus.OK)
    public String ping(@PathVariable("ping") String ping) {
        return ping.replace('i', 'o');
    }

    @Override @Operation(summary = "GET USER: Get any type of User by it's ID")
    @Tag(name = "Admin operations")
    @GetMapping("/auth/users")    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<VBUserDTO> get(@RequestParam String id) throws HttpResponseException {
        return new ResponseEntity<>(service.getUnknown(id), HttpStatus.FOUND);
    }

    @Override @Operation(summary = "GET ADMIN: Get admin user by it's ID")
    @GetMapping("/dev/users/admin")    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AdminDTO> getAdmin(@RequestParam String id) {
        return new ResponseEntity<>(service.getAdmin(id), HttpStatus.FOUND);
    }

    @Tag(name = "Admin operations")
    @Override @Operation(summary = "GET ACCOUNT HOLDER: Get AccountHolder User by it's ID")
    @GetMapping("/auth/users/account-holder")   @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AccountHolderDTO> getAccountHolder(@RequestParam String id) {
        return new ResponseEntity<>(service.getAccountHolder(id), HttpStatus.FOUND);
    }

    @Tag(name = "Main operations")
    @Override @Operation(summary = "GET OWN USER: Get User by it's Authentication")
    @GetMapping("/auth/users/own")   @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AccountHolderDTO> getOwnUser(Authentication auth) {
        RefreshableKeycloakSecurityContext context = (RefreshableKeycloakSecurityContext) auth.getCredentials();
        AccessToken accessToken = context.getToken();
        return new ResponseEntity<>(service.getOwnerFromToken(accessToken,true), HttpStatus.FOUND);
    }
    @Tag(name = "Admin operations")
    @Override @Operation(summary = "GET THIRD PARTY: Get ThirdParty User by it's ID")
    @GetMapping("/auth/users/third-party")   @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ThirdPartyDTO> getThirdParty(@RequestParam String id) {
        return new ResponseEntity<>(service.getThirdParty(id), HttpStatus.FOUND);
    }
    //------------------------------------------------------------------------------GET ALL END POINTS

    @Override @Operation(summary = "GET ALL ADMIN: Get a list with all Admin users")
    @GetMapping("/dev/users/admin/all")    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AdminDTO>> getAllAdmin() {
        return new ResponseEntity<>(service.getAllAdmin(), HttpStatus.FOUND);
    }

    @Override @Operation(summary = "GET ALL ACCOUNT HOLDERS: Get a list with all AccountHolder users")
    @GetMapping("/dev/users/account-holder/all")    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AccountHolderDTO>> getAllAccountHolder() {
        return new ResponseEntity<>(service.getAllAccountHolder(), HttpStatus.FOUND);
    }

    @Tag(name = "Admin operations")
    @Override @Operation(summary = "GET ALL THIRD PARTY: Get a list with all thirdParty users")
    @GetMapping("/auth/users/third-party/all")    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ThirdPartyDTO>> getThirdParty() {
        return new ResponseEntity<>(service.getAllThirdParty(), HttpStatus.FOUND);
    }

    //------------------------------------------------------------------------------CREATE END POINTS
    @Override @Hidden
    @PostMapping("/client/users/new/account-holder")
    @ResponseStatus(HttpStatus.CREATED)
    public String createAccountHolder(@RequestBody NewAccountHolderRequest request) throws HttpResponseException {
        if(service.existsById(request.getId())||service.existsByUsername(request.getUsername()))throw new HttpResponseException(409,"User already exists" );
        return service.create(AccountHolderDTO.fromRequest(request)).getId();
    }
    @Hidden @Override
    @PostMapping("/client/users/new/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public String createAdmin(@RequestBody NewAdminRequest request) throws HttpResponseException {
        if(service.existsById(request.getId())||service.existsByUsername(request.getUserName()))throw new HttpResponseException(409,"User already exists" );
        AdminDTO dto = AdminDTO.fromRequest(request);
        return service.create(dto).getId();
    }
    @Tag(name = "Admin operations")
    @Override @Operation(summary = "CREATE THIRD PARTY: Create new ThirdParty user")
    @PostMapping("/auth/users/new/third-party")    @ResponseStatus(HttpStatus.CREATED)
    public void createThirdParty(@RequestBody ThirdPartyDTO dto, @RequestParam String id) throws HttpResponseException {
        dto.setId(id);
        service.create(dto);
    }

    //------------------------------------------------------------------------------UPDATE END POINTS
    @Operation(summary = "UPDATE ADMIN: Update any Admin user attribute")
    @Override @PatchMapping("/dev/users/update/admin")   @ResponseStatus(HttpStatus.OK)
    public void updateAdmin(@RequestParam String id, @RequestBody AdminDTO dto) throws HttpResponseException {
        service.update(id, dto);
    }
    @Tag(name = "Admin operations")
    @Operation(summary = "UPDATE ACCOUNT HOLDER: Update any AccountHolder user attribute")
    @Override @PatchMapping("/auth/users/update/account-holder")
    @ResponseStatus(HttpStatus.CREATED)
    public void updateAccountHolder(@RequestParam String id, @RequestBody AccountHolderDTO dto) throws HttpResponseException {
        service.update(id, dto);
    }

    @Tag(name = "Admin operations")
    @Operation(summary = "UPDATE THIRD PARTY: Update any ThirdParty user attribute")
    @Override @PatchMapping("/auth/users/update/third-party")
    @ResponseStatus(HttpStatus.CREATED)
    public void updateThirdParty(@RequestParam String id, @RequestBody ThirdPartyDTO dto) throws HttpResponseException {
        service.update(id, dto);
    }

    //------------------------------------------------------------------------------DELETE END POINTS

    @Operation(summary = "DELETE USER: Delete any user")
    @Override    @DeleteMapping("/dev/users/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestParam String id) throws HttpResponseException {
        service.delete(id);
    }


}
