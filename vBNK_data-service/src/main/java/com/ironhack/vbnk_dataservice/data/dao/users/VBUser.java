package com.ironhack.vbnk_dataservice.data.dao.users;

import com.ironhack.vbnk_dataservice.data.dto.users.AccountHolderDTO;
import com.ironhack.vbnk_dataservice.data.dto.users.AdminDTO;
import com.ironhack.vbnk_dataservice.data.dto.users.ThirdPartyDTO;
import com.ironhack.vbnk_dataservice.data.dto.users.VBUserDTO;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.http.client.HttpResponseException;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.time.Instant;

@Entity @Hidden
@Getter @Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class VBUser {

    @Id
    private String id;

    @Column(updatable = false,unique = true,nullable = false)
    private String username;
    private String firstName,lastName;
    @CreationTimestamp
    @Column(updatable = false)
    private Instant creationDate;
    @UpdateTimestamp
    private Instant updateDate;

    public static VBUser fromUnknownDTO(VBUserDTO unknown) throws HttpResponseException {
        return switch (unknown.getClass().getSimpleName()) {
            case "AccountHolderDTO" -> AccountHolder.fromDTO((AccountHolderDTO) unknown);
            case "AdminDTO" -> VBAdmin.fromDTO((AdminDTO) unknown);
            case "ThirdPartyDTO" -> ThirdParty.fromDTO((ThirdPartyDTO) unknown);
            default ->
                    throw new HttpResponseException(HttpStatus.I_AM_A_TEAPOT.value(), HttpStatus.I_AM_A_TEAPOT.getReasonPhrase());
        };
    }
}
