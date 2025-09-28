package com.familystore.familystore.viewmodels.supabaseclient;

import androidx.annotation.NonNull;

import com.familystore.familystore.BuildConfig;
import com.familystore.familystore.models.App;
import com.familystore.familystore.models.AppPreview;
import com.familystore.familystore.models.StorageObject;
import com.familystore.familystore.viewmodels.retrofitapi.AppsDatabaseApi;
import com.familystore.familystore.viewmodels.retrofitapi.StorageApi;
import com.familystore.familystore.viewmodels.supabaseclient.common.BaseClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppsClient extends BaseClient {
    private final AppsDatabaseApi appsDatabaseApi;
    private final StorageApi storageApi;

    public AppsClient() {
        this.appsDatabaseApi = retrofitDb.create(AppsDatabaseApi.class);
        this.storageApi = retrofitStorage.create(StorageApi.class);
    }

    public void fetchAppPreviews(Consumer<List<AppPreview>> onAppPreviewsFetched) {
        Callback<List<AppPreview>> responseCallback = new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<AppPreview>> call,
                    @NonNull Response<List<AppPreview>> response) {
                List<AppPreview> appPreviews = response.body();
                if (appPreviews == null) {
                    appPreviews = Collections.emptyList();
                }
                appPreviews.forEach(app -> app.setLogoUrl(
                        storageApi.getDownloadUrl("Apps/" + app.getId() + "/logo.png")));
                onAppPreviewsFetched.accept(appPreviews);
            }

            @Override
            public void onFailure(@NonNull Call<List<AppPreview>> call, @NonNull Throwable t) {
                onAppPreviewsFetched.accept(Collections.emptyList());
            }
        };
        List<String> select = List.of(
                "id",
                "created_at",
                "brand:brands(id,name)",
                "last_updated",
                "name",
                "version"
        );
        appsDatabaseApi
                .queryAppPreviews(BuildConfig.SUPABASE_PUBLISHABLE_KEY, joinSelect(select))
                .enqueue(responseCallback);
    }

    private void fetchAppPictureUrls(int id, Consumer<List<String>> onAppPicturesUrls) {
        String pictureFolderPath = "Apps/" + id + "/pictures/";

        Callback<List<StorageObject>> responseCallback = new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<StorageObject>> call,
                    @NonNull Response<List<StorageObject>> response) {
                List<StorageObject> responseBody = response.body();
                List<String> pictureUrls;
                if (responseBody == null) {
                    pictureUrls = Collections.emptyList();
                } else {
                    pictureUrls = responseBody.stream()
                            .map(obj -> storageApi
                                    .getDownloadUrl(pictureFolderPath + obj.name()))
                            .collect(Collectors.toList());
                }
                onAppPicturesUrls.accept(pictureUrls);
            }

            @Override
            public void onFailure(@NonNull Call<List<StorageObject>> call, @NonNull Throwable t) {
                onAppPicturesUrls.accept(Collections.emptyList());
            }
        };
        Map<String, Object> body = Map.of("prefix", pictureFolderPath);
        storageApi.list(BuildConfig.SUPABASE_PUBLISHABLE_KEY, body)
                .enqueue(responseCallback);
    }

    /**
     * Note: onAppFetched will be called twice - once when the initial data is available,
     * and the second time once app pictures are available
     */
    public void fetchAppById(int id, Consumer<App> onAppFetched) {
        Callback<List<App>> responseCallback = new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<App>> call,
                    @NonNull Response<List<App>> response) {
                List<App> responseBody = response.body();
                if (responseBody == null || responseBody.isEmpty()) {
                    onAppFetched.accept(null);
                    return;
                }
                App app = responseBody.get(0);
                app.setLogoUrl(storageApi.getDownloadUrl("Apps/" + id + "/logo.png"));
                app.setDownloadUrl(storageApi.getDownloadUrl("Apps/" + id + "/latest.apk"));
                onAppFetched.accept(app);
                fetchAppPictureUrls(id, pictureUrls -> {
                    app.setPictureUrls(pictureUrls);
                    onAppFetched.accept(app);
                });
            }

            @Override
            public void onFailure(@NonNull Call<List<App>> call, @NonNull Throwable t) {
            }
        };
        List<String> select = List.of(
                "id",
                "created_at",
                "brand:brands(id,name)",
                "changelog",
                "description",
                "last_updated",
                "name",
                "version"
        );
        appsDatabaseApi
                .queryAppById(BuildConfig.SUPABASE_PUBLISHABLE_KEY, equalTo(id), joinSelect(select))
                .enqueue(responseCallback);
    }
}
