package com.example.poke;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import static android.content.Context.MODE_PRIVATE;
import static com.example.poke.setAlarm.cancel;
import static com.example.poke.setAlarm.startAlarmBroadcastReceiver;


public class FridgeAdapter extends  RecyclerView.Adapter<FridgeAdapter.ViewHolder> {
    private ArrayList<UserIngredient> ingredientsList;
    Context context;
    Calendar today = Calendar.getInstance();
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    long date;
    public boolean alarm_flag = true;
    private DatabaseReference mDatabase;

    private OnItemClickListener mlistener = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mlistener = listener;
    }

    public FridgeAdapter(ArrayList<UserIngredient> list) {
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
    public FridgeAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_recyclerview,parent,false);
        FridgeAdapter.ViewHolder holder = new FridgeAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String[] dday=ingredientsList.get(position).getExpirationDate().split("-");
        boolean mainflag =((MainActivity)MainActivity.mContext).alarm;
        SharedPreferences sharedPreferences = context.getSharedPreferences("bibleNotify",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("SetTimeH", 21);
        editor.putInt("SetTimeM", 41);
        editor.putInt("count",0);
        editor.putString("Started","yes");
        editor.commit();

        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialog);
                builder.setMessage("재료를 삭제 하겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        String uid = user.getUid();
                        mDatabase.child("ingredient").child(uid).child(ingredientsList.get(position).getIngredientKey()).setValue(null);
                        Toast.makeText(context,ingredientsList.get(position).getIngredientTitle() + " 삭제 ", Toast.LENGTH_SHORT).show();
                        ingredientsList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, ingredientsList.size());
                    }
                });
                builder.setNegativeButton("아니오",null);
                builder.create().show();
            }
        });

        int[] days=new int[3];
        for(int i=0;i<3;i++){
            days[i]=Integer.parseInt(dday[i]);
        }

        cal.set(days[0],days[1]-1,days[2]);
        date = (cal.getTimeInMillis() - today.getTimeInMillis());
//        Glide.with(holder.itemView)
//                .load(ingredientsList.get(position).getCategory())
//                .into(holder.image);
        date /= 86400000;
        holder.title.setText(String.valueOf(ingredientsList.get(position).getIngredientTitle()));
        holder.reg_date.setText(ingredientsList.get(position).getExpirationDate());
        if(date < 0){
            holder.day.setText(("D+" + Long.toString(Math.abs(date))));
            holder.day.setBackground(ContextCompat.getDrawable(context, R.drawable.border_gray));
        }
        else {
            holder.day.setText(("D-" + Long.toString(date)));
            if(date<=3) holder.day.setBackground(ContextCompat.getDrawable(context, R.drawable.border_red));
            else holder.day.setBackground(ContextCompat.getDrawable(context, R.drawable.border_green));
        }

        if(date <= 3) {
            if(date<0) holder.fridgeImage.setImageTintList(ColorStateList.valueOf(Color.parseColor("#BAC4CC")));
            else holder.fridgeImage.setImageTintList(ColorStateList.valueOf(Color.parseColor("#E60000")));


            if(alarm_flag==true&&mainflag==false){
                setAlarm.startAlarmBroadcastReceiver(context, sharedPreferences);
                alarm_flag=false;
            }
        }
        else{
            holder.fridgeImage.setImageTintList(ColorStateList.valueOf(Color.parseColor("#29D67E")));
        }
    }

    @Override
    public int getItemCount() {
        return (ingredientsList != null ? ingredientsList.size() : 0);
    }

    public void filterList(ArrayList<UserIngredient> filteredList) {
        ingredientsList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //ImageView image;
        TextView title;
        TextView day;
        TextView reg_date;
        ImageView deleteImage;
        ImageView fridgeImage;

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

            this.title = itemView.findViewById(R.id.ingredientTitleView);
            this.reg_date = itemView.findViewById(R.id.textView13);
            this.day = itemView.findViewById(R.id.dDay);
            this.deleteImage = itemView.findViewById(R.id.deleteImage);
            this.fridgeImage = itemView.findViewById(R.id.categoryView);
        }

    }


}
