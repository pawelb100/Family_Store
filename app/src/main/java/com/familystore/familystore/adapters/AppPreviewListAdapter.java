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
import com.familystore.familystore.listeners.AppPreviewListClickListener;
import com.familystore.familystore.models.AppPreview;
import com.familystore.familystore.models.AppSortOrder;
import com.familystore.familystore.utils.BaseDateUtils;
import com.familystore.familystore.utils.DiffUtilCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AppPreviewListAdapter extends RecyclerView.Adapter<AppPreviewListAdapter.ViewHolder> {

    private final Context context;
    private @NonNull List<AppPreview> appPreviewList;
    private final AppPreviewListClickListener listener;
    private AppSortOrder currentSortOrder = null;
    private static final Object PAYLOAD_SORT_ORDER = new Object();

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,
                                 int position) {
        AppPreview currentItem = appPreviewList.get(position);

        viewHolder.tvName.setText(currentItem.getName());
        viewHolder.tvAuthor.setText(currentItem.getBrand().name());
        bindDate(viewHolder.tvDate, currentItem);

        Picasso.get()
                .load(currentItem.getLogoUrl())
                .into(viewHolder.ivLogo);

        viewHolder.parentView.setOnClickListener(v ->
                listener.onClick(currentItem.getId())
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,
                                 int position,
                                 @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(viewHolder, position, payloads);
        } else {
            AppPreview currentItem = appPreviewList.get(position);
            bindDate(viewHolder.tvDate, currentItem);
        }
    }

    private void bindDate(TextView tvDate, AppPreview currentItem) {
        switch (currentSortOrder) {
            case LAST_UPDATED -> tvDate.setText(context.getString(
                    R.string.last_updated_date,
                    BaseDateUtils.getTimeDifferenceString(
                            currentItem.getLastUpdated().toInstant().toEpochMilli(),
                            System.currentTimeMillis()
                    )
            ));
            case PUBLISHED -> tvDate.setText(context.getString(
                    R.string.published_date,
                    BaseDateUtils.getTimeDifferenceString(
                            currentItem.getCreatedAt().toInstant().toEpochMilli(),
                            System.currentTimeMillis()
                    )
            ));
        }
    }


    private void calculateDiff(List<AppPreview> oldData, List<AppPreview> newData) {
        DiffUtilCallback<AppPreview> diffUtilCallback = new DiffUtilCallback<>(oldData, newData);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        diffResult.dispatchUpdatesTo(this);
    }

    public void sort(AppSortOrder order) {
        currentSortOrder = order;
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
        notifyItemRangeChanged(0, appPreviewList.size(), PAYLOAD_SORT_ORDER);
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
        private final TextView tvDate;
        private final View parentView;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.parentView = view;
            this.ivLogo = view.findViewById(R.id.logo);
            this.tvName = view.findViewById(R.id.name);
            this.tvAuthor = view.findViewById(R.id.author);
            this.tvDate = view.findViewById(R.id.date);
        }
    }
}

