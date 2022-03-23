package com.whatsapp.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.whatsapp.Dao.UserDao;
import com.whatsapp.Entity.UserEntity;
import com.whatsapp.RoomDb.ThermoBiDatabase;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<UserEntity>> allUser;

    public UserRepository(Application application){
        ThermoBiDatabase thermoBiDatabase = ThermoBiDatabase.getInstance(application);
        userDao = thermoBiDatabase.userDao();
        allUser = userDao.getAllUser();
    }

    public LiveData<List<UserEntity>> getAllUser() {
        return allUser;
    }

   public void insertUser(UserEntity userEntity){
        new InsertUserAsyncTask(userDao).execute(userEntity);
   }

   public void deleteUser(){
        new DeleteUserAsyncTask(userDao).execute();
   }

  public void updateAccessToken(UserEntity userEntity){
        new UpdateAccessTokenAsyncTask(userDao).execute(userEntity);
  }

   private static class InsertUserAsyncTask extends AsyncTask<UserEntity,Void,Void> {
        private UserDao userDao;
        private InsertUserAsyncTask(UserDao userDao){
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(UserEntity... userEntities) {
            userDao.insertUser(userEntities[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<Void,Void,Void>{
        private UserDao userDao;

        private DeleteUserAsyncTask(UserDao userDao){
            this.userDao =userDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.deleteUser();
            return null;
        }
    }


    private static class UpdateAccessTokenAsyncTask extends AsyncTask<UserEntity,Void,Void>{
        private UserDao userDao;

        private UpdateAccessTokenAsyncTask(UserDao userDao){
            this.userDao = userDao;
        }


        @Override
        protected Void doInBackground(UserEntity... userEntities) {
            UserEntity userEntity = userEntities[0];
            userDao.updateAccessToken(userEntity.accessToken);
            return null;
        }
    }

}
