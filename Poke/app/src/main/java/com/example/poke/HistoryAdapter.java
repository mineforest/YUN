package com.example.poke;

import android.content.Context;
import android.text.Layout;
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
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.CustomViewHolder> {

    private ArrayList<UserHistory> historyList;

    public HistoryAdapter(ArrayList<UserHistory> historyList) {
        this.historyList = historyList;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HistoryAdapter.CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(historyList.get(position).getRecipeImage())
                .into(holder.history_image);
        holder.history_rec.setText(String.valueOf(historyList.get(position).getRecipeTitle()));
        holder.history_date.setText(String.valueOf(historyList.get(position).getDate()));
        holder.history_rate.setText(String.valueOf(historyList.get(position).getRate()));
    }

    @Override
    public int getItemCount() {
        return (historyList != null ? historyList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView history_image;
        TextView history_rec;
        TextView history_date;
        TextView history_rate;

        public CustomViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.history_image = itemView.findViewById(R.id.history_image);
            this.history_rec = itemView.findViewById(R.id.history_rec);
            this.history_date = itemView.findViewById(R.id.history_date);
            this.history_rate = itemView.findViewById(R.id.history_rate);

        }
    }
}