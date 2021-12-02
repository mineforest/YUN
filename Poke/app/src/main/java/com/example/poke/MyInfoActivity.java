package com.example.poke;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
        View view = inflater.inflate(R.layout.activity_myinfo,container,false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
            uid = user.getUid();

        historyFragment = new HistoryFragment();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        profileView = view.findViewById(R.id.Profileimage);
        nickNameTextView = view.findViewById(R.id.Nickname);
        historyButton = view.findViewById(R.id.historyButton);
        dipsButton = view.findViewById(R.id.dibsButton);
        allergyButton = view.findViewById(R.id.allergyButton);
        historyButton.setOnClickListener(this);
        dipsButton.setOnClickListener(this);
        allergyButton.setOnClickListener(this);
        historyButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.info_btn));
        mDatabase.addValueEventListener(allergyListener);

        getChildFragmentManager().beginTransaction().add(R.id.InfoFrame,new HistoryFragment()).commit();

        DatabaseReference myRef = database.getReference("users");

        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo info = dataSnapshot.getValue(UserInfo.class);
                try{
                    String nickname = info.getNickName();
                    nickNameTextView.setText(nickname);
                }catch(NullPointerException e){
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        return view;
    }

    ValueEventListener allergyListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            historyButton.setText("평가한 요리\n"+ dataSnapshot.child("history").child(uid).getChildrenCount());
            dipsButton.setText("찜한요리\n"+ dataSnapshot.child("dips").child(uid).getChildrenCount());
            allergyButton.setText("알러지/기피\n"+ dataSnapshot.child("allergy").child(uid).getChildrenCount());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.historyButton:
                fragmentTransaction.replace(R.id.InfoFrame,new HistoryFragment()).commit();
                historyButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.info_btn));
                allergyButton.setBackgroundColor(Color.parseColor("#00ff0000"));
                dipsButton.setBackgroundColor(Color.parseColor("#00ff0000"));
                break;
            case R.id.dibsButton:
                fragmentTransaction.replace(R.id.InfoFrame,new DipsFragment()).commit();
                historyButton.setBackgroundColor(Color.parseColor("#00ff0000"));
                allergyButton.setBackgroundColor(Color.parseColor("#00ff0000"));
                dipsButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.info_btn));
                break;
            case R.id.allergyButton:
                fragmentTransaction.replace(R.id.InfoFrame,new AddAllergy()).commit();
                historyButton.setBackgroundColor(Color.parseColor("#00ff0000"));
                allergyButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.info_btn));
                dipsButton.setBackgroundColor(Color.parseColor("#00ff0000"));
                break;
        }
    }
}