package com.familystore.familystore.viewmodels.supabaseclient;

import com.familystore.familystore.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseClient {
    protected final Retrofit retrofitDb;
    protected final Retrofit retrofitStorage;

    public BaseClient() {
        retrofitDb = new Retrofit.Builder()
                .baseUrl(BuildConfig.SUPABASE_URL + "/rest/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitStorage = new Retrofit.Builder()
                .baseUrl(BuildConfig.SUPABASE_URL + "/storage/v1/object/public/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
