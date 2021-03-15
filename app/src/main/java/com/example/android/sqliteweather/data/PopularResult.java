package com.example.android.sqliteweather.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class PopularResult {
    private int id;

    public PopularResult(int id) {
        this.id = id;
    }

    public int getId() { return id; }

    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<PopularResult> {
        @Override
        public PopularResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject resultsObj = json.getAsJsonObject();

            return new PopularResult(
                (int)Math.round(resultsObj.getAsJsonPrimitive("id").getAsInt())
            );
        }
    }
}
