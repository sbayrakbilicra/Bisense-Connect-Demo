package com.whatsapp.Model;

public class AuthenticateModel {
    public String userNameOrEmailAddress;
    public String password;

    public AuthenticateModel(){

    }

    public AuthenticateModel(String userNameOrEmailAddress, String password) {
        this.userNameOrEmailAddress = userNameOrEmailAddress;
        this.password = password;
    }
}
