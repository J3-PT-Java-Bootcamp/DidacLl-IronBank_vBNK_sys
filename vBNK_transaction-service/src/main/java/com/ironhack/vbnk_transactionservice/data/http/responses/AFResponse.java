package com.ironhack.vbnk_transactionservice.data.http.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AFResponse {
    private int validationSpamBot=-1;
    private int validationSpamHuman=-1;
    private int validationReiterateTrans=-1;
    private int validationAmountAVG=-1;
    private int validationLegalReq=-1;

    private boolean allValidated;

    public boolean isAllValidated(){
        return !(validationSpamBot<0
                ||validationSpamHuman<0
                ||validationReiterateTrans<0
                || validationAmountAVG<0
                ||validationLegalReq<0);
    }
    public boolean isAllOK(){
        return (validationSpamBot
                +validationSpamHuman
                +validationReiterateTrans
                + validationAmountAVG
                +validationLegalReq)==0;
    }
    public int getFraudLevel(){
        int total = (validationSpamBot
                +validationSpamHuman
                +validationReiterateTrans
                + validationAmountAVG
                +validationLegalReq);
        return Math.min(Math.floorDiv(total,2),3);
    }
}
