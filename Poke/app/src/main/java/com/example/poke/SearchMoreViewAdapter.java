package com.example.poke;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchMoreViewAdapter extends RecyclerView.Adapter<SearchMoreViewAdapter.ItemViewHolder> {

    private ArrayList<Recipe_get> rcp_list;
    private Intent intent;
    Context context;

    public SearchMoreViewAdapter(ArrayList<Recipe_get> rcp_list) {
        this.rcp_list = rcp_list;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @NonNull
    @Override
    public SearchMoreViewAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_recipes_item_insearch, parent, false);
        return new SearchMoreViewAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchMoreViewAdapter.ItemViewHolder holder, int position) {
        if (rcp_list != null && position < rcp_list.size()) {
            Recipe_get rcp = rcp_list.get(position);
            holder.rcp_title.setText(rcp.getName());

            Glide.with(holder.itemView)
                    .load(rcp.getThumbnail())
                    .into(holder.rcp_thumbnail);

            holder.rcp_thumbnail.setOnClickListener(v -> {
                intent = new Intent(v.getContext(), Recipe_Info.class);
                intent.putExtra("rcp_id", rcp_list.get(position).getId());
                v.getContext().startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return rcp_list == null ? 0 : rcp_list.size();
    }

    public void filterList(ArrayList<Recipe_get> filteredList) {
        rcp_list = filteredList;
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView rcp_thumbnail;
        public TextView rcp_title;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            rcp_thumbnail = itemView.findViewById(R.id.rcp_thumbnail);
            rcp_title = itemView.findViewById(R.id.rcp_title);
        }
    }
}
