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

public class MovieRepository {
    private static final String TAG = MovieRepository.class.getSimpleName();
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private MutableLiveData<ArrayList<MovieData>> movies;
    private MutableLiveData<LoadingStatus> loadingStatus;
    private MovieService movieService;

    public MovieRepository() {
        this.movies = new MutableLiveData<>();
        this.movies.setValue(null);

        this.loadingStatus = new MutableLiveData<>();
        this.loadingStatus.setValue(LoadingStatus.SUCCESS);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(MovieData.class, new MovieData.JsonDeserializer())
                .serializeNulls()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.movieService = retrofit.create(MovieService.class);
    }

    public LiveData<ArrayList<MovieData>> getMovies() {
        return this.movies;
    }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public void loadMovieData(String apiKey, ArrayList<Integer> ids) {
//        if (shouldFetchForecast(location, units)) {
            Log.d(TAG, "fetching new movie data");
            this.movies.setValue(null);
            this.loadingStatus.setValue(LoadingStatus.LOADING);
            ArrayList<MovieData> tempMovies = new ArrayList<>();
            Log.d(TAG, "ids: " + ids.size());

            for (int i = 0; i < ids.size(); i++) {
                Call<MovieData> req = this.movieService.fetchMovieData(String.valueOf(ids.get(i)), apiKey);
                req.enqueue(new Callback<MovieData>() {
                    @Override
                    public void onResponse(Call<MovieData> call, Response<MovieData> response) {
                        if (response.code() == 200) {
//                            Log.d(TAG, "successful response");
                            tempMovies.add(response.body());
                            movies.setValue(tempMovies);
                            loadingStatus.setValue(LoadingStatus.SUCCESS);
                        } else {
                            loadingStatus.setValue(LoadingStatus.ERROR);
                            Log.d(TAG, "unsuccessful API request: " + call.request().url());
                            Log.d(TAG, "  -- response status code: " + response.code());
                            Log.d(TAG, "  -- response: " + response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieData> call, Throwable t) {
                        loadingStatus.setValue(LoadingStatus.ERROR);
                        Log.d(TAG, "unsuccessful API request: " + call.request().url());
                        t.printStackTrace();
                    }
                });
            }
//        } else {
//            Log.d(TAG, "using cached forecast data for location: " + location + ", units: " + units);
//        }
    }

//    private boolean shouldFetchForecast(String location, String units) {
//        /*
//         * Fetch forecast if there isn't currently one stored.
//         */
//        FiveDayForecast currentForecast = this.fiveDayForecast.getValue();
//        if (currentForecast == null) {
//            return true;
//        }
//
//        /*
//         * Fetch forecast if there was an error fetching the last one.
//         */
//        if (this.loadingStatus.getValue() == LoadingStatus.ERROR) {
//            return true;
//        }
//
//        /*
//         * Fetch forecast if either location or units have changed.
//         */
//        if (!TextUtils.equals(location, this.currentLocation) || !TextUtils.equals(units, this.currentUnits)) {
//            return true;
//        }
//
//        /*
//         * Fetch forecast if the earliest of the current forecast data is timestamped before "now".
//         */
//        if (currentForecast.getForecastDataList() != null && currentForecast.getForecastDataList().size() > 0) {
//            ForecastData firstForecastData = currentForecast.getForecastDataList().get(0);
//            if (firstForecastData.getEpoch() * 1000L < System.currentTimeMillis()) {
//                return true;
//            }
//        }
//
//        /*
//         * Otherwise, don't fetch the forecast.
//         */
//        return false;
//    }
}
