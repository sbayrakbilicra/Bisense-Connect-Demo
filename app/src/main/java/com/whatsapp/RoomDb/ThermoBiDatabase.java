package com.whatsapp.RoomDb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.whatsapp.Dao.ThermoBiDao;
import com.whatsapp.Dao.UserDao;
import com.whatsapp.Entity.ThermoBiEntity;
import com.whatsapp.Entity.UserEntity;

@Database(entities = {ThermoBiEntity.class, UserEntity.class},version = 6)
public abstract class ThermoBiDatabase extends RoomDatabase {

    private static ThermoBiDatabase instance;

    public abstract ThermoBiDao thermoBiDao();
    public abstract UserDao userDao();

    public static synchronized ThermoBiDatabase getInstance(Context context){
        if(instance ==null){
            instance = Room.databaseBuilder(context.getApplicationContext(), ThermoBiDatabase.class,"thermobi_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    /*private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{
        private ThermoBiDao thermoBiDao;
        private PopulateDbAsyncTask(ThermoBiDatabase db){
         thermoBiDao = db.thermoBiDao();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            thermoBiDao.insert();
            return null;
        }
    }*/

}
