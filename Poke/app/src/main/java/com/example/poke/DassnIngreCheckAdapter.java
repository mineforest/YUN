package com.example.poke;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class DassnIngreCheckAdapter extends RecyclerView.Adapter<DassnIngreCheckAdapter.CustomViewHolder> {

    private ArrayList<String> ingres;

    public DassnIngreCheckAdapter(ArrayList<String> ingres) {
        this.ingres = ingres;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    private OnItemClickListener onItemClickListener = null;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public DassnIngreCheckAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dassn_ingre_item,parent,false);
        return new DassnIngreCheckAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DassnIngreCheckAdapter.CustomViewHolder holder, int position) {
        if(ingres != null && position < ingres.size()) {
            holder.textView.setText(ingres.get(position));
        }
    }

    @Override
    public int getItemCount()  {
        return (ingres != null ? ingres.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.dassn_item_tv);
            textView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if(onItemClickListener != null){
                            if(textView.getPaintFlags() == 0) textView.setPaintFlags(textView.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                            else textView.setPaintFlags(0);
                            onItemClickListener.onItemClick(v, pos);
                        }
                    }
                }
            });
        }
    }
}

