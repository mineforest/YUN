package com.example.poke;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MyInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_info);

        findViewById(R.id.historyButton).setOnClickListener(onClickListener);
        findViewById(R.id.dibsButton).setOnClickListener(onClickListener);
        findViewById(R.id.allergyButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.historyButton:


                case R.id.dibsButton:


                case R.id.allergyButton:
                    myStartActivity(CheckAllergyActivity.class);
                    break;

            }
        }
    };


    private void startToast(String msg){
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this,c);
        startActivity(intent);
    }
}
