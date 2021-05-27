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
public class DipsAdapter extends RecyclerView.Adapter<DipsAdapter.CustomViewHolder> {

    private ArrayList<UserDibs> dipsList;

    public DipsAdapter(ArrayList<UserDibs> dipsList) {
        this.dipsList = dipsList;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dips_list,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DipsAdapter.CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(dipsList.get(position).getDipsImage())
                .into(holder.dips_image);
        holder.dips_rec.setText(String.valueOf(dipsList.get(position).getDipsTitle()));

    }

    @Override
    public int getItemCount() {
        return (dipsList != null ? dipsList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView dips_image;
        TextView dips_rec;

        public CustomViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.dips_image = itemView.findViewById(R.id.dips_image);
            this.dips_rec = itemView.findViewById(R.id.dips_rec);


        }
    }
}