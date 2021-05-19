package com.example.poke;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

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

        findViewById(R.id.toggleButton2).setOnClickListener(onClickListener);
        findViewById(R.id.toggleButton3).setOnClickListener(onClickListener);
        findViewById(R.id.toggleButton4).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           Log.d("tag",v.getContext().toString());

        }
    };
//
//    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            if(isChecked){
//                preferencesList.add(new UserPreference(buttonView.getText().toString()));
//            }
//
//            else{
//                preferencesList.remove(new UserPreference(buttonView.getText().toString()));
//            }
//
//        }
//    };

private void startToast(String msg){
    Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
}
}
