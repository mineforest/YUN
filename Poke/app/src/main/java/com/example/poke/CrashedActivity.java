package com.example.poke;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.poke.R;

public class CrashedActivity extends AppCompatActivity {
    private int cnt = 5;
    private TextView textView;
    private Handler handler = new Handler();;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            textView.setText(cnt + "");
            if (cnt <= 0) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(10);
            } else {
                handler.postDelayed(runnable, 1000);
                cnt -= 1;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crash_quit);

        getSupportActionBar().hide();
        textView = findViewById(R.id.crash_cnt);
        handler.post(runnable);

    }
}
