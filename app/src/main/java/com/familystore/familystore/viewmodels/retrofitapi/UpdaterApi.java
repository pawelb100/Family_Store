package com.familystore.familystore.viewmodels.retrofitapi;

import com.familystore.familystore.models.FSReleaseData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface UpdaterApi {
    @GET("fs_release")
    Call<List<FSReleaseData>> queryFsReleaseTable(
            @Header("apiKey") String apiKey,
            @Query("select") String select,
            @Query("limit") int limit
    );
}
