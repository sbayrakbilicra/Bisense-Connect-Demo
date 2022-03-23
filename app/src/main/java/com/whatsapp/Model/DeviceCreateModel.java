package com.whatsapp.Model;

import com.google.gson.JsonArray;

import java.util.List;

public class DeviceCreateModel {

    public int ownerUserId;
    public String description;
    public String name;
    public int type;

    /*public DeviceCreateModel(String name, int type) {
        this.name = name;
        this.type = type;
    }*/
    public int softwareVersion;
    public JsonArray deviceSettings;
    public List<DeviceActionsModel> deviceActionsModels;
    public List<SensorsModel> sensorsModels;

    public DeviceCreateModel(int ownerUserId, String description, String name, int type, int softwareVersion, JsonArray deviceSettings, List<DeviceActionsModel> deviceActionsModels, List<SensorsModel> sensorsModels) {
        this.ownerUserId = ownerUserId;
        this.description = description;
        this.name = name;
        this.type = type;
        this.softwareVersion = softwareVersion;
        this.deviceSettings = deviceSettings;
        this.deviceActionsModels = deviceActionsModels;
        this.sensorsModels = sensorsModels;
    }


}
