package com.example.poke;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyInfoActivity extends AppCompatActivity {
    TextView allergyCountTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_info);
        mAuth = FirebaseAuth.getInstance();

        allergyCountTextView = findViewById(R.id.allergyCountTextView);
        findViewById(R.id.historyButton).setOnClickListener(onClickListener);
        findViewById(R.id.dibsButton).setOnClickListener(onClickListener);
        findViewById(R.id.allergyButton).setOnClickListener(onClickListener);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(allergyListener);
    }

    ValueEventListener allergyListener = new ValueEventListener() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            allergyCountTextView.setText(Long.toString(dataSnapshot.child("preference").child(uid).getChildrenCount()));

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

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
