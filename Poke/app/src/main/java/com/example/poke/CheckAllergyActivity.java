package com.example.poke;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CheckAllergyActivity extends AppCompatActivity {
    private static final String Tag = "CheckAllergyActivity";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;

    ArrayList<TextView> textList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_allergy);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        textView1 = (TextView)findViewById(R.id.textView1);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView3 = (TextView)findViewById(R.id.textView3);
        textView4 = (TextView)findViewById(R.id.textView4);
        textList.add(textView1);
        textList.add(textView2);
        textList.add(textView3);
        textList.add(textView4);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("allergy").child(uid).addChildEventListener(allergyListener);
        //mDatabase.addValueEventListener(allergyListener);
    }
//
//    ValueEventListener allergyListener = new ValueEventListener() {
//            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//            String uid = user.getUid();
//            int i=0;
//
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            for (DataSnapshot snapshot : dataSnapshot.child("preference").child(uid).getChildren()) {
//                textList.get(i).setText(snapshot.getValue().toString());
//                i++;
//                }
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//    };

    ChildEventListener allergyListener = new ChildEventListener() {

            int i=0;
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            UserAllergy allergy = snapshot.getValue(UserAllergy.class);
            textList.get(i).setText(allergy.getAllergy());
            //textList.get(i).setText(snapshot.child("preference").child(uid).getChildren().toString());
            i++;

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

}


