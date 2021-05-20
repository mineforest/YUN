package com.example.poke;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private ArrayList<UserHistory> historyList;
    private HistoryAdapter mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    ArrayList<TextView> tli;
    private DatabaseReference mDatabase;
    String uid;
    TextView tv1;
    TextView tv2;
    TextView tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historydd);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        tv1 = (EditText) findViewById(R.id.titleTextView);
        tv2 = (EditText) findViewById(R.id.dateTextView);
        tv3 = (EditText) findViewById(R.id.idTextView);

//        recyclerView = (RecyclerView)findViewById(R.id.history_rv);
//        linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        historyList = new ArrayList<>();
//
//        mainAdapter = new HistoryAdapter(historyList);
//
//        mainAdapter.notifyDataSetChanged();
//        recyclerView.setAdapter(mainAdapter);
        tv1.setText(historyList.get(0).getRecipeTitle());
        tv2.setText(historyList.get(0).getDate());
        tv3.setText(historyList.get(0).getRecipeID());

        //mDatabase.child("history").child(uid).addChildEventListener(childEventListener);
        mDatabase.addValueEventListener(allergyListener);
    }

    ValueEventListener allergyListener = new ValueEventListener() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            int i=0;

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.child("history").child(uid).getChildren()) {
                tli.get(i).setText(snapshot.getValue().toString());
                i++;
                }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
   };
//
//    ChildEventListener childEventListener = new ChildEventListener() {
//        @Override
//        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//            UserHistory history = snapshot.getValue(UserHistory.class);
//            historyList.add(new UserHistory(history.getRecipeTitle(),  history.getRecipeID(), history.getDate()));
//
//        }
//
//        @Override
//        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//        }
//
//        @Override
//        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//        }
//
//        @Override
//        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//    };

}
