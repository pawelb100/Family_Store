package com.familystore.familystore.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
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

    private static final String ONESIGNAL_APP_ID = BuildConfig.ONESIGNAL_APP_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsManager settingsManager = new SettingsManager(PreferenceManager.getDefaultSharedPreferences(this), this);
        settingsManager.setTheme();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.topAppBar);

        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        MainViewModel viewModel = viewModelProvider.get(MainViewModel.class);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {

            if (navDestination.getId() == R.id.appFragment) {
                binding.bottomNavigationView.setVisibility(View.GONE);
            }
            else
                binding.bottomNavigationView.setVisibility(View.VISIBLE);
        });

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

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