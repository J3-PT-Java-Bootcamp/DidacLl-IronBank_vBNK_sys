package com.ironhack.vbnk_dataservice.data.dto.users;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Hidden
@Getter @Setter
@NoArgsConstructor
public abstract class VBUserDTO {
    private String id;

    private String username,firstName,lastName;
}
