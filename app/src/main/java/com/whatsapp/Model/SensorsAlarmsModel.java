package com.whatsapp.Model;

public class SensorsAlarmsModel {

    public int sensorId;
    public String name;
    public String value;
    public int type;
    public int notificationTypes;
    public int Id;

    public SensorsAlarmsModel(int sensorId, String name, String value, int type, int notificationTypes, int id) {
        this.sensorId = sensorId;
        this.name = name;
        this.value = value;
        this.type = type;
        this.notificationTypes = notificationTypes;
        Id = id;
    }
}
