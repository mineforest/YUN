package com.example.poke;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class CheckAllergyActivity extends AppCompatActivity {
    private static final String Tag = "CheckAllergyActivity";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_allergy);
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(allergyListener);
    }

    ValueEventListener allergyListener = new ValueEventListener() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            int i=1;
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.child("users").child(uid).child("preference").getChildren()) {
                Log.d(Tag,i+" "+snapshot.getValue().toString());
                i++;
                //((TextView)findViewById(R.id.textView+i)).setText(snapshot.getValue().toString());
                }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}


