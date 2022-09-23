package com.ironhack.vbnk_dataservice.data.dao.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.vbnk_dataservice.data.Address;
import com.ironhack.vbnk_dataservice.data.dao.Notification;
import com.ironhack.vbnk_dataservice.data.dto.users.AccountHolderDTO;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Hidden
public class AccountHolder extends VBUser {
    @NotNull
    @Column(columnDefinition = "DATE")
    private LocalDate dateOfBirth;
    @NotNull
    @Embedded
    private Address primaryAddress;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "mail_street")),
            @AttributeOverride(name = "city", column = @Column(name = "mail_city")),
            @AttributeOverride(name = "country", column = @Column(name = "mail_country")),
            @AttributeOverride(name = "additionalInfo", column = @Column(name = "mail_additional_info")),
            @AttributeOverride(name = "streetNumber", column = @Column(name = "mail_street_number")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "mail_zip_code")),
    })
    private Address mailingAddress;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Notification> pendingNotifications;

    public static AccountHolder fromDTO(AccountHolderDTO dto) {
        return newAccountHolder(dto.getUsername(), dto.getId(), dto.getFirstName(), dto.getLastName()).setDateOfBirth(dto.getDateOfBirth())
                .setPrimaryAddress(dto.getPrimaryAddress())
                .setMailingAddress(dto.getMailingAddress());
    }

    public static AccountHolder newAccountHolder(String username, String id,String firstname, String lastname) {
        var user = new AccountHolder();
        user.setId(id).setUsername(username).setLastName(lastname).setFirstName(firstname);;
        return user;
    }
}
