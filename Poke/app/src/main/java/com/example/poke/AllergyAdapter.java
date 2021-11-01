package com.example.poke;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

    public AllergyAdapter(ArrayList<String> allergyArrayList) {
        this.allergyArrayList = allergyArrayList;
    }

    Context context;
    private Intent intent;
    private DatabaseReference mDatabase;


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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String uid = user.getUid();
        String rid = allergyArrayList.get(position);

//        mDatabase.child("allergy").child(uid).child(rid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                Log.d("onDataChange: ", snapshot.getKey());
//                if (snapshot != null && snapshot.getKey() == rid) {
//                    holder.checkBox.setChecked(true);
//                } else {
//                    holder.checkBox.setChecked(false);
//                }
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        holder.checkBox.setText(allergyArrayList.get(position));

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 {
                     if(isChecked) {
                         mDatabase.child("allergy").child(uid).child(allergyArrayList.get(position)).setValue(new UserAllergy(rid));
                         Snackbar.make(buttonView,"알레르기가 변경되었습니다.", Snackbar.LENGTH_LONG).show();
                     }
                     else {
                         String path = mDatabase.child("allergy").child(uid).push().getKey();
                         mDatabase.child("allergy").child(uid).child(rid).removeValue();
                         Snackbar.make(buttonView,"알레르기가 변경되었습니다.", Snackbar.LENGTH_LONG).show();
                     }

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (allergyArrayList != null ? allergyArrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;
//        CheckBox checkBox;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.checkBox = itemView.findViewById(R.id.allergy_check);

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


