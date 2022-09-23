package com.ironhack.vbnk_dataservice.data.http.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "New AccountHolder Request")
@Tag(name = "HTTP Requests")
public class NewAccountHolderRequest {
    @NotNull(message = "id should not be null")
    private String id;
    @NotNull(message = "username should not be null")
    private String username;
    @NotNull(message = "email should not be null")
    private String email;
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    @NotNull
    private LocalDate dateOfBirth;
    @NotNull
    private String mainStreet;
    @NotNull
    private String mainCity;
    @NotNull
    private String mainCountry;
    @NotNull
    private String mainAdditionalInfo;
    private String mailStreet, mailCity, mailCountry, mailAdditionalInfo;
    @NotNull
    private Integer mainStreetNumber, mainZipCode;
    private Integer mailStreetNumber, mailZipCode;
}
