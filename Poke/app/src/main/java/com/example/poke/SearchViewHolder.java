package com.example.poke;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
public class SearchViewHolder extends RecyclerView.ViewHolder {

    public ImageView search_thumbnail;
    public TextView search_title;

    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);
        search_thumbnail = itemView.findViewById(R.id.search_image);
        search_title = itemView.findViewById(R.id.search_title);
    }
}