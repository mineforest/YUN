package com.example.poke;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.poke.R;
import com.example.poke.Recipe_Info;
import com.example.poke.Recipe_get;
import com.example.poke.SearchViewHolder;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder>  {

    private Intent intent;

    private ArrayList<Recipe_get> searchList;

    public SearchAdapter(ArrayList<Recipe_get> search_list) {

        this.searchList = search_list;

    }

    Context context;

    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list,parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        if (searchList != null && position < searchList.size()) {
            Recipe_get recipe_get = searchList.get(position);
            holder.search_title.setText(recipe_get.getName());
            Glide.with(holder.itemView)
                    .load(recipe_get.getThumbnail())
                    .into(holder.search_thumbnail);

            holder.search_thumbnail.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    intent = new Intent(v.getContext(), Recipe_Info.class);
                    intent.putExtra("rcp_id",searchList.get(position).getId());
                    v.getContext().startActivity(intent);
                   // Toast.makeText(v.getContext(), searchList.get(position).getId(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void  filterList(ArrayList<Recipe_get> filteredList) {
        searchList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (searchList != null ? searchList.size() : 0);
    }

}