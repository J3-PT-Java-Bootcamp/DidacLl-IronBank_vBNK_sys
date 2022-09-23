package com.ironhack.vbnk_dataservice.utils;

import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VBNKConfig {
    public static final String VBNK_ENTITY_CODE="1312";
    public static final String VBNK_INTERNATIONAL_CODE ="ES51";

    public static final BigDecimal VBNK_PENALTY_FEE = BigDecimal.valueOf(40);
    public static final Money VBNK_CHECKING_MIN_BALANCE = new Money(BigDecimal.valueOf(250));
    public static final BigDecimal VBNK_MONTH_MAINTENANCE_FEE = BigDecimal.valueOf(12);

    public static final String VBNK_MIN_CREDIT_LIMIT = "100.00";
    public static final String VBNK_CURRENCY_DEF = "EUR";
    public static final String VBNK_MAX_INTEREST_RATE = "0.2";
    public static final String VBNK_MIN_INTEREST_RATE = "0.1";
    public static final String VBNK_MAX_SAVINGS_INTEREST_RATE = "0.5";
    public static final String VBNK_MAX_SAVINGS_MINIMUM_BALANCE = "1000.00";
    public static final String VBNK_MIN_SAVINGS_MINIMUM_BALANCE = "100.00";
    public static final String VBNK_MIN_SAVINGS_INTEREST_RATE = "0.0025";
    public static final String VBNK_MAX_CREDIT_LIMIT = "100000.00";
    public static final String VBNK_ENTITY_NAME = "vBNK.sys";

    private static final String VBNK_MAIN_ACCOUNT = "vbnk_main";
    public static List<Character> getNumbersFromId(String userId){
        var chrLst= userId.trim().toCharArray();
        var retVal= new ArrayList<Character>();
        for(char ch : chrLst)if(Character.isDigit(ch))retVal.add(ch);
        return retVal;
    }

    public static String getUserIdFromAuth(Authentication auth){
        RefreshableKeycloakSecurityContext context = (RefreshableKeycloakSecurityContext) auth.getCredentials();
        return context.getToken().getSubject();
    }
    public static String getUsernameFromAuth(Authentication auth){
        RefreshableKeycloakSecurityContext context = (RefreshableKeycloakSecurityContext) auth.getCredentials();
        return context.getToken().getPreferredUsername();
    }
    public static String getTokenFromAuth(Authentication auth){
        RefreshableKeycloakSecurityContext context = (RefreshableKeycloakSecurityContext) auth.getCredentials();
        return context.getTokenString();
    }
    public static boolean isAdmin(Authentication auth){
        RefreshableKeycloakSecurityContext context = (RefreshableKeycloakSecurityContext) auth.getCredentials();
        var roles= context.getToken().getResourceAccess().get("vbnk-sys").getRoles();
        return roles.contains("admin");
    }

}
