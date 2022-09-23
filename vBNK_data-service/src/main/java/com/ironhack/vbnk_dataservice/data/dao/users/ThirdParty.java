package com.ironhack.vbnk_dataservice.data.dao.users;

import com.ironhack.vbnk_dataservice.data.dto.users.ThirdPartyDTO;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Hidden
public class ThirdParty extends VBUser {
    private String hashKey;
    private String internationalCode,entityCode;
    private String serverUrl;

    public static ThirdParty fromDTO(ThirdPartyDTO dto) {
        return newThirdParty(dto.getUsername(), dto.getId(), dto.getFirstName(), dto.getLastName())
                .setHashKey(dto.getHashKey())
                .setInternationalCode(dto.getInternationalCode())
                .setEntityCode(dto.getEntityCode())
                .setServerUrl(dto.getServerUrl());
    }

    public static ThirdParty newThirdParty(String name, String id,String firstname,String lastname) {
        var user = new ThirdParty();
        user.setId(id).setUsername(name).setFirstName(firstname).setLastName(lastname);
        return user;
    }
}
