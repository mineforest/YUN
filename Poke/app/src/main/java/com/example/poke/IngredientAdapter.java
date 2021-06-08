package com.example.poke;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class IngredientAdapter extends  RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private ArrayList<UserIngredient> ingredientsList;
    Context context;
    Calendar today = Calendar.getInstance();
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    long date;
    TextView textView;

    private OnItemClickListener mlistener = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mlistener = listener;
    }

    public IngredientAdapter(ArrayList<UserIngredient> list) {
        this.ingredientsList = list;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @NonNull
    @NotNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_recyclerview,parent,false);
        IngredientAdapter.ViewHolder holder = new IngredientAdapter.ViewHolder(view);
        textView = view.findViewById(R.id.dDay);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] dday=ingredientsList.get(position).getExpirationDate().split("-");

        int[] days=new int[3];
        for(int i=0;i<3;i++){
            days[i]=Integer.parseInt(dday[i]);
        }

        cal.set(days[0],days[1]-1,days[2]);
        date = (cal.getTimeInMillis() - today.getTimeInMillis());
//        Glide.with(holder.itemView)
//                .load(ingredientsList.get(position).getCategory())
//                .into(holder.image);

        holder.title.setText(String.valueOf(ingredientsList.get(position).getIngredientTitle()));
        if(date/86400000 < 0){
            holder.day.setText(("D+" + Long.toString(Math.abs(date/86400000))));
        }
        else {
            holder.day.setText(("D-" + Long.toString(date / 86400000)));
        }
        
        if(date/86400000 < 4)
            textView.setBackground(ContextCompat.getDrawable(context,R.drawable.border_red));
    }



    @Override
    public int getItemCount() {
        return (ingredientsList != null ? ingredientsList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //ImageView image;
        TextView title;
        TextView day;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                int pos = getAdapterPosition();
                                                if(pos != RecyclerView.NO_POSITION){
                                                    if(mlistener != null){
                                                        mlistener.onItemClick(v, pos);
                                                    }
                                                }
                                            }
                                        });

                    //  this.image = itemView.findViewById(R.id.categoryView);
            this.title = itemView.findViewById(R.id.ingredientTitleView);
            this.day = itemView.findViewById(R.id.dDay);

        }

    }
}
