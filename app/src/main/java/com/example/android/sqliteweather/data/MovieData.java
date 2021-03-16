package com.example.android.sqliteweather.data;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.Serializable;
import java.lang.reflect.Type;

public class MovieData implements Serializable {
    private final static String ICON_URL_FORMAT_STR = "https://image.tmdb.org/t/p/w500";

    private String title;
    private float popularity;
    private String overview;
    private String poster_path;
    private String release_date;
    private float vote_average;
    private String genre;

    public MovieData(String title, float popularity, String overview, String poster_path, String release_date, float vote_average, String genre) {
        this.title = title;
        this.popularity = popularity;
        this.overview = overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.genre = genre;
    }

    public String getTitle() { return this.title; }

    public float getPopularity() { return this.popularity; }

    public String getOverview() { return this.overview; }

    public String getIconUrl() { return this.poster_path; }

    public String getReleaseDate() { return this.release_date; }

    public float getVoteAverage() { return this.vote_average; }

    public String getGenre() { return this.genre; }

    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<MovieData> {
        @Override
        public MovieData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject resultsObj = json.getAsJsonObject();
            JsonArray genreArr = resultsObj.getAsJsonArray("genres");
            JsonObject genreObj = genreArr.get(0).getAsJsonObject();
            //Log.d("heeheehoo", "genreObj" + genreObj);

            if(resultsObj.getAsJsonPrimitive("poster_path")!=null) {
                return new MovieData(
                        resultsObj.getAsJsonPrimitive("title").getAsString(),
                        (float)Math.round(resultsObj.getAsJsonPrimitive("popularity").getAsFloat()) / 100,
                        resultsObj.getAsJsonPrimitive("overview").getAsString(),
                        resultsObj.getAsJsonPrimitive("poster_path").getAsString(),
                        resultsObj.getAsJsonPrimitive("release_date").getAsString(),
                        (float)Math.round(resultsObj.getAsJsonPrimitive("vote_average").getAsFloat()),
                        genreObj.getAsJsonPrimitive("name").getAsString()
                );
            } else {
                return new MovieData(
                        resultsObj.getAsJsonPrimitive("title").getAsString(),
                        (float)Math.round(resultsObj.getAsJsonPrimitive("popularity").getAsFloat()) / 100,
                        resultsObj.getAsJsonPrimitive("overview").getAsString(),
                        null,
                        resultsObj.getAsJsonPrimitive("release_date").getAsString(),
                        (float)Math.round(resultsObj.getAsJsonPrimitive("vote_average").getAsFloat()),
                        genreObj.getAsJsonPrimitive("name").getAsString()
                );
            }
        }
    }
}
