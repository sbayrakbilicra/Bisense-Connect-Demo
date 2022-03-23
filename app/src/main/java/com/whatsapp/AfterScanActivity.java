package com.whatsapp;

import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.whatsapp.Entity.ThermoBiEntity;
import com.whatsapp.Model.DeviceActionsModel;
import com.whatsapp.Model.DeviceCreateModel;
import com.whatsapp.Model.DeviceSettingsModel;
import com.whatsapp.Model.SensorsAlarmsModel;
import com.whatsapp.Model.SensorsModel;
import com.whatsapp.ViewModel.BilicraApiViewModel;
import com.whatsapp.ViewModel.ThermoBiViewModel;
import com.whatsapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;

public class AfterScanActivity extends AppCompatActivity {
    public static int counter;
    private String deviceId;
    private String sensorId;
    private ArrayList<DeviceSettingsModel> deviceSettingsModelArrayList;
    private ThermoBiViewModel thermoBiViewModel;
    private BilicraApiViewModel bilicraApiViewModel;
    SharedPreferences sharedPreferences;
    private Button cancelButton;
    private Button connectTryAgainButton;
    private TextView connectTextView;
    private ProgressBar progressBar;
    CountDownTimer countDownTimer1;
    CountDownTimer countDownTimer2;
    //private ThermoBiEntity thermoBiEntity;

    private BluetoothLeService bluetoothLeService;

    private static final long START_GATT_DELAY = 1000;
    private final Handler mStartGattHandler = new Handler();
    private final Runnable mStartGattRunnable = new Runnable() {
        @Override
        public void run() {
            Intent gattServiceIntent = new Intent(AfterScanActivity.this, BluetoothLeService.class);
            bindService(gattServiceIntent,serviceConnection, Context.BIND_AUTO_CREATE);
            //startService(gattServiceIntent);
        }
    };

