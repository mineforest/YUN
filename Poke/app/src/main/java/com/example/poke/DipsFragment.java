package com.example.poke;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DipsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private ArrayList<UserDibs> dibsList;
    private FirebaseAuth mAuth;
    String uid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dips, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
          uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) view.findViewById(R.id.dips_rv);
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
        dibsList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();



        mDatabase.child("dips").child(uid).addChildEventListener(dipsChildEventListener);

        adapter = new DipsAdapter(dibsList);
        recyclerView.setAdapter(adapter);

        return view;

    }

    ChildEventListener dipsChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            UserDibs dibs = snapshot.getValue(UserDibs.class);
            dibsList.add(new UserDibs(dibs.getDipsImage(),dibs.getDipsTitle()));
            adapter.notifyDataSetChanged();
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



}

