package com.example.android.sqliteweather.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.Serializable;
import java.lang.reflect.Type;

public class PopularMovieData implements Serializable {
    private final static String ICON_URL_FORMAT_STR = "https://image.tmdb.org/t/p/w500";

    private String title;
    private float popularity;
    private String overview;
    private String poster_path;
    private String release_date;
    private float vote_average;

    public PopularMovieData(String title, float popularity, String overview, String poster_path, String release_date, float vote_average) {
        this.title = title;
        this.popularity = popularity;
        this.overview = overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.vote_average = vote_average;
    }

    public String getTitle() { return this.title; }

    public float getPopularity() { return this.popularity; }

    public String getOverview() { return this.overview; }

    public String getIconUrl() { return this.poster_path; }

    public String getReleaseDate() { return this.release_date; }

    public float getVoteAverage() { return this.vote_average; }

    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<PopularMovieData> {
        @Override
        public PopularMovieData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject resultsObj = json.getAsJsonObject();
//            JsonObject mainObj = listObj.getAsJsonObject("main");
//            JsonArray weatherArr = listObj.getAsJsonArray("weather");
//            JsonObject weatherObj = weatherArr.get(0).getAsJsonObject();
//            JsonObject cloudsObj = listObj.getAsJsonObject("clouds");
//            JsonObject windObj = listObj.getAsJsonObject("wind");

            return new PopularMovieData(
                resultsObj.getAsJsonPrimitive("title").getAsString(),
                (float)Math.round(resultsObj.getAsJsonPrimitive("popularity").getAsFloat()) / 100,
                    resultsObj.getAsJsonPrimitive("overview").getAsString(),
                    resultsObj.getAsJsonPrimitive("poster_path").getAsString(),
                    resultsObj.getAsJsonPrimitive("release_date").getAsString(),
                    (float)Math.round(resultsObj.getAsJsonPrimitive("vote_average").getAsFloat())
            );
        }
    }
}
