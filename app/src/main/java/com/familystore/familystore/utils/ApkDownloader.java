package com.familystore.familystore.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

public class ApkDownloader {

    private DownloadManager downloadManager;
    private DownloadManager.Request request;

    private final String subPath;

    public ApkDownloader(Context context, String url, String appName, String appVersion) {
        String fileName = appName + " wersja " + appVersion + ".apk";
        Uri uri = Uri.parse(url);
        subPath = "/FamilyStore/" + fileName;

        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, subPath);
    }

    public void download() {
        deleteIfExists();
        downloadManager.enqueue(request);
    }

    private void deleteIfExists(){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + subPath);
        file.delete();
    }

}
