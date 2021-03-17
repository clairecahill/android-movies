package com.example.android.sqliteweather.data;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONObject;

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
        private Object JsonNull;

        @Override
        public MovieData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject resultsObj = json.getAsJsonObject();
            JsonArray genreArr = resultsObj.getAsJsonArray("genres");

            String release_date = "null";
            String poster = "null";
            String overview = "null";
            String genres = "null";

            if (genreArr.size() > 0)
            {
                JsonObject genreObj = genreArr.get(0).getAsJsonObject();
                genres = genreObj.getAsJsonPrimitive("name").getAsString();
            }

            if (resultsObj.has("release_date"))
            {
                release_date = resultsObj.getAsJsonPrimitive("release_date").getAsString();
            }

            if (resultsObj.has("poster_path"))
            {
                if(!resultsObj.get("poster_path").isJsonNull())
                {
                    Log.d("tag", " " + resultsObj.get("poster_path").toString());
                    poster = resultsObj.getAsJsonPrimitive("poster_path").getAsString();
                }
            }

            else if (resultsObj.get("poster_path") == JsonNull)
            {
                poster = "null";
            }

            if (resultsObj.has("overview"))
            {
                overview = resultsObj.getAsJsonPrimitive("overview").getAsString();
            }


            System.out.println("poster" + poster);

            return new MovieData(
                    resultsObj.getAsJsonPrimitive("title").getAsString(),
                    (float) Math.round(resultsObj.getAsJsonPrimitive("popularity").getAsFloat()) / 100,
                    overview,
                    poster,
                    release_date,
                    (float) Math.round(resultsObj.getAsJsonPrimitive("vote_average").getAsFloat()),
                    genres
            );

        }
    }
}
