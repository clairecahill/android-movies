package com.example.android.sqliteweather.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {
    @GET("movie/popular")
    Call<PopularMovies> fetchPopularMovies(
        @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}")
    Call<MovieData> fetchMovieData(
        @Path(value = "movie_id")
        String id,
        @Query("api_key") String apiKey
    );
}
