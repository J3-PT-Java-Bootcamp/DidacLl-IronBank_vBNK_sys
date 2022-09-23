package com.ironhack.vbnk_dataservice.data.dto.users;

import com.ironhack.vbnk_dataservice.data.dao.users.ThirdParty;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Hidden
@Getter @Setter
@NoArgsConstructor
public class ThirdPartyDTO extends VBUserDTO {

    private String hashKey;
    private String internationalCode,entityCode;
    private String serverUrl;


    public static ThirdPartyDTO fromEntity(ThirdParty entity) {
        return newThirdPartyDTO(entity.getUsername(), entity.getId()).setHashKey(entity.getHashKey())
                .setHashKey(entity.getHashKey())
                .setInternationalCode(entity.getInternationalCode())
                .setEntityCode(entity.getEntityCode())
                .setServerUrl(entity.getServerUrl());
    }

    public static ThirdPartyDTO newThirdPartyDTO(String name, String id) {
        var user = new ThirdPartyDTO();
        user.setId(id).setUsername(name);
        return user;
    }
}
