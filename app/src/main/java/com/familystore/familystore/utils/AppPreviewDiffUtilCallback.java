package com.familystore.familystore.utils;

import androidx.recyclerview.widget.DiffUtil;

import com.familystore.familystore.models.AppPreview;

import java.util.List;
import java.util.Objects;


public class AppPreviewDiffUtilCallback extends DiffUtil.Callback {

    private List<AppPreview> oldList;
    private List<AppPreview> newList;

    public AppPreviewDiffUtilCallback(List<AppPreview> oldList, List<AppPreview> newList) {
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
        return Objects.equals(oldList.get(oldItemPosition).getId(), newList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}