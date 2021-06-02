package com.example.poke;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.CustomViewHolder> {

    private ArrayList<UserHistory> historyList;

    public HistoryAdapter(ArrayList<UserHistory> historyList) {
        this.historyList = historyList;
    }

    Context context;
    private Intent intent;

    public HistoryAdapter() {

    }

    public interface OnItemClickListener {
        void onItemClick(View v,int position);
    }

    private OnItemClickListener mListener = null;



    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HistoryAdapter.CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(historyList.get(position).getRecipeImage())
                .into(holder.history_image);
        holder.history_rec.setText(String.valueOf(historyList.get(position).getRecipeTitle()));
        holder.history_date.setText(String.valueOf(historyList.get(position).getDate()));
        holder.history_rate.setRating(Float.parseFloat(String.valueOf(historyList.get(position).getRate())));

        holder.history_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), Recipe_Info.class);
                intent.putExtra("rcp_id", historyList.get(position).getRcp_id());
                v.getContext().startActivity(intent);
                Toast.makeText(v.getContext(), historyList.get(position).getRcp_id(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return (historyList != null ? historyList.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView history_image;
        TextView history_rec;
        TextView history_date;
        RatingBar history_rate;

        public CustomViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.history_image = itemView.findViewById(R.id.history_image);
            this.history_rec = itemView.findViewById(R.id.history_rec);
            this.history_date = itemView.findViewById(R.id.history_date);
            this.history_rate = itemView.findViewById(R.id.history_rate);


        }

    }
}