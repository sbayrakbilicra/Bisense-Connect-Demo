package com.whatsapp.Repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.whatsapp.Client.RetrofitClient;
import com.whatsapp.Model.AuthenticateModel;
import com.whatsapp.Model.DeviceCreateModel;
import com.whatsapp.Model.RefreshAuthenticationModel;
import com.whatsapp.Model.SensorDataCreateModel;
import com.whatsapp.Model.SensorsModel;
import com.whatsapp.Model.UserGroupCreateModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BilicraApiRepository {
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

    public BilicraApiRepository(Application application) {
        //Her bir api isteği için MutabLiveData oluştur
        createNewUserLiveData = new MutableLiveData<>();
        createNewDeviceLiveData = new MutableLiveData<>();
        createNewSensorLiveData = new MutableLiveData<>();
        getAllSensorLiveData = new MutableLiveData<>();
        createNewSensorDataLiveData = new MutableLiveData<>();
        deleteDeviceLiveData = new MutableLiveData<>();
        getAllUserGroupLiveData = new MutableLiveData<>();
        deleteUserGroupLiveData = new MutableLiveData<>();
        getUserLiveData = new MutableLiveData<>();
        createNewUserGroupLiveData = new MutableLiveData<>();
        getCurrentLoginInformationsLiveData = new MutableLiveData<>();
        refreshAuthenticationLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<ResponseBody> getCreateUserObserver(){
         return createNewUserLiveData;
    }

    public MutableLiveData<ResponseBody> getCreateDeviceObserver(){
        return createNewDeviceLiveData;
    }

    public MutableLiveData<ResponseBody> getCreateSensorObserver(){
        return createNewSensorLiveData;
    }

    public MutableLiveData<ResponseBody> getAllSensorObserver(){
        return getAllSensorLiveData;
    }

    public MutableLiveData<ResponseBody> getCreateSensorDataObserver(){
        return createNewSensorDataLiveData;
    }

    public MutableLiveData<ResponseBody> getDeleteDeviceObserver(){
        return deleteDeviceLiveData;
    }

    public MutableLiveData<ResponseBody> getAllUserGroupObserver(){
        return getAllUserGroupLiveData;
    }

    public MutableLiveData<ResponseBody> getDeleteUserGroupObserver(){
        return deleteUserGroupLiveData;
    }

    public MutableLiveData<ResponseBody> getUserObserver(){
        return getUserLiveData;
    }

    public MutableLiveData<ResponseBody> createNewUserGroupObserver(){
        return createNewUserGroupLiveData;
    }

    public MutableLiveData<ResponseBody> getCurrentLoginInformationsObserver(){
        return getCurrentLoginInformationsLiveData;
    }

    public MutableLiveData<ResponseBody> refreshAuthenticationObserver(){
        return refreshAuthenticationLiveData;
    }

    public void createNewUser(AuthenticateModel authenticateModel){
        Call<ResponseBody> userLogin1 = RetrofitClient.getInstance().getMyApi().userLogin(authenticateModel);
        userLogin1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()){
                    createNewUserLiveData.postValue(response.body());
                } else if(response.code()==401){

                    createNewUserLiveData.postValue(null);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                createNewUserLiveData.postValue(null);
            }
        });
    }


    public void createNewDevice(DeviceCreateModel deviceCreateModel){
        Call<ResponseBody> createNewDevice = RetrofitClient.getInstance().getMyApi().deviceCreate(deviceCreateModel);
        createNewDevice.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    createNewDeviceLiveData.postValue(response.body());
                }else if(response.code()==401){
                    createNewDeviceLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                createNewDeviceLiveData.postValue(null);
            }
        });
    }

    public void createNewSensor(SensorsModel sensorsModel){
        Call<ResponseBody> createNewSensor = RetrofitClient.getInstance().getMyApi().sensorCreate(sensorsModel);
        createNewSensor.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    createNewSensorLiveData.postValue(response.body());
                }else{
                    createNewSensorLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                 createNewSensorLiveData.postValue(null);
            }
        });
    }

    public void getAllSensor(int deviceId){
        Call<ResponseBody> getAllSensor = RetrofitClient.getInstance().getMyApi().sensorGetAll(deviceId);
        getAllSensor.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    getAllSensorLiveData.postValue(response.body());
                }else{
                    getAllSensorLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getAllSensorLiveData.postValue(null);
            }
        });
    }

   public void createSensorData(SensorDataCreateModel sensorDataCreateModel){
        Call<ResponseBody> createSensorData = RetrofitClient.getInstance().getMyApi().sensorDataCreate(sensorDataCreateModel);
        createSensorData.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    createNewSensorDataLiveData.postValue(response.body());
                }else{
                    createNewSensorDataLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                createNewSensorDataLiveData.postValue(null);
            }
        });
   }

   public void deleteDevice(int deviceId){
        Call<ResponseBody> deleteDevice = RetrofitClient.getInstance().getMyApi().deleteDevice(deviceId);
        deleteDevice.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    deleteDeviceLiveData.postValue(response.body());
                }else{
                    System.out.println("DeleteDevice: "+response.code());
                    deleteDeviceLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("DeleteDevice Fail");
                deleteDeviceLiveData.postValue(null);
            }
        });
   }

   public void getAllUserGroup(){
        Call<ResponseBody> getAllUserGroup = RetrofitClient.getInstance().getMyApi().getAllUserGroup();
        getAllUserGroup.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    getAllUserGroupLiveData.postValue(response.body());
                }else{
                    getAllUserGroupLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getAllUserGroupLiveData.postValue(null);
            }
        });
   }

   public void deleteUserGroup(int id){
        Call<ResponseBody> deleteUserGroup = RetrofitClient.getInstance().getMyApi().deleteUserGroup(id);
        deleteUserGroup.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    deleteUserGroupLiveData.postValue(response.body());

                }else{
                    deleteUserGroupLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                deleteUserGroupLiveData.postValue(null);
            }
        });

   }

   public void getUser(String email){
        Call<ResponseBody> getUser = RetrofitClient.getInstance().getMyApi().userGet(email);
        getUser.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    getUserLiveData.postValue(response.body());
                }else{
                    getUserLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                  getUserLiveData.postValue(null);
            }
        });
   }

   public void createNewUserGroup(UserGroupCreateModel userGroupCreateModel){
        Call<ResponseBody> createNewUserGroup = RetrofitClient.getInstance().getMyApi().userGroupCreate(userGroupCreateModel);
        createNewUserGroup.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    createNewUserGroupLiveData.postValue(response.body());
                }else{
                    createNewUserGroupLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                createNewUserGroupLiveData.postValue(null);
            }
        });
   }

   public void getCurrenLoginInformations(){
        Call<ResponseBody> getCurrentLoginInformations = RetrofitClient.getInstance().getMyApi().getCurrentLoginInformations();
        getCurrentLoginInformations.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    getCurrentLoginInformationsLiveData.postValue(response.body());
                }else{
                    getCurrentLoginInformationsLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getCurrentLoginInformationsLiveData.postValue(null);
            }
        });
   }

   public void refreshAuthentication(RefreshAuthenticationModel refreshAuthenticationModel){
        Call<ResponseBody> refreshAuthentication = RetrofitClient.getInstance().getMyApi().refreshAuthentication(refreshAuthenticationModel);
        refreshAuthentication.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    System.out.println("refreshAuthentication Başarılı");
                    refreshAuthenticationLiveData.postValue(response.body());
                }else{
                    System.out.println("refreshAuthentication Başarısız");
                    refreshAuthenticationLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("refreshAuthentication Fail");
                    refreshAuthenticationLiveData.postValue(null);
            }
        });

   }





}
