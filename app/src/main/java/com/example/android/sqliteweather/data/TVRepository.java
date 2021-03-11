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

public class TVRepository {
    private static final String TAG = MovieRepository.class.getSimpleName();
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private MutableLiveData<PopularTVShows> popularTVShows;
    private MutableLiveData<LoadingStatus> loadingStatus;

    private PopularTVService popularTVService;

    public TVRepository()
    {
        this.popularTVShows = new MutableLiveData<>();
        this.popularTVShows.setValue(null);

        this.loadingStatus = new MutableLiveData<>();
        this.loadingStatus.setValue(LoadingStatus.SUCCESS);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(PopularTVShowsData.class, new PopularTVShowsData.JsonDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.popularTVService = retrofit.create(PopularTVService.class);
    }

    public LiveData<PopularTVShows> getPopularTVShows() { return this.popularTVShows; }

    public LiveData<LoadingStatus> getLoadingStatus() { return this.loadingStatus; }

    public void loadPopularTVShows(String apiKey)
    {
        Log.d(TAG, "fetching new popular tv show data");
        this.popularTVShows.setValue(null);
        this.loadingStatus.setValue(LoadingStatus.LOADING);

        Call<PopularTVShows> req = this.popularTVService.fetchPopularTVShows(apiKey);
        req.enqueue(new Callback<PopularTVShows>() {
            @Override
            public void onResponse(Call<PopularTVShows> call, Response<PopularTVShows> response) {
                if (response.code() == 200) {
                    Log.d(TAG, "successful response");
                    popularTVShows.setValue(response.body());
//                    Log.d(TAG, response.body().getPopularTVResults().get(0).getTitle());
                    loadingStatus.setValue(LoadingStatus.SUCCESS);
                } else {
                    loadingStatus.setValue(LoadingStatus.ERROR);
                    Log.d(TAG, "unsuccessful API request: " + call.request().url());
                    Log.d(TAG, "  -- response status code: " + response.code());
                    Log.d(TAG, "  -- response: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<PopularTVShows> call, Throwable t) {
                loadingStatus.setValue(LoadingStatus.ERROR);
                Log.d(TAG, "unsuccessful API request: " + call.request().url());
                t.printStackTrace();
            }
        });
    }
}
