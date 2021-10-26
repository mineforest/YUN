package com.example.poke;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class Recipe_Info extends AppCompatActivity {
    private Recipe_get rcp;
    private String recipe_id;
    private ImageView recipe_image;
    private TextView recipe_title_tv;
    private TextView recipe_time_tv;
    private ChipGroup chipGroup;
    private Chip chip;
    RecipeIngre_Adapter adapter;
    private DatabaseReference mDatabase;
    String uid;
    private Button doneButton;
    private ImageView backButton;
    private ImageView heartButton;
    ProgressDialog progressDialog;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        chipGroup = (ChipGroup)findViewById(R.id.tagGroup);
        doneButton = findViewById(R.id.doneButton);
        backButton = findViewById(R.id.backButton);
        heartButton = findViewById(R.id.heartButton);

        progressDialog = new ProgressDialog(Recipe_Info.this);

        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        progressDialog.setCancelable(false);

        doneButton.setOnClickListener(clickListener);
        backButton.setOnClickListener(backButtonClickListener);
        heartButton.setOnClickListener(heartClickListener);

        recipe_image = findViewById(R.id.rcpinfo_thumbnail);
        recipe_title_tv = findViewById(R.id.title_txt);
        recipe_time_tv = findViewById(R.id.timeText);

        Intent intent = getIntent();
        recipe_id = intent.getStringExtra("rcp_id");

        class StartRunnable implements Runnable{
                    @Override
                    public void run() {
                        try {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    DocumentReference docRef= db.collection("recipe").document(recipe_id);

                                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            rcp = documentSnapshot.toObject(Recipe_get.class);

                                            String[] string;
                                            for (String t : rcp.getTag()) {
                                                string = (t.split(","));
                                                for (String s : string) {
                                                    addChip(s);
                                                }
                                            }

                                            Glide.with(getApplicationContext()).load(rcp.getThumbnail()).into(recipe_image);
                                            recipe_title_tv.setText(rcp.getName());
                                            recipe_time_tv.setText("약 " + rcp.getTime() + "분");

                                            RecyclerView recyclerView = findViewById(R.id.ingre_recyclerView);
                                            recyclerView.setHasFixedSize(true);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                            adapter = new RecipeIngre_Adapter(rcp.getIngre_list());
                                            recyclerView.setAdapter(adapter);

                                            if (rcp.getSauce_list().isEmpty()) {
                                                LinearLayout linearLayout = findViewById(R.id.sauce_layout);
                                                linearLayout.setVisibility(View.INVISIBLE);
                                            } else {
                                                RecyclerView recyclerView2 = findViewById(R.id.sauce_recyclerView);
                                                recyclerView2.setHasFixedSize(true);
                                                recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                                adapter = new RecipeIngre_Adapter(rcp.getSauce_list());
                                                recyclerView2.setAdapter(adapter);
                                            }
                                        }
                                    });
                        }catch (Exception e){
                        }finally {
                            progressDialog.dismiss();
                        }
                    }
            }

        StartRunnable sr = new StartRunnable();
        Thread stop = new Thread(sr);
        stop.start();

        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        stop.interrupt();

    }

    View.OnClickListener backButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    View.OnClickListener heartClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
                        Snackbar.make(v, "찜 목록에서 삭제되었습니다.", Snackbar.LENGTH_LONG).show();
                    } else {
                        UserDibs userDibs = new UserDibs(rid, thumbnail, rtitle);
                        mDatabase.child("dips").child(uid).child(rid).setValue(userDibs);
                        Snackbar.make(v, "찜 목록에 추가되었습니다.", Snackbar.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), Recipe_step_Activity.class);
            ArrayList<String> ingres = new ArrayList<>();
            for(Map<String,String> ingre: rcp.getIngre_list()) {
                ingres.add(ingre.get("ingre_name"));
            }
            intent.putExtra("rcp",rcp);
            intent.putStringArrayListExtra("ingre",ingres);
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