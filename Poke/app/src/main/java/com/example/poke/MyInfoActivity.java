package com.example.poke;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.core.Context;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyInfoActivity extends Fragment {
    TextView allergyCountTextView;
    TextView historyCountTextView;
    TextView dibsCountTextView;
    TextView nickNameTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<UserHistory> historyList;

    private HistoryAdapter mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    String uid;
    private Context context;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet,container,false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        nickNameTextView=(TextView)view.findViewById(R.id.Nickname);
        dibsCountTextView = view.findViewById(R.id.dibsCountTextView);
        historyCountTextView = view.findViewById(R.id.historyCountTextView);
        allergyCountTextView = view.findViewById(R.id.allergyCountTextView);
        view.findViewById(R.id.historyButton).setOnClickListener(onClickListener);
        view.findViewById(R.id.dibsButton).setOnClickListener(onClickListener);
        view.findViewById(R.id.allergyButton).setOnClickListener(onClickListener);
        mDatabase.addValueEventListener(allergyListener);
        recyclerView = (RecyclerView)view.findViewById(R.id.history_rv);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        historyList = new ArrayList<>();
        mDatabase.addValueEventListener(userValueEventListener);
        mDatabase.child("history").child(uid).addChildEventListener(historyChildEventListener);
        mainAdapter = new HistoryAdapter(historyList);
        recyclerView.setAdapter(mainAdapter);

        return view;
    }

//        @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.my_info);
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        uid=user.getUid();
//
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        nickNameTextView=(TextView)findViewById(R.id.Nickname);
//        dibsCountTextView = findViewById(R.id.dibsCountTextView);
//        historyCountTextView = findViewById(R.id.historyCountTextView);
//        allergyCountTextView = findViewById(R.id.allergyCountTextView);
//        findViewById(R.id.historyButton).setOnClickListener(onClickListener);
//        findViewById(R.id.dibsButton).setOnClickListener(onClickListener);
//        findViewById(R.id.allergyButton).setOnClickListener(onClickListener);
//        mDatabase.addValueEventListener(allergyListener);
//        recyclerView = (RecyclerView)findViewById(R.id.history_rv);
//        recyclerView.setHasFixedSize(true);
//        linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        historyList = new ArrayList<>();
//        mDatabase.addValueEventListener(userValueEventListener);
//        mDatabase.child("history").child(uid).addChildEventListener(historyChildEventListener);
//        mainAdapter = new HistoryAdapter(historyList);
//        recyclerView.setAdapter(mainAdapter);
//    }


    ValueEventListener allergyListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            allergyCountTextView.setText(Long.toString(dataSnapshot.child("allergy").child(uid).getChildrenCount()));
            dibsCountTextView.setText(Long.toString(dataSnapshot.child("dibs").child(uid).getChildrenCount()));
            historyCountTextView.setText(Long.toString(dataSnapshot.child("history").child(uid).getChildrenCount()));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ValueEventListener userValueEventListener = new ValueEventListener() {
        private ArrayList<String> userInfoArrayList = new ArrayList<>();

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot dataSnapshot : snapshot.child("users").child(uid).getChildren()) {
                userInfoArrayList.add(dataSnapshot.getValue().toString());
            }
                nickNameTextView.setText(userInfoArrayList.get(3));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    ChildEventListener historyChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            UserHistory history = snapshot.getValue(UserHistory.class);
            historyList.add(new UserHistory(history.getRecipeTitle(), history.getRecipeImage(), history.getDate(), history.getRate()));
            mainAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.historyButton:
                    myStartActivity(HistoryFragment.class);
                break;

                case R.id.dibsButton:
                break;

                case R.id.allergyButton:
                    myStartActivity(CheckAllergyActivity.class);
                    break;

            }
        }
    };


    private void startToast(String msg){
        Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(getActivity(),c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
