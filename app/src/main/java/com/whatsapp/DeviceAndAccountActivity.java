package com.whatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.whatsapp.databinding.ActivityDeviceAndAccountBinding;

public class DeviceAndAccountActivity extends AppCompatActivity {

    ActivityDeviceAndAccountBinding deviceAndAccountBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceAndAccountBinding = ActivityDeviceAndAccountBinding.inflate(getLayoutInflater());
        View view = deviceAndAccountBinding.getRoot();
        setContentView(view);

        deviceAndAccountBinding.AccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceAndAccountActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        deviceAndAccountBinding.DeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceAndAccountActivity.this, AfterConnectActivity.class);
                startActivity(intent);
            }
        });

        deviceAndAccountBinding.imageView19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


    }


}