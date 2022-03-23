package com.whatsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.whatsapp.Entity.ThermoBiEntity;
import com.whatsapp.ViewModel.ThermoBiViewModel;
import com.whatsapp.databinding.ActivityThermoBiDetailsBinding;

import java.util.List;

public class ThermoBiDetailsActivity extends AppCompatActivity {

    ActivityThermoBiDetailsBinding activityThermoBiDetailsBinding;
    ThermoBiViewModel thermoBiViewModel;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityThermoBiDetailsBinding = ActivityThermoBiDetailsBinding.inflate(getLayoutInflater());
        View view = activityThermoBiDetailsBinding.getRoot();
        setContentView(view);
        sharedPreferences = this.getSharedPreferences("com.okankocakose.wearosdeneme",MODE_PRIVATE);


        thermoBiViewModel = new ViewModelProvider(ThermoBiDetailsActivity.this).get(ThermoBiViewModel.class);


        thermoBiViewModel.getAllThermobi().observe(this, new Observer<List<ThermoBiEntity>>() {
            @Override
            public void onChanged(List<ThermoBiEntity> thermoBiEntities) {
                if(thermoBiEntities.size()>0){
                    activityThermoBiDetailsBinding.macAdressTextView.setText(thermoBiEntities.get(0).macAddress);
                }
            }
        });

        String firmwareVersiyon = sharedPreferences.getString("firmwareVersiyon","");
        activityThermoBiDetailsBinding.firmwareVersionTextView.setText(firmwareVersiyon);

        String battery_level = sharedPreferences.getString("batteryLevel","");
        activityThermoBiDetailsBinding.batteryLevel.setText("%"+battery_level);


        activityThermoBiDetailsBinding.imageView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

    }


}