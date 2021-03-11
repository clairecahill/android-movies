package com.example.android.sqliteweather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.sqliteweather.data.LoadingStatus;
import com.example.android.sqliteweather.data.PopularTVShows;
import com.example.android.sqliteweather.data.TVRepository;

public class TVShowsViewModel extends ViewModel {
    private TVRepository repository;
    private LiveData<PopularTVShows> popularTVShows;
    private LiveData<LoadingStatus> loadingStatus;

    public TVShowsViewModel() {
        this.repository = new TVRepository();
        popularTVShows = repository.getPopularTVShows();
        loadingStatus = repository.getLoadingStatus();
    }

    public LiveData<PopularTVShows> getPopularTVShows() {
        return this.popularTVShows;
    }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public void loadPopularTVShows(String apiKey) {
        this.repository.loadPopularTVShows(apiKey);
    }

}
