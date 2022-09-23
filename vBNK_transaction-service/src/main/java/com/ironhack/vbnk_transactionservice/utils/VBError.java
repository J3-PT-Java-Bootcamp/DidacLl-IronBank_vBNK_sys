package com.ironhack.vbnk_transactionservice.utils;

import lombok.Getter;

@Getter
public enum VBError {
    NOT_ENOUGH_FOUNDS("AC401",
            "Transaction failed. Check your balance to ensure you've got founds enough for this transaction."),
    USER_NOT_FOUND("US404",
            "Provided user not found, please contact your account manager."),
    UNAVAILABLE_ACCOUNT("AC500",
            "Selected account is not available. Please, contact your account manager."),
    ACCOUNT_NOT_FOUND("AC404"
            , "Provided account doesn't exist, please try with another one."),
    AF_1("AF101",
            "For security reasons this operation is not allowed. Please try it later."),
    AF_2("AF202",
            "For security reasons this operation is not allowed. Contact your account manager for more information"),
    AF_3("AF303",
            "Suspicious account activity detected. Your account is temporally block for security reasons. Please contact with your account manager."),
    FATAL_ERROR("ERR00", "Uncontrolled error, please try again later.");
    private final String message, errorCode;

    VBError(String errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }
}
