package com.example.poke;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Recipe_step_Activity extends AppCompatActivity {

    private Recipe_get rcp;
    private RecipeStep_Adapter recipeStep_adapter;
    private SpringDotsIndicator springDotsIndicator;
    private Button nextbutton;
    private ViewPager2 viewPager;
    private ArrayList<String> ingres;
    private TextToSpeech tts;
    private Boolean isTTS = false;
    private ImageView ttsBtn;
    private HashMap<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_step_viewpager);
        Intent intent = getIntent();
        rcp = intent.getParcelableExtra("rcp");
        ingres = intent.getStringArrayListExtra("ingre");

        springDotsIndicator = findViewById(R.id.dots_indicator);
        nextbutton = findViewById(R.id.nextButton);
        viewPager = findViewById(R.id.recipe_viewpager);
        ttsBtn = findViewById(R.id.tts_btn);

        ttsBtn.setOnClickListener(ttsButtonClickListener);
        nextbutton.setOnClickListener(nextButtonClickListener);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == recipeStep_adapter.getItemCount()-1) {
                    nextbutton.setText("요리 완료");
                }
                else {
                    nextbutton.setText("다음");
                }
            }
        });

        tts = new TextToSpeech(Recipe_step_Activity.this, status -> {
            if(status!= TextToSpeech.ERROR) {
                tts.setLanguage(Locale.KOREAN);
                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "pokepoke");

                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                        ttsBtn.setImageResource(R.drawable.ic_pause_black_24dp);
                        isTTS = true;
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        int curr = viewPager.getCurrentItem();
                        viewPager.setCurrentItem(curr+1,true);
                        ttsBtn.setImageResource(R.drawable.ic_volume_up_black_24dp);
                    }

                    @Override
                    public void onError(String utteranceId) {

                    }
                });
            }
        });

        viewPager.setClipToPadding(false);
        viewPager.setPadding(50,0,50,0);
        viewPager.setOffscreenPageLimit(3);
        viewPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float v = 1-Math.abs(position);
                page.setScaleY(0.8f + v * 0.2f);
            }
        });
        viewPager.setPageTransformer(transformer);
        recipeStep_adapter = new RecipeStep_Adapter(rcp.getRecipe_img(), rcp.getRecipe());
        viewPager.setAdapter(recipeStep_adapter);
        springDotsIndicator.setViewPager2(viewPager);

    }

    View.OnClickListener nextButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int curr = viewPager.getCurrentItem();
            if(curr == recipeStep_adapter.getItemCount()-1) {
                Intent intent = new Intent(getApplicationContext(), RatingActivity.class);

                intent.putExtra("ingre", ingres);
                intent.putExtra("rcp", rcp);
                startActivity(intent);
            }
            else viewPager.setCurrentItem(curr+1,true);
        }
    };

    View.OnClickListener ttsButtonClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            if(!isTTS ) {
                String text = rcp.getRecipe().get(viewPager.getCurrentItem());
                tts.setPitch(1.0f);
                tts.setSpeechRate(1.0f);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
            }
            else {
                tts.stop();
                ttsBtn.setImageResource(R.drawable.ic_volume_up_black_24dp);
                isTTS = false;
            }
        }
    };

    @Override
    protected void onDestroy() {
        tts.stop();
        tts.shutdown();
        super.onDestroy();
    }
}
