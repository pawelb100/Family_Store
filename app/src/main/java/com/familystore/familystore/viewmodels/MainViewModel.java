package com.familystore.familystore.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.familystore.familystore.listeners.database.AppListListener;
import com.familystore.familystore.listeners.database.SingleAppListener;
import com.familystore.familystore.models.App;
import com.familystore.familystore.models.AppPreview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final FirebaseDatabase database;
    private final FirebaseAuth auth;

    private ValueEventListener appListListener = null;

    private final DatabaseReference appListReference;
    private final DatabaseReference usersReference;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        appListReference = database.getReference().child("Family Store 2/Apps");
        usersReference = database.getReference().child("Family Store 2/Users");
    }

    public void addAppListListener(AppListListener listener) {
        appListListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<AppPreview> apps = new ArrayList<>();

                if (snapshot.exists())
                    for (DataSnapshot child : snapshot.getChildren()) {
                        AppPreview app = child.getValue(AppPreview.class);
                        apps.add(app);
                    }

                listener.onResult(apps);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        appListReference.addValueEventListener(appListListener);
    }

    public void removeAppListListener() {
        if (appListListener !=null) {
            appListReference.removeEventListener(appListListener);
            appListListener = null;
        }
    }


    public void getAppById(String id, SingleAppListener listener) {

        appListReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                App app = snapshot.getValue(App.class);
                listener.onResult(app);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

}
