package com.familystore.familystore.listeners.database;

import com.familystore.familystore.models.AppPreview;

import java.util.List;

public interface AppPreviewListListener {
     void onResult(List<AppPreview> result);

     void onLogoUrlLoaded(int position);

}
