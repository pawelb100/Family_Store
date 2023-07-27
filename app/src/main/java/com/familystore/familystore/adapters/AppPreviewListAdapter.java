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
import com.familystore.familystore.listeners.lists.AppPreviewListClickListener;
import com.familystore.familystore.models.AppPreview;
import com.familystore.familystore.utils.BaseDateUtils;
import com.familystore.familystore.utils.DiffUtilCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AppPreviewListAdapter extends RecyclerView.Adapter<AppPreviewListAdapter.ViewHolder>  {

    private final Context context;
    private final List<AppPreview> appPreviewList;
    private final AppPreviewListClickListener listener;


    public AppPreviewListAdapter(Context context, List<AppPreview> appPreviewList, AppPreviewListClickListener listener) {
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        if (currentItem.getLastUpdated() != -1) {
            viewHolder.tvLastUpdated.setText(context.getString(
                    R.string.last_updated_date,
                    BaseDateUtils.getTimeDifferenceString(
                            currentItem.getLastUpdated(), System.currentTimeMillis()
                    )
            ));
            viewHolder.tvLastUpdated.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvLastUpdated.setText("");
            viewHolder.tvLastUpdated.setVisibility(View.GONE);
        }

        // callback is necessary to display logos which urls are
        // loaded with delay
        currentItem.setLogoUpdateCallback(() -> {
            Picasso.get()
                    .load(currentItem.getLogoUrl())
                    .into(viewHolder.ivLogo);
        });
        // if logo url is instantly available, callback has to be
        // called manually
        if (!currentItem.getLogoUrl().equals("")) {
            currentItem.getLogoUpdateCallback().call();
        }

        viewHolder.parentView.setOnClickListener(v -> {
            listener.onClick(currentItem.getId());
        });
    }

    private void calculateDiff(List<AppPreview> oldData, List<AppPreview> newData) {
        DiffUtilCallback<AppPreview> diffUtilCallback = new DiffUtilCallback<>(oldData, newData);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
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
                break;
            default:
        }
        calculateDiff(oldData, appPreviewList);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivLogo;
        private final TextView tvName;
        private final TextView tvVersion;
        private final TextView tvLastUpdated;
        private final View parentView;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.parentView = view;
            this.ivLogo = view.findViewById(R.id.logo);
            this.tvName = view.findViewById(R.id.name);
            this.tvVersion = view.findViewById(R.id.version);
            this.tvLastUpdated = view.findViewById(R.id.lastUpdated);
        }

    }

}

