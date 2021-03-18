package com.example.android.sqliteweather.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TVRepository {
    private static final String TAG = PopularMovieRepository.class.getSimpleName();
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private MutableLiveData<ArrayList<TVShowsData>> TVShows;
    private MutableLiveData<LoadingStatus> loadingStatus;

    private TVService TVService;

    public TVRepository()
    {
        this.TVShows = new MutableLiveData<>();
        this.TVShows.setValue(null);

        this.loadingStatus = new MutableLiveData<>();
        this.loadingStatus.setValue(LoadingStatus.SUCCESS);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(TVShowsData.class, new TVShowsData.JsonDeserializer())
                .serializeNulls()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.TVService = retrofit.create(TVService.class);
    }

    public LiveData<ArrayList<TVShowsData>> getTVShows() { return this.TVShows; }

    public LiveData<LoadingStatus> getLoadingStatus() { return this.loadingStatus; }

    public void loadTVShowsData(String apiKey, ArrayList<Integer> ids)
    {
        Log.d(TAG, "fetching new popular tv show data");
        this.TVShows.setValue(null);
        this.loadingStatus.setValue(LoadingStatus.LOADING);
        ArrayList<TVShowsData> tempTVShows = new ArrayList<>();

        for (int i = 0; i < ids.size(); i++) {
            Call<TVShowsData> req = this.TVService.fetchTVShowData(String.valueOf(ids.get(i)), apiKey);
            req.enqueue(new Callback<TVShowsData>() {
                @Override
                public void onResponse(Call<TVShowsData> call, Response<TVShowsData> response) {
                    if (response.code() == 200) {
                        Log.d(TAG, "successful response");
                        tempTVShows.add(response.body());
                        TVShows.setValue(tempTVShows);
                        loadingStatus.setValue(LoadingStatus.SUCCESS);
                    } else {
                        loadingStatus.setValue(LoadingStatus.ERROR);
                        Log.d(TAG, "unsuccessful API request: " + call.request().url());
                        Log.d(TAG, "  -- response status code: " + response.code());
                        Log.d(TAG, "  -- response: " + response.toString());
                    }
                }

                @Override
                public void onFailure(Call<TVShowsData> call, Throwable t) {
                    loadingStatus.setValue(LoadingStatus.ERROR);
                    Log.d(TAG, "unsuccessful API request: " + call.request().url());
                    t.printStackTrace();
                }
            });
        }
    }
}
