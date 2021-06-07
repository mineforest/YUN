package com.example.poke;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RatingActivity extends AppCompatActivity {
    private SearchView searchView;
    private Button doneButton;
    private RatingBar ratingBar;
    private ChipGroup chipGroup;
    private Chip chip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

//        searchView = findViewById(R.id.searchView);
        doneButton = findViewById(R.id.doneButton2);
        ratingBar = findViewById(R.id.ratingBar2);
        chipGroup = findViewById(R.id.ratingGroup);
        Intent intent =getIntent();

        ArrayList<String> arrayList = intent.getStringArrayListExtra("list");
            if(arrayList == null)
                Log.d("null", "null");
//                addChip(arrayList);
        }

    public void addChip(ArrayList<String> al){
        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics()
        );

        for(int i =0; i<al.size(); i++){
            chip =(Chip) this.getLayoutInflater().inflate(R.layout.history_chip, null, false);
            chip.setText(al.get(i));
            chip.setChecked(true);
            chip.setPadding(paddingDp, 0, paddingDp, 0);
            chipGroup.addView(chip);
        }
    }
}
