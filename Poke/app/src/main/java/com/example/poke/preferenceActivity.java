package com.example.poke;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class preferenceActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    public String uid;

    ArrayList<UserPreference>  preferencesList= new ArrayList<UserPreference>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();
        findViewById(R.id.nextButton).setOnClickListener(onClickListener);
        Chip c = findViewById(R.id.chip1);
        Chip c1 = findViewById(R.id.chip2);
        Chip c2 = findViewById(R.id.chip3);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

//    public void getChipGroupValues(){
//        nextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                for (int i=0; i<chipGroup.getChildCount();i++){
//                    Chip chip = (Chip)chipGroup.getChildAt(i);
//                    Log.i("outside if ", i+ " chip = " + chip.getText().toString());
//                    if (chip.isChecked()){
//                        Log.i("inside if ", i+ " chip = " + chip.getText().toString());
//                        textView.setText(chip.getText().toString());
//                    }
//                }
//            }
//        });
//    }

private void startToast(String msg){
    Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
}
}
