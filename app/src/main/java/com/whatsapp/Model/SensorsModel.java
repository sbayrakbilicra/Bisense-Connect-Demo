package com.whatsapp.Model;

import java.util.List;

public class SensorsModel {

    public String groupName;
    public int groupOrder;
    public String name;
    public String description;
    public int type;
    public int deviceId;
    public boolean isDeleted;
    public int deleteUserId;
    public String deletionTime;
    public String lastModificationTime;
    public int lastModifierUserId;
    public String creationTime;
    public int creatorUserId;
    public int ID;
    public List<SensorsAlarmsModel> sensorsAlarmsModels;

    public SensorsModel(String groupName, int groupOrder, String name, String description, int type, int deviceId, boolean isDeleted, int deleteUserId, String deletionTime, String lastModificationTime, int lastModifierUserId, String creationTime, int creatorUserId, int ID, List<SensorsAlarmsModel> sensorsAlarmsModels) {
        this.groupName = groupName;
        this.groupOrder = groupOrder;
        this.name = name;
        this.description = description;
        this.type = type;
        this.deviceId = deviceId;
        this.isDeleted = isDeleted;
        this.deleteUserId = deleteUserId;
        this.deletionTime = deletionTime;
        this.lastModificationTime = lastModificationTime;
        this.lastModifierUserId = lastModifierUserId;
        this.creationTime = creationTime;
        this.creatorUserId = creatorUserId;
        this.ID = ID;
        this.sensorsAlarmsModels = sensorsAlarmsModels;
    }
}
