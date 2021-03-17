package com.example.android.sqliteweather.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class SavedFilmsRepository {
    private SavedFilmsDAO savedFilmsDAO;

    public SavedFilmsRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        savedFilmsDAO = db.savedFilmsDAO();
    }

    public void insertFilmsRepo(FilmsEntityRepository film){
        AppDatabase.databaseWriteExecutor.execute(
                new Runnable(){
                    @Override
                    public void run() {
                        savedFilmsDAO.insert(film);
                    }
                });
    }

    public void deleteFilmsRepo(FilmsEntityRepository film) {
        AppDatabase.databaseWriteExecutor.execute(
                new Runnable() {
                    @Override
                    public void run(){
                        savedFilmsDAO.delete(film);
                    }
                });
    }

    public LiveData<List<FilmsEntityRepository>> getAllFilms(){ return savedFilmsDAO.getAllLocations(); }
}
