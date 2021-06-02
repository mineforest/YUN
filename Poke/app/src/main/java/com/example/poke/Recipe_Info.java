package com.example.poke;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static android.content.ContentValues.TAG;

public class Recipe_Info extends AppCompatActivity {
    private Recipe_get  rcp;
    private String recipe_id;
    private ImageView recipe_image;
    private TextView recipe_title;
    private TextView recipe_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_info);

        recipe_image = findViewById(R.id.rcpinfo_thumbnail);
        recipe_title = findViewById(R.id.title_txt);
        recipe_tag = findViewById(R.id.tag_txt);

        Intent intent = getIntent();
        recipe_id = intent.getStringExtra("rcp_id");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("recipe").document(recipe_id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String title = documentSnapshot.getData().get("name").toString();
                String thumbnail = documentSnapshot.getData().get("thumbnail").toString();
                String cook_time = documentSnapshot.getData().get("time").toString();
                List<Map<String, String>> ingre_list = (List<Map<String, String>>) documentSnapshot.get("ingre_list");
                List<Map<String, String>> sauce_list = (List<Map<String, String>>) documentSnapshot.get("sauce_list");
                String url = documentSnapshot.getData().get("url").toString();
                List<Long> ingredient_ids = (List<Long>) documentSnapshot.get("ingredient_ids");
                List<String> recipe_list = (List<String>) documentSnapshot.get("recipe");
                List<String> recipe_img = (List<String>) documentSnapshot.get("recipe_img");
                List<String> tags = (List<String>) documentSnapshot.get("tag");

                rcp = new Recipe_get(recipe_id, title, thumbnail,  url, ingredient_ids, cook_time, ingre_list, sauce_list, recipe_list, recipe_img, tags);

                Glide.with(getApplicationContext()).load(rcp.getThumbnail()).into(recipe_image);
                recipe_title.setText(rcp.getName());
                recipe_tag.setText(rcp.getTag().toString());
            }
        });

    }
}
