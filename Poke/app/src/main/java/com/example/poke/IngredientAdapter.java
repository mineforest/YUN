package com.example.poke;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class IngredientAdapter extends  RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private ArrayList<UserIngredient> ingredientsList;
    Context context;
    Calendar today = Calendar.getInstance();
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    long date;
    TextView textView;
    FirebaseMessagingService firebaseMessagingService = new FirebaseMessagingService();

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
        date /= 86400000;

        holder.title.setText(String.valueOf(ingredientsList.get(position).getIngredientTitle()));
        if(date < 0){
            holder.day.setText(("D+" + Long.toString(Math.abs(date))));
        }
        else {
            holder.day.setText(("D-" + Long.toString(date)));
        }
        if(date <= 3) {
            holder.day.setBackground(ContextCompat.getDrawable(context, R.drawable.border_red));
            diaryNotification(String.valueOf(ingredientsList.get(position).getIngredientTitle()));
        }
        else{
            holder.day.setBackground(ContextCompat.getDrawable(context, R.drawable.border_green));
        }


    }
    public void diaryNotification(String name)
    {
        Boolean dailyNotify = true; // 무조건 알람을 사용
        Calendar c = Calendar.getInstance();
//        String ingre = name;
        c.set(Calendar.HOUR_OF_DAY, 20);
        c.set(Calendar.MINUTE, 55);
        c.set(Calendar.SECOND, 00);
        PackageManager pm = this.context.getPackageManager();
        ComponentName receiver = new ComponentName(context, DeviceBootReceiver.class);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
//        alarmIntent.putExtra("ingre",ingre);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // 사용자가 매일 알람을 허용했다면
        if (dailyNotify) {
            if (alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                }
            }
            // 부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

        }
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
