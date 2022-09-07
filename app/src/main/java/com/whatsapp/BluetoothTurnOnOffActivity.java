package com.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.whatsapp.databinding.ActivityBluetoothTurnOnOffBinding;

public class BluetoothTurnOnOffActivity extends AppCompatActivity {
    ActivityBluetoothTurnOnOffBinding activityBluetoothTurnOnOffBinding;
    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBluetoothTurnOnOffBinding = ActivityBluetoothTurnOnOffBinding.inflate(getLayoutInflater());
        View view = activityBluetoothTurnOnOffBinding.getRoot();
        setContentView(view);


        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver1, filter1);

        if(!bluetoothAdapter.isEnabled()){
            activityBluetoothTurnOnOffBinding.button.setVisibility(View.VISIBLE);
            activityBluetoothTurnOnOffBinding.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("BLE turn on clicked");
                    activityBluetoothTurnOnOffBinding.button.setVisibility(View.INVISIBLE);
                    activityBluetoothTurnOnOffBinding.progresBarBluetooth.setVisibility(View.VISIBLE);
                    bluetoothAdapter.enable();

                }
            });

        }else if(bluetoothAdapter.isEnabled()){
            activityBluetoothTurnOnOffBinding.button.setVisibility(View.INVISIBLE);
            activityBluetoothTurnOnOffBinding.button2.setVisibility(View.VISIBLE);
            activityBluetoothTurnOnOffBinding.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("BLE turn off clicked");
                    bluetoothAdapter.disable();
                    activityBluetoothTurnOnOffBinding.button2.setVisibility(View.INVISIBLE);
                    activityBluetoothTurnOnOffBinding.progresBarBluetooth.setVisibility(View.VISIBLE);
                    //activityBluetoothTurnOnOffBinding.button.setVisibility(View.VISIBLE);

                    /*activityBluetoothTurnOnOffBinding.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("BLE turn on clicked");
                            activityBluetoothTurnOnOffBinding.button.setVisibility(View.INVISIBLE);
                            activityBluetoothTurnOnOffBinding.progresBarBluetooth.setVisibility(View.VISIBLE);
                            bluetoothAdapter.enable();
                        }
                    });*/
                }
            });
        }


    }


    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch(state) {
                    case BluetoothAdapter.STATE_OFF:
                        System.out.println("STATE_OFF");
                        bluetoothAdapter.enable();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        System.out.println("STATE_ON");
                        Intent intent2 = new Intent(BluetoothTurnOnOffActivity.this,AfterLoginActivity.class);
                        startActivity(intent2);
                        finish();
                        break;
                }

            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcastReceiver1);
    }
}