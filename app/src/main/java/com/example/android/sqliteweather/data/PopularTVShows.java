package com.example.android.sqliteweather.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PopularTVShows {
    @SerializedName("results")
    private ArrayList<PopularResult> popularTVResults;

    public ArrayList<PopularResult> getPopularTVResults() {
        return popularTVResults;
    }
}
