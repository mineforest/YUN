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

import java.util.List;
import java.util.Map;

public class RecipeIngre_Adapter extends RecyclerView.Adapter<RecipeIngre_Adapter.CustomViewHolder> {

    private final List<Map<String, String>> list;

    public RecipeIngre_Adapter(List<Map<String, String>> list) {
        this.list = list;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_ingre_itemview,parent,false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecipeIngre_Adapter.CustomViewHolder holder, int position) {
        if(list != null && position < list.size()) {
            holder.i_name.setText(list.get(position).get("ingre_name"));
            holder.i_amount.setText(list.get(position).get("ingre_count"));
            holder.i_unit.setText(list.get(position).get("ingre_unit"));
        }
    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView i_name;
        public TextView i_amount;
        public TextView i_unit;

        public CustomViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.i_name = itemView.findViewById(R.id.i_name_txt);
            this.i_amount = itemView.findViewById(R.id.i_amount_txt);
            this.i_unit = itemView.findViewById(R.id.i_unit_txt);
        }
    }
}