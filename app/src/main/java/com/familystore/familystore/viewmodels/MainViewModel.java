package com.familystore.familystore.viewmodels;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.familystore.familystore.BuildConfig;
import com.familystore.familystore.listeners.database.ResultListener;
import com.familystore.familystore.listeners.database.UpdateListener;
import com.familystore.familystore.models.App;
import com.familystore.familystore.models.AppPreview;
import com.familystore.familystore.viewmodels.supabaseclient.AppsClient;
import com.familystore.familystore.viewmodels.supabaseclient.UpdaterClient;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final UpdaterClient updaterClient;
    private final AppsClient appsClient;

    public MainViewModel(@NonNull Application application) {
        super(application);
        updaterClient = new UpdaterClient();
        appsClient = new AppsClient();
    }

    public void addAppPreviewListListener(ResultListener<List<AppPreview>> listener) {
        appsClient.fetchAppPreviews(listener);
    }

    public void getAppById(int id, ResultListener<App> listener) {
        appsClient.fetchAppById(id, listener);
    }

    public void checkAvailableUpdate(UpdateListener listener) {
        updaterClient.fetchLatestFsRelease(fsReleaseData -> {
            String currentVersion = BuildConfig.VERSION_NAME;
            if (fsReleaseData.releaseId().equals(currentVersion)) {
                return;
            }
            listener.onUpdateAvailable(
                    Uri.parse(fsReleaseData.downloadUrl()),
                    fsReleaseData.releaseId()
            );
        });
    }
}
