package com.example.poke;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.CustomViewHolder> {

    private ArrayList<UserHistory> historyList;

    public HistoryAdapter(ArrayList<UserHistory> historyList){
        this.historyList = historyList;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public HistoryAdapter.CustomViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull HistoryAdapter.CustomViewHolder holder, int position) {

        holder.history_image.setImageResource(historyList.get(position).getRecipeImage());
        holder.history_rec.setText(historyList.get(position).getRecipeTitle());
        holder.history_date.setText(historyList.get(position).getDate());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curName = holder.history_rec.getText().toString();
                Toast.makeText(v.getContext(),curName, Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                remove(holder.getAdapterPosition());

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != historyList ? historyList.size() : 0);
    }

    public void remove(int position) {
        try {
            historyList.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView history_image;
        protected TextView history_rec;
        protected TextView history_date;

        public CustomViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.history_image = (ImageView) itemView.findViewById(R.id.history_image);
            this.history_rec = (TextView) itemView.findViewById(R.id.history_rec);
            this.history_rec = (TextView) itemView.findViewById(R.id.history_date);

        }
    }
}
