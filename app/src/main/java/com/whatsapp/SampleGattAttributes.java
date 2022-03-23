package com.whatsapp;

import java.util.HashMap;

public class SampleGattAttributes {
    //Characteristics
    public static String BATTERY_CHARACTERISTIC = "00002a19-0000-1000-8000-00805f9b34fb";//Battery Characteristic
    public static String TEMPERATURE_MEASUREMENT ="00002a1c-0000-1000-8000-00805f9b34fb"; // canliveri okuma karakteristik
    //public static String TEMPERATURE_MEASUREMENT_RX ="00002a1c-0000-1000-8000-00805f9b34fb"; //Yazma
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String WRITE_UUIDRX = "e093f3b5-00a3-a9e5-9eca-40026e0edc24";//canli veri yazma karakteristik
    public static String OS_TYPE_CHAR_UUIDTX = "e093f3b5-00a3-a9e5-9eca-40036e0edc24";//oku
    public static String MAC_ID_UUIDRX= "e093f3b5-00a3-a9e5-9eca-40056e0edc24";//yaz
    public static String STORED_DATA_UUID = "e093f3b5-00a3-a9e5-9eca-40046e0edc24";
    public static String TURN_OFF_CHARACTERISTIC = "c1db7675-27ed-4853-bda5-44e7e3e2f650"; // TurnOff Characteristic

    //Servises
    public static String SERVICE_UUID_TURN_OFF = "c07c51c3-aefd-441b-8a29-48c956378b76"; // TurnOff Service UUID
    public static String SERVICE_FOTA = "1c58095a-a10b-4497-90b7-11263b439e08";
    public static String TEMPERATURE_SERVICE_UUID = "00001809-0000-1000-8000-00805f9b34fb";//canlı veri okuma servisi
    public static String OS_TYPE_SERVICE_UUID = "e093f3b5-00a3-a9e5-9eca-40016e0edc24";//Yazma-okuma Servisi
    public static String BATTERY_SERVICE_UUID = "0000180f-0000-1000-8000-00805f9b34fb";//Battery Servisi

}
