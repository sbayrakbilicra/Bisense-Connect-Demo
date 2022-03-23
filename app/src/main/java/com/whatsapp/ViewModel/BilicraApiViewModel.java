package com.whatsapp.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.whatsapp.Model.AuthenticateModel;
import com.whatsapp.Model.DeviceCreateModel;
import com.whatsapp.Model.RefreshAuthenticationModel;
import com.whatsapp.Model.SensorDataCreateModel;
import com.whatsapp.Model.SensorsModel;
import com.whatsapp.Model.UserGroupCreateModel;
import com.whatsapp.Repository.BilicraApiRepository;

import okhttp3.ResponseBody;


public class BilicraApiViewModel extends AndroidViewModel {
    private BilicraApiRepository bilicraApiRepository;
    private MutableLiveData<ResponseBody> createNewUserLiveData;
    private MutableLiveData<ResponseBody> createNewDeviceLiveData;
    private MutableLiveData<ResponseBody> createNewSensorLiveData;
    private MutableLiveData<ResponseBody> getAllSensorLiveData;
    private MutableLiveData<ResponseBody> createNewSensorDataLiveData;
    private MutableLiveData<ResponseBody> deleteDeviceLiveData;
    private MutableLiveData<ResponseBody> getAllUserGroupLiveData;
    private MutableLiveData<ResponseBody> deleteUserGroupLiveData;
    private MutableLiveData<ResponseBody> getUserLiveData;
    private MutableLiveData<ResponseBody> createNewUserGroupLiveData;
    private MutableLiveData<ResponseBody> getCurrentLoginInformationsLiveData;
    private MutableLiveData<ResponseBody> refreshAuthenticationLiveData;


    public BilicraApiViewModel(@NonNull Application application) {
        super(application);
        bilicraApiRepository = new BilicraApiRepository(application);
        createNewUserLiveData = new MutableLiveData<>();
        createNewDeviceLiveData= new MutableLiveData<>();
        createNewSensorLiveData = new MutableLiveData<>();
        getAllSensorLiveData = new MutableLiveData<>();
        createNewSensorDataLiveData = new MutableLiveData<>();
        deleteDeviceLiveData = new MutableLiveData<>();
        deleteUserGroupLiveData = new MutableLiveData<>();
        getUserLiveData = new MutableLiveData<>();
        createNewUserGroupLiveData = new MutableLiveData<>();
        getCurrentLoginInformationsLiveData = new MutableLiveData<>();
        refreshAuthenticationLiveData = new MutableLiveData<>();
    }


    public MutableLiveData<ResponseBody> getCreateUserObserver(){
        return bilicraApiRepository.getCreateUserObserver();
    }

    public MutableLiveData<ResponseBody> getCreateDeviceObserver(){
        return bilicraApiRepository.getCreateDeviceObserver();
    }

    public MutableLiveData<ResponseBody> getCreateSensorObserver(){
        return bilicraApiRepository.getCreateSensorObserver();
    }

    public MutableLiveData<ResponseBody> getAllSensorObserver(){
        return bilicraApiRepository.getAllSensorObserver();
    }

    public MutableLiveData<ResponseBody> getCreateNewSensorDataObserver(){
        return bilicraApiRepository.getCreateSensorDataObserver();
    }

    public MutableLiveData<ResponseBody> getDeleteDeviceObserver(){
        return bilicraApiRepository.getDeleteDeviceObserver();
    }

    public MutableLiveData<ResponseBody> getAllUserGroupObserver(){
        return bilicraApiRepository.getAllUserGroupObserver();
    }

    public MutableLiveData<ResponseBody> getDeleteUserGroupObserver(){
        return bilicraApiRepository.getDeleteUserGroupObserver();
    }

    public MutableLiveData<ResponseBody> getUserObserver(){
        return bilicraApiRepository.getUserObserver();
    }

    public MutableLiveData<ResponseBody> createNewUserGroupObserver(){
        return bilicraApiRepository.createNewUserGroupObserver();
    }

    public MutableLiveData<ResponseBody> getCurrentLoginInformationsObserver(){
        return bilicraApiRepository.getCurrentLoginInformationsObserver();
    }
    public MutableLiveData<ResponseBody> refreshAuthenticationObserver(){
        return bilicraApiRepository.refreshAuthenticationObserver();
    }




    public void createNewUser(AuthenticateModel authenticateModel){
       bilicraApiRepository.createNewUser(authenticateModel);
    }

    public void createNewDevice(DeviceCreateModel deviceCreateModel){
        bilicraApiRepository.createNewDevice(deviceCreateModel);
    }

    public void createNewSensor(SensorsModel sensorsModel){
        bilicraApiRepository.createNewSensor(sensorsModel);
    }

    public void getAllSensor(int deviceId){
        bilicraApiRepository.getAllSensor(deviceId);
    }

    public void createNewSensorData(SensorDataCreateModel sensorDataCreateModel){
        bilicraApiRepository.createSensorData(sensorDataCreateModel);
    }

    public void deleteDevice(int deviceId){
        bilicraApiRepository.deleteDevice(deviceId);
    }

    public void getAllUserGroup(){
        bilicraApiRepository.getAllUserGroup();
    }

    public void deleteUserGroup(int id){
        bilicraApiRepository.deleteUserGroup(id);
    }

    public void getUser(String email){
        bilicraApiRepository.getUser(email);
    }

    public void createNewUserGroup(UserGroupCreateModel userGroupCreateModel){
        bilicraApiRepository.createNewUserGroup(userGroupCreateModel);
    }

    public void getCurrentLoginInformations(){
        bilicraApiRepository.getCurrenLoginInformations();
    }

    public void refreshAuthentication(RefreshAuthenticationModel refreshAuthenticationModel){
        bilicraApiRepository.refreshAuthentication(refreshAuthenticationModel);
    }


}
