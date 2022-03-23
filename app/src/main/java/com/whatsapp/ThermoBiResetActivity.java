package com.whatsapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.whatsapp.databinding.ActivityThermoBiResetBinding;

public class ThermoBiResetActivity extends AppCompatActivity {

    ActivityThermoBiResetBinding activityThermoBiResetBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityThermoBiResetBinding = ActivityThermoBiResetBinding.inflate(getLayoutInflater());
        View view = activityThermoBiResetBinding.getRoot();
        setContentView(view);
        activityThermoBiResetBinding.ThermoBiResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();

            }
        });
    }


}