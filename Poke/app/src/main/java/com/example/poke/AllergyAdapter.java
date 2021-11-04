package com.example.poke;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllergyAdapter extends RecyclerView.Adapter<AllergyAdapter.CustomViewHolder> {
    private ArrayList<String> allergyArrayList;
    private ArrayList<Boolean> allergyMapping;

    public AllergyAdapter(ArrayList<String> allergyArrayList, ArrayList<Boolean> allergyMapping) {
        this.allergyArrayList = allergyArrayList;
        this.allergyMapping = allergyMapping;
    }

    Context context;
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    private AllergyAdapter.OnItemClickListener onItemClickListener = null;
    public void setOnItemClickListener(AllergyAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @NonNull
    @NotNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_allergy,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AllergyAdapter.CustomViewHolder holder, int position)
    {
        holder.allery_tv.setText(allergyArrayList.get(position));
        if (allergyMapping.get(position)) {
            holder.allery_tv.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6BB4AE")));
            holder.allery_tv.setTextColor(ColorStateList.valueOf(Color.parseColor("#DFEFED")));
        }
        else {
            holder.allery_tv.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DFEFED")));
            holder.allery_tv.setTextColor(ColorStateList.valueOf(Color.parseColor("#6BB4AE")));
        }
    }

    @Override
    public int getItemCount() {
        return (allergyArrayList != null ? allergyArrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView allery_tv;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.allery_tv = itemView.findViewById(R.id.allergy_check);

            itemView.setOnClickListener(v -> {
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if(onItemClickListener != null){
                            onItemClickListener.onItemClick(v, pos);
                        }
                    }
                });
            }
        }

}


