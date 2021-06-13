package com.example.poke;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;

public class MainViewpageViewholder extends RecyclerView.ViewHolder {
    public ImageView rcp_thumbnail;
    public TextView rcp_title;
    public TextView rcp_cooktime;
    public TextView rate;
    public TextView keyword;
    public TextView tags;

    public MainViewpageViewholder(@NonNull View itemView) {
        super(itemView);
        rcp_thumbnail = itemView.findViewById(R.id.main_circle_image);
        rcp_title = itemView.findViewById(R.id.main_pager_title);
        rcp_cooktime = itemView.findViewById(R.id.main_pager_time);
        rate = itemView.findViewById(R.id.main_pager_rate);
        keyword = itemView.findViewById(R.id.useless);
        tags = itemView.findViewById(R.id.main_pager_tags);
    }
}
