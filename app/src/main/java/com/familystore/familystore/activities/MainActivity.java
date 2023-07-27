package com.familystore.familystore.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.familystore.familystore.BuildConfig;
import com.familystore.familystore.R;
import com.familystore.familystore.databinding.ActivityMainBinding;
import com.familystore.familystore.utils.ApkDownloader;
import com.familystore.familystore.utils.SettingsManager;
import com.familystore.familystore.viewmodels.MainViewModel;
import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private MainViewModel viewModel;

    private static final String ONESIGNAL_APP_ID = "89f2f3d2-146a-4d89-b1e3-c1c4abc405b3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsManager settingsManager = new SettingsManager(PreferenceManager.getDefaultSharedPreferences(this), this);
        settingsManager.setTheme();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.topAppBar);

        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        viewModel = viewModelProvider.get(MainViewModel.class);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {

            if (navDestination.getId() == R.id.appFragment) {
                binding.bottomNavigationView.setVisibility(View.GONE);
            }
            else
                binding.bottomNavigationView.setVisibility(View.VISIBLE);
        });

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

        requestPermissions();

        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        OneSignal.promptForPushNotifications();

        if (!BuildConfig.DEBUG)
            viewModel.checkAvailableUpdate(this::showAvailableUpdate);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Navigation.findNavController(binding.getRoot()).handleDeepLink(intent);
    }

    private void showAvailableUpdate(Uri downloadUrl, String updateVersion) {

        new AlertDialog.Builder(this)
                .setTitle(R.string.update_dialog_title)
                .setIcon(R.mipmap.ic_launcher_foreground)
                .setMessage(getString(R.string.update_dialog_message, updateVersion))
                .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {

                    ApkDownloader apkDownloader = new ApkDownloader(this, downloadUrl.toString(), "Family Store", updateVersion);
                    apkDownloader.download();

                })
                .setNegativeButton(getString(R.string.cancel), null)
                .create().show();
    }
}