package com.whatsapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.whatsapp.Entity.ThermoBiEntity;
import com.whatsapp.ViewModel.ThermoBiViewModel;
import com.whatsapp.databinding.ActivityAfterLoginBinding;

import java.util.List;


public class AfterLoginActivity extends AppCompatActivity {

    private ActivityAfterLoginBinding activityAfterLoginBinding;
    private ThermoBiViewModel thermoBiViewModel;
    private ThermoBiEntity thermoBiEntity;
    private String address;
    private BluetoothAdapter bluetoothAdapter;
    CountDownTimer countDownTimer;
    int REQUEST_ENABLE_BT=1;
    boolean key = true;


    private BluetoothLeService bluetoothLeService;
    private static final long START_GATT_DELAY = 500;
    private final Handler mStartGattHandler = new Handler();
    private final Runnable mStartGattRunnable = new Runnable() {
        @Override
        public void run() {
            Intent gattServiceIntent = new Intent(AfterLoginActivity.this, BluetoothLeService.class);
            bindService(gattServiceIntent,serviceConnection, Context.BIND_AUTO_CREATE);

        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bluetoothLeService = ((BluetoothLeService.LocalBinder)service ).getService();
            System.out.println("SERVICE BAGLANDI MI????????????");
            if(bluetoothLeService!=null){

                if(!bluetoothLeService.initialize()){
                    System.out.println("Unable to initialize Bluetooth");
                    //finish();
                }

                //perform device connection
                CountDownTimer countDownTimer = new CountDownTimer(1000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {



                    }

                    @Override
                    public void onFinish() {
                        System.out.println("Connect Denemesi Bitti");


                    }
                }.start();

                System.out.println("Counter: "+ AfterScanActivity.counter);
                if(address !=null){
                    if(AfterScanActivity.counter!=1){

                        //bluetoothLeService.connect(address);

                    }
                }


            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("ServiceDisconnected");
            bluetoothLeService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAfterLoginBinding = ActivityAfterLoginBinding.inflate(getLayoutInflater());
        View view = activityAfterLoginBinding.getRoot();
        setContentView(view);




        //Bluetooth Kapalı ise Açma İsteği Gönderme
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();


        if(bluetoothAdapter == null || !bluetoothAdapter.isEnabled()){
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
        }


        activityAfterLoginBinding.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unregisterReceiver(gattUpdateReceiver);
                bluetoothAdapter.stopLeScan(scanCallback);
                Intent intent = new Intent(AfterLoginActivity.this, AddDeviceActivity.class);
                startActivity(intent);
            }
        });

