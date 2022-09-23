package com.ironhack.vbnk_dataservice.data;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable @Hidden
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    String street, city, country, additionalInfo;
    Integer streetNumber, zipCode;

}
