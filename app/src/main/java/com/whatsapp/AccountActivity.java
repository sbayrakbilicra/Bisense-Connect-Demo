package com.whatsapp;

import android.app.AlertDialog;
import android.bluetooth.BluetoothGatt;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.whatsapp.Entity.UserEntity;
import com.whatsapp.ViewModel.BilicraApiViewModel;
import com.whatsapp.ViewModel.ThermoBiViewModel;
import com.whatsapp.ViewModel.UserViewModel;
import com.whatsapp.R;
import com.whatsapp.databinding.ActivityAccountBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;

public class AccountActivity extends AppCompatActivity {

     ActivityAccountBinding activityAccountBinding;
     ThermoBiViewModel thermoBiViewModel;
     UserViewModel userViewModel;
     BilicraApiViewModel bilicraApiViewModel;
     int userId;
     String userName;
     String emailAddress;
     SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAccountBinding = ActivityAccountBinding.inflate(getLayoutInflater());
        View view = activityAccountBinding.getRoot();
        setContentView(view);

        sharedPreferences = this.getSharedPreferences("com.okankocakose.wearosdeneme", MODE_PRIVATE);
        userViewModel = new ViewModelProvider(AccountActivity.this).get(UserViewModel.class);
        bilicraApiViewModel = new ViewModelProvider(AccountActivity.this).get(BilicraApiViewModel.class);
        thermoBiViewModel = new ViewModelProvider(AccountActivity.this).get(ThermoBiViewModel.class);
        userViewModel.getAllUser().observe(AccountActivity.this, new Observer<List<UserEntity>>() {
            @Override
            public void onChanged(List<UserEntity> userEntities) {
                /*for (int i=0;i<userEntities.size();i++){
                    System.out.println("userId: "+userEntities.get(i).userId);
                    System.out.println("AccessToken: "+userEntities.get(i).accessToken);
                }*/
                if(userEntities.size()>0){
                    userId = userEntities.get(0).userId;
                    System.out.println("UserID: "+userId);
                }

            }
        });




       activityAccountBinding.imageView17.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onBackPressed();
               finish();
           }
       });

       getUserFromAPI();

       activityAccountBinding.logOut.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this,AlertDialog.THEME_HOLO_DARK);
               builder.setTitle("Oturumu Kapat");
               builder.setIcon(R.drawable.thermometerrr);
               builder.setMessage("Oturumu Kapatmak İstiyor Musunuz ? ");
               builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                       //Delete ThermoBi from SharedPreferences
                       sharedPreferences.edit().remove("deviceId").apply();
                       sharedPreferences.edit().remove("sensorId").apply();
                       sharedPreferences.edit().remove("firmwareVersiyon").apply();
                       sharedPreferences.edit().remove("batteryLevel").apply();

                       thermoBiViewModel.deleteAllThermoBi();

                       userViewModel.deleteUser();
                       try {
                           BluetoothLeService.globalBluetoothGatt.disconnect();
                           BluetoothLeService.globalBluetoothGatt.close();
                           /*for(BluetoothGatt bluetoothGatt:BluetoothLeService.bluetoothGattList){
                               bluetoothGatt.disconnect();
                               bluetoothGatt.close();
                               BluetoothLeService.bluetoothGattList.remove(bluetoothGatt);
                           }*/

                       }catch (Exception e){
                           e.printStackTrace();
                       }

                       Intent intent = new Intent(AccountActivity.this, BeforeLoginActivity.class);
                       startActivity(intent);
                       finishAffinity();

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

    }




    private void getUserFromAPI(){
    bilicraApiViewModel.getCurrentLoginInformations();
    bilicraApiViewModel.getCurrentLoginInformationsObserver().observe(AccountActivity.this, new Observer<ResponseBody>() {
        @Override
        public void onChanged(ResponseBody responseBody) {
            if(responseBody==null){
                System.out.println("getCurrentLoginInformations Başarısız");
            }else{
                System.out.println("getCurrentLoginInformations Başarılı");
                try {
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    System.out.println("JsonObject: "+jsonObject);
                    userName = jsonObject.getJSONObject("result").getJSONObject("user").getString("userName");
                    emailAddress =  jsonObject.getJSONObject("result").getJSONObject("user").getString("emailAddress");
                    System.out.println("username: "+userName);
                    System.out.println("emailAddress: "+emailAddress);
                    activityAccountBinding.userNameText.setText(userName);
                    activityAccountBinding.emailAddressText.setText(emailAddress);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    });

    }



}