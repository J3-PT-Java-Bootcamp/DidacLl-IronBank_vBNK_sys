package com.ironhack.vbnk_dataservice.data.http.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "New Admin Request")
@Tag(name = "HTTP Requests")
public class NewAdminRequest {
    @NotNull
    private String id;
    @NotNull
    private String userName,email,firstname,lastname;

    

}
