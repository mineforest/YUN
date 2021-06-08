package com.example.poke;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RatingActivity extends AppCompatActivity {
    private SearchView searchView;
    private Button doneButton;
    private RatingBar ratingBar;
    private ChipGroup chipGroup;
    private Chip chip;
    private ArrayList<String> arrayList;
    private ArrayList<String> recipeList;
    private List dbList;
    private ArrayList<String> dbArrayList;
    private ArrayList<String> chipArrayList;
    private ListView listView;
    private IngredientListAdapter adapter;
    private UserHistory userHistory;
    private SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Date date;
    private String now;
    private String uid;
    int cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Intent intent = getIntent();
        cnt=0;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uid = user.getUid();
        arrayList = new ArrayList<>();
        userHistory = new UserHistory();
        recipeList = new ArrayList<>();
        chipArrayList = new ArrayList<>();
        dbList = new ArrayList<String>();
        dbArrayList = new ArrayList<>();
        searchView =(SearchView)findViewById(R.id.searchView);
        doneButton =(Button) findViewById(R.id.doneButton2);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar2);
        chipGroup = (ChipGroup)findViewById(R.id.ratingGroup);
        listView = (ListView)findViewById(R.id.listView);

        arrayList.addAll(intent.getStringArrayListExtra("ingre"));
        recipeList.addAll(intent.getStringArrayListExtra("recipe"));
        searchView.setQueryHint("재료 검색");
        searchView.setIconified(false);
        searchView.onActionViewExpanded();
        searchView.setQuery(null,false);
        searchView.setOnQueryTextListener(onQueryTextListener);

        mDatabase.child("ingredient").child(uid).addChildEventListener(childEventListener);

        adapter = new IngredientListAdapter(dbList, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemClickListener);
        doneButton.setOnClickListener(clickListener);
        for(int i =0; i<arrayList.size(); i++) {
            addChip(arrayList.get(i));
        }
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(!chipArrayList.contains(dbList.get(position).toString())) {
                addChip(dbList.get(position).toString());
            }
        }
    };

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            UserIngredient ingredient = snapshot.getValue(UserIngredient.class);
            dbArrayList.add(ingredient.getIngredientTitle());
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        }
        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        }
        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)  {}
        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if(newText.length()==0){
                dbList.clear();
                adapter.notifyDataSetChanged();
                return false;
            }
            dbList.clear();
            cnt=0;
            if(cnt<7) {
                for (int i = 0; i < dbArrayList.size(); i++) {
                    if (dbArrayList.get(i).toLowerCase().contains(newText.toLowerCase())) {
                        dbList.add(dbArrayList.get(i));
                        cnt++;
                    }
                }
            }
            adapter.notifyDataSetChanged();
            return false;
        }
    };

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = new Date();
                now = form.format(date);
                long l = (long)ratingBar.getRating();
                userHistory = new UserHistory(recipeList.get(0), recipeList.get(1), recipeList.get(2), now, l);
                mDatabase.child("history").child(uid).push().setValue(userHistory);
                deleteIngredient();
                onBackPressed();
            }
        };

    public void addChip(String text){
        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 15,
                getResources().getDisplayMetrics()
        );
            chip =(Chip) this.getLayoutInflater().inflate(R.layout.history_chip, null, false);
            chip.setText(text);
            chipArrayList.add(text);
            chip.setPadding(paddingDp, 0, paddingDp, 0);
            chip.setCheckable(false);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleChipCloseIconClicked((Chip)v);
                }
            });
            chipGroup.addView(chip);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void handleChipCloseIconClicked(Chip chip) {
        ChipGroup parent = (ChipGroup) chip.getParent();
        for(int i =0; i<chipArrayList.size(); i++){
            if(chipArrayList.get(i).equals(chip.getText().toString()))
                chipArrayList.remove(i);
        }
        parent.removeView(chip);
    }

    public void deleteIngredient() {
        mDatabase.child("ingredient").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (chipArrayList.contains(dataSnapshot.child("ingredientTitle").getValue(String.class))) {
                            dataSnapshot.getRef().removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
