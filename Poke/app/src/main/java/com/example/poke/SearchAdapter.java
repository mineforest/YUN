package com.example.poke;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
public class SearchAdapter extends RecyclerView.Adapter<CardViewHolder> {

    private ArrayList<Recipe_get> searchList;

    public SearchAdapter(ArrayList<Recipe_get> searchList) {
        this.searchList = searchList;
    }

    Context context;

    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @NonNull
    @NotNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list,parent,false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CardViewHolder holder, int position) {
        if (searchList != null && position < searchList.size()) {
            Recipe_get rcp = searchList.get(position);
            holder.rcp_title.setText(rcp.getName());
            Glide.with(holder.itemView)
                    .load(rcp.getThumbnail())
                    .into(holder.rcp_thumbnail);
            Log.d("dddddddddddd","helooooooo");
        }

    }

    @Override
    public int getItemCount() {
        return (searchList != null ? searchList.size() : 0);
    }

//    public class CardViewHolder extends RecyclerView.ViewHolder {
//        ImageView search_image;
//        TextView search_title;
//
//        public CardViewHolder(@NonNull @NotNull View itemView) {
//            super(itemView);
//            this.search_image = itemView.findViewById(R.id.search_image);
//            this.search_title = itemView.findViewById(R.id.search_title);
//
//
//        }
//    }
}