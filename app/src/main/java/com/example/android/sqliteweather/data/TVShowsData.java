package com.example.android.sqliteweather.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.Serializable;
import java.lang.reflect.Type;

public class TVShowsData implements Serializable {

    private String title;
    private float popularity;
    private String overview;
    private String poster_path;
    private String first_air_date;
    private float vote_average;
    private String genre;

    public TVShowsData(String title, float popularity, String overview, String poster_path, String first_air_date, float vote_average, String genre) {
        this.title = title;
        this.popularity = popularity;
        this.overview = overview;
        this.poster_path = poster_path;
        this.first_air_date = first_air_date;
        this.vote_average = vote_average;
        this.genre = genre;
    }

    public String getTitle() { return this.title; }

    public float getPopularity() { return this.popularity; }

    public String getOverview() { return this.overview; }

    public String getIconUrl() { return this.poster_path; }

    public String getFirstAirDate() { return this.first_air_date; }

    public float getVoteAverage() { return this.vote_average; }

    public String getGenre() { return this.genre; }

    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<TVShowsData> {
        @Override
        public TVShowsData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject resultsObj = json.getAsJsonObject();
            JsonArray genreArr = resultsObj.getAsJsonArray("genres");
            JsonObject genreObj = genreArr.get(0).getAsJsonObject();

            return new TVShowsData(
                    resultsObj.getAsJsonPrimitive("name").getAsString(),
                    (float)Math.round(resultsObj.getAsJsonPrimitive("popularity").getAsFloat()) / 100,
                    resultsObj.getAsJsonPrimitive("overview").getAsString(),
                    resultsObj.getAsJsonPrimitive("poster_path").getAsString(),
                    resultsObj.getAsJsonPrimitive("first_air_date").getAsString(),
                    (float)Math.round(resultsObj.getAsJsonPrimitive("vote_average").getAsFloat()),
                    genreObj.getAsJsonPrimitive("name").getAsString()
            );
        }
    }
}
