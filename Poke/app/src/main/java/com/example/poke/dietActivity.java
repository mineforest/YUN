package com.example.poke;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class dietActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    public String uid;

    ArrayList<UserDiet> dietList= new ArrayList<UserDiet>();
    String d[] = {"고기좋아","고기싫어"};

    private ChipGroup chipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();

        chipGroup = findViewById(R.id.cgOption);

        for(int i =0; i<d.length; i++){
            Chip chip = new Chip(this);
            ChipDrawable drawable = ChipDrawable.createFromAttributes(this, null, 0,
                    R.style.Widget_MaterialComponents_Chip_Choice);
            chip.setChipDrawable(drawable);

            chip.setPadding(10,10,10,10);
            chip.setText(d[i]);

            chipGroup.addView(chip);

        }
    }

}
