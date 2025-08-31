package com.familystore.familystore.viewmodels.supabaseclient;

import androidx.annotation.NonNull;

import com.familystore.familystore.BuildConfig;
import com.familystore.familystore.listeners.database.ResultListener;
import com.familystore.familystore.models.FSReleaseData;
import com.familystore.familystore.viewmodels.retrofitapi.UpdaterApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdaterClient extends BaseClient {
    private final UpdaterApi updaterApi;

    public UpdaterClient() {
        updaterApi = retrofitDb.create(UpdaterApi.class);
    }

    public void fetchLatestFsRelease(ResultListener<FSReleaseData> resultListener) {
        Callback<List<FSReleaseData>> responseCallback = new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<FSReleaseData>> call,
                    @NonNull Response<List<FSReleaseData>> response) {
                List<FSReleaseData> tableRows = response.body();
                if (tableRows == null || tableRows.isEmpty()) {
                    return;
                }
                resultListener.onResult(tableRows.get(0));
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<FSReleaseData>> call,
                    @NonNull Throwable t) {
            }
        };

        updaterApi
                .queryFsReleaseTable(BuildConfig.SUPABASE_PUBLISHABLE_KEY, "*", 1)
                .enqueue(responseCallback);
    }
}
