package com.example.poke;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CardViewHolder> {

    private final ArrayList<Recipe_get> mRcplist;

    CustomAdapter(ArrayList<Recipe_get> rcp_list) {
        this.mRcplist = rcp_list;
    }

    Context context;

    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_test, parent, false);
        return new CardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        if (mRcplist != null && position < mRcplist.size()) {
            Recipe_get rcp = mRcplist.get(position);
            holder.rcp_title.setText(rcp.getName());
            holder.rcp_cooktime.setText(rcp.getTime());
            Glide.with(holder.itemView)
                    .load(rcp.getThumbnail())
                    .into(holder.rcp_thumbnail);
            Log.d("dddddddddddd","helooooooo");
        }

    }

    @Override
    public int getItemCount() {
        return (mRcplist != null ? mRcplist.size() : 0);
    }
}
