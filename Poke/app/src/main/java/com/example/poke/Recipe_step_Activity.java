package com.example.poke;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
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
    private ImageView timerBtn;
    private Dialog dialog;
    private TextView timer_minimi;
    private CountDownTimer timer;
    private long milliLeft;
    private HashMap<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_step_viewpager);
        Intent intent = getIntent();
        rcp = intent.getParcelableExtra("rcp");
        ingres = intent.getStringArrayListExtra("ingre");

        getSupportActionBar().hide();

        springDotsIndicator = findViewById(R.id.dots_indicator);
        nextbutton = findViewById(R.id.nextButton);
        viewPager = findViewById(R.id.recipe_viewpager);
        ttsBtn = findViewById(R.id.tts_btn);
        timer_minimi = findViewById(R.id.timer_minimi);

        timerBtn = findViewById(R.id.timer_btn);
        milliLeft = 180000;

        dialog = new Dialog(Recipe_step_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.timer_dialog);

        timerBtn.setOnClickListener(timerButtonClickListener);
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

    View.OnClickListener timerButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog();
        }
    };

    public void showDialog() {
        dialog.show();
        TextView timer_tv = dialog.findViewById(R.id.timer_timer);
        Button start_btn = dialog.findViewById(R.id.timer_start);
        Button bonus_btn = dialog.findViewById(R.id.timer_plus);
        Button minus_btn = dialog.findViewById(R.id.timer_minus);

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start_btn.getText().equals("START")) {
                    timerStart(milliLeft, timer_tv);
                    start_btn.setText("PAUSE");
                    timerBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#E60000")));
                    start_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8C8B8A")));
                }
                else if(start_btn.getText().equals("PAUSE")) {
                    timerPause();
                    start_btn.setText("START");
                    timerBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                    start_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#29D67E")));
                }
            }
        });
        bonus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerPause();
                milliLeft += 15000;
                long min = (milliLeft / (1000 * 60));
                long sec = ((milliLeft / 1000) - min * 60);
                timer_tv.setText(min + ":" + sec);
                timer_minimi.setText(min + ":" + sec);
                start_btn.setText("START");
                start_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#29D67E")));
            }
        });
        minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(milliLeft > 15000) {
                    timerPause();
                    milliLeft -= 15000;
                    long min = (milliLeft / (1000 * 60));
                    long sec = ((milliLeft / 1000) - min * 60);
                    timer_tv.setText(min + ":" + sec);
                    timer_minimi.setText(min + ":" + sec);
                    start_btn.setText("START");
                    start_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#29D67E")));
                }
            }
        });
    }

    public void timerStart(long timeLengthMilli, TextView timer_tv) {
        timer = new CountDownTimer(timeLengthMilli, 1000) {
            @Override
            public void onTick(long milliTillFinish) {
                milliLeft = milliTillFinish;
                long min = (milliTillFinish / (1000 * 60));
                long sec = ((milliTillFinish / 1000) - min * 60);
                timer_tv.setText(min + ":" + sec);
                timer_minimi.setText(min + ":" + sec);
            }
            @Override
            public void onFinish() {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Recipe_step_Activity.this);
                if(preferences.getBoolean("timerAlarm",false)) {
                    MediaPlayer player = MediaPlayer.create(Recipe_step_Activity.this, R.raw.poke_for_acapella);
                    player.start();
                }
                else{
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    ringtone.play();
                }
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(2000);
                timer_tv.setText("POKE!");
                timer_minimi.setText("완료");
            }
        };
        timer.start();
    }
    public void timerPause() {
        if(timer != null) timer.cancel();
    }

    @Override
    protected void onDestroy() {
        tts.stop();
        tts.shutdown();
        timerPause();
        super.onDestroy();
    }
}
