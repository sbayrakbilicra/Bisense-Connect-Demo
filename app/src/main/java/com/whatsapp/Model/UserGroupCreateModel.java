package com.whatsapp.Model;

import com.google.gson.JsonArray;

public class UserGroupCreateModel {
    public int ownerUserId;
    public String name;
    public JsonArray permissions;
    public JsonArray userGroupUsers;

    public UserGroupCreateModel(int ownerUserId, String name, JsonArray permissions, JsonArray userGroupUsers) {
        this.ownerUserId = ownerUserId;
        this.name = name;
        this.permissions = permissions;
        this.userGroupUsers = userGroupUsers;
    }
}
