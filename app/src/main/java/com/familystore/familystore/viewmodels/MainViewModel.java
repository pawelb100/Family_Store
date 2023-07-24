package com.familystore.familystore.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.familystore.familystore.listeners.database.AppPreviewListListener;
import com.familystore.familystore.listeners.database.SingleAppListener;
import com.familystore.familystore.listeners.database.UserListener;
import com.familystore.familystore.models.App;
import com.familystore.familystore.models.AppPreview;
import com.familystore.familystore.models.User;
import com.google.firebase.auth.FirebaseAuth;
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

    private final FirebaseDatabase database;
    private final FirebaseAuth auth;
    private final FirebaseStorage storage;

    private final DatabaseReference appListReference;
    private final DatabaseReference usersReference;
    private final StorageReference appDataReference;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        appListReference = database.getReference().child("Family Store 2/Apps");
        usersReference = database.getReference().child("Family Store 2/Users");

        appDataReference = storage.getReference().child("Family Store 2/Apps");
    }

    public void addAppPreviewListListener(AppPreviewListListener listener) {
        appListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<AppPreview> apps = new ArrayList<>();

                if (snapshot.exists()) {
                    int i = 0;
                    for (DataSnapshot child : snapshot.getChildren()) {
                        AppPreview app = child.getValue(AppPreview.class);
                        assert app != null;
                        int finalI = i;
                        appDataReference
                                .child(app.getId())
                                .child("logo.png")
                                .getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    // refresh logo
                                    app.setLogoUrl(uri.toString());
                                    listener.onLogoUrlLoaded(finalI);
                                });
                        apps.add(app);
                        i++;
                    }
                    // initial list load
                    listener.onResult(apps);
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

    public void getUserById(String id, UserListener listener) {

        usersReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                listener.onResult(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}
