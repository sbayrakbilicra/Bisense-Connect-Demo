package com.whatsapp;

import android.app.AlertDialog;
import android.bluetooth.BluetoothGatt;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.whatsapp.ViewModel.BilicraApiViewModel;
import com.whatsapp.ViewModel.ThermoBiViewModel;
import com.whatsapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

public class AfterConnectActivity extends AppCompatActivity {
     Button detailsActivity;
     ImageView imageView2;
     ImageView imageView3;
     Button thermoBiShareButton;
     Button deleteThermoBiButton;
     ProgressBar detailsProgressBar;
     ProgressBar thermoBiShareProgressBar;
     ThermoBiViewModel thermoBiViewModel;
     BilicraApiViewModel bilicraApiViewModel;
     SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_connect);


        thermoBiViewModel = new ViewModelProvider(AfterConnectActivity.this).get(ThermoBiViewModel.class);
        bilicraApiViewModel = new ViewModelProvider(AfterConnectActivity.this).get(BilicraApiViewModel.class);
        sharedPreferences = this.getSharedPreferences("com.okankocakose.wearosdeneme", MODE_PRIVATE);

        detailsActivity = findViewById(R.id.detailsButton);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        thermoBiShareButton= findViewById(R.id.shareButton);
        deleteThermoBiButton = findViewById(R.id.deleteThermoBi);
        detailsProgressBar = findViewById(R.id.deviceDetailProgressBar);
        thermoBiShareProgressBar = findViewById(R.id.shareThermoBiProgressBar);



        detailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsActivity.setVisibility(View.INVISIBLE);
                detailsProgressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(AfterConnectActivity.this, ThermoBiDetailsActivity.class);
                startActivity(intent);
            }
        });

        deleteThermoBiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(AfterConnectActivity.this,AlertDialog.THEME_HOLO_DARK);
                builder.setTitle("ThermoBi Sil");
                builder.setIcon(R.drawable.thermometerrr);
                builder.setMessage("ThermoBi cihazını silmek istiyor musunuz?");
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            BluetoothLeService.globalBluetoothGatt.disconnect();
                            BluetoothLeService.globalBluetoothGatt.close();

                            /*for(BluetoothGatt bluetoothGatt : BluetoothLeService.bluetoothGattList){
                                bluetoothGatt.disconnect();
                                bluetoothGatt.close();
                                System.out.println("deleteBluetoothGatt: "+bluetoothGatt);
                                BluetoothLeService.bluetoothGattList.remove(bluetoothGatt);
                            }*/
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                        //Delete ThermoBi from Database
                        thermoBiViewModel.deleteAllThermoBi();

                        //Delete ThermoBi from SharedPreferences
                        sharedPreferences.edit().remove("deviceId").apply();
                        sharedPreferences.edit().remove("sensorId").apply();
                        sharedPreferences.edit().remove("firmwareVersiyon").apply();
                        sharedPreferences.edit().remove("batteryLevel").apply();

                        //Delete ThermoBi from API
                        //deleteDevice();
                        deleteDeviceFromAPI();

                        //Delete UserGroup from API
                        //getAllUserGroup();
                        getAllUserGroupFromAPI();



                    }
                });

                builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }
                });
                builder.show();




            }
        });


       thermoBiShareButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               thermoBiShareButton.setVisibility(View.INVISIBLE);
               thermoBiShareProgressBar.setVisibility(View.VISIBLE);
               Intent intent = new Intent(AfterConnectActivity.this, ThermoBiShareActivity.class);
               startActivity(intent);
           }
       });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        /*backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });*/

        //firmwareVersion.setText(BluetoothLeService.firmwareVersionString);

    }


    @Override
    protected void onStop() {
        super.onStop();
        detailsProgressBar.setVisibility(View.INVISIBLE);
        detailsActivity.setVisibility(View.VISIBLE);

        thermoBiShareProgressBar.setVisibility(View.INVISIBLE);
        thermoBiShareButton.setVisibility(View.VISIBLE);

    }




    private void deleteDeviceFromAPI(){
        int deviceId = sharedPreferences.getInt("deviceId",0);
        System.out.println("deviceIdMethod: "+deviceId);
        bilicraApiViewModel.deleteDevice(deviceId);

        bilicraApiViewModel.getDeleteDeviceObserver().observe(AfterConnectActivity.this, new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                if(responseBody==null){
                    System.out.println("deleteDeviceFromAPI Başarısız");
                }else{
                    System.out.println("deleteDeviceFromAPI Başarılı");
                }
            }
        });

    }

    private void getAllUserGroupFromAPI(){

        bilicraApiViewModel.getAllUserGroup();
        bilicraApiViewModel.getAllUserGroupObserver().observe(AfterConnectActivity.this, new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                if(responseBody==null){
                    System.out.println("getAllUserGroupFromAPI başarısız");
                }else{
                    System.out.println("getAllUserGroupFromAPI başarılı");
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody.string());
                        System.out.println("GetAllUserGroup: "+jsonObject);


                        for(int i =0 ; i<jsonObject.getJSONObject("result").getJSONArray("items").length(); i++){
                            System.out.println("getAllUserGroupEmailAdresses "+  jsonObject.getJSONObject("result").getJSONArray("items").getJSONObject(i).getJSONArray("userGroupUsers").getJSONObject(0).getJSONObject("user").getString("emailAddress"));

                            int userIdUserGroup = jsonObject.getJSONObject("result").getJSONArray("items").getJSONObject(i).getInt("id");
                            System.out.println("UserIdUserGroup: "+userIdUserGroup);
                             //DeleteUserGroup Çağırımı
                            deleteUserGroupFromAPI(userIdUserGroup);


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        });

    }


