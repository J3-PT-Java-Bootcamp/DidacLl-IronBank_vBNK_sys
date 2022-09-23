package com.ironhack.vbnk_dataservice.data.dto.users;

import com.ironhack.vbnk_dataservice.data.dao.users.VBAdmin;
import com.ironhack.vbnk_dataservice.data.http.request.NewAdminRequest;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public class AdminDTO extends VBUserDTO {

    public static AdminDTO fromEntity(VBAdmin entity) {
        return newAdminDTO(entity.getUsername(), entity.getId(), entity.getFirstName(), entity.getLastName());
    }

    public static AdminDTO newAdminDTO(String username, String id,String firstname, String lastname) {
        var user = new AdminDTO();
        user.setId(id).setUsername(username).setLastName(lastname).setFirstName(firstname);
        return user;
    }

    public static AdminDTO fromRequest(NewAdminRequest request) {
        return newAdminDTO(request.getUserName(),request.getId(), request.getFirstname(), request.getLastname());
    }
}
