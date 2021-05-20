package com.example.poke;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AllergyActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    public String uid;
    ArrayList<String> allergyList = new ArrayList<>();
    private ChipGroup chipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergy);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();

        chipGroup = findViewById(R.id.allergyOption);
        findViewById(R.id.nextButton3).setOnClickListener(nextClickListener);
        findViewById(R.id.previousButton3).setOnClickListener(preClickListener);
    }

    View.OnClickListener nextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for(int list : chipGroup.getCheckedChipIds()){
                Chip chip = findViewById(list);
                allergyList.add(chip.getText().toString());
                //mDatabase.child("allergy").child(uid).push().setValue(new UserAllergy(chip.getText().toString()));
            }
            myStartActivity(MainActivity.class);
        }
    };

    View.OnClickListener preClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}
