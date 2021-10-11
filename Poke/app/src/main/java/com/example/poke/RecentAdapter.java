package com.example.poke;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewHolder> {

    private ArrayList<String> mData = null ;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1 ;

        ViewHolder(View itemView) {
            super(itemView) ;

            textView1 = itemView.findViewById(R.id.recent) ;
        }
    }

    RecentAdapter(ArrayList<String> list) {
        mData = list ;
    }

    @Override
    public RecentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.recentview, parent, false) ;
        RecentAdapter.ViewHolder vh = new RecentAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(RecentAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String text = mData.get(position) ;
        holder.textView1.setText(text) ;
//
//        holder.textView1.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                String rc = (String) holder.textView1.getText();
//
//                SearchFragment.cl
//
//                Intent intent = new Intent(v.getContext(), SearchFragment.class);
//                intent.putExtra("rcp_id",holder.textView1.getText());
//                v.getContext().startActivity(intent);
//                // Toast.makeText(v.getContext(), searchList.get(position).getId(),Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}