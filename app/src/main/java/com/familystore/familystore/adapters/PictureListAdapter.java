package com.familystore.familystore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.familystore.familystore.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PictureListAdapter extends RecyclerView.Adapter<PictureListAdapter.ViewHolder>  {

    private final Context context;
    private List<String> pictureUrls;


    public PictureListAdapter(Context context, List<String> pictureUrls) {
        this.context = context;
        this.pictureUrls = pictureUrls;
    }

    @Override
    public int getItemCount() {
        return this.pictureUrls.size();
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
                        .inflate(R.layout.list_item_picture, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        String currentItem = pictureUrls.get(position);

        Picasso.get()
                .load(currentItem)
                .into(viewHolder.imageView);

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.imageView = view.findViewById(R.id.imageView);
        }

    }

}

