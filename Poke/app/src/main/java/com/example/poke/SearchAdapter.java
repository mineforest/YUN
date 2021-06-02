package com.example.poke;

import android.content.Context;
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
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.CustomViewHolder> {

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
    public CustomViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchAdapter.CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(searchList.get(position).getThumbnail())
                .into(holder.search_image);
        holder.search_title.setText(String.valueOf(searchList.get(position).getRcp_title()));

    }

    @Override
    public int getItemCount() {
        return (searchList != null ? searchList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView search_image;
        TextView search_title;

        public CustomViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.search_image = itemView.findViewById(R.id.search_image);
            this.search_title = itemView.findViewById(R.id.search_title);


        }
    }
}