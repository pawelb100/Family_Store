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
import com.familystore.familystore.models.Brand;
import com.familystore.familystore.viewmodels.supabaseclient.UpdaterClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final DatabaseReference appListReference;
    private final DatabaseReference brandsReference;
    private final StorageReference appDataReference;

    private final UpdaterClient updaterClient;

    public MainViewModel(@NonNull Application application) {
        super(application);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        appListReference = database.getReference().child("Family Store 2/Apps");
        brandsReference = database.getReference().child("Family Store 2/Brands");

        appDataReference = storage.getReference().child("Family Store 2/Apps");
        updaterClient = new UpdaterClient();
    }

    public void addAppPreviewListListener(ResultListener<List<AppPreview>> listener) {
        appListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<AppPreview> apps = new ArrayList<>();

                if (snapshot.exists()) {
                    // initial list load
                    for (DataSnapshot child : snapshot.getChildren()) {
                        AppPreview app = child.getValue(AppPreview.class);
                        assert app != null;
                        apps.add(app);
                    }
                    listener.onResult(apps);

                    // load logos
                    // has to be invoked after apps are loaded to the adapter
                    // to enable default sorting
                    for (int i = 0; i < apps.size(); i++) {
                        AppPreview app = apps.get(i);
                        appDataReference
                                .child(app.getId())
                                .child("logo.png")
                                .getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    // refresh logo
                                    app.setLogoUrl(uri.toString());
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void getAppById(String id, ResultListener<App> listener) {

        appListReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                App app = snapshot.getValue(App.class);
                // initial load
                listener.onResult(app);
                // this is executed after listener.onResult to notify the
                // fragment that there is no app with this id
                if (app == null) {
                    return;
                }
                // add download urls for missing properties
                // logo
                appDataReference
                        .child(app.getId())
                        .child("logo.png")
                        .getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            app.setLogoUrl(uri.toString());
                            listener.onResult(app);
                        });
                // app download url
                appDataReference
                        .child(app.getId())
                        .child("latest.apk")
                        .getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            app.setDownloadUrl(uri.toString());
                            listener.onResult(app);
                        });
                // picture urls
                appDataReference
                        .child(app.getId())
                        .child("pictures")
                        .listAll()
                        .addOnSuccessListener(listResult -> {
                            List<String> pictureUrls = new ArrayList<>();
                            for (StorageReference pictureRef : listResult.getItems()) {
                                pictureRef.getDownloadUrl()
                                        .addOnSuccessListener(uri -> {
                                            pictureUrls.add(uri.toString());
                                            // refresh picture list
                                            listener.onResult(app);
                                        });
                            }
                            app.setPictureUrls(pictureUrls);
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getBrandById(String id, ResultListener<Brand> listener) {

        brandsReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Brand brand = snapshot.getValue(Brand.class);
                listener.onResult(brand);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void getBrandList(ResultListener<List<Brand>> listener) {
        brandsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Brand> brands = new ArrayList<>();

                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Brand brand = child.getValue(Brand.class);
                        assert brand != null;
                        brands.add(brand);
                    }
                    listener.onResult(brands);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
