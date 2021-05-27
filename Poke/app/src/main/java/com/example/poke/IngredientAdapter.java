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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IngredientAdapter extends  RecyclerView.Adapter<IngredientAdapter.ViewHolder>{
    private ArrayList<UserIngredient> ingredientsList;

    public IngredientAdapter(ArrayList<UserIngredient> list) {
        this.ingredientsList = list;
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
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_recyclerview,parent,false);
        IngredientAdapter.ViewHolder holder = new IngredientAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Glide.with(holder.itemView)
//                .load(ingredientsList.get(position).getCategory())
//                .into(holder.image);
        holder.title.setText(String.valueOf(ingredientsList.get(position).getIngredientTitle()));
        holder.day.setText(String.valueOf(ingredientsList.get(position).getExpirationDate()));
    }

    @Override
    public int getItemCount() {
        return (ingredientsList != null ? ingredientsList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //ImageView image;
        TextView title;
        TextView day;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
          //  this.image = itemView.findViewById(R.id.categoryView);
            this.title = itemView.findViewById(R.id.ingredientTitleView);
            this.day = itemView.findViewById(R.id.dDay);

        }
    }

}
