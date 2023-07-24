package com.familystore.familystore.listeners;

import android.net.Uri;

public interface UpdateListener {
    void onUpdateAvailable(Uri downloadUrl, String version);
}
