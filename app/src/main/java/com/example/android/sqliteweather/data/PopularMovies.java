package com.example.android.sqliteweather.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PopularMovies {
    @SerializedName("results")
    private ArrayList<PopularMovieData> popularMovieResults;

    public ArrayList<PopularMovieData> getPopularMovieResults() {
        return popularMovieResults;
    }

}