    private  ServiceConnection serviceConnection = new ServiceConnection() {
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
                 countDownTimer1 = new CountDownTimer(60000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        System.out.println("Connecte Kaç Kere Girdi1???");

                    }

                    @Override
                    public void onFinish() {
                        System.out.println("Connect Denemesi Bitti");
                        connectTryAgainButton.setVisibility(View.VISIBLE);
                        cancelButton.setVisibility(View.INVISIBLE);
                        connectTextView.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }.start();
                bluetoothLeService.connect(MainActivity.bluetoothDevices.get(ThermoBiScanAdapter.devicePosition).getAddress());
                System.out.println("Hangi Cihaza Connect Oluyor ?: "+ MainActivity.bluetoothDevices.get(ThermoBiScanAdapter.devicePosition).getAddress());


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
        setContentView(R.layout.activity_after_scan);
        cancelButton = findViewById(R.id.calloff);
        connectTryAgainButton = findViewById(R.id.connectTryAgainButton);
        connectTextView = findViewById(R.id.textView);
        progressBar = findViewById(R.id.progressBar);

        deviceSettingsModelArrayList = new ArrayList<>();

        thermoBiViewModel = new ViewModelProvider(this).get(ThermoBiViewModel.class);
        bilicraApiViewModel = new ViewModelProvider(this).get(BilicraApiViewModel.class);

        sharedPreferences = this.getSharedPreferences("com.okankocakose.wearosdeneme",MODE_PRIVATE);

        mStartGattHandler.postDelayed(mStartGattRunnable,START_GATT_DELAY);

        connectTryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothLeService.connect(MainActivity.bluetoothDevices.get(ThermoBiScanAdapter.devicePosition).getAddress());
                connectTryAgainButton.setVisibility(View.INVISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                connectTextView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                countDownTimer2 = new CountDownTimer(60000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        System.out.println("Connecte Kaç Kere Girdi İç???");

                    }

                    @Override
                    public void onFinish() {
                        System.out.println("Connect Denemesi Bitti İç");
                        connectTryAgainButton.setVisibility(View.VISIBLE);
                        cancelButton.setVisibility(View.INVISIBLE);
                        connectTextView.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                }.start();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    unregisterReceiver(gattUpdateReceiver);
                    BluetoothLeService.globalBluetoothGatt.disconnect();
                    BluetoothLeService.globalBluetoothGatt.close();
                    /*for(BluetoothGatt bluetoothGatt : BluetoothLeService.bluetoothGattList){
                        bluetoothGatt.disconnect();
                        bluetoothGatt.close();
                        BluetoothLeService.bluetoothGattList.remove(bluetoothGatt);
                    }*/
                }catch (Exception e){
                    e.printStackTrace();
                }
                onBackPressed();
                finish();

            }
        });

    }


    private final BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)){
                System.out.println("BURAYA GİRİYOR AAAAA AfterScanActivity");
                counter=0;
                counter++;

            }else if(BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)){
                System.out.println("BURAYA GİRİYOR BBBBB AfterScanActivity");
                if(BluetoothLeService.globalStatus==133){
                    try {
                        countDownTimer1.cancel();
                        countDownTimer2.cancel();
                        BluetoothLeService.globalStatus=0;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


            }else if(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)){
                System.out.println("BURAYA GİRİYOR CCCCC AfterScanActivity");
            }else if(BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)){
                System.out.println("SON NOKTAYA GİRİYOR: AfterScanActivity "+intent.getStringExtra(BluetoothLeService.EXTRA_DATA));

            }else if(BluetoothLeService.FIRST_INDEX0.equals(action)){

                if(BluetoothLeService.index1=='1'){

                    try {
                        countDownTimer1.cancel();
                        countDownTimer2.cancel();
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                    ThermoBiEntity thermoBiEntity = new ThermoBiEntity("ThermoBi","-", MainActivity.bluetoothDevices.get(ThermoBiScanAdapter.devicePosition).getAddress());
                    thermoBiViewModel.insert(thermoBiEntity);

                    createNewDevice();
                    //createDevice();

                    Intent intent1 = new Intent(AfterScanActivity.this, AfterLoginActivity.class);
                    startActivity(intent1);
                    unregisterReceiver(gattUpdateReceiver);
                    finishAffinity();

                }else if(BluetoothLeService.index1=='0'){
                    countDownTimer1.cancel();
                    countDownTimer2.cancel();
                    Intent intent1 = new Intent(AfterScanActivity.this, ThermoBiResetActivity.class);
                    startActivity(intent1);
                    unregisterReceiver(gattUpdateReceiver);
                    finish();
                }

            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter(){
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.FIRST_INDEX0);
        return intentFilter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(gattUpdateReceiver,makeGattUpdateIntentFilter());

    }



    private void createNewDevice(){
        try {
            DeviceSettingsModel deviceSettingsModel = new DeviceSettingsModel("ServiceUUID", MainActivity.bluetoothDevices.get(ThermoBiScanAdapter.devicePosition).getAddress());
            if(!deviceSettingsModelArrayList.contains(deviceSettingsModel)){
                deviceSettingsModelArrayList.add(deviceSettingsModel);
                for (int i=0; i<deviceSettingsModelArrayList.size();i++){
                    System.out.println("eklenmiş cihazlar: "+deviceSettingsModelArrayList.get(i).value);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder().create();
        JsonArray jsonArray = gson.toJsonTree(deviceSettingsModelArrayList).getAsJsonArray();
        System.out.println("DeviceSettingsJsonArray: "+jsonArray);


        DeviceCreateModel deviceCreateModel = new DeviceCreateModel(0,"","ThermoBi",0,0,jsonArray,new ArrayList<DeviceActionsModel>(),new ArrayList<SensorsModel>());
        bilicraApiViewModel.createNewDevice(deviceCreateModel);

        bilicraApiViewModel.getCreateDeviceObserver().observe(AfterScanActivity.this, new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                if(responseBody == null){
                    System.out.println("Cihaz Kaydı Başarısız");
                }else{
                    System.out.println("Cihaz Kayıt Başarılı");
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseBody.string());
                        System.out.println(jsonObject.getJSONObject("result"));
                        deviceId = jsonObject.getJSONObject("result").getString("id");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Device ID bilgisi sharedPreferences içerisinde tutuldu.
                    sharedPreferences.edit().putInt("deviceId",Integer.parseInt(deviceId)).apply();

                    //Device Sensor Create
                    createNewSensor();

                }
            }
        });

    }



   private void createNewSensor(){
        SensorsModel sensorsModel = new SensorsModel("",0,"ThermoBi Sensor Okan","",0,Integer.parseInt(deviceId),false,0,"","",0,"",0,0,new ArrayList<SensorsAlarmsModel>());
        bilicraApiViewModel.createNewSensor(sensorsModel);

        bilicraApiViewModel.getCreateSensorObserver().observe(AfterScanActivity.this, new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                if(responseBody==null){
                    System.out.println("Sensor Kaydı Başarısız");
                }else{
                    System.out.println("Sensor Kayıt Başarılı");
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseBody.string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("ResultSensor: "+jsonObject);
                    getAllSensor();
                }
            }
        });
   }


  private void getAllSensor(){
      int deviceIdInt = Integer.parseInt(deviceId);
      bilicraApiViewModel.getAllSensor(deviceIdInt);

      bilicraApiViewModel.getAllSensorObserver().observe(AfterScanActivity.this, new Observer<ResponseBody>() {
          @Override
          public void onChanged(ResponseBody responseBody) {
              if(responseBody == null){
                  System.out.println("getAllSensor Başarısız");
              }else{

                  System.out.println("SensorGetAll Başarılı");
                  JSONObject jsonObject = null;
                  try {
                      jsonObject = new JSONObject(responseBody.string());
                  } catch (JSONException e) {
                      e.printStackTrace();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
                  try {
                      //System.out.println(jsonObject.getJSONObject("result").getJSONArray("items").get(0));
                      JSONObject newObject = (JSONObject) jsonObject.getJSONObject("result").getJSONArray("items").get(0);
                      System.out.println(newObject.getString("id"));
                      sensorId = newObject.getString("id");

                      sharedPreferences.edit().putInt("sensorId",Integer.parseInt(sensorId)).apply();

                      //Kontrol
                      int deviceId = sharedPreferences.getInt("deviceId",0);
                      int sensorId = sharedPreferences.getInt("sensorId",0);

                      System.out.println("sharedPreferences deviceId: "+deviceId);
                      System.out.println("sharedPreferences sensorId: "+sensorId);

                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
              }
          }
      });

  }













    /*public void createDevice(){

        try {
            DeviceSettingsModel deviceSettingsModel = new DeviceSettingsModel("ServiceUUID",MainActivity.bluetoothDevices.get(ThermoBiScanAdapter.devicePosition).getAddress());
            if(!deviceSettingsModelArrayList.contains(deviceSettingsModel)){
                deviceSettingsModelArrayList.add(deviceSettingsModel);
                for (int i=0; i<deviceSettingsModelArrayList.size();i++){
                    System.out.println("eklenmiş cihazlar: "+deviceSettingsModelArrayList.get(i).value);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder().create();
        JsonArray jsonArray = gson.toJsonTree(deviceSettingsModelArrayList).getAsJsonArray();
        System.out.println("DeviceSettingsJsonArray: "+jsonArray);


        DeviceCreateModel deviceCreateModel = new DeviceCreateModel(0,"","ThermoBi",0,0,jsonArray,new ArrayList<DeviceActionsModel>(),new ArrayList<SensorsModel>());
        Call<ResponseBody> deviceCreate = RetrofitClient.getInstance().getMyApi().deviceCreate(deviceCreateModel);
        deviceCreate.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    System.out.println("Cihaz Kayıt Edildi");
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        System.out.println(jsonObject.getJSONObject("result"));
                        deviceId = jsonObject.getJSONObject("result").getString("id");

                         //Device ID bilgisi sharedPreferences içerisinde tutuldu.
                         sharedPreferences.edit().putInt("deviceId",Integer.parseInt(deviceId)).apply();

                         createSensor();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else{

                    System.out.println("Cihaz Kaydı Başarısız"+response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Cihaz Kayıt Fail");
            }
        });


    }








    public void createSensor(){
     Call<ResponseBody> createSensor = RetrofitClient.getInstance().getMyApi().sensorCreate(new SensorsModel("",0,"ThermoBi Sensor Okan","",0,Integer.parseInt(deviceId),false,0,"","",0,"",0,0,new ArrayList<SensorsAlarmsModel>()));
     createSensor.enqueue(new Callback<ResponseBody>() {
         @Override
         public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
         if(response.code()==200){
             System.out.println("Sensor Create Response Başarılı");
             JSONObject jsonObject = null;
             try {
                 jsonObject = new JSONObject(response.body().string());
             } catch (JSONException e) {
                 e.printStackTrace();
             } catch (IOException e) {
                 e.printStackTrace();
             }
             System.out.println("ResultSensor: "+jsonObject);
             sensorGetAll();
          }else{
             System.out.println("Sensor Create Response Başarısız");

         }


         }

         @Override
         public void onFailure(Call<ResponseBody> call, Throwable t) {
             System.out.println("Sensor Kayıt Fail");
         }
     });
    }







    public void sensorGetAll(){
        int deviceIdInt = Integer.parseInt(deviceId);
        Call<ResponseBody> sensorGetAll = RetrofitClient.getInstance().getMyApi().sensorGetAll(deviceIdInt);
        sensorGetAll.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    System.out.println("SensorGetAll Başarılı");
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        //System.out.println(jsonObject.getJSONObject("result").getJSONArray("items").get(0));
                        JSONObject newObject = (JSONObject) jsonObject.getJSONObject("result").getJSONArray("items").get(0);
                        System.out.println(newObject.getString("id"));
                        sensorId = newObject.getString("id");

                        sharedPreferences.edit().putInt("sensorId",Integer.parseInt(sensorId)).apply();

                        //Kontrol
                        int deviceId = sharedPreferences.getInt("deviceId",0);
                        int sensorId = sharedPreferences.getInt("sensorId",0);

                        System.out.println("sharedPreferences deviceId: "+deviceId);
                        System.out.println("sharedPreferences sensorId: "+sensorId);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    System.out.println("SensorGetAll Başarısız");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("SensorGetAll Fail");
            }
        });


    }*/












}