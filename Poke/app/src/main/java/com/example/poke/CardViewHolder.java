package com.example.poke;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

public class CardViewHolder extends RecyclerView.ViewHolder {

    public ImageView rcp_thumbnail;
    public TextView rcp_title;
    public TextView rcp_cooktime;
    public TextView rate;
    public TextView score;

    public CardViewHolder(@NonNull View itemView) {
        super(itemView);
        rcp_thumbnail = itemView.findViewById(R.id.rcp_thumbnail);
        rcp_title = itemView.findViewById(R.id.rcp_title);
        rcp_cooktime = itemView.findViewById(R.id.cook_time);
        rate = itemView.findViewById(R.id.rate);
        score = itemView.findViewById(R.id.score);
    }
}