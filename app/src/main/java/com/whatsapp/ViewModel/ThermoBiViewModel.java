package com.whatsapp.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.whatsapp.Entity.ThermoBiEntity;
import com.whatsapp.Repository.ThermoBiRepository;

import java.util.List;

public class ThermoBiViewModel extends AndroidViewModel {

    private ThermoBiRepository thermoBiRepository;
    private LiveData<List<ThermoBiEntity>> allThermoBi;


    public ThermoBiViewModel(@NonNull Application application) {
        super(application);
        thermoBiRepository = new ThermoBiRepository(application);
        allThermoBi = thermoBiRepository.getAllThermoBi();
    }

    public void insert(ThermoBiEntity thermoBiEntity){
        thermoBiRepository.insert(thermoBiEntity);
    }

    public void update(ThermoBiEntity thermoBiEntity){
        thermoBiRepository.update(thermoBiEntity);
    }

    public void delete(ThermoBiEntity thermoBiEntity){
        thermoBiRepository.delete(thermoBiEntity);
    }

    public void deleteAllThermoBi(){
        thermoBiRepository.deleteAllThermoBi();
    }

    public LiveData<List<ThermoBiEntity>> getAllThermobi(){
        return allThermoBi;
    }

    public void updateQuery(ThermoBiEntity thermoBiEntity){
        thermoBiRepository.updateQuery(thermoBiEntity);
    }



}
