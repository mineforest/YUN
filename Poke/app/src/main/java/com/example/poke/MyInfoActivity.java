package com.example.poke;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class MyInfoActivity extends Fragment implements View.OnClickListener{
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

    private FragmentManager fragmentManager;

    static HistoryFragment historyFragment;
    static WishFragment wishFragment;



    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_info,container,false);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        historyFragment = new HistoryFragment();
        wishFragment = new WishFragment();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        nickNameTextView=(TextView)view.findViewById(R.id.Nickname);
        dibsCountTextView = view.findViewById(R.id.dibsCountTextView);
        historyCountTextView = view.findViewById(R.id.historyCountTextView);
        allergyCountTextView = view.findViewById(R.id.allergyCountTextView);
        Button historyButton = (Button) view.findViewById(R.id.historyButton);
        Button dipsButton = (Button) view.findViewById(R.id.dibsButton);
        Button allergyButton = (Button) view.findViewById(R.id.allergyButton);
        historyButton.setOnClickListener(this);
        dipsButton.setOnClickListener(this);
        allergyButton.setOnClickListener(this);
        mDatabase.addValueEventListener(allergyListener);

        recyclerView = (RecyclerView)view.findViewById(R.id.history_rv);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        historyList = new ArrayList<>();
        mDatabase.addValueEventListener(userValueEventListener);
        mainAdapter = new HistoryAdapter(historyList);
        recyclerView.setAdapter(mainAdapter);

        return view;
    }

    ValueEventListener allergyListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            allergyCountTextView.setText(Long.toString(dataSnapshot.child("allergy").child(uid).getChildrenCount()));
            dibsCountTextView.setText(Long.toString(dataSnapshot.child("dips").child(uid).getChildrenCount()));
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


    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (v.getId()) {
            case R.id.historyButton:
                fragmentTransaction.replace(R.id.InfoFrame,new HistoryFragment()).commit();
                break;
            case R.id.dibsButton:
                fragmentTransaction.replace(R.id.InfoFrame,new DipsFragment()).commit();
                break;
            case R.id.allergyButton:
                fragmentTransaction.replace(R.id.InfoFrame,new AllergyFragment()).commit();
                break;
        }
    }



    private void startToast(String msg){
        Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();
    }


}
