package com.example.android.sqliteweather;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.sqliteweather.data.CitiesRepo;
import com.example.android.sqliteweather.data.LoadingStatus;
import com.example.android.sqliteweather.data.SavedCitiesRepository;

import java.util.List;

public class CityViewModel extends AndroidViewModel {
    private SavedCitiesRepository savedCitiesRepository;


    public CityViewModel(@NonNull Application application) {
        super(application);
        this.savedCitiesRepository = new SavedCitiesRepository(application);
    }

    public void insertCity(CitiesRepo city)
    {
        this.savedCitiesRepository.insertCity(city);
    }

    public void deleteCity(CitiesRepo city)
    {
        this.savedCitiesRepository.deleteCity(city);
    }

    public LiveData<List<CitiesRepo>> getAllCities()
    {
        return this.savedCitiesRepository.getAllCities();
    }
}
