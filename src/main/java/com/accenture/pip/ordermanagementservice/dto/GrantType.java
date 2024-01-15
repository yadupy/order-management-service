package com.accenture.pip.ordermanagementservice.dto;

public enum GrantType {
    CLIENT_CREDENTIALS("client_credentials"),
    PASSWORD("password"),
    REFRESH_TOKEN("refresh_token");

    public final String grantType;

    GrantType(String grantType){
        this.grantType = grantType;
    }
}
