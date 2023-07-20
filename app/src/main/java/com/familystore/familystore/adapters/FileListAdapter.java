package com.familystore.familystore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.familystore.familystore.R;
import com.familystore.familystore.listeners.lists.FileListClickListener;
import com.familystore.familystore.models.AppPreview;
import com.familystore.familystore.utils.ListDiffUtilCallback;
import com.familystore.familystore.utils.DateUtils;

import java.io.File;
import java.util.List;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder>  {

    private final Context context;
    private List<File> files;
    private FileListClickListener listener;

    public FileListAdapter(Context context, List<File> files, FileListClickListener listener) {
        this.context = context;
        this.files = files;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return this.files.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater
                        .from(context)
                        .inflate(R.layout.list_item_file, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        File currentItem = files.get(position);

        viewHolder.filename.setText(currentItem.getName().replace(".apk", ""));
        viewHolder.downloadDate.setText("Pobrano: "+ DateUtils.getDateStrFromEpochMilli(currentItem.lastModified()));

        viewHolder.view.setOnClickListener(view -> listener.onClick(currentItem));

        viewHolder.view.setOnLongClickListener(view -> {
            listener.onLongClick(currentItem, position);
            return true;
        });

    }

    public void updateData(List<File> newData) {

        ListDiffUtilCallback<File> diffUtilCallback = new ListDiffUtilCallback<>(files, newData);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);

        files = newData;
        diffResult.dispatchUpdatesTo(this);
    }

    public void removeItem(int position) {
        files.remove(position);
        this.notifyItemRemoved(position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView filename;
        private final TextView downloadDate;
        private final View view;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.filename = view.findViewById(R.id.filename);
            this.downloadDate = view.findViewById(R.id.downloadDate);
            this.view = view;
        }

    }

}

