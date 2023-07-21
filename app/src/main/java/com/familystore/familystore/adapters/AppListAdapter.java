package com.familystore.familystore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.familystore.familystore.R;
import com.familystore.familystore.listeners.lists.AppListClickListener;
import com.familystore.familystore.models.AppPreview;
import com.familystore.familystore.utils.AppPreviewDiffUtilCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder>  {

    private final Context context;
    private List<AppPreview> appPreviewList;
    private final AppListClickListener listener;


    public AppListAdapter(Context context, List<AppPreview> appPreviewList, AppListClickListener listener) {
        this.context = context;
        this.appPreviewList = appPreviewList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return this.appPreviewList.size();
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
                        .inflate(R.layout.list_item_app, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        AppPreview currentItem = appPreviewList.get(position);

        viewHolder.tvName.setText(currentItem.getName());
        viewHolder.tvVersion.setText(context.getString(
                R.string.version_info,
                currentItem.getVersion()
        ));

        if (!"".equals(currentItem.getLogoUrl()))
            Picasso.get()
                    .load(currentItem.getLogoUrl())
                    .into(viewHolder.ivLogo);



        viewHolder.parentView.setOnClickListener(v -> {
            listener.onClick(currentItem.getId());
        });
    }

    public void updateData(List<AppPreview> newData, int position) {
        appPreviewList = newData;
        if (position != -1) {
            this.notifyItemChanged(position);
        }
    }

    private void calculateDiff(List<AppPreview> oldData, List<AppPreview> newData) {
        AppPreviewDiffUtilCallback appPreviewDiffUtilCallback = new AppPreviewDiffUtilCallback(oldData, newData);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(appPreviewDiffUtilCallback);
        diffResult.dispatchUpdatesTo(this);
    }

    public void sort(AppPreview.Order order) {

        if (appPreviewList.isEmpty())
            return;

        List<AppPreview> oldData = new ArrayList<>(appPreviewList);

        switch (order) {
            case PUBLISHED:
                appPreviewList.sort((first, second) -> {
                    int id1 = Integer.parseInt(first.getId());
                    int id2 = Integer.parseInt(second.getId());
                    return Integer.compare(id2, id1);
                });
                calculateDiff(oldData, appPreviewList);
                break;
            case LAST_UPDATED:
                appPreviewList.sort((first, second) -> {
                    long timestamp1 = first.getLastUpdated();
                    long timestamp2 = second.getLastUpdated();

                    if (timestamp1 == timestamp2) {
                        int id1 = Integer.parseInt(first.getId());
                        int id2 = Integer.parseInt(second.getId());
                        return Integer.compare(id2, id1);
                    }
                    else
                        return Long.compare(timestamp2, timestamp1);
                });
                calculateDiff(oldData, appPreviewList);
                break;
            default:
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivLogo;
        private final TextView tvName;
        private final TextView tvVersion;
        private final View parentView;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.parentView = view;
            this.ivLogo = view.findViewById(R.id.logo);
            this.tvName = view.findViewById(R.id.name);
            this.tvVersion = view.findViewById(R.id.version);
        }

    }

}

