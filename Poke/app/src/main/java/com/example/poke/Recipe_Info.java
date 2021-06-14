package com.example.poke;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

import android.widget.CheckBox;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Recipe_Info extends AppCompatActivity {
    private Recipe_get rcp;
    private String recipe_id;
    private ImageView recipe_image;
    private TextView recipe_title;
    private TextView recipe_tag;
    private TextView recipe_time;
    private ChipGroup chipGroup;
    private Chip chip;
    private MaterialToolbar toolbar;
    RecipeIngre_Adapter adapter;
    RecipeStep_Adapter adapter2;
    private DatabaseReference mDatabase;
    private ArrayList ingreList = new ArrayList<>();
    String uid;
    private Button doneButton;
    private TextView avg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_info);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

//        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        chipGroup = (ChipGroup)findViewById(R.id.tagGroup);
        doneButton = findViewById(R.id.doneButton);
        toolbar = (MaterialToolbar) findViewById(R.id.topAppBarr);
        toolbar.inflateMenu(R.menu.top_app_bar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_ios_new_white_24dp);

        doneButton.setOnClickListener(clickListener);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                mDatabase = FirebaseDatabase.getInstance().getReference();
                if (user != null)
                    uid = user.getUid();
                String rid = rcp.getId();
                String thumbnail = rcp.getThumbnail();
                String rtitle = rcp.getName();

                mDatabase.child("dips").child(uid).orderByChild("rcp_id").equalTo(rid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot != null && snapshot.getChildren() != null && snapshot.getChildren().iterator().hasNext()) {
                            mDatabase.child("dips").child(uid).child(rid).removeValue();
                            Snackbar.make(findViewById(R.id.topAppBarr), "찜 목록에서 삭제되었습니다.", Snackbar.LENGTH_LONG).show();
                        } else {
                            UserDibs userDibs = new UserDibs(rid, thumbnail, rtitle);
                            mDatabase.child("dips").child(uid).child(rid).setValue(userDibs);
                            Snackbar.make(findViewById(R.id.topAppBarr), "찜 목록에 추가되었습니다.", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                return false;
            }
        });

        recipe_image = findViewById(R.id.rcpinfo_thumbnail);
        recipe_title = findViewById(R.id.title_txt);
       // recipe_tag = findViewById(R.id.tag_txt);
        recipe_time = findViewById(R.id.timeText);
       // avg = findViewById(R.id.average);

        Intent intent = getIntent();
        recipe_id = intent.getStringExtra("rcp_id");
        if (recipe_id == null) {
            Log.d("error","errorror");
        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CountDownLatch done = new CountDownLatch(1);

            DocumentReference docRef = db.collection("recipe").document(recipe_id);

            done.countDown();

            try {
                done.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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

                    rcp = new Recipe_get(recipe_id, title, thumbnail, url, ingredient_ids, cook_time, ingre_list, sauce_list, recipe_list, recipe_img, tags);

                    String[] string;
                    for(String t : tags){
                        string = (t.split(","));
                        for(String s : string){
                            addChip(s);
                        }
                    }

                    Glide.with(getApplicationContext()).load(rcp.getThumbnail()).into(recipe_image);
                    recipe_title.setText(rcp.getName());
//                    recipe_tag.setText(rcp.getTag().toString());
                    recipe_time.setText("약 " + rcp.getTime() + "분");

                    RecyclerView recyclerView = findViewById(R.id.ingre_recyclerView);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new RecipeIngre_Adapter(rcp.getIngre_list());
                    recyclerView.setAdapter(adapter);

                    RecyclerView recyclerView2 = findViewById(R.id.sauce_recyclerView);
                    recyclerView2.setHasFixedSize(true);
                    recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new RecipeIngre_Adapter(rcp.getSauce_list());
                    recyclerView2.setAdapter(adapter);

                    RecyclerView recyclerView3 = findViewById(R.id.recipe_recyclerView);
                    recyclerView3.setHasFixedSize(true);
                    recyclerView3.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter2 = new RecipeStep_Adapter(rcp.getRecipe_img(), rcp.getRecipe());
                    recyclerView3.setAdapter(adapter2);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ddddddddddddd", "실패~");
                        }
                    });
        }
    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
//                finish();
//                return true;
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ArrayList<String> al = new ArrayList<>();
            ArrayList<String> recipeList = new ArrayList<>();
            Intent intent = new Intent(getApplicationContext(), RatingActivity.class);

            recipeList.add(rcp.getId());
            recipeList.add(rcp.getName());
            recipeList.add(rcp.getThumbnail());

            for(int i=0; i<rcp.getIngre_list().size(); i++) {
              al.add(rcp.getIngre_list().get(i).get("ingre_name"));
            }
            intent.putStringArrayListExtra("ingre",al);
            intent.putStringArrayListExtra("recipe",recipeList);
            startActivity(intent);
        }
    };

    public void addChip(String text){
        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 15,
                getResources().getDisplayMetrics()
        );
        chip =(Chip) this.getLayoutInflater().inflate(R.layout.tag_chip, null, false);
        chip.setText(text);
        chip.setClickable(false);
        chip.setPadding(paddingDp, 0, paddingDp, 0);
        chip.setCheckable(false);
        chipGroup.addView(chip);
    }
}