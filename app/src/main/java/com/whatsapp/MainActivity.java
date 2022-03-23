package com.whatsapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.whatsapp.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;

    private BluetoothAdapter bluetoothAdapter;
    int REQUEST_ENABLE_BT=1;
    public static ArrayList<BluetoothDevice> bluetoothDevices;
    private boolean scanning;
    Handler handler;
    ThermoBiScanAdapter thermoBiScanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mainBinding.getRoot();
        setContentView(view);

        mainBinding.imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        bluetoothDevices = new ArrayList<BluetoothDevice>();

        //ADAPTER and RecyclerView CODES
        mainBinding.ThermoBiScanRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        thermoBiScanAdapter = new ThermoBiScanAdapter(bluetoothDevices,MainActivity.this);
        mainBinding.ThermoBiScanRecyclerView.setAdapter(thermoBiScanAdapter);


        //BLE SCAN CODES

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if(bluetoothAdapter == null || !bluetoothAdapter.isEnabled()){
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
        }
        //uuids[0]=UUID.fromString("e093f3b5-00a3-a9e5-9eca-40016e0edc24");
        handler = new Handler();



        scanLeDevice();
        //bluetoothAdapter.startLeScan(uuids,scanCallback);

        //bluetoothAdapter.stopLeScan(scanCallback);


    }

   private BluetoothAdapter.LeScanCallback scanCallback= new BluetoothAdapter.LeScanCallback() {
       @Override
       public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
           //System.out.println("Device1111111: "+device.getName()+" "+device.getAddress());
         if(device.getName()!=null){
             if(device.getName().equals("ThermoBi")){
                 System.out.println("Device : "+device.getAddress()+" "+device.getName());

                try{
                    if(!bluetoothDevices.contains(device)){
                        bluetoothDevices.add(device);
                        //textViewThermoBi.setText(bluetoothDevices.get(0).getAddress());
                        thermoBiScanAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

             }
         }


       }

   };

    private void scanLeDevice(){
        if(!scanning){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanning = false;
                    bluetoothAdapter.stopLeScan(scanCallback);
                }
            },3000);
            scanning = true;
            bluetoothAdapter.startLeScan(scanCallback);
        }else{
            scanning = false;
            bluetoothAdapter.stopLeScan(scanCallback);

        }
    }


}