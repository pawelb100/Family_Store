package com.familystore.familystore.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileManager {

    private final File directory;

    private Context context;

    public FileManager(Context context) {
        directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/FamilyStore");
        this.context = context;
    }

    public List<File> getFiles() {
        List<File> result = new ArrayList<>();
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".apk"))
                    result.add(file);
            }
        }
        Collections.sort(result);
        return result;
    }

    public void installApk(File file) {
        Uri fileUri = FileProvider.getUriForFile(
                context, context.getPackageName() + ".fileprovider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public boolean deleteFile(File file) {
        return file.delete();
    }



}
