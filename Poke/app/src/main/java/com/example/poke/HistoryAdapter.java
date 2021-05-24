package com.example.poke;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.core.Context;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.CustomViewHolder> {

    private ArrayList<UserHistory> historyList;
    private Context context;

    public HistoryAdapter(ArrayList<UserHistory> historyList, Context context) {
        this.historyList = historyList;
        this.context = context;
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
        holder.history_rec.setText(historyList.get(position).getRecipeTitle());
        holder.history_date.setText(historyList.get(position).getDate());
        holder.history_rate.setText(historyList.get(position).getRate()+"");
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