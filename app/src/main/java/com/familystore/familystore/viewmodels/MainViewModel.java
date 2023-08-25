package com.familystore.familystore.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.familystore.familystore.BuildConfig;
import com.familystore.familystore.listeners.database.AppPreviewListListener;
import com.familystore.familystore.listeners.database.SingleAppListener;
import com.familystore.familystore.listeners.database.UpdateListener;
import com.familystore.familystore.listeners.database.BrandListener;
import com.familystore.familystore.models.App;
import com.familystore.familystore.models.AppPreview;
import com.familystore.familystore.models.Brand;
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

    private final StorageReference updateReference;

    public MainViewModel(@NonNull Application application) {
        super(application);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        appListReference = database.getReference().child("Family Store 2/Apps");
        brandsReference = database.getReference().child("Family Store 2/Brands");

        appDataReference = storage.getReference().child("Family Store 2/Apps");
        updateReference = storage.getReference().child("Family Store 2/Releases");

    }

    public void addAppPreviewListListener(AppPreviewListListener listener) {
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
                        int finalI = i;
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


    public void getAppById(String id, SingleAppListener listener) {

        appListReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                App app = snapshot.getValue(App.class);
                // just in case to avoid crashes
                if (app == null) {
                    return;
                }
                // initial load
                listener.onResult(app);
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

    public void getBrandById(String id, BrandListener listener) {

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

    public void checkAvailableUpdate(UpdateListener listener) {
        updateReference
                .listAll()
                .addOnSuccessListener(listResult -> {
                    if (listResult.getItems().size() != 0) {

                        String currentVersion = BuildConfig.VERSION_NAME;
                        StorageReference reference = listResult.getItems().get(0);

                        if (!reference.getName().equals("fs-" + currentVersion + ".apk")) {
                            reference.getDownloadUrl().addOnSuccessListener(downloadUrl -> {

                                String updateVersion = reference.getName().replace("fs-", "").replace(".apk", "");
                                listener.onUpdateAvailable(downloadUrl, updateVersion);
                            });
                        }
                    }
                });
    }


}
