package com.whatsapp.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.whatsapp.Entity.UserEntity;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insertUser(UserEntity userEntity);

    @Query("DELETE FROM user_table")
    void deleteUser();

    @Query("SELECT * FROM user_table")
    LiveData<List<UserEntity>> getAllUser();

    @Query("UPDATE user_table SET accessToken= :accessToken")
    void updateAccessToken(String accessToken);

}
