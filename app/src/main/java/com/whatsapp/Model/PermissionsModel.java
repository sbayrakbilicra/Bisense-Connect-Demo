package com.whatsapp.Model;

public class PermissionsModel {
    public int deviceId;
    public int sensorId;
    public int sensorPermissions;

    public PermissionsModel(int deviceId, int sensorId, int sensorPermissions) {
        this.deviceId = deviceId;
        this.sensorId = sensorId;
        this.sensorPermissions = sensorPermissions;
    }

}
