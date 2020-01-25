package com.example.werkstuk.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.werkstuk.TimeInstance;

import java.util.List;

@Dao
public interface TimeInstanceDAO {

    @Insert
    void insert(TimeInstance timeInstance);

    @Update
    void update(TimeInstance timeInstance);

    @Delete
    void delete(TimeInstance timeInstance);

    @Query("SELECT * FROM timeInstance_table")
    LiveData<List<TimeInstance>> getAllTimeInstances();

    @Query("SELECT * FROM timeInstance_table WHERE `on` = 1")
    List<TimeInstance> getAllTimeInstancesListIsOn();

    /*
    @Query("SELECT * FROM timeInstance_table WHERE id = :id AND `on` = 1")
    TimeInstance getByeIdAndIsOn(long id);
    */

    @Query("DELETE FROM timeInstance_table")
    void deleteAll();

}
