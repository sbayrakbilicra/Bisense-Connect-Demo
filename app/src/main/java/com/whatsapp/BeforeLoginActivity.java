package com.whatsapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.whatsapp.Entity.ThermoBiEntity;
import com.whatsapp.Entity.UserEntity;
import com.whatsapp.ViewModel.ThermoBiViewModel;
import com.whatsapp.ViewModel.UserViewModel;
import com.whatsapp.databinding.ActivityBeforeLoginBinding;

import java.util.List;


public class BeforeLoginActivity extends AppCompatActivity {

    public static String accessToken;
    private ActivityBeforeLoginBinding activityBeforeLoginBinding;
    private UserViewModel userViewModel;
    private ThermoBiViewModel thermoBiViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBeforeLoginBinding = ActivityBeforeLoginBinding.inflate(getLayoutInflater());
        View view = activityBeforeLoginBinding.getRoot();
        setContentView(view);


        userViewModel = new ViewModelProvider(BeforeLoginActivity.this).get(UserViewModel.class);
        thermoBiViewModel = new ViewModelProvider(BeforeLoginActivity.this).get(ThermoBiViewModel.class);

            userViewModel.getAllUser().observe(this, new Observer<List<UserEntity>>() {
                @Override
                public void onChanged(List<UserEntity> userEntities) {

                    if(userEntities.size()>0){
                        for (int i =0 ; i<userEntities.size();i++){

                            System.out.println("Database içindeki  userId: " +userEntities.get(i).userId);
                            System.out.println("Database içindeki AccessToken: "+userEntities.get(i).accessToken);
                            accessToken = userEntities.get(i).accessToken;
                            activityBeforeLoginBinding.beforeLoginButton.setVisibility(View.INVISIBLE);
                            activityBeforeLoginBinding.progressBarBeforeLogin.setVisibility(View.INVISIBLE);


                            thermoBiViewModel.getAllThermobi().observe(BeforeLoginActivity.this, new Observer<List<ThermoBiEntity>>() {
                                @Override
                                public void onChanged(List<ThermoBiEntity> thermoBiEntities) {
                                    if(thermoBiEntities.size()>0){
                                        Intent intent = new Intent(BeforeLoginActivity.this, BluetoothTurnOnOffActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Intent intent = new Intent(BeforeLoginActivity.this, AfterLoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });


                        }
                    }else{
                        System.out.println("Size : 0");
                        activityBeforeLoginBinding.beforeLoginButton.setVisibility(View.VISIBLE);
                    }

                }
            });



        activityBeforeLoginBinding.beforeLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    activityBeforeLoginBinding.beforeLoginButton.setVisibility(View.INVISIBLE);
                    activityBeforeLoginBinding.progressBarBeforeLogin.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(BeforeLoginActivity.this, LoginActivity.class);
                    startActivity(intent);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop");
        activityBeforeLoginBinding.progressBarBeforeLogin.setVisibility(View.INVISIBLE);
        activityBeforeLoginBinding.beforeLoginButton.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
    }
}