       activityAfterLoginBinding.afterLoginSettings.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               bluetoothAdapter.stopLeScan(scanCallback);
               Intent intent = new Intent(AfterLoginActivity.this, DeviceAndAccountActivity.class);
               startActivity(intent);

           }
       });

       activityAfterLoginBinding.tryAgainButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               activityAfterLoginBinding.tryAgainButton.setVisibility(View.INVISIBLE);
               bluetoothAdapter.startLeScan(scanCallback);
           }
       });


        thermoBiViewModel = new ViewModelProvider(AfterLoginActivity.this).get(ThermoBiViewModel.class);
        thermoBiViewModel.getAllThermobi().observe(this, new Observer<List<ThermoBiEntity>>() {
            @Override
            public void onChanged(List<ThermoBiEntity> thermoBiEntities) {
                if(thermoBiEntities.size()>0){
                    address = thermoBiEntities.get(0).macAddress;
                    activityAfterLoginBinding.afterLoginSettings.setVisibility(View.VISIBLE);
                    activityAfterLoginBinding.plusButton.setVisibility(View.INVISIBLE);
                    activityAfterLoginBinding.HomeScreenTextView.setText(thermoBiEntities.get(0).name);
                }else{
                    bluetoothAdapter.stopLeScan(scanCallback);
                    activityAfterLoginBinding.HomeScreenTextView.setText("Ekli Cihaz Yok");
                    activityAfterLoginBinding.textView7.setVisibility(View.VISIBLE);
                    activityAfterLoginBinding.textView7.setText("-");
                    activityAfterLoginBinding.afterLoginSettings.setVisibility(View.INVISIBLE);
                    activityAfterLoginBinding.explanation2.setVisibility(View.INVISIBLE);
                    activityAfterLoginBinding.tryAgainButton.setVisibility(View.INVISIBLE);
                    activityAfterLoginBinding.plusButton.setVisibility(View.VISIBLE);
                    activityAfterLoginBinding.progressBarAfterLogin.setVisibility(View.INVISIBLE);
                }
            }
        });



        if(AfterScanActivity.counter==1){
            activityAfterLoginBinding.textView7.setVisibility(View.INVISIBLE);
            activityAfterLoginBinding.progressBarAfterLogin.setVisibility(View.VISIBLE);

        }else{
            activityAfterLoginBinding.progressBarAfterLogin.setVisibility(View.INVISIBLE);
            activityAfterLoginBinding.textView7.setVisibility(View.VISIBLE);
            activityAfterLoginBinding.explanation2.setVisibility(View.VISIBLE);
            activityAfterLoginBinding.explanation2.setText("Cihazınızın Açık Olduğundan Emin Olun");
            //Bluetooth taramasını sürekli açık tutuyoruz.
            if(bluetoothAdapter.isEnabled()){
                bluetoothAdapter.startLeScan(scanCallback);
            }

        }
        mStartGattHandler.postDelayed(mStartGattRunnable,START_GATT_DELAY);

    }

    private BluetoothAdapter.LeScanCallback scanCallback= new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            System.out.println("Device : "+device.getAddress()+" "+device.getName());
            if(device.getName()!=null ){
                if(device.getName().equals("ThermoBi")){
                    //activityAfterLoginBinding.explanation2.setVisibility(View.VISIBLE);
                    //activityAfterLoginBinding.explanation2.setText("Cihazınızın Açık Olduğundan Emin Olun");
                    if(address!=null){
                        if(device.getAddress().equals(address)){
                            if(AfterScanActivity.counter!=1){
                                System.out.println("Device : "+device.getAddress()+" "+device.getName());
                                //activityAfterLoginBinding.explanation2.setVisibility(View.INVISIBLE);

                                activityAfterLoginBinding.explanation2.setText("Cihaza Bağlantı İsteği Yollandı");
                                bluetoothLeService.connect(address);
                                //cihazı bulup 1 kere connect isteği atana kadar bluetooth taramasını kapatmıyoruz.
                                //cihaz bulup connect isteği attığında ise bluetooth taramasını kapatıyoruz.
                                bluetoothAdapter.stopLeScan(scanCallback);
                            }
                        }
                    }
                }

            }
        }

    };


    private final BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();


            if(BluetoothLeService.ACTION_GATT_CONNECT_REQUEST_SEND.equals(action)){
                System.out.println("AfterLoginActivity ACTION_GATT_CONNECT_REQUEST_SEND");
                activityAfterLoginBinding.textView7.setVisibility(View.INVISIBLE);
                activityAfterLoginBinding.progressBarAfterLogin.setVisibility(View.VISIBLE);

                 countDownTimer = new CountDownTimer(60000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        System.out.println("Kalan Saniye: "+millisUntilFinished/1000);
                    }

                    @Override
                    public void onFinish() {
                        System.out.println("AfterLoginActivity Connect Sağlanamadı");
                        try {
                            System.out.println("KEY: "+key);
                            if(key){
                                activityAfterLoginBinding.progressBarAfterLogin.setVisibility(View.INVISIBLE);
                                activityAfterLoginBinding.textView7.setVisibility(View.VISIBLE);
                                activityAfterLoginBinding.textView7.setText("-");
                                activityAfterLoginBinding.explanation2.setVisibility(View.INVISIBLE);
                                activityAfterLoginBinding.tryAgainButton.setVisibility(View.VISIBLE);
                                key=true;
                                BluetoothLeService.globalBluetoothGatt.disconnect();
                                BluetoothLeService.globalBluetoothGatt.close();

                                /*for(BluetoothGatt bluetoothGatt:BluetoothLeService.bluetoothGattList){
                                    bluetoothGatt.disconnect();
                                    bluetoothGatt.close();
                                    BluetoothLeService.bluetoothGattList.remove(bluetoothGatt);
                                }*/

                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }.start();

            }else if(BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)){
                System.out.println("AfterLoginActivity ACTION_GATT_CONNECTED");
                key=false;
                activityAfterLoginBinding.tryAgainButton.setVisibility(View.INVISIBLE);
                activityAfterLoginBinding.textView7.setVisibility(View.INVISIBLE);
                activityAfterLoginBinding.progressBarAfterLogin.setVisibility(View.VISIBLE);
                try {
                    countDownTimer.cancel();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else if(BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)){
                System.out.println("AfterLoginActivity ACTION_GATT_DISCONNECTED");
                activityAfterLoginBinding.explanation2.setVisibility(View.INVISIBLE);
                activityAfterLoginBinding.progressBarAfterLogin.setVisibility(View.INVISIBLE);
                activityAfterLoginBinding.textView7.setVisibility(View.VISIBLE);
                activityAfterLoginBinding.textView7.setText("-");

                if(BluetoothLeService.globalStatus==133){
                    try {
                        countDownTimer.cancel();
                        BluetoothLeService.globalBluetoothGatt.disconnect();
                        BluetoothLeService.globalBluetoothGatt.close();
                        /*for(BluetoothGatt bluetoothGatt:BluetoothLeService.bluetoothGattList){
                            bluetoothGatt.disconnect();
                            bluetoothGatt.close();
                            BluetoothLeService.bluetoothGattList.remove(bluetoothGatt);
                        }*/
                        activityAfterLoginBinding.tryAgainButton.setVisibility(View.VISIBLE);
                        BluetoothLeService.globalStatus=0;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    /*activityAfterLoginBinding.textView7.setVisibility(View.INVISIBLE);
                    activityAfterLoginBinding.progressBarAfterLogin.setVisibility(View.VISIBLE);*/
                }
                //bluetoothAdapter.startLeScan(scanCallback);

            }else if(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)){
                System.out.println("AfterLoginActivity ACTION_GATT_SERVICES_DISCOVERED");

            }else if(BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)){
                System.out.println("AfterLoginAcitivty ACTION_DATA_AVAILABLE: "+intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                activityAfterLoginBinding.progressBarAfterLogin.setVisibility(View.INVISIBLE);
                activityAfterLoginBinding.textView7.setVisibility(View.VISIBLE);
                activityAfterLoginBinding.textView7.setText(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                activityAfterLoginBinding.explanation2.setVisibility(View.VISIBLE);


                try {
                    String s1 = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                    String s2 = s1.replace(",",".");
                    float data =  Float.parseFloat(s2);
                    if(data < 34){
                        activityAfterLoginBinding.explanation2.setText("Düşük");
                    }else if(34 < data && data < 38){
                        activityAfterLoginBinding.explanation2.setText("Normal");
                    }else if(data>38){
                        activityAfterLoginBinding.explanation2.setText("Yüksek");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

                //Database Register
                thermoBiEntity = new ThermoBiEntity("ThermoBi",intent.getStringExtra(BluetoothLeService.EXTRA_DATA),address);
                thermoBiViewModel.insert(thermoBiEntity);

            }


        }
    };





    private static IntentFilter makeGattUpdateIntentFilter(){
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECT_REQUEST_SEND);
        return intentFilter;
    }


    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume AfterLoginActivity");
        registerReceiver(gattUpdateReceiver,makeGattUpdateIntentFilter());
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Result: "+resultCode);
        if(resultCode==-1){
            bluetoothAdapter.startLeScan(scanCallback);
        }
    }



}