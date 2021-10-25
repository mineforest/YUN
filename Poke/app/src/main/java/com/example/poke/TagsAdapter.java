package com.example.poke;

import android.annotation.SuppressLint;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TagsAdapter extends RecyclerView.Adapter<SearchViewHolder>  {

    private Intent intent;

    private ArrayList<String> tagset;
    private ArrayList<ArrayList<Recipe_get>>  tag_contents;

    public TagsAdapter(ArrayList<String> tagset, ArrayList<ArrayList<Recipe_get>> tag_contents) {

        this.tagset = tagset;
        this.tag_contents = tag_contents;
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
    public void onBindViewHolder(@NonNull SearchViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (tagset != null && position < tagset.size()) {
            String tag = tagset.get(position);
            holder.search_title.setText(tag);
        }
    }

    @Override
    public int getItemCount() {
        return (tagset != null ? tagset.size() : 0);
    }

}