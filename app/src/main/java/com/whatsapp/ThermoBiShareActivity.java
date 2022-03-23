package com.whatsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.whatsapp.Client.RetrofitClient;
import com.whatsapp.Model.PermissionsModel;
import com.whatsapp.Model.UserGroupCreateModel;
import com.whatsapp.Model.UserGroupUsersModel;
import com.whatsapp.ViewModel.BilicraApiViewModel;
import com.whatsapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThermoBiShareActivity extends AppCompatActivity {
    ImageView backImageView;
    EditText emailEditText;
    Button shareButton;
    String emailAddress;
    Button sharingListButton;
    SharedPreferences sharedPreferences;
    String userGroupEmailAddress;
    BilicraApiViewModel bilicraApiViewModel;
    int deviceId;
    int sensorId;
    public int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermo_bi_share);
        bilicraApiViewModel = new ViewModelProvider(ThermoBiShareActivity.this).get(BilicraApiViewModel.class);
        sharedPreferences = this.getSharedPreferences("com.okankocakose.wearosdeneme", MODE_PRIVATE);

         deviceId = sharedPreferences.getInt("deviceId",0);
         sensorId = sharedPreferences.getInt("sensorId",0);

        System.out.println("ThermoBiShareActivity sharedPreferences deviceId: "+deviceId);
        System.out.println("ThermoBiShareActivity sharedPreferences sensorId: "+sensorId);

        backImageView = findViewById(R.id.imageView6);
        emailEditText = findViewById(R.id.editText);
        shareButton = findViewById(R.id.ThermoBiShareButton);
        sharingListButton = findViewById(R.id.sharingListButton);

        sharingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThermoBiShareActivity.this, SharingListActivity.class);
                startActivity(intent);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailAddress = emailEditText.getText().toString();
                getAllUserGroup(emailAddress);
                //getAllUserGroupFromAPI(emailAddress);
            }
        });

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }



    /*private void getAllUserGroupFromAPI(String emailAddressParameter){
        bilicraApiViewModel.getAllUserGroup();
        bilicraApiViewModel.getAllUserGroupObserver().observe(ThermoBiShareActivity.this, new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                if(responseBody==null){
                    System.out.println("getAllUserGroupFromAPI başarısız");
                }else{
                    System.out.println("getAllUserGroupFromAPI başarılı");
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody.string());
                        System.out.println("GetAllUserGroup: "+jsonObject);

                        boolean key = true;

                        for(int i =0 ; i<jsonObject.getJSONObject("result").getJSONArray("items").length(); i++){
                            System.out.println("getAllUserGroupEmailAdresses "+  jsonObject.getJSONObject("result").getJSONArray("items").getJSONObject(i).getJSONArray("userGroupUsers").getJSONObject(0).getJSONObject("user").getString("emailAddress"));
                            userGroupEmailAddress = jsonObject.getJSONObject("result").getJSONArray("items").getJSONObject(i).getJSONArray("userGroupUsers").getJSONObject(0).getJSONObject("user").getString("emailAddress");
                            if(emailAddressParameter.equals(userGroupEmailAddress)){
                                Toast.makeText(getApplicationContext(), "Cihaz Bu Kullanıcı İle Zaten Paylaşıldı", Toast.LENGTH_SHORT).show();
                                key = false;
                                break;
                            }


                        }

                        if(key){
                            //userGet(emailAddressParameter);
                            //Burada getUser Çağıracaksın
                            getUserFromAPI(emailAddressParameter);

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


    private void getUserFromAPI(String emailAddressParameter){
        bilicraApiViewModel.getUser(emailAddressParameter);
        bilicraApiViewModel.getUserObserver().observe(ThermoBiShareActivity.this, new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                if(responseBody == null){
                    System.out.println("getUserFromAPI başarısız");
                    //Kullanıcı emaili yanlış girerse burası çalışıcak.
                    Intent intent = new Intent(ThermoBiShareActivity.this,WrongEmailAddressActivity.class);
                    startActivity(intent);
                }else{
                    System.out.println("UserGet Başarılı");
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody.string());
                        userId = jsonObject.getJSONObject("result").getInt("id");
                        System.out.println("UserId:"+userId);

                        //createUserGroup burada çağıracaksın
                        //userGroupCreate();
                        createNewUserGroup();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


    private void createNewUserGroup(){
        PermissionsModel permissionsModel = new PermissionsModel(deviceId,sensorId,2);
        ArrayList<PermissionsModel> permissionsModelArrayList = new ArrayList<PermissionsModel>();
        permissionsModelArrayList.add(permissionsModel);
        Gson gson = new GsonBuilder().create();
        JsonArray jsonArray = gson.toJsonTree(permissionsModelArrayList).getAsJsonArray();
        System.out.println(jsonArray);

        UserGroupUsersModel userGroupUsersModel = new UserGroupUsersModel(userId);
        ArrayList<UserGroupUsersModel> userGroupUsersModelArrayList = new ArrayList<UserGroupUsersModel>();
        userGroupUsersModelArrayList.add(userGroupUsersModel);
        Gson gson1 = new GsonBuilder().create();
        JsonArray jsonArray1= gson1.toJsonTree(userGroupUsersModelArrayList).getAsJsonArray();
        System.out.println(jsonArray1);

        UserGroupCreateModel userGroupCreateModel = new UserGroupCreateModel(LoginActivity.ownerUserId,"OkanUserGroup",jsonArray,jsonArray1);

        bilicraApiViewModel.createNewUserGroup(userGroupCreateModel);
        bilicraApiViewModel.createNewUserGroupObserver().observe(ThermoBiShareActivity.this, new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                if(responseBody==null){
                    System.out.println("createNewUserGroup başarısız");
                }else{
                    System.out.println("createNewUserGroup başarılı");
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody.string());
                        System.out.println(jsonObject);
                        Toast.makeText(getApplicationContext(), "Paylaşım Gerçekleştirildi", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }*/











    public void getAllUserGroup(String emailAddressParameter){

        Call<ResponseBody> getAllUserGroup = RetrofitClient.getInstance().getMyApi().getAllUserGroup();
        getAllUserGroup.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    System.out.println("UserGroupGetAll Başarılı");
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        System.out.println("GetAllUserGroup: "+jsonObject);

                         boolean key = true;

                        for(int i =0 ; i<jsonObject.getJSONObject("result").getJSONArray("items").length(); i++){
                            System.out.println("getAllUserGroupEmailAdresses "+  jsonObject.getJSONObject("result").getJSONArray("items").getJSONObject(i).getJSONArray("userGroupUsers").getJSONObject(0).getJSONObject("user").getString("emailAddress"));
                             userGroupEmailAddress = jsonObject.getJSONObject("result").getJSONArray("items").getJSONObject(i).getJSONArray("userGroupUsers").getJSONObject(0).getJSONObject("user").getString("emailAddress");
                            if(emailAddressParameter.equals(userGroupEmailAddress)){
                                Toast.makeText(getApplicationContext(), "Cihaz Bu Kullanıcı İle Zaten Paylaşıldı", Toast.LENGTH_SHORT).show();
                                 key = false;
                                 break;
                            }


                        }

                        if(key){
                            userGet(emailAddressParameter);
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



    }



    public void userGet(String email){

        Call<ResponseBody> userGet = RetrofitClient.getInstance().getMyApi().userGet(email);
        userGet.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    System.out.println("UserGet Başarılı");
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                         userId = jsonObject.getJSONObject("result").getInt("id");
                         System.out.println("UserId:"+userId);

                             userGroupCreate();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("UserGet Başarısız");
                    //Kullanıcı emaili yanlış girerse burası çalışıcak.
                    Intent intent = new Intent(ThermoBiShareActivity.this, WrongEmailAddressActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("UserGet Fail");
            }
        });


    }


    public void userGroupCreate(){


       //int deviceIdInt = Integer.parseInt(AfterScanActivity.deviceId);
       //int sensorIdInt = Integer.parseInt(AfterScanActivity.sensorId);


        PermissionsModel permissionsModel = new PermissionsModel(deviceId,sensorId,2);
        ArrayList<PermissionsModel> permissionsModelArrayList = new ArrayList<PermissionsModel>();
        permissionsModelArrayList.add(permissionsModel);
        Gson gson = new GsonBuilder().create();
        JsonArray jsonArray = gson.toJsonTree(permissionsModelArrayList).getAsJsonArray();
        System.out.println(jsonArray);

        UserGroupUsersModel userGroupUsersModel = new UserGroupUsersModel(userId);
        ArrayList<UserGroupUsersModel> userGroupUsersModelArrayList = new ArrayList<UserGroupUsersModel>();
        userGroupUsersModelArrayList.add(userGroupUsersModel);
        Gson gson1 = new GsonBuilder().create();
        JsonArray jsonArray1= gson1.toJsonTree(userGroupUsersModelArrayList).getAsJsonArray();
        System.out.println(jsonArray1);

        UserGroupCreateModel userGroupCreateModel = new UserGroupCreateModel(LoginActivity.ownerUserId,"OkanUserGroup",jsonArray,jsonArray1);



        Call<ResponseBody> userGroupCreate = RetrofitClient.getInstance().getMyApi().userGroupCreate(userGroupCreateModel);
        userGroupCreate.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    System.out.println("UserGroupCreate Başarılı");
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        System.out.println(jsonObject);
                        Toast.makeText(getApplicationContext(), "Paylaşım Gerçekleştirildi", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println();
                }else{
                    System.out.println("UserGroupCreate Başarısız");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("UserGroupCreate Fail");

            }
        });

    }











}