package com.nasr.recipesproject.constant;

import org.springframework.beans.factory.annotation.Value;

public class ConstantField {

    private static String SIGNING_KEY="MTa2jJfTx1nHGabFxqcmOOQ7tqbyXq";

    public static String ACCESS_TOKEN="access_token";
    public static String REFRESH_TOKEN="refresh_token";

    public static Long ACCESS_TOKEN_EXPIRATION_TIME=(long) 10 * 60 * 1000;
    public static Long REFRESH_TOKEN_EXPIRATION_TIME=(long) 20 * 60 * 1000;


    public static String getSigningKey(){
        return SIGNING_KEY;
    }

}
