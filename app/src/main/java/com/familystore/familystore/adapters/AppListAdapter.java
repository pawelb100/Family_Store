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
import com.familystore.familystore.utils.AppListDiffUtilCallback;
import com.squareup.picasso.Picasso;

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

    public void updateData(List<AppPreview> newData) {

        AppListDiffUtilCallback diffUtilCallback = new AppListDiffUtilCallback(appPreviewList, newData);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);

        appPreviewList = newData;
        diffResult.dispatchUpdatesTo(this);
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

