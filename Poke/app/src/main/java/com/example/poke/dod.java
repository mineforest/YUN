package com.example.poke;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class dod extends AppCompatActivity {
    TextView tv1;
    TextView tv2;
    TextView tv3;
    private DatabaseReference mDatabase;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historydd);

        tv1 = (EditText) findViewById(R.id.titleTextView);
        tv2 = (EditText) findViewById(R.id.dateTextView);
        tv3 = (EditText) findViewById(R.id.idTextView);
        findViewById(R.id.okButton).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            String uid = user.getUid();

             String title = tv1.getText().toString();
            String date = tv2.getText().toString();
            int id = Integer.parseInt(tv3.getText().toString());

            UserHistory userHistory = new UserHistory(title, id, date );
            mDatabase.child("history").child(uid).push().setValue(userHistory);
        }
    };
}
