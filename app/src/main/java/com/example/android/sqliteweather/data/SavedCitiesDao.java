package com.example.android.sqliteweather.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import java.util.List;


@Dao
public interface SavedCitiesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CitiesRepo city);

    @Delete
    void delete(CitiesRepo city);

    @Query("SELECT * FROM location ORDER BY timestamp DESC")
    LiveData<List<CitiesRepo>> getAllCities();
}
