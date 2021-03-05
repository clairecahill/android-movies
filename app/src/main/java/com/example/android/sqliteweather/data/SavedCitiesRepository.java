package com.example.android.sqliteweather.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class SavedCitiesRepository {
    private SavedCitiesDao savedCitiesDao;

    public SavedCitiesRepository(Application application)
    {
        AppDatabase database = AppDatabase.getDatabase(application);
        savedCitiesDao = database.savedCitiesDao();
    }

    public LiveData<List<CitiesRepo>> getAllCities()
    {
        return savedCitiesDao.getAllCities();
    }

    public void insertCity(CitiesRepo city)
    {
        AppDatabase.databaseWriteExecutor.execute(new Runnable()
        {
            @Override
            public void run() {
                savedCitiesDao.insert(city);
            }
        });
    }

    public void deleteCity(CitiesRepo city)
    {
        AppDatabase.databaseWriteExecutor.execute(new Runnable()
        {
            @Override
            public void run() {
                savedCitiesDao.delete(city);
            }
        });
    }

}
