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
}
