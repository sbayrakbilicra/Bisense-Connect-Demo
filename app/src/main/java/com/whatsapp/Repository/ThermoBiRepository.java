package com.whatsapp.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.whatsapp.Dao.ThermoBiDao;
import com.whatsapp.Entity.ThermoBiEntity;
import com.whatsapp.RoomDb.ThermoBiDatabase;

import java.util.List;

public class ThermoBiRepository {

    private ThermoBiDao thermoBiDao;
    private LiveData<List<ThermoBiEntity>> allThermoBi;

    public ThermoBiRepository(Application application){
        ThermoBiDatabase database = ThermoBiDatabase.getInstance(application);
        thermoBiDao = database.thermoBiDao();
        allThermoBi = thermoBiDao.getAllThermoBi();
    }

    public void insert(ThermoBiEntity thermoBiEntity){
      new InsertThermoBiAsyncTask(thermoBiDao).execute(thermoBiEntity);
    }

    public void update(ThermoBiEntity thermoBiEntity){
     new UpdateThermoBiAsyncTask(thermoBiDao).execute(thermoBiEntity);
    }

    public void delete(ThermoBiEntity thermoBiEntity){
     new DeleteThermoBiAsyncTask(thermoBiDao).execute(thermoBiEntity);
    }

    public void deleteAllThermoBi(){
        new DeleteAllThermoBiAsyncTask(thermoBiDao).execute();
    }

    public void updateQuery(ThermoBiEntity thermoBiEntity){
        new UpdateQueryAsyncTask(thermoBiDao).execute(thermoBiEntity);
    }


    //Bu method için AsyncTask yazmamıza gerek yok. Çünkü LiveData dönüyor. LiveData zaten işlemleri backgroundda yapıyor.
    public LiveData<List<ThermoBiEntity>> getAllThermoBi(){
        return allThermoBi;
    }

    private static class InsertThermoBiAsyncTask extends AsyncTask<ThermoBiEntity,Void,Void>{
       private ThermoBiDao thermoBiDao;
       private InsertThermoBiAsyncTask(ThermoBiDao thermoBiDao){
           this.thermoBiDao = thermoBiDao;
       }
        @Override
        protected Void doInBackground(ThermoBiEntity... thermoBiEntities) {
             thermoBiDao.insert(thermoBiEntities[0]);
            return null;
        }
    }


    private static class UpdateThermoBiAsyncTask extends AsyncTask<ThermoBiEntity,Void,Void>{
        private ThermoBiDao thermoBiDao;
        private UpdateThermoBiAsyncTask(ThermoBiDao thermoBiDao){
            this.thermoBiDao = thermoBiDao;
        }
        @Override
        protected Void doInBackground(ThermoBiEntity... thermoBiEntities) {
            thermoBiDao.update(thermoBiEntities[0]);
            return null;
        }

    }

    private static class DeleteThermoBiAsyncTask extends AsyncTask<ThermoBiEntity,Void,Void>{
        private ThermoBiDao thermoBiDao;

        private DeleteThermoBiAsyncTask(ThermoBiDao thermoBiDao){
            this.thermoBiDao = thermoBiDao;
        }
        @Override
        protected Void doInBackground(ThermoBiEntity... thermoBiEntities) {
            thermoBiDao.delete(thermoBiEntities[0]);
            return null;
        }

    }


    private static class DeleteAllThermoBiAsyncTask extends AsyncTask<Void,Void,Void>{
        private ThermoBiDao thermoBiDao;

        private DeleteAllThermoBiAsyncTask(ThermoBiDao thermoBiDao){
            this.thermoBiDao = thermoBiDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            thermoBiDao.deleteAllThermoBi();
            return null;
        }

    }

    private static class UpdateQueryAsyncTask extends AsyncTask<ThermoBiEntity,Void,Void>{
      private ThermoBiDao thermoBiDao;

        public UpdateQueryAsyncTask(ThermoBiDao thermoBiDao) {
            this.thermoBiDao = thermoBiDao;
        }

        @Override
        protected Void doInBackground(ThermoBiEntity... thermoBiEntities) {
            ThermoBiEntity thermoBiEntity = thermoBiEntities[0];
            thermoBiDao.updateQuery(thermoBiEntity.temperature,thermoBiEntity.macAddress);
            return null;
        }
    }



}
