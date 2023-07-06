package com.familystore.familystore.listeners.database;

import com.familystore.familystore.models.AppPreview;

import java.util.List;

public interface AppListListener {
     void onResult(List<AppPreview> result, int changedItemId);

}
