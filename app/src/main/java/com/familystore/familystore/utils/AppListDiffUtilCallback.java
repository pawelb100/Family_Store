package com.familystore.familystore.utils;

import androidx.recyclerview.widget.DiffUtil;

import com.familystore.familystore.models.App;
import com.familystore.familystore.models.AppPreview;

import java.util.List;


public class AppListDiffUtilCallback extends DiffUtil.Callback {

    private List<AppPreview> oldList;
    private List<AppPreview> newList;

    public AppListDiffUtilCallback(List<AppPreview> oldList, List<AppPreview> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition == newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}

