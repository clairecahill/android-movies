package com.example.android.sqliteweather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.sqliteweather.data.LoadingStatus;
import com.example.android.sqliteweather.data.PopularTVShows;
import com.example.android.sqliteweather.data.PopularTVRepository;
import com.example.android.sqliteweather.data.TVRepository;
import com.example.android.sqliteweather.data.TVShowsData;

import java.util.ArrayList;

public class TVShowsViewModel extends ViewModel {
    private PopularTVRepository repository;
    private LiveData<PopularTVShows> popularTVShows;
    private LiveData<LoadingStatus> loadingStatus;
    private TVRepository tvRepository;
    private LiveData<ArrayList<TVShowsData>> tvShowsData;

    public TVShowsViewModel() {
        this.repository = new PopularTVRepository();
        this.tvRepository = new TVRepository();
        popularTVShows = repository.getPopularTVShows();
        loadingStatus = repository.getLoadingStatus();
        tvShowsData = tvRepository.getTVShows();
    }

    public LiveData<PopularTVShows> getPopularTVShows() {
        return this.popularTVShows;
    }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public LiveData<ArrayList<TVShowsData>> getTvShowsData() { return this.tvShowsData; }

    public void loadPopularTVShows(String apiKey) {
        this.repository.loadPopularTVShows(apiKey);
    }

    public void loadTVShowsData(String apiKey, ArrayList<Integer> ids) {
        this.tvRepository.loadTVShowsData(apiKey, ids);
    }

}
