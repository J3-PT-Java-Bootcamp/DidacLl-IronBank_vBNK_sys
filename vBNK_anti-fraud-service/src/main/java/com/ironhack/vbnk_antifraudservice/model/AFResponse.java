package com.ironhack.vbnk_antifraudservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Integer value meaning: -1 NOT_VALIDATED / 0 OK / 1..3 Fraud level detected")

public class AFResponse {
    private int validationSpamBot = -1;
    private int validationSpamHuman = -1;
    private int validationReiterateTrans = -1;
    private int validationAmountAVG = -1;
    private int validationLegalReq = -1;
    private boolean allValidated;
}
