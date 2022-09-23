package com.ironhack.vbnk_dataservice.data.dto.users;

import com.ironhack.vbnk_dataservice.data.Address;
import com.ironhack.vbnk_dataservice.data.dao.users.AccountHolder;
import com.ironhack.vbnk_dataservice.data.http.request.NewAccountHolderRequest;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Hidden
@Getter @Setter
@NoArgsConstructor
public class AccountHolderDTO extends VBUserDTO {
    private String secretKey;
    private LocalDate dateOfBirth;
    private Address primaryAddress;
    private Address mailingAddress;

    public AccountHolderDTO(String id) {
        super();
        this.setId(id);
    }

    public static AccountHolderDTO fromEntity(AccountHolder entity) {
        return newAccountHolderDTO(entity.getUsername(), entity.getId(), entity.getFirstName(), entity.getLastName())
                .setDateOfBirth(entity.getDateOfBirth())
                .setMailingAddress(entity.getMailingAddress())
                .setPrimaryAddress(entity.getPrimaryAddress());
    }

    public static AccountHolderDTO newAccountHolderDTO(String username, String id,String firstname,String lastname) {
        var user = new AccountHolderDTO();
        user.setId(id).setUsername(username).setFirstName(firstname).setLastName(lastname);
        return user;
    }

    @Override
    public AccountHolderDTO setUsername(String userName) {
        super.setUsername(userName);
        return this;
    }

    public static AccountHolderDTO fromRequest(NewAccountHolderRequest request){
        return newAccountHolderDTO(request.getUsername(), request.getId(), request.getFirstname(), request.getLastname())
                .setDateOfBirth(request.getDateOfBirth())
                .setMailingAddress(new Address(request.getMailStreet(),
                        request.getMailCity(),
                        request.getMailCountry(),
                        request.getMailAdditionalInfo(),
                        request.getMailStreetNumber(),
                        request.getMailZipCode()))
                .setPrimaryAddress(new Address(request.getMainStreet(),
                        request.getMainCity(),
                        request.getMainCountry(),
                        request.getMainAdditionalInfo(),
                        request.getMainStreetNumber(),
                        request.getMainZipCode()));
    }
}
