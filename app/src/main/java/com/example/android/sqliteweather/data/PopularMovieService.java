package com.example.android.sqliteweather.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PopularMovieService {
    @GET("movie/popular")
    Call<PopularMovies> fetchPopularMovies(
            @Query("api_key") String apiKey
    );
}
