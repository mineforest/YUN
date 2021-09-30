package com.example.poke;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.poke.CardViewHolder;
import com.example.poke.R;
import com.example.poke.Recipe_Info;
import com.example.poke.Recipe_get;
import com.example.poke.UserIngredient;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomAdapter extends RecyclerView.Adapter<CardViewHolder> {

    private final ArrayList<Recipe_get> mRcplist;
    private Intent intent;
    CustomAdapter(ArrayList<Recipe_get> rcp_list) {this.mRcplist = rcp_list;}

    Context context;

    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new CardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        if (mRcplist != null && position < mRcplist.size()) {
            Recipe_get rcp = mRcplist.get(position);
            holder.rcp_title.setText(rcp.getName());
            holder.rcp_cooktime.setText(rcp.getTime()+"분");
            holder.rate.setText(rcp.getRate() + "%");

//            holder.materialCardView.setRadius(10);
//            if (rcp.getRate() < 20) holder.materialCardView.setBackgroundColor(context.getColor(R.color.match1));
//            else if (rcp.getRate() < 40 ) holder.materialCardView.setBackgroundColor(context.getColor(R.color.match2));
//            else if (rcp.getRate() < 60 ) holder.materialCardView.setBackgroundColor(context.getColor(R.color.match3));
//            else if (rcp.getRate() < 80 ) holder.materialCardView.setBackgroundColor(context.getColor(R.color.match4));
//            else holder.materialCardView.setBackgroundColor(context.getColor(R.color.match5));
            Glide.with(holder.itemView)
                    .load(rcp.getThumbnail())
                    .into(holder.rcp_thumbnail);

            holder.rcp_thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(v.getContext(), Recipe_Info.class);
                    intent.putExtra("rcp_id", mRcplist.get(position).getId());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mRcplist.size() >= 8) return 8;
        else return (mRcplist != null ? mRcplist.size() : 0);
    }
}
