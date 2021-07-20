package com.example.poke;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainViewpageAdapter  extends RecyclerView.Adapter<MainViewpageViewholder> {

    private static final int NUM_PAGES = 5;
    private final ArrayList<Recipe_get> mRcplist;
    private Intent intent;
    private Chip chip;
    private Context context;
    LayoutInflater layoutInflater;

    MainViewpageAdapter(ArrayList<Recipe_get> rcp_list) {
        this.mRcplist = rcp_list;
    }

    @NonNull
    @Override
    public MainViewpageViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_viewpager_view, parent, false);
        context=parent.getContext();
        layoutInflater = LayoutInflater.from(context);
        return new MainViewpageViewholder(layoutView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MainViewpageViewholder holder, int position) {
        if (mRcplist != null && position < mRcplist.size()) {
            Recipe_get rcp = mRcplist.get(position);
            holder.keyword.setText("오늘의 저녁");
            holder.rcp_title.setText(rcp.getName());
            holder.rcp_cooktime.setText(rcp.getTime()+"분");
            holder.rate.setText((rcp.getRate())+"%");

            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 15,
                    context.getResources().getDisplayMetrics()
            );
            int l=0;

            String[] string;
            for(String t : rcp.getTag()){
                string = (t.split(","));
                if(l>1){
                    break;
                }
                l++;
                for(String s : string){
                    chip =(Chip)layoutInflater.inflate(R.layout.tag_chip, null, false);
                    chip.setText(s);
                    chip.setClickable(false);
                    chip.setPadding(paddingDp, 0, paddingDp, 0);
                    chip.setCheckable(false);
                    holder.chipGroup.addView(chip);
                }
            }

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
        return NUM_PAGES;
    }
}
