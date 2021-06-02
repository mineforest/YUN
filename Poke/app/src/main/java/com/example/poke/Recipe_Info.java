package com.example.poke;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import org.jetbrains.annotations.NotNull;

import static android.content.ContentValues.TAG;

public class Recipe_Info extends AppCompatActivity {
    private String Image;
    private String Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_info);
        String recipe;
        Intent intent = getIntent();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Image = intent.getStringExtra("Image");
        Title = intent.getStringExtra("Title");

        db.collection("recipe").get(Source.valueOf(Image)).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " =>" + document.getData());
                    }
                } else {
                    Log.w(TAG,"Error getting documents.", task.getException());
                }
            }
        });

    }
}
