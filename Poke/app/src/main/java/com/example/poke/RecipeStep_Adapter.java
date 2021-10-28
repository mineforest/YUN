package com.example.poke;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class RecipeStep_Adapter extends RecyclerView.Adapter<RecipeStep_Adapter.CustomViewHolder> {

    private final List<String> rcp_urls;
    private final List<String> rcp_steps;

    public RecipeStep_Adapter(List<String> rcp_urls, List<String> rcp_steps) {
        this.rcp_urls = rcp_urls;
        this.rcp_steps = rcp_steps;
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
    public RecipeStep_Adapter.CustomViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_step_itemview,parent,false);

        return new RecipeStep_Adapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecipeStep_Adapter.CustomViewHolder holder, int position) {
        if(rcp_urls != null && position < rcp_urls.size()) {
            Glide.with(this.context).load(rcp_urls.get(position)).into(holder.rcp_img);
        }
        if(rcp_steps != null && position < rcp_steps.size()) {
            holder.rcp_step_txt.setText(rcp_steps.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return (rcp_urls != null ? rcp_urls.size() : 0);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public ImageView rcp_img;
        public TextView rcp_step_txt;
        public ImageView tts_active;
        private TextToSpeech tts;
        private Boolean isTTS = false;

        public CustomViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.rcp_img = itemView.findViewById(R.id.step_img);
            this.rcp_step_txt = itemView.findViewById(R.id.step_txt);
            this.tts_active = itemView.findViewById(R.id.tts_active);
            //TTS 관련코드
            tts = new TextToSpeech(itemView.getContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status!= TextToSpeech.ERROR) tts.setLanguage(Locale.KOREAN);
                }
            });

            tts_active.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    if(!isTTS ) {
                        String text = rcp_step_txt.getText().toString();
                        tts.setPitch(1.0f);
                        tts.setSpeechRate(1.0f);
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                        tts_active.setImageResource(R.drawable.ic_pause_black_24dp);
                        isTTS = true;
                    }
                    else {
                        tts.stop();
                        tts_active.setImageResource(R.drawable.ic_volume_up_black_24dp);
                        isTTS = false;
                    }
                }
            });
        }
    }
}
