package com.whatsapp.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.whatsapp.Entity.UserEntity;
import com.whatsapp.Repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private LiveData<List<UserEntity>> allUser;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        allUser = userRepository.getAllUser();
    }

    public LiveData<List<UserEntity>> getAllUser(){
       return allUser;
    }

    public void insertUser(UserEntity userEntity){
        userRepository.insertUser(userEntity);
    }

    public void deleteUser(){
        userRepository.deleteUser();
    }

    public void updateAccessToken(UserEntity userEntity){
        userRepository.updateAccessToken(userEntity);
    }

    public void updateRefreshToken(UserEntity userEntity){
        userRepository.updateRefreshToken(userEntity);
    }

}
