package com.whatsapp.Model;

public class RefreshAuthenticationModel {
    public String refreshToken;
    public String accessToken;

    public RefreshAuthenticationModel(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

}
