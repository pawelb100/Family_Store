package com.familystore.familystore.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.familystore.familystore.BuildConfig;
import com.familystore.familystore.models.App;
import com.familystore.familystore.models.AppPreview;
import com.familystore.familystore.models.FSReleaseData;
import com.familystore.familystore.viewmodels.supabaseclient.AppsClient;
import com.familystore.familystore.viewmodels.supabaseclient.UpdaterClient;

import java.util.List;
import java.util.function.Consumer;

public class MainViewModel extends AndroidViewModel {

    private final UpdaterClient updaterClient;
    private final AppsClient appsClient;

    public MainViewModel(@NonNull Application application) {
        super(application);
        updaterClient = new UpdaterClient();
        appsClient = new AppsClient();
    }

    public void getAppPreviewList(Consumer<List<AppPreview>> listener) {
        appsClient.fetchAppPreviews(listener);
    }

    public void getAppById(int id, Consumer<App> listener) {
        appsClient.fetchAppById(id, listener);
    }

    public void checkAvailableUpdate(Consumer<FSReleaseData> onUpdateAvailable) {
        updaterClient.fetchLatestFsRelease(fsReleaseData -> {
            String currentVersion = BuildConfig.VERSION_NAME;
            if (fsReleaseData.releaseId().equals(currentVersion)) {
                return;
            }
            onUpdateAvailable.accept(fsReleaseData);
        });
    }
}
