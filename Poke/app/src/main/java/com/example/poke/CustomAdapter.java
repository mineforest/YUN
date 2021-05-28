package com.example.poke;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private final Recipe_get[] rcps;

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView getRcp_thumbnail() {
            return rcp_thumbnail;
        }

        public TextView getRcp_name() {
            return rcp_name;
        }

        public TextView getRcp_subtitle() {
            return rcp_subtitle;
        }

        public TextView getCook_time() {
            return cook_time;
        }

        private ImageView rcp_thumbnail;
        private TextView rcp_name;
        private TextView rcp_subtitle;
        private TextView cook_time;

        public ViewHolder(@NonNull @NotNull View v) {
            super(v);
            // 클릭 이벤트
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                }
            });
            rcp_thumbnail = (ImageView) v.findViewById(R.id.rcp_thumbnail);
            rcp_name = (TextView) v.findViewById(R.id.rcp_title);
            cook_time = (TextView) v.findViewById(R.id.cook_time);
            rcp_subtitle = (TextView) v.findViewById(R.id.rcp_subtitle);
        }

    }
    public CustomAdapter(Recipe_get[] dataSet) { rcps = dataSet;}

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_test, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder viewHolder, int position) {
//        Glide.with(viewHolder.itemView).load(rcps[position].getThumbnail()).into(viewHolder.getRcp_thumbnail());
//        viewHolder.getRcp_name().setText(rcps[position].getRcp_title());
//        viewHolder.getRcp_subtitle().setText(rcps[position].getRcp_id());
//        viewHolder.getCook_time().setText(rcps[position].getCook_time());
        Glide.with(viewHolder.itemView).load("https://cloudfront.haemukja.com/vh.php?url=https://d1hk7gw6lgygff.cloudfront.net/uploads/direction/image_file/48554/pad_thumb_0.JPG&convert=jpgmin&rt=600").into(viewHolder.getRcp_thumbnail());
        viewHolder.getRcp_name().setText("김치볶음밥");
        viewHolder.getRcp_subtitle().setText("볶음밥");
        viewHolder.getCook_time().setText("15분");
    }

    @Override
    public int getItemCount() {
        return rcps.length;
    }

}
