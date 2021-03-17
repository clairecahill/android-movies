package com.example.android.sqliteweather.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "films")
public class FilmsEntityRepository {
    @PrimaryKey
    @NonNull
    public String film;
    public long timestamp;
}
