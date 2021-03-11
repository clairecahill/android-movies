package com.example.android.sqliteweather.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.Serializable;
import java.lang.reflect.Type;

public class PopularTVShowsData implements Serializable {

    private String title;
    private float popularity;
    private String overview;
    private String poster_path;
    private String first_air_date;
    private float vote_average;

    public PopularTVShowsData(String title, float popularity, String overview, String poster_path, String first_air_date, float vote_average) {
        this.title = title;
        this.popularity = popularity;
        this.overview = overview;
        this.poster_path = poster_path;
        this.first_air_date = first_air_date;
        this.vote_average = vote_average;
    }

    public String getTitle() { return this.title; }

    public float getPopularity() { return this.popularity; }

    public String getOverview() { return this.overview; }

    public String getIconUrl() { return this.poster_path; }

    public String getFirstAirDate() { return this.first_air_date; }

    public float getVoteAverage() { return this.vote_average; }

    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<PopularTVShowsData> {
        @Override
        public PopularTVShowsData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject resultsObj = json.getAsJsonObject();

            return new PopularTVShowsData(
                    resultsObj.getAsJsonPrimitive("name").getAsString(),
                    (float)Math.round(resultsObj.getAsJsonPrimitive("popularity").getAsFloat()) / 100,
                    resultsObj.getAsJsonPrimitive("overview").getAsString(),
                    resultsObj.getAsJsonPrimitive("poster_path").getAsString(),
                    resultsObj.getAsJsonPrimitive("first_air_date").getAsString(),
                    (float)Math.round(resultsObj.getAsJsonPrimitive("vote_average").getAsFloat())
            );
        }
    }
}
