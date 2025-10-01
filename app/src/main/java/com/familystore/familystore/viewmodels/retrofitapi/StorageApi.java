package com.familystore.familystore.viewmodels.retrofitapi;

import com.familystore.familystore.BuildConfig;
import com.familystore.familystore.models.StorageObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface StorageApi {
    String STORAGE_PATH = "storage/v1/object/";

    @POST(STORAGE_PATH + "list/default")
    Call<List<StorageObject>> list(
            @Header("apiKey") String apiKey,
            @Body Map<String, Object> body
    );

    default String getDownloadUrl(String path) {
        return BuildConfig.SUPABASE_URL + STORAGE_PATH + "public/default/" + path;
    }
}
