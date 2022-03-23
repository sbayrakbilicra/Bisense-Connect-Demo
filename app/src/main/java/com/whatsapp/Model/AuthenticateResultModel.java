package com.whatsapp.Model;

import com.google.gson.annotations.SerializedName;

public class AuthenticateResultModel {

    @SerializedName("accessToken")
    public String accessToken;

    @SerializedName("encryptedAccessToken")
    public String encryptedAccessToken;

    @SerializedName("expireInSeconds")
    public int expireInSeconds;

    @SerializedName("userID")
    public int userID;

    @SerializedName("refreshToken")
    public String refreshToken;



}