private void deleteUserGroupFromAPI(int id){
        bilicraApiViewModel.deleteUserGroup(id);
        bilicraApiViewModel.getDeleteUserGroupObserver().observe(AfterConnectActivity.this, new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                if(responseBody ==null){
                    System.out.println("deleteUserGroupFromAPI başarısız");
                }else{
                    System.out.println("deleteUserGroupFromAPI başarılı");
                }
            }
        });
}












   /* public void deleteDevice(){

        int deviceId = sharedPreferences.getInt("deviceId",0);
        System.out.println("deviceIdMethod: "+deviceId);
        Call<ResponseBody> deleteDevice = RetrofitClient.getInstance().getMyApi().deleteDevice(deviceId);
        deleteDevice.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    System.out.println("Delete Device Başarılı");
                }else{
                    System.out.println("Delete Device Başarısız");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Delete Device Fail");
            }
        });


    }*/






   /* public void getAllUserGroup(){

        Call<ResponseBody> getAllUserGroup = RetrofitClient.getInstance().getMyApi().getAllUserGroup();
        getAllUserGroup.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    System.out.println("UserGroupGetAll Başarılı");
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        System.out.println("GetAllUserGroup: "+jsonObject);



                        for(int i =0 ; i<jsonObject.getJSONObject("result").getJSONArray("items").length(); i++){
                            System.out.println("getAllUserGroupEmailAdresses "+  jsonObject.getJSONObject("result").getJSONArray("items").getJSONObject(i).getJSONArray("userGroupUsers").getJSONObject(0).getJSONObject("user").getString("emailAddress"));

                            int userIdUserGroup = jsonObject.getJSONObject("result").getJSONArray("items").getJSONObject(i).getInt("id");
                            System.out.println("UserIdUserGroup: "+userIdUserGroup);
                            deleteUserGroup(userIdUserGroup);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else{
                    System.out.println("UserGroupGetAll Başarısız");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("UserGroupGetAll Fail");
            }
        });



    }*/


    /*public void deleteUserGroup(int id){

        Call<ResponseBody> deleteUserGroup = RetrofitClient.getInstance().getMyApi().deleteUserGroup(id);
        deleteUserGroup.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    System.out.println("DeleteUserGroup Başarılı");

                }else{
                    System.out.println("DeleteUserGroup Başarısız");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("DeleteUserGroup Fail");

            }
        });


    }*/






}