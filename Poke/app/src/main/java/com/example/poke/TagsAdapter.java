package com.example.poke;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.SearchViewHolder>  {

    private final ArrayList<String> tagset;
    private final ArrayList<ArrayList<Recipe_get>> tag_contents;

    public TagsAdapter(ArrayList<String> tagset, ArrayList<ArrayList<Recipe_get>> tag_contents) {

        this.tagset = tagset;
        this.tag_contents = tag_contents;
    }

    Context context;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    private TagsAdapter.OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(TagsAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

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
            holder.cnt_tv.setText(tag_contents.get(position).size() + "개");
            if(tag_contents.get(position).size() != 0){
                Glide.with(holder.itemView)
                        .load(tag_contents.get(position).get(0).getThumbnail())
                        .into(holder.search_thumbnail);
            }
        }

    }

    @Override
    public int getItemCount() {
        return (tagset != null ? tagset.size() : 0);
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        public ImageView search_thumbnail;
        public TextView search_title;
        public TextView cnt_tv;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            search_title = itemView.findViewById(R.id.search_title);
            search_thumbnail = itemView.findViewById(R.id.tag_imageView);
            cnt_tv = itemView.findViewById(R.id.count_tv);

            itemView.setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){
                    if(onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, pos);
                    }
                }
            });
        }
    }

}