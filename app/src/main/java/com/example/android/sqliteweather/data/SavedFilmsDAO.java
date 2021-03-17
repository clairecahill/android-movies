package com.example.android.sqliteweather.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SavedFilmsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FilmsEntityRepository film);

    @Delete
    void delete(FilmsEntityRepository film);

    @Query("SELECT * FROM films ORDER BY timestamp DESC")
    LiveData<List<FilmsEntityRepository>> getAllLocations();
}
