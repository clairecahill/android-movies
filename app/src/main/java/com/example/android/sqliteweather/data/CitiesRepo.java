package com.example.android.sqliteweather.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "location")
public class CitiesRepo implements Serializable {
    @PrimaryKey
    @NonNull
    public String city;

    public long timestamp;
}
