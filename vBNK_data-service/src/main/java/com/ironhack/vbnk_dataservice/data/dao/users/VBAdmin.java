package com.ironhack.vbnk_dataservice.data.dao.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.vbnk_dataservice.data.dao.Notification;
import com.ironhack.vbnk_dataservice.data.dto.users.AdminDTO;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Setter @Getter
@NoArgsConstructor
@Entity @Hidden
public class VBAdmin extends VBUser {
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Notification> pendingNotifications;

    public static VBAdmin fromDTO(AdminDTO dto) {
        return newVBAdmin(dto.getUsername(), dto.getId(),dto.getFirstName(),dto.getLastName());
    }

    public static VBAdmin newVBAdmin(String name, String id, String firstName, String lastName) {
        var user = new VBAdmin();
        user.setId(id).setUsername(name).setLastName(lastName).setFirstName(firstName);;
        return user;
    }
}
