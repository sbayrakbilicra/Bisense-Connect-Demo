package com.whatsapp.Model;

public class RefreshAuthenticateModel {

    public String refreshToken;
    public String accessToken;

    public RefreshAuthenticateModel(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }


}
