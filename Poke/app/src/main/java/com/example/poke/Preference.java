package com.example.poke;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Preference extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        findViewById(R.id.menuButton1);
        findViewById(R.id.menuButton2);
        findViewById(R.id.menuButton3);
    }

        public void onClick(View v) {
            switch (v.getId()){
                case R.id.menuButton1:
                    profileUpdate();
                    break;
                case R.id.menuButton2:
                    profileUpdate();
                    break;
                case R.id.menuButton3:
                    profileUpdate();
                    break;
            }
        }

}
