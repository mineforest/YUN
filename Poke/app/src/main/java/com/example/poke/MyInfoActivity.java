package com.example.poke;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyInfoActivity extends Fragment implements View.OnClickListener{
    TextView nickNameTextView;
    ImageView profileView;
    private DatabaseReference mDatabase;
    private ArrayList<UserHistory> historyList;
    private HistoryAdapter mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    String uid;
    Button historyButton;
    Button dipsButton;
    Button allergyButton;
    static HistoryFragment historyFragment;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main,container,false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
            uid = user.getUid();

        historyFragment = new HistoryFragment();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        profileView=(ImageView)view.findViewById(R.id.Profileimage);
        nickNameTextView=(TextView)view.findViewById(R.id.Nickname);
        historyButton = (Button) view.findViewById(R.id.historyButton);
        dipsButton = (Button) view.findViewById(R.id.dibsButton);
        allergyButton = (Button) view.findViewById(R.id.allergyButton);
        historyButton.setOnClickListener(this);
        dipsButton.setOnClickListener(this);
        allergyButton.setOnClickListener(this);
        historyButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_shape));
        mDatabase.addValueEventListener(allergyListener);

       getChildFragmentManager().beginTransaction().add(R.id.InfoFrame,new HistoryFragment()).commit();
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
            historyButton.setText("평가한 요리\n"+Long.toString(dataSnapshot.child("allergy").child(uid).getChildrenCount()));
            dipsButton.setText("찜한요리\n"+Long.toString(dataSnapshot.child("dips").child(uid).getChildrenCount()));
            allergyButton.setText("알러지/기피\n"+Long.toString(dataSnapshot.child("history").child(uid).getChildrenCount()));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ValueEventListener userValueEventListener = new ValueEventListener() {
        private ArrayList<String> userInfoArrayList = new ArrayList<>();

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.child("users").child(uid).getChildren()) {
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
                historyButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_shape));
                allergyButton.setBackgroundColor(Color.parseColor("#00ff0000"));
                dipsButton.setBackgroundColor(Color.parseColor("#00ff0000"));
                break;
            case R.id.dibsButton:
                fragmentTransaction.replace(R.id.InfoFrame,new DipsFragment()).commit();
                historyButton.setBackgroundColor(Color.parseColor("#00ff0000"));
                allergyButton.setBackgroundColor(Color.parseColor("#00ff0000"));
                dipsButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_shape));
                break;
            case R.id.allergyButton:
                fragmentTransaction.replace(R.id.InfoFrame,new Menu()).commit();
                historyButton.setBackgroundColor(Color.parseColor("#00ff0000"));
                allergyButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_shape));
                dipsButton.setBackgroundColor(Color.parseColor("#00ff0000"));
                break;
        }
    }

    private void startToast(String msg){
        Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();
    }


}
