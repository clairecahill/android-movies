package com.example.android.sqliteweather;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.sqliteweather.data.FilmsEntityRepository;
import com.example.android.sqliteweather.data.SavedFilmsRepository;

import java.util.List;

public class SavedFilmsViewModel extends AndroidViewModel {
    private SavedFilmsRepository mSavedFilmRepo;

    public SavedFilmsViewModel(@NonNull Application application) {
        super(application);
        mSavedFilmRepo = new SavedFilmsRepository(application);
    }

    public void insertFilm(FilmsEntityRepository film){ mSavedFilmRepo.insertFilmsRepo(film); }

    public void deleteFilm(FilmsEntityRepository film){ mSavedFilmRepo.deleteFilmsRepo(film); }

    public LiveData<List<FilmsEntityRepository>> getAllFilms(){ return mSavedFilmRepo.getAllFilms(); }
}
