package com.example.poke;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
public class DipsAdapter extends RecyclerView.Adapter<DipsAdapter.CustomViewHolder> {

    private final ArrayList<UserDibs> dipsList;

    public DipsAdapter(ArrayList<UserDibs> dipsList) {
        this.dipsList = dipsList;
    }

    Context context;
    private Intent intent;
    private DatabaseReference mDatabase;

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

        holder.dips_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), Recipe_Info.class);
                intent.putExtra("rcp_id", dipsList.get(position).getRcp_id());
                v.getContext().startActivity(intent);
            }
        });

        holder.dips_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), Recipe_Info.class);
                intent.putExtra("rcp_id", dipsList.get(position).getRcp_id());
                v.getContext().startActivity(intent);
            }
        });
        holder.dips_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialog);
                builder.setMessage("찜 목록에서 지울까요?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        String uid = user.getUid();
                        String rid = dipsList.get(position).getRcp_id();
                        mDatabase.child("dips").child(uid).child(rid).removeValue();
                        Snackbar.make(v,"찜 목록에서 삭제되었습니다.", Snackbar.LENGTH_LONG).show();
                        dipsList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, dipsList.size());
                    }
                });
                builder.setNegativeButton("취소",null);
                builder.create().show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return (dipsList != null ? dipsList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView dips_image;
        TextView dips_rec;
        ImageView dips_heart;

        public CustomViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.dips_image = itemView.findViewById(R.id.dips_image);
            this.dips_rec = itemView.findViewById(R.id.dips_rec);
            this.dips_heart = itemView.findViewById(R.id.dips_heart);
        }
    }
}