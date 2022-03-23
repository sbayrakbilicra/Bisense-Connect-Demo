package com.whatsapp.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class ThermoBiEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "temperature")
    public String temperature;

    @ColumnInfo(name = "macAddress")
    public String macAddress;

    public ThermoBiEntity(String name, String temperature,String macAddress) {
        this.name = name;
        this.temperature = temperature;
        this.macAddress=macAddress;
    }

    public void setId(int id) {
        this.id = id;
    }

}
