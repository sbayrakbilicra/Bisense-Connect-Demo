package com.whatsapp.Model;

public class SensorDataCreateModel {
    public String data;
    public long timestamp;
    public int sensorId;

    public SensorDataCreateModel(String data, long timestamp, int sensorId) {
        this.data = data;
        this.timestamp = timestamp;
        this.sensorId = sensorId;
    }


}
