package com.example.android.sqliteweather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.sqliteweather.data.LoadingStatus;
import com.example.android.sqliteweather.data.MovieRepository;
import com.example.android.sqliteweather.data.PopularMovies;

public class MovieViewModel extends ViewModel {
    private MovieRepository repository;
    private LiveData<PopularMovies> popularMovies;
    private LiveData<LoadingStatus> loadingStatus;

    public MovieViewModel() {
        this.repository = new MovieRepository();
        popularMovies = repository.getPopularMovies();
        loadingStatus = repository.getLoadingStatus();
    }

    public LiveData<PopularMovies> getPopularMovies() {
        return this.popularMovies;
    }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public void loadPopularMovies(String apiKey) {
        this.repository.loadPopularMovies(apiKey);
    }
}
