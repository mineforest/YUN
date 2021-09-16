package com.example.poke;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

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
        if (mRcplist.size() > 6) return 8;
        return (mRcplist != null ? mRcplist.size() : 0);
    }
}
