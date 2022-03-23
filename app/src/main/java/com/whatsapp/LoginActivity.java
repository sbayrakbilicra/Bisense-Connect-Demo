package com.whatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.whatsapp.Client.RetrofitClient;
import com.whatsapp.Entity.UserEntity;
import com.whatsapp.Model.AuthenticateModel;
import com.whatsapp.Model.RefreshAuthenticationModel;
import com.whatsapp.ViewModel.BilicraApiViewModel;
import com.whatsapp.ViewModel.UserViewModel;
import com.whatsapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    public static int ownerUserId;
    private String accessToken;
    private String refreshToken;
    private Button loginButton;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private ProgressBar progressBar;
    private ImageView backButton;
    private UserViewModel userViewModel;
    private BilicraApiViewModel bilicraApiViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.LoginButton);
        userNameEditText = findViewById(R.id.appCompatEditText);
        passwordEditText = findViewById(R.id.appCompatEditText2);
        backButton = findViewById(R.id.imageView15);
        progressBar = findViewById(R.id.loginProgressBar);

        userViewModel = new ViewModelProvider(LoginActivity.this).get(UserViewModel.class);
        initBilicraApiViewModel();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //authenticateModel = new AuthenticateModel(userNameEditText.getText().toString(),passwordEditText.getText().toString());
                createNewUser();
                loginButton.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                //userLogin1();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }


    private void createNewUser(){
       String userName =  userNameEditText.getText().toString();
       String password =  passwordEditText.getText().toString();
       AuthenticateModel authenticateModel = new AuthenticateModel(userName,password);
       bilicraApiViewModel.createNewUser(authenticateModel);
    }


    private void initBilicraApiViewModel(){
        bilicraApiViewModel = new ViewModelProvider(this).get(BilicraApiViewModel.class);
        bilicraApiViewModel.getCreateUserObserver().observe(LoginActivity.this, new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                if(responseBody == null){
                    System.out.println("Failed to login");
                    Toast.makeText(LoginActivity.this,"Failed to login",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    loginButton.setVisibility(View.VISIBLE);
                }else{
                    System.out.println("Successfully logined");
                    Toast.makeText(LoginActivity.this,"Successfully logined",Toast.LENGTH_SHORT).show();

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseBody.string());
                        System.out.println("Gelen veri : "+jsonObject.getJSONObject("result"));
                        BeforeLoginActivity.accessToken = jsonObject.getJSONObject("result").getString("accessToken");
                        accessToken = jsonObject.getJSONObject("result").getString("accessToken");
                        refreshToken = jsonObject.getJSONObject("result").getString("refreshToken");
                        System.out.println("accessToken: "+accessToken);
                        System.out.println("refreshToken: "+refreshToken);
                        ownerUserId = jsonObject.getJSONObject("result").getInt("userId");
                        userViewModel.insertUser(new UserEntity(ownerUserId, BeforeLoginActivity.accessToken));

                        RefreshAuthenticationModel refreshAuthenticationModel = new RefreshAuthenticationModel(refreshToken,accessToken);
                        bilicraApiViewModel.refreshAuthentication(refreshAuthenticationModel);

                        Intent intent = new Intent(LoginActivity.this, AfterLoginActivity.class);
                        startActivity(intent);
                        finishAffinity();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }








































    private void deviceGetAll(){

        Call<ResponseBody> getAllDevice = RetrofitClient.getInstance().getMyApi().getAllDevice();
        getAllDevice.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    try {
                        System.out.println("DeviceGetAll Başarılı");
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        System.out.println(jsonObject);
                        int totalCount = jsonObject.getJSONObject("result").getInt("totalCount");

                        /*jsonObjectArrayList = new ArrayList<JSONObject>();

                        for(int i = 0; i< jsonObject.getJSONObject("result").getJSONArray("items").length();i++){
                            System.out.println("JSONARRAYYYY: "+jsonObject.getJSONObject("result").getJSONArray("items").getJSONObject(i));
                                jsonObjectArrayList.add(jsonObject.getJSONObject("result").getJSONArray("items").getJSONObject(i));

                        }
                        afterLoginAdapter = new AfterLoginAdapter(jsonObjectArrayList);
                        afterLoginAdapter.notifyDataSetChanged();*/


                        if(totalCount!=0){

                        Intent intent = new Intent(LoginActivity.this, AfterLoginActivity.class);
                        finishAffinity();
                        startActivity(intent);

                        }else{
                            Intent intent = new Intent(LoginActivity.this, AfterLoginActivity.class);
                            //Given below code close all activities that stay behind
                            finishAffinity();
                            startActivity(intent);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("DeviceGetAll Başarısız");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("DeviceGetAll Fail");
            }
        });




    }





    private void userLogin1(){


         Call<ResponseBody> loginCall = RetrofitClient.getInstance().getMyApi().userLogin(new AuthenticateModel("okankckse","123456"));
         loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    //loginButton.setClickable(false);

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        System.out.println("Gelen veri : "+jsonObject.getJSONObject("result"));
                        //System.out.println("AccessToken : "+jsonObject.getJSONObject("result").getString("accessToken"));
                        BeforeLoginActivity.accessToken = jsonObject.getJSONObject("result").getString("accessToken");
                        /*AuthenticateResultModel authenticateResultModel = new AuthenticateResultModel();
                        authenticateResultModel.accessToken = jsonObject.getJSONObject("result").getString("accessToken");
                        authenticateResultModel.encryptedAccessToken = jsonObject.getJSONObject("result").getString("encryptedAccessToken");
                        authenticateResultModel.expireInSeconds = jsonObject.getJSONObject("result").getInt("expireInSeconds");
                        authenticateResultModel.userID = jsonObject.getJSONObject("result").getInt("userId");
                        authenticateResultModel.refreshToken = jsonObject.getJSONObject("result").getString("refreshToken");*/
                        ownerUserId = jsonObject.getJSONObject("result").getInt("userId");

                        userViewModel.insertUser(new UserEntity(ownerUserId, BeforeLoginActivity.accessToken));

                        Intent intent = new Intent(LoginActivity.this, AfterLoginActivity.class);
                        startActivity(intent);
                        finishAffinity();

                        //deviceGetAll();

                    }catch (IOException | JSONException e){
                        e.printStackTrace();
                    }
                    System.out.println("Kullanıcı Girişi Başarılı");

                }else{
                    System.out.println("Kullanıcı Girişi Başarısız");
                    Toast.makeText(getApplicationContext(), "Kullanıcı Adı veya Şifrenizi Tekrar Giriniz", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    loginButton.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("fail");
                Toast.makeText(getApplicationContext(), "İnternet Bağlantınızı Kontrol Ediniz", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                loginButton.setVisibility(View.VISIBLE);
            }
        });




    }


   /* private void userLogin2(){
        Call<AuthenticateResultModel> loginCall2 = RetrofitClient.getInstance().getMyApi().userLogin1(new AuthenticateModel(userNameEditText.getText().toString(),passwordEditText.getText().toString()));
        loginCall2.enqueue(new Callback<AuthenticateResultModel>() {
            @Override
            public void onResponse(Call<AuthenticateResultModel> call, Response<AuthenticateResultModel> response) {
                if(response.code()==200){
                    System.out.println("Başarılı");
                    loginButton.setClickable(false);
                    AuthenticateResultModel authenticateResultModel = new AuthenticateResultModel();
                    authenticateResultModel.accessToken = response.body().accessToken;
                    accessToken = response.body().accessToken;
                    System.out.println("AccessToken: "+accessToken);
                    authenticateResultModel.userID= response.body().userID;
                    authenticateResultModel.encryptedAccessToken = response.body().encryptedAccessToken;
                    authenticateResultModel.expireInSeconds= response.body().expireInSeconds;
                    Intent intent = new Intent(LoginActivity.this,AddDeviceActivity.class);
                    //Given below code close all activities that stay behind
                    finishAffinity();
                    startActivity(intent);
                }else{
                    System.out.println("Kullanıcı Girişi Başarısız");
                    loginButton.setClickable(true);
                }
            }

            @Override
            public void onFailure(Call<AuthenticateResultModel> call, Throwable t) {
                System.out.println("Fail");
                loginButton.setClickable(true);
                t.printStackTrace();
            }
        });
    }*/









}