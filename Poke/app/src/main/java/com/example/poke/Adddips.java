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

public class Adddips extends AppCompatActivity {
    TextView tv1;
    TextView tv2;
    private DatabaseReference mDatabase;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_dips);

        tv1 = (EditText) findViewById(R.id.dipsimageView);
        tv2 = (EditText) findViewById(R.id.dipstitleview);
        findViewById(R.id.pushbutton).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            String uid = user.getUid();

            String image = tv1.getText().toString();
            String title = tv2.getText().toString();
            UserDibs userDibs = new UserDibs(image, title);
            mDatabase.child("dips").child(uid).push().setValue(userDibs);
        }
    };
}
