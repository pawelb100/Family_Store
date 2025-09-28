package com.familystore.familystore.viewmodels.retrofitapi;

import com.familystore.familystore.models.App;
import com.familystore.familystore.models.AppPreview;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface AppsDatabaseApi {
    @GET("apps")
    Call<List<AppPreview>> queryAppPreviews(
            @Header("apiKey") String apiKey,
            @Query("select") String select
    );

    @GET("apps")
    Call<List<App>> queryAppById(
            @Header("apiKey") String apiKey,
            @Query("id") String idFilter,
            @Query("select") String select
    );
}
