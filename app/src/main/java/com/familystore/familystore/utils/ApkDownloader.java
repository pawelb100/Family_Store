package com.familystore.familystore.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class ApkDownloader {

    private DownloadManager downloadManager;
    private DownloadManager.Request request;

    public ApkDownloader(Context context, String url, String appName, String appVersion) {
        String fileName = appName + " wersja " + appVersion + ".apk";
        Uri uri = Uri.parse(url);

        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/FamilyStore/" + fileName);
    }

    public void download() {
        downloadManager.enqueue(request);
    }


}
