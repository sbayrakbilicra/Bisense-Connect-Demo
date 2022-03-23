package com.whatsapp.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    public int idUserPrimary;

    @ColumnInfo(name = "userId")
    public int userId;

    @ColumnInfo(name = "accessToken")
    public String accessToken;


    public UserEntity(int userId, String accessToken) {
        this.userId = userId;
        this.accessToken = accessToken;
    }


}
