package com.example.android.sqliteweather.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PopularTVService {
    @GET("tv/popular")
    Call<PopularTVShows> fetchPopularTVShows(
            @Query("api_key") String apiKey
    );
}
