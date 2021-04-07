package com.example.tcnk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.os.Bundle;
import android.view.View;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity {
    TextInputLayout TIL;
    AppCompatEditText TIE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TIL =(TextInputLayout) findViewById(R.id.TIL);
        TIE = (AppCompatEditText) findViewById(R.id.TIE);

        TIL.setCounterEnabled(true);
        TIL.setCounterMaxLength(100);

//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
//        StorageReference pathReference = storageRef.child("ima/다운로드.jpg");
    }

    public void click(View view){
        String data = TIE.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.setValue(data);
    }

}