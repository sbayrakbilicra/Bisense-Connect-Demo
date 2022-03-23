package com.whatsapp.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.whatsapp.Entity.ThermoBiEntity;

import java.util.List;

@Dao
public interface ThermoBiDao {

    @Insert
    void insert(ThermoBiEntity thermoBiEntity);

    @Update
    void update(ThermoBiEntity thermoBiEntity);

    @Delete
    void delete(ThermoBiEntity thermoBiEntity);


    @Query("SELECT * FROM note_table")
    LiveData<List<ThermoBiEntity>> getAllThermoBi();

    @Query("DELETE FROM note_table")
    void deleteAllThermoBi();

    @Query("UPDATE note_table SET temperature = :temperature WHERE macAddress =:address")
    void updateQuery(String temperature,String address);



}
