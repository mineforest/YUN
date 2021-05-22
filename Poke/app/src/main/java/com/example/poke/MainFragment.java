package com.example.poke;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainFragment extends AppCompatActivity implements AllergyFragment.OnMyListener{
    private FragmentManager fragmentManager;
    private Button preButton;
    private Button nextButton;
    private TextView text;
    private int i = 1;
    private Fragment pre, diet, allergy;
    private ChipGroup preGroup, dietGroup, allergyGroup;
    private DatabaseReference mDatabase;
    private String uid;
    private ArrayList<String> preList, dietList, allergyList = new ArrayList<>();
    Bundle bundle;
    LayoutInflater layoutInflater;
    View header;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_main_fragment);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        preButton = findViewById(R.id.fragmentPreviousButton);
        nextButton = findViewById(R.id.fragmentNextButton);
        text = findViewById(R.id.ageTextView);
        nextButton.setOnClickListener(nextClickListener);
        preButton.setOnClickListener(preClickListener);
        preGroup = findViewById(R.id.preOption);
        dietGroup = findViewById(R.id.dietOption);
        allergyGroup = findViewById(R.id.allergyOption);
        bundle = new Bundle();
        pre = new PreferenceFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container2, pre).commit();
        preButton.setText(" ");
        nextButton.setText("다음");
        text.setText("1 / 3");
    }

View.OnClickListener nextClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
                   next(i);
    }
};

    View.OnClickListener preClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                pre(i);
        }
    };

private void next(int fragment){
                switch(fragment){
                    case 1:
                        if(diet == null){
                            diet = new DietFragment();
                            fragmentManager.beginTransaction().add(R.id.frame_container2,diet).commit();
                        }
                        layoutInflater = getLayoutInflater();
                        header = layoutInflater.inflate(R.layout.preference,null);
                        preGroup = header.findViewById(R.id.preOption);

                        for(int list : preGroup.getCheckedChipIds()){
                            Chip chip = findViewById(list);
                            preList.add(chip.getText().toString());
                            //mDatabase.child("preference").child(uid).push().setValue(new UserPreference(chip.getText().toString()));
                        }
                        bundle.putStringArrayList("preference", preList);
                        pre.setArguments(bundle);

                        i=2;
                        preButton.setText("이전");
                        nextButton.setText("다음");
                        text.setText("2 / 3");
                        if(pre != null) fragmentManager.beginTransaction().hide(pre).commit();
                        if(diet != null) fragmentManager.beginTransaction().show(diet).commit();
                        if(allergy != null) fragmentManager.beginTransaction().hide(allergy).commit();
                        break;

                    case 2:
                        if(allergy == null){
                            allergy = new AllergyFragment();
                            fragmentManager.beginTransaction().add(R.id.frame_container2,allergy).commit();
                        }
                        layoutInflater = getLayoutInflater();
                        header = layoutInflater.inflate(R.layout.activity_diet,null);
                        dietGroup = header.findViewById(R.id.dietOption);

                        for(int list : dietGroup.getCheckedChipIds()){
                            Chip chip = findViewById(list);
                            dietList.add(chip.getText().toString());
                            //mDatabase.child("preference").child(uid).push().setValue(new UserPreference(chip.getText().toString()));
                        }
                        bundle.putStringArrayList("diet", dietList);
                        diet.setArguments(bundle);

                        i=3;
                        preButton.setText("이전");
                        nextButton.setText("완료");
                        text.setText("3 / 3");
                        if(pre != null) fragmentManager.beginTransaction().hide(pre).commit();
                        if(diet != null) fragmentManager.beginTransaction().hide(diet).commit();
                        if(allergy != null) fragmentManager.beginTransaction().show(allergy).commit();
                        break;

                    case 3:
                      //  Log.d("pre", preList.get(0));
                       // Log.d("diet", dietList.get(0));
                        myStartActivity(MainActivity.class);

                }
}

private void pre(int fragment){
            switch (fragment){
                case 2:
                    if(pre == null){
                        pre = new PreferenceFragment();
                        fragmentManager.beginTransaction().add(R.id.frame_container2,pre).commit();
                    }
                    i=1;
                    nextButton.setText("다음");
                    text.setText("1 / 3");
                    preButton.setText(" ");

                    if(pre != null) fragmentManager.beginTransaction().show(pre).commit();
                    if(diet != null) fragmentManager.beginTransaction().hide(diet).commit();
                    if(allergy != null) fragmentManager.beginTransaction().hide(allergy).commit();
                    break;

                case 3:
                    if(diet == null){
                        diet = new DietFragment();
                        fragmentManager.beginTransaction().add(R.id.frame_container2,diet).commit();
                    }

                    i=2;
                    preButton.setText("이전");
                    nextButton.setText("다음");
                    text.setText("2 / 3");
                    if(pre != null) fragmentManager.beginTransaction().hide(pre).commit();
                    if(diet != null) fragmentManager.beginTransaction().show(diet).commit();
                    if(allergy != null) fragmentManager.beginTransaction().hide(allergy).commit();
                    break;
            }
        }

    @Override
    public void onReceivedData(ArrayList pre, ArrayList diet) {

    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

}
