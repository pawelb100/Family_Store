package com.familystore.familystore.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainViewModel extends AndroidViewModel {

    private final FirebaseDatabase database;
    private final FirebaseAuth auth;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
    }

}
