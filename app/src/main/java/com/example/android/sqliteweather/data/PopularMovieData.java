package com.example.android.sqliteweather.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.Serializable;
import java.lang.reflect.Type;

public class PopularMovieData implements Serializable {
    private String title;
    private double popularity;

    public PopularMovieData(String title, double popularity) {
        this.title = title;
        this.popularity = popularity;
    }

    public String getTitle() { return this.title; }

    public double getPopularity() { return this.popularity; }

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
                (double)Math.round(resultsObj.getAsJsonPrimitive("popularity").getAsDouble())
            );
        }
    }
}
