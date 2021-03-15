package com.example.android.sqliteweather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.sqliteweather.data.LoadingStatus;
import com.example.android.sqliteweather.data.MovieData;
import com.example.android.sqliteweather.data.MovieRepository;
import com.example.android.sqliteweather.data.PopularMovieRepository;
import com.example.android.sqliteweather.data.PopularMovies;

import java.util.ArrayList;

public class MovieViewModel extends ViewModel {
    private PopularMovieRepository popularMovieRepository;
    private LiveData<PopularMovies> popularMovies;
    private LiveData<LoadingStatus> loadingStatus;
    private MovieRepository movieRepository;
    private LiveData<ArrayList<MovieData>> movieData;


    public MovieViewModel() {
        this.popularMovieRepository = new PopularMovieRepository();
        popularMovies = popularMovieRepository.getPopularMovies();
        loadingStatus = popularMovieRepository.getLoadingStatus();
        this.movieRepository = new MovieRepository();
        this.movieData = movieRepository.getMovies();
    }

    public LiveData<PopularMovies> getPopularMovies() {
        return this.popularMovies;
    }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public LiveData<ArrayList<MovieData>> getMovieData() { return this.movieData; }

    public void loadPopularMovies(String apiKey) {
        this.popularMovieRepository.loadPopularMovies(apiKey);
    }

    public void loadMovieData(String apiKey, ArrayList<Integer> ids) {
         this.movieRepository.loadMovieData(apiKey, ids);
    }
}
