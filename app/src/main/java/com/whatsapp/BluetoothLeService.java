package com.whatsapp;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.whatsapp.Client.RetrofitClient;
import com.whatsapp.Model.SensorDataCreateModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BluetoothLeService extends Service {

    SharedPreferences sharedPreferences;
    int sensorId;
    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = this.getSharedPreferences("com.okankocakose.wearosdeneme",MODE_PRIVATE);
        System.out.println("BluetoothLe Servis OnCreate");

    }


    public final static String ACTION_GATT_CONNECT_REQUEST_SEND =
            "com.okankocakose.bluetoothapp.ACTION_GATT_CONNECT_REQUEST_SEND";
    public final static String ACTION_GATT_CONNECTED =
            "com.okankocakose.bluetoothapp.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.okankocakose.bluetoothapp.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.okankocakose.bluetoothapp.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.okankocakose.bluetoothapp.ACTION_DAT_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.okankocakose.bluetoothapp.EXTRA_DATA";
    public final static String BATTERY_LEVEL =
            "com.okankocakose.bluetoothapp.BATTERY_LEVEL";
    public final static String BATTERY_LEVEL_DATA =
            "com.okankocakose.bluetoothapp.BATTERY_LEVEL_DATA";
    public final static String FIRST_INDEX0 =
            "com.okankocakose.bluetoothapp.FIRST_INDEX";
    public static int globalStatus;

    public  String firmwareVersionString;


    private BluetoothGattCharacteristic characteristicRead;
    private BluetoothGattCharacteristic characteristicWrite;
    private BluetoothGattCharacteristic characteristicMeasurement;
    private BluetoothGattService requestService;
    private BluetoothGattService myTempratureService;//Service of livedata
    private BluetoothGattService batteryService;
    private BluetoothGattCharacteristic batteryCharacteristic;
    public BluetoothGattCharacteristic writeLiveTemp;//yaz 1
    public BluetoothGattCharacteristic readLiveTemp;//oku
    public static BluetoothGattCharacteristic globalBluetoohGattCharacteristics;
    public static BluetoothGatt globalBluetoothGatt;
    public static List<BluetoothGatt> bluetoothGattList = new ArrayList<BluetoothGatt>();
    public static char index1;
    private String lastValue="";
    private ArrayList<Integer> decimalStoredData = new ArrayList<>(); //Stored Data List
    private int counter=0;
    private Binder binder = new LocalBinder();

    BluetoothGatt bluetoothGatt;
    BluetoothAdapter bluetoothAdapter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("onBind triggered");
        return binder;
    }

    class LocalBinder extends Binder{
        public BluetoothLeService getService(){
            return BluetoothLeService.this;
        }
    }

    public boolean initialize(){
        //final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
              bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
              if(bluetoothAdapter == null){
                  System.out.println("Unable to obtain a BluetoohAdapter");
                  return false;
              }
              return true;
    }

    BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if(newState == BluetoothProfile.STATE_CONNECTED){
                System.out.println("NewState Connected : "+newState);
                System.out.println("Başarıyla Bağlandı.(OnConnectionStateChange)");
                getConnectedDevices();
                //globalBluetoothGatt=gatt;
                System.out.println("ConnectedGatt: "+gatt);
                //bluetoothGattList.add(gatt);

                /*for(BluetoothGatt bluetoothGatt : bluetoothGattList){
                    System.out.println("GattList: "+bluetoothGatt);
                }*/
                broadcastUpdate(ACTION_GATT_CONNECTED);
                gatt.discoverServices();
            }else if(newState == BluetoothProfile.STATE_DISCONNECTED){
                System.out.println("NewState Disconnected : "+newState);
                System.out.println("Başarıyla Bağlanamadı.(OnConnectionStateChange)");
                broadcastUpdate(ACTION_GATT_DISCONNECTED);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            requestService = gatt.getService(UUID.fromString(SampleGattAttributes.OS_TYPE_SERVICE_UUID));
            myTempratureService = gatt.getService(UUID.fromString(SampleGattAttributes.TEMPERATURE_SERVICE_UUID));
            batteryService = gatt.getService(UUID.fromString(SampleGattAttributes.BATTERY_SERVICE_UUID));
            batteryCharacteristic = batteryService.getCharacteristic(UUID.fromString(SampleGattAttributes.BATTERY_CHARACTERISTIC));
            characteristicWrite = requestService.getCharacteristic(UUID.fromString(SampleGattAttributes.MAC_ID_UUIDRX));
            characteristicRead = requestService.getCharacteristic(UUID.fromString(SampleGattAttributes.OS_TYPE_CHAR_UUIDTX));
            characteristicMeasurement = requestService.getCharacteristic(UUID.fromString(SampleGattAttributes.STORED_DATA_UUID));
            writeLiveTemp = requestService.getCharacteristic(UUID.fromString(SampleGattAttributes.WRITE_UUIDRX));
            readLiveTemp = myTempratureService.getCharacteristic(UUID.fromString(SampleGattAttributes.TEMPERATURE_MEASUREMENT));


            if(status == BluetoothGatt.GATT_SUCCESS){
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);

                for (BluetoothGattService bluetoothGattService : getSupportedServices(gatt)) {
                    //System.out.println("ALL Services:"  + bluetoothGattService.getUuid());
                    /*if (SampleGattAttributes.SERVICE_UUID_TURN_OFF.equals(bluetoothGattService.getUuid().toString())) {
                        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : getSupportedGattCharacteristics(bluetoothGattService)) {
                            //System.out.println("All characteristics of SERVICE_UUID_TURN_OFF : " + bluetoothGattCharacteristic.getUuid());
                            if ( SampleGattAttributes.TURN_OFF_CHARACTERISTIC.equals(bluetoothGattCharacteristic.getUuid().toString())) {
                                System.out.println(gatt.getDevice().getAddress());
                                System.out.println("gatt : "+gatt.toString());
                                //bluetoothGatts.add(gatt);
                                globalBluetoohGattCharacteristics = bluetoothGattCharacteristic;
                            }
                        }
                    }*/


                    if(SampleGattAttributes.BATTERY_SERVICE_UUID.equals(bluetoothGattService.getUuid().toString())){
                        for(BluetoothGattCharacteristic bluetoothGattCharacteristic: getSupportedGattCharacteristics(bluetoothGattService)){
                            if(SampleGattAttributes.BATTERY_CHARACTERISTIC.equals(bluetoothGattCharacteristic.getUuid().toString())){
                                System.out.println("battery characteristic var ");
                                //setCharacteristicNotification(gatt,batteryCharacteristic,true);
                            }
                        }
                    }

                }

                        String str = "OKAN";
                        byte[] bytess = str.getBytes();
                        System.out.println("Array as String " + Arrays.toString(bytess));
                        characteristicWrite.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                        characteristicWrite.setValue(bytess);
                        gatt.writeCharacteristic(characteristicWrite);
                        setCharacteristicNotification(gatt,characteristicRead,true);


            }

            else{
                System.out.println("onServicesDiscovered: " + status);
            }

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            System.out.println("onCharacteristicRead");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                byte[] myValue = characteristic.getValue();
                System.out.println("ReadArray : " + Arrays.toString(myValue));

            }

        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            System.out.println("onCharacteristicWrite1: "+writeLiveTemp.getUuid());
            System.out.println("onCharacteristicWrite2: "+characteristic.getUuid());
                if(status != BluetoothGatt.GATT_SUCCESS){
                    System.out.println("WRITE FAIL tekrar deniyor..");
                    System.out.println("Status: "+status);
                    globalStatus = status;

                    gatt.writeCharacteristic(characteristic);
                }

                if(writeLiveTemp.getUuid() == characteristic.getUuid()){

                    System.out.println("Live data Write işlemi");
                    System.out.println("Write işlemine ait adres: "+gatt.getDevice().getAddress());
                    /*Runnable runnable1 = new Runnable() {
                        @Override
                        public void run() {

                        }
                    };
                    AsyncTask.execute(runnable1);*/

                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }).start();*/
                    setCharacteristicNotification(gatt,readLiveTemp,true);

                }
                /*if(characteristicWrite.getUuid() == characteristic.getUuid()){
                    //gatt.readCharacteristic(characteristicRead);
                    setCharacteristicNotification(gatt,characteristicRead,true);
                }*/
                else{

                    byte[] bytes = {1};
                    writeLiveTemp.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                    writeLiveTemp.setValue(bytes);
                    gatt.writeCharacteristic(writeLiveTemp);
                    System.out.println("LIVE DATA YAZIILIYOR ŞU ADRESE: "+ gatt.getDevice().getAddress());




                  /*Thread thread2 = new Thread(new Runnable() {
                      @Override
                      public void run() {
                          byte[] bytes = {1};
                          writeLiveTemp.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                          writeLiveTemp.setValue(bytes);
                          gatt.writeCharacteristic(writeLiveTemp);
                          System.out.println("LIVE DATA YAZIILIYOR ŞU ADRESE: "+ gatt.getDevice().getAddress());
                      }
                  });
                  thread2.start();
                    System.out.println("Thread2: "+thread2.getName());*/


                    //setCharacteristicNotification(gatt,characteristicMeasurement,true);


                }

        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            super.onCharacteristicChanged(gatt,characteristic);
            System.out.println("onCharacteristicChanged: "+ characteristic.getUuid().toString());
            byte[] myValue = characteristic.getValue();
            System.out.println("onChangeArray : " + Arrays.toString(myValue) + " " + gatt.getDevice().getAddress());




            if(characteristic.getUuid() == batteryCharacteristic.getUuid()){
                byte[] myValue1 = characteristic.getValue();
                System.out.println("onChangeArrayBattery : " + Arrays.toString(myValue1) + " " + gatt.getDevice().getAddress());
                String battery_level = Arrays.toString(myValue1).charAt(1)+""+Arrays.toString(myValue1).charAt(2)+""+Arrays.toString(myValue1).charAt(3);
                sharedPreferences.edit().putString("batteryLevel",battery_level).apply();

            }


            if(characteristic.getUuid() == characteristicMeasurement.getUuid()){
                System.out.println("Depolanan sicaklik onCharacteristicChanged "+ gatt.getDevice().getAddress());
                convertStoredData(characteristic);
                byte[] bytes = {1};
                writeLiveTemp.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                writeLiveTemp.setValue(bytes);
                gatt.writeCharacteristic(writeLiveTemp);
                System.out.println("LIVE DATA YAZIILIYOR ŞU ADRESE: "+ gatt.getDevice().getAddress());
                //broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }

            else if(characteristic.getUuid() == characteristicRead.getUuid()){
                counter++;
                System.out.println("CounterBLE: "+counter);

                /*if(counter==2){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            byte[] bytes = {1};
                            writeLiveTemp.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                            writeLiveTemp.setValue(bytes);
                            gatt.writeCharacteristic(writeLiveTemp);
                            System.out.println("LIVE DATA YAZIILIYOR ŞU ADRESE: "+ gatt.getDevice().getAddress());
                        }
                    }).start();
                    counter=0;
                }*/





                    index1 = Arrays.toString(myValue).charAt(1);
                    System.out.println("Index1: "+index1);
                    broadcastUpdate(FIRST_INDEX0);


                    String byteToString = Arrays.toString(myValue);
                    char IndexLast =  byteToString.charAt(byteToString.length()-2);
                    char IndexLast1 = byteToString.charAt(byteToString.length()-5);
                    char IndexLast2 = byteToString.charAt(byteToString.length()-8);
                    char IndexLast3 = byteToString.charAt(byteToString.length()-11);
                    char[] firmwareVersionCharArray = new char[7];
                    firmwareVersionCharArray[0]= IndexLast3;
                    firmwareVersionCharArray[1]='.';
                    firmwareVersionCharArray[2] = IndexLast2;
                    firmwareVersionCharArray[3]='.';
                    firmwareVersionCharArray[4] = IndexLast1;
                    firmwareVersionCharArray[5]='.';
                    firmwareVersionCharArray[6] = IndexLast;
                    firmwareVersionString = "v"+String.valueOf(firmwareVersionCharArray);
                    System.out.println("FirmwareVersion: "+ firmwareVersionString);
                    sharedPreferences.edit().putString("firmwareVersiyon",firmwareVersionString).apply();



                    //Canlı Veri okuma kanalını direkt olarak açtık.
                    //setCharacteristicNotification(gatt,readLiveTemp,true);

                    /*byte[] bytes = {1};
                    writeLiveTemp.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                    writeLiveTemp.setValue(bytes);
                    gatt.writeCharacteristic(writeLiveTemp);
                    System.out.println("LIVE DATA YAZIILIYOR ŞU ADRESE: "+ gatt.getDevice().getAddress());*/

                /*char[] ch = new char[Arrays.toString(myValue).length()];
                for (int i = 0; i < Arrays.toString(myValue).length(); i++) {
                    ch[i] = Arrays.toString(myValue).charAt(i);
                }
                System.out.println("nedir ilk dizi" + ch[1]);
                if(Arrays.toString(myValue).getBytes().length>2) {
                    if (String.valueOf(ch[1]).equals("0")) {
                        System.out.println("PROb");
                        //gatt.close();
                    }
                }*/


            }
            else {
                if(!characteristic.getUuid().toString().toLowerCase().equals("e093f3b5-00a3-a9e5-9eca-40036e0edc24")
                        && !characteristic.getUuid().toString().toLowerCase().equals("e093f3b5-00a3-a9e5-9eca-40046e0edc24"))
                {
                    System.out.println("Canlı Sicaklik verisi: " + gatt.getDevice().getAddress());
                    System.out.println("Canlı Sicaklik verisi KARAKTER ne ? : " + characteristic.getUuid());

                    final StringBuilder stringBuilder = new StringBuilder(myValue.length);
                    for (int i = 2; i >= 1; i--) {
                        stringBuilder.append(String.format("%02X", myValue[i]));
                    }
                    System.out.println("String builder: " + stringBuilder.toString());
                    String a = stringBuilder.toString();
                    int liveData = Integer.parseInt(a, 16);
                    System.out.println("live data virgulsuz: " + liveData);
                    double newValue = liveData * 0.01;
                    lastValue = new DecimalFormat("##.#").format(newValue);
                    System.out.println("live data virgullü:" + new DecimalFormat("##.#").format(newValue));

                    System.out.println("LiveDataGatt: "+gatt);
                    globalBluetoothGatt = gatt;

                    broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                    sensorDataCreate();
                    //gatt.readCharacteristic(characteristicRead);
                    setCharacteristicNotification(gatt,batteryCharacteristic,true);


                }
            }
           /*else{
               System.out.println("ELSE KISMINA GİRİYORRR");
               byte[] bytes = {1};
               writeLiveTemp.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
               writeLiveTemp.setValue(bytes);
               gatt.writeCharacteristic(writeLiveTemp);
           }*/

        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }
    };



    public boolean connect(final String address){
        System.out.println("CONNECTE GELIYOR MU ???");
        if(bluetoothAdapter ==null || address == null){
            System.out.println("BluetoothAdapter not initialized or unspecified adress.");
            return false;
        }

        final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        device.connectGatt(getApplicationContext(),false,gattCallback);

        broadcastUpdate(ACTION_GATT_CONNECT_REQUEST_SEND);
        return true;

    }

    private void broadcastUpdate(final String action){
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,final BluetoothGattCharacteristic characteristic){
        final Intent intent = new Intent(action);



        if(characteristic.getUuid() == characteristicMeasurement.getUuid()){
            String a = decimalStoredData.get(0).toString();
            intent.putExtra(EXTRA_DATA,a);
        }
        if(characteristic.getUuid() == readLiveTemp.getUuid()){
            System.out.println("LAST VALUE : "+lastValue);
            intent.putExtra(EXTRA_DATA,lastValue);
        }
        sendBroadcast(intent);

    }

    public List<BluetoothGattService> getSupportedServices(BluetoothGatt gatt){
        return gatt.getServices();
    }

    public List<BluetoothGattCharacteristic> getSupportedGattCharacteristics(BluetoothGattService gattService){
        if(gattService == null) return null;
        return gattService.getCharacteristics();
    }




    public void setCharacteristicNotification(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic,boolean enabled) {
        if (gatt == null) {
            System.out.println("BluetoothGatt not initialized");
            return;
        }

        gatt.setCharacteristicNotification(characteristic, enabled);


        if (SampleGattAttributes.TEMPERATURE_MEASUREMENT.equals(characteristic.getUuid().toString())) {
            System.out.println("TEMPERATURE_MEASUREMENT descriptor");
            //BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            System.out.println("bu descriptor : "+ gatt.getDevice().getAddress());
            characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG)).setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
            gatt.writeDescriptor(characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG)));
        }
        /*if(SampleGattAttributes.MAC_ID_UUIDRX.equals(characteristic.getUuid().toString())){
            System.out.println("TEST");
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(descriptor);
        }*/
        if(SampleGattAttributes.OS_TYPE_CHAR_UUIDTX.equals(characteristic.getUuid().toString())){
            System.out.println("OS_TYPE_CHAR_UUIDTX descriptor" + gatt.getDevice().getAddress());
            //BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG)).setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG)));


        }
        if(SampleGattAttributes.STORED_DATA_UUID.equals(characteristic.getUuid().toString())){
            System.out.println("STORED_DATA_UUID Descriptor: "+ gatt.getDevice().getAddress());
            //BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG)).setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG)));
        }

        if(SampleGattAttributes.BATTERY_CHARACTERISTIC.equals(characteristic.getUuid().toString())){
            System.out.println("BATTERY_LEVEL_CHARACTERISTIC Descriptor: "+gatt.getDevice().getAddress());
            characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG)).setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG)));
        }


    }


    public ArrayList<Integer> convertStoredData(BluetoothGattCharacteristic characteristic){
        byte[] myValue = characteristic.getValue();
        ArrayList<String> arrayList = new ArrayList<>();
        System.out.println("ReadArray : " + Arrays.toString(myValue));
        if(characteristic.getUuid() == characteristicMeasurement.getUuid()) {
            System.out.println("sicaklikkk");
            if (characteristic != null && characteristic.getValue() != null) {
                byte[] bytes = characteristic.getValue();
                int[] decimal = new int[bytes.length];
                ArrayList<String> arrayList1 = new ArrayList<>();

                for (int i = 0; i < bytes.length; i++) {
                    decimal[i] = bytes[i];
                    arrayList.add(Integer.toBinaryString(decimal[i]));

                }
                for (String b : arrayList) {

                    while (b.length() != 8) {
                        b = "0" + b;
                    }
                    arrayList1.add(b);
                }
                ArrayList<String> dizim = new ArrayList<>();
                for (int i = 1; i < arrayList1.size(); i += 2) {
                    String deger = arrayList1.get(i) + arrayList1.get(i - 1);
                    dizim.add(deger);
                }
                for (String a : dizim) {
                    System.out.println("Decimal Depolanan sicaklik:" + Integer.parseInt(a, 2));
                    decimalStoredData.add(Integer.parseInt(a, 2));
                }
            }
        }
        return decimalStoredData;
    }


    public void close(){
        System.out.println("Close");
        if(bluetoothGatt ==null){
            return;
        }
        bluetoothGatt.close();
        bluetoothGatt=null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("onUnBind KISMINDAAAAAAAAAAAA");
        return super.onUnbind(intent);
    }




    public void sensorDataCreate(){
        Long tsLong = System.currentTimeMillis()/1000;
        //int sensorIdInt = Integer.parseInt(AfterScanActivity.sensorId);

        //sharedPreferences = this.getSharedPreferences("com.okankocakose.wearosdeneme",MODE_PRIVATE);
        sensorId = sharedPreferences.getInt("sensorId",0);
        System.out.println("SensorId: "+sensorId);

        Call<ResponseBody> sensorDataCreate = RetrofitClient.getInstance().getMyApi().sensorDataCreate(new SensorDataCreateModel(lastValue,tsLong,sensorId));
        sensorDataCreate.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    System.out.println("SensorDataCreate Başarılı");
                    System.out.println("sensorId BLE : "+sensorId);

                }else {

                    System.out.println("SensorDataCreate Başarısız");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    public void getConnectedDevices (){
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        List<BluetoothDevice> devices = bluetoothManager.getConnectedDevices(BluetoothProfile.GATT);
        for(BluetoothDevice device: devices){
            System.out.println("ConnectedDevices: "+ device.getAddress());

        }
    }


    @Override
    public void onDestroy() {
        System.out.println("BleOnDestroy");
        super.onDestroy();
    }

}
