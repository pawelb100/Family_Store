package com.familystore.familystore.viewmodels.supabaseclient.common;

import com.familystore.familystore.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.time.OffsetDateTime;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseClient {
    protected final Retrofit retrofitDb;
    protected final Retrofit retrofitStorage;

    public BaseClient() {
        JsonDeserializer<OffsetDateTime> dateParser = (json, type, ctx) ->
                OffsetDateTime.parse(json.getAsString());
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(OffsetDateTime.class, dateParser)
                .create();

        retrofitDb = new Retrofit.Builder()
                .baseUrl(BuildConfig.SUPABASE_URL + "rest/v1/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        retrofitStorage = new Retrofit.Builder()
                .baseUrl(BuildConfig.SUPABASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    protected static String joinSelect(List<String> columns) {
        return String.join(",", columns);
    }

    protected static String equalTo(Number value) {
        return "eq." + value;
    }
}
