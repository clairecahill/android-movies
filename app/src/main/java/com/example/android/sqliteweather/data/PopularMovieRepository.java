package com.example.android.sqliteweather.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PopularMovieRepository {
    private static final String TAG = PopularMovieRepository.class.getSimpleName();
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private MutableLiveData<PopularMovies> popularMovies;
    private MutableLiveData<LoadingStatus> loadingStatus;
    private MovieService movieService;

    public PopularMovieRepository() {
        this.popularMovies = new MutableLiveData<>();
        this.popularMovies.setValue(null);

        this.loadingStatus = new MutableLiveData<>();
        this.loadingStatus.setValue(LoadingStatus.SUCCESS);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(PopularResult.class, new PopularResult.JsonDeserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.movieService = retrofit.create(MovieService.class);
    }

    public LiveData<PopularMovies> getPopularMovies() {
        return this.popularMovies;
    }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

//    public void loadPopularMovies(String apiKey) {
////        if (shouldFetchForecast(location, units)) {
//            Log.d(TAG, "fetching new popular movie data");
//            this.popularMovies.setValue(null);
//            this.loadingStatus.setValue(LoadingStatus.LOADING);
//            Call<PopularMovies> req = this.movieService.fetchPopularMovies(apiKey);
//            req.enqueue(new Callback<PopularMovies>() {
//                @Override
//                public void onResponse(Call<PopularMovies> call, Response<PopularMovies> response) {
//                    if (response.code() == 200) {
//                        Log.d(TAG, "successful response: " + call.request().url());
//                        popularMovies.setValue(response.body());
//                        loadingStatus.setValue(LoadingStatus.SUCCESS);
//                    } else {
//                        loadingStatus.setValue(LoadingStatus.ERROR);
//                        Log.d(TAG, "unsuccessful API request: " + call.request().url());
//                        Log.d(TAG, "  -- response status code: " + response.code());
//                        Log.d(TAG, "  -- response: " + response.toString());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<PopularMovies> call, Throwable t) {
//                    loadingStatus.setValue(LoadingStatus.ERROR);
//                    Log.d(TAG, "unsuccessful API request: " + call.request().url());
//                    t.printStackTrace();
//                }
//            });
////        } else {
////            Log.d(TAG, "using cached forecast data for location: " + location + ", units: " + units);
////        }
//    }

    public void loadPopularMovies(String apiKey, String sort) {
//        if (shouldFetchForecast(location, units)) {
        Log.d(TAG, "fetching new popular movie data");
        this.popularMovies.setValue(null);
        this.loadingStatus.setValue(LoadingStatus.LOADING);
        Call<PopularMovies> req = this.movieService.fetchSortedMovies(apiKey, sort);
        req.enqueue(new Callback<PopularMovies>() {
            @Override
            public void onResponse(Call<PopularMovies> call, Response<PopularMovies> response) {
                if (response.code() == 200) {
                    Log.d(TAG, "in sorting successful response: " + call.request().url());
                    popularMovies.setValue(response.body());
                    loadingStatus.setValue(LoadingStatus.SUCCESS);
                }

                else {
                    loadingStatus.setValue(LoadingStatus.ERROR);
                    Log.d(TAG, "unsuccessful API request: " + call.request().url());
                    Log.d(TAG, "  -- response status code: " + response.code());
                    Log.d(TAG, "  -- response: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<PopularMovies> call, Throwable t) {
                loadingStatus.setValue(LoadingStatus.ERROR);
                Log.d(TAG, "unsuccessful API request: " + call.request().url());
                t.printStackTrace();
            }
        });
    }
}
