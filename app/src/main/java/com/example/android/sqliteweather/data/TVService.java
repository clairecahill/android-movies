package com.example.android.sqliteweather.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TVService {
    @GET("tv/popular")
    Call<PopularTVShows> fetchPopularTVShows(
            @Query("api_key") String apiKey
    );

    @GET("tv/{tv_id}")
    Call<TVShowsData> fetchTVShowData(
            @Path(value = "tv_id")
                    String id,
            @Query("api_key") String apiKey
    );

    @GET("discover/tv")
    Call<PopularTVShows> fetchSortedTVShows(
            @Query("api_key") String apiKey,
            @Query("sort_by") String sort
    );
}
