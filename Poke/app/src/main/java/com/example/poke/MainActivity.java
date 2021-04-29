package com.example.poke;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //로그인 되어 있지 않으면 로그인 화면으로
        if( FirebaseAuth.getInstance().getCurrentUser() == null) {
            startLoginActivity();
        }

        findViewById(R.id.logoutButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.logoutButton:
                    FirebaseAuth.getInstance().signOut();
                    startLoginActivity();
                    break;
            }
        }
    };

    private void startLoginActivity() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

}