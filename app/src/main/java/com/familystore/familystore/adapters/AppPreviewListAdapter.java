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
import com.familystore.familystore.models.AppSortOrder;
import com.familystore.familystore.utils.BaseDateUtils;
import com.familystore.familystore.utils.DiffUtilCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AppPreviewListAdapter extends RecyclerView.Adapter<AppPreviewListAdapter.ViewHolder> {

    private final Context context;
    private List<AppPreview> appPreviewList;
    private final AppPreviewListClickListener listener;

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
        viewHolder.tvAuthor.setText(currentItem.getBrand().name());

        if (currentItem.getLastUpdated() != null) {
            viewHolder.tvLastUpdated.setText(context.getString(
                    R.string.last_updated_date,
                    BaseDateUtils.getTimeDifferenceString(
                            currentItem.getLastUpdated().toInstant().toEpochMilli(),
                            System.currentTimeMillis()
                    )
            ));
            viewHolder.tvLastUpdated.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvLastUpdated.setText("");
            viewHolder.tvLastUpdated.setVisibility(View.GONE);
        }

        Picasso.get()
                .load(currentItem.getLogoUrl())
                .into(viewHolder.ivLogo);

        viewHolder.parentView.setOnClickListener(v ->
                listener.onClick(currentItem.getId())
        );
    }

    private void calculateDiff(List<AppPreview> oldData, List<AppPreview> newData) {
        DiffUtilCallback<AppPreview> diffUtilCallback = new DiffUtilCallback<>(oldData, newData);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        diffResult.dispatchUpdatesTo(this);
    }

    public void sort(AppSortOrder order) {

        if (appPreviewList.isEmpty())
            return;

        List<AppPreview> oldData = new ArrayList<>(appPreviewList);

        switch (order) {
            case PUBLISHED -> appPreviewList.sort(Comparator
                    .comparing(AppPreview::getCreatedAt)
                    .reversed());
            case LAST_UPDATED -> appPreviewList.sort(Comparator
                    .comparing(AppPreview::getLastUpdated)
                    .reversed());
        }
        calculateDiff(oldData, appPreviewList);
    }

    public void filterByBrandId(int brandId) {
        appPreviewList = appPreviewList.stream()
                .filter(appPreview -> appPreview.getBrand().id() == brandId)
                .collect(Collectors.toList());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivLogo;
        private final TextView tvName;
        private final TextView tvAuthor;
        private final TextView tvLastUpdated;
        private final View parentView;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.parentView = view;
            this.ivLogo = view.findViewById(R.id.logo);
            this.tvName = view.findViewById(R.id.name);
            this.tvAuthor = view.findViewById(R.id.author);
            this.tvLastUpdated = view.findViewById(R.id.lastUpdated);
        }
    }
}

