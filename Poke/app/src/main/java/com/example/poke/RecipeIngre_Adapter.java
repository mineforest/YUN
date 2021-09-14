package com.example.poke;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class RecipeIngre_Adapter extends RecyclerView.Adapter<RecipeIngre_Adapter.CustomViewHolder> {

    private DatabaseReference mDatabase;
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String uid = user.getUid();

        mDatabase.child("ingredient").child(uid).orderByChild("ingredientTitle").equalTo(list.get(position).get("ingre_name")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot != null && snapshot.getChildren() != null && snapshot.getChildren().iterator().hasNext()) {
                    holder.checkBox.setChecked(true);
                } else {
                    holder.checkBox.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        if(list != null && position < list.size()) {
            holder.i_name.setText(list.get(position).get("ingre_name"));
            holder.i_amount.setText(list.get(position).get("ingre_count")+list.get(position).get("ingre_unit"));
        }

    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView i_name;
        public TextView i_amount;
        public CheckBox checkBox;

        public CustomViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.i_name = itemView.findViewById(R.id.i_name_txt);
            this.i_amount = itemView.findViewById(R.id.i_amount_txt);
            this.checkBox = itemView.findViewById(R.id.check);
        }
    }
}