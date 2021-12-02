package com.example.poke;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RatingActivity extends AppCompatActivity {
    private Recipe_get rcp;
    private SearchView searchView;
    private Button doneButton;
    private ArrayList<String> ingreList;
    private List dbList;
    private ArrayList<String> dbArrayList;
    private ArrayList<String> deleteList;
    private ListView listView;
    private IngredientListAdapter adapter;
    private DatabaseReference mDatabase;
    private String uid;
    private DassnIngreCheckAdapter dassnIngreCheckAdapter;
    private DassnIngreCheckAdapter trash_adapter;
    private RecyclerView recyclerView;
    private RecyclerView trash_rv;
    int cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        rcp = intent.getParcelableExtra("rcp");
        ingreList = intent.getStringArrayListExtra("ingre");

        cnt=0;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uid = user.getUid();
        dbList = new ArrayList<String>();
        dbArrayList = new ArrayList<>();
        deleteList = new ArrayList<>();
        searchView =findViewById(R.id.searchView);
        doneButton = findViewById(R.id.doneButton2);
        listView = findViewById(R.id.listView);
        recyclerView = findViewById(R.id.dassn_rv);
        trash_rv = findViewById(R.id.trash_rv);
        searchView.setQueryHint("재료 검색");
        searchView.onActionViewExpanded();
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(true);
        searchView.clearFocus();
        searchView.setQuery(null,false);
        searchView.setOnQueryTextListener(onQueryTextListener);

        mDatabase.child("ingredient").child(uid).addChildEventListener(childEventListener);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        dassnIngreCheckAdapter = new DassnIngreCheckAdapter(ingreList);
        recyclerView.setAdapter(dassnIngreCheckAdapter);

        trash_rv.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(RecyclerView.VERTICAL);
        trash_rv.setLayoutManager(linearLayoutManager2);
        trash_adapter = new DassnIngreCheckAdapter(deleteList);
        trash_rv.setAdapter(trash_adapter);

        dassnIngreCheckAdapter.setOnItemClickListener(new DassnIngreCheckAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String ingre_name = ingreList.get(position);
                deleteList.add(0,ingre_name);
                ingreList.remove(ingre_name);
                dassnIngreCheckAdapter.notifyItemRemoved(position);
                trash_adapter.notifyDataSetChanged();
            }
        });

        trash_adapter.setOnItemClickListener(new DassnIngreCheckAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String ingre_name = deleteList.get(position);
                ingreList.add(0, ingre_name);
                deleteList.remove(ingre_name);
                trash_adapter.notifyItemRemoved(position);
                dassnIngreCheckAdapter.notifyDataSetChanged();
            }
        });

        adapter = new IngredientListAdapter(dbList, this);
        listView.setAdapter(adapter);
        listView.bringToFront();
        listView.setOnItemClickListener(itemClickListener);
        doneButton.setOnClickListener(clickListener);
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(!ingreList.contains(dbList.get(position).toString()) & !deleteList.contains(dbList.get(position).toString())) {
                ingreList.add(dbList.get(position).toString());
                dassnIngreCheckAdapter.notifyDataSetChanged();
                searchView.setQuery(null,false);
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
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("delList",deleteList);
                bundle.putParcelable("rcp",rcp);
                final RatingBottomSheetFrag ratingBottomSheetFrag = new RatingBottomSheetFrag(getApplicationContext());
                ratingBottomSheetFrag.setArguments(bundle);
                ratingBottomSheetFrag.show(getSupportFragmentManager(),null);
            }
        };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
