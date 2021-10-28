package com.example.poke;

import android.app.Activity;
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
    EditText nickNameTextView;
    ImageView profileView;
    private DatabaseReference mDatabase;
    String uid;
    Button historyButton;
    Button dipsButton;
    Button allergyButton;
    ImageView edit_nickname;
    static HistoryFragment historyFragment;
    ArrayList<String> nlist;

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
        profileView=(ImageView)view.findViewById(R.id.Profileimage);
        nickNameTextView=(EditText) view.findViewById(R.id.Nickname);
        historyButton = (Button) view.findViewById(R.id.historyButton);
        dipsButton = (Button) view.findViewById(R.id.dibsButton);
        allergyButton = (Button) view.findViewById(R.id.allergyButton);
        historyButton.setOnClickListener(this);
        dipsButton.setOnClickListener(this);
        allergyButton.setOnClickListener(this);
        historyButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.info_btn));
        mDatabase.addValueEventListener(allergyListener);
        edit_nickname = view.findViewById(R.id.edit_name);
        nlist = new ArrayList<String>();

        getChildFragmentManager().beginTransaction().add(R.id.InfoFrame,new HistoryFragment()).commit();

        DatabaseReference myRef = database.getReference("users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo info = dataSnapshot.getValue(UserInfo.class);
                info = new UserInfo(info.getNickName());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    nlist.add(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo info = dataSnapshot.getValue(UserInfo.class);
                String nickname = info.getNickName();
                nickNameTextView.setText(nickname);
                nickNameTextView.setTextColor(Color.GRAY);
                edit_nickname.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        nickNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickNameTextView.setClickable(true);
                nickNameTextView.setEnabled(true);
                edit_nickname.setVisibility(View.VISIBLE);
                nickNameTextView.setSelection(nickNameTextView.getText().length());
                edit_nickname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myRef.child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String getnick = nickNameTextView.getText().toString();
                                for(int i=0;i<nlist.size();i++) {
                                    if (nlist.contains(getnick)) {
                                        startToast("닉네임 중복입니다.");
                                        break;
                                    } else {
                                        myRef.child(uid).child("nickName").setValue(getnick);
                                        //getnick != myRef.child(uid).child("nickName").toString()
                                    }
                                }
                                nickNameTextView.setSelection(nickNameTextView.getText().length());
                                hideKeyboard(getActivity());

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                            }
                        });
                    }
                });
            }
        });

        return view;
    }

    ValueEventListener allergyListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            historyButton.setText("평가한 요리\n"+Long.toString(dataSnapshot.child("history").child(uid).getChildrenCount()));
            dipsButton.setText("찜한요리\n"+Long.toString(dataSnapshot.child("dips").child(uid).getChildrenCount()));
            allergyButton.setText("알러지/기피\n"+Long.toString(dataSnapshot.child("allergy").child(uid).getChildrenCount()));
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
                fragmentTransaction.replace(R.id.InfoFrame,new Menu()).commit();
                historyButton.setBackgroundColor(Color.parseColor("#00ff0000"));
                allergyButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.info_btn));
                dipsButton.setBackgroundColor(Color.parseColor("#00ff0000"));
                break;
        }
    }

    private void startToast(String msg){
        Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();

        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
