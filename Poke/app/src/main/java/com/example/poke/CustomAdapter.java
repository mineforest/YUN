package com.example.poke;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
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
    private DatabaseReference mDatabase;
    String uid;
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
            holder.rcp_cooktime.setText(rcp.getTime()+"분");

            mDatabase = FirebaseDatabase.getInstance().getReference();
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            mDatabase.child("ingredient").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<String> myIngreList = new ArrayList<>();
                    int cnt = 0;
                    if (snapshot.exists()) {
                        for (DataSnapshot ridSnapshot : snapshot.getChildren()) {
                            UserIngredient ingres = ridSnapshot.getValue(UserIngredient.class);
                            myIngreList.add(ingres.getIngredientTitle());
                        }
                    }
                    for (String myIngre : myIngreList) {
                        for (Map<String, String> tmpIngre : rcp.getIngre_list()) {
                            if (tmpIngre.containsValue(myIngre)) {
                                Log.d("HAPPENED?", cnt+"");
                                cnt++;
                            }
                        }
                    }
                    Long rate = Math.round((double) cnt / (double) rcp.getIngre_list().size() * 100.0);
                    holder.rate.setText(rate + "%");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

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
        return (mRcplist != null ? mRcplist.size() : 0);
    }
}
