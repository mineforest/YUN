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

public class MainMoreRecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final ArrayList<Recipe_get> rcp_list;
    private Intent intent;
    Context context;

    public MainMoreRecipeAdapter(ArrayList<Recipe_get> rcp_list) {
        this.rcp_list = rcp_list;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_recipes_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }

    }

    @Override
    public int getItemCount() {
        return rcp_list == null ? 0 : rcp_list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return rcp_list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView rcp_thumbnail;
        public TextView rcp_title;
        public TextView rcp_cooktime;
        public TextView rate;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            rcp_thumbnail = itemView.findViewById(R.id.rcp_thumbnail);
            rcp_title = itemView.findViewById(R.id.rcp_title);
            rcp_cooktime = itemView.findViewById(R.id.cook_time);
            rate = itemView.findViewById(R.id.rate);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {
        if (rcp_list != null && position < rcp_list.size()) {
            Recipe_get rcp = rcp_list.get(position);
            viewHolder.rcp_title.setText(rcp.getName());
            viewHolder.rcp_cooktime.setText(rcp.getTime()+"분");
            viewHolder.rate.setText(rcp.getRate() + "%");

            Glide.with(viewHolder.itemView)
                    .load(rcp.getThumbnail())
                    .into(viewHolder.rcp_thumbnail);

            viewHolder.rcp_thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(v.getContext(), Recipe_Info.class);
                    intent.putExtra("rcp_id", rcp_list.get(position).getId());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
