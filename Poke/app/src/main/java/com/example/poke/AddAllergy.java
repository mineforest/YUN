package com.example.poke;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddAllergy extends Fragment {
    private String uid;
    private ArrayList<String> allergyList;
    private ArrayList<Boolean> allergyMapping;
    private AllergyAdapter allergyAdapter;
    private RecyclerView rec_allergy;
    private DatabaseReference mDatabase;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.allergy_list, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) uid = user.getUid();

        allergyMapping = new ArrayList<>();
        allergyList = new ArrayList<>();
        String kindAllery = "새우 굴 게 홍합 오징어 전복 고등어 조개 메밀 밀 대두 호두 땅콩 계란 우유 쇠고기 돼지고기 닭고기 복숭아 토마토 잣";
        for(String str : kindAllery.split(" ")) {
            allergyList.add(str);
            allergyMapping.add(false);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("allergy").child(uid).addChildEventListener(allergyChildEventListener);

        allergyAdapter = new AllergyAdapter(allergyList, allergyMapping);

        allergyAdapter.setOnItemClickListener(setOnItemClickListener);

        rec_allergy = view.findViewById(R.id.list_allergy);
        rec_allergy.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rec_allergy.setLayoutManager(gridLayoutManager);
        rec_allergy.setAdapter(allergyAdapter);

        return view;
    }

    AllergyAdapter.OnItemClickListener setOnItemClickListener = new AllergyAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            String target = allergyList.get(position);
            if(allergyMapping.get(position)) { // 이미 알러지 등록 한 상태 -> remove
                mDatabase.child("allergy").child(uid).child(target).removeValue();
                allergyMapping.set(position, false);
                Snackbar.make(getActivity().findViewById(android.R.id.content),target + "이(가) 삭제되었습니다.",Snackbar.LENGTH_SHORT).show();
            }
            else {
                mDatabase.child("allergy").child(uid).child(target).setValue(new UserAllergy(target));
                allergyMapping.set(position, true);
                Snackbar.make(getActivity().findViewById(android.R.id.content),target + "이(가) 추가되었습니다.",Snackbar.LENGTH_SHORT).show();
            }
            allergyAdapter.notifyDataSetChanged();
        }
    };

    ChildEventListener allergyChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            UserAllergy allergy = snapshot.getValue(UserAllergy.class);
            if(allergyList.contains(allergy.getAllergy())){
                allergyMapping.set(allergyList.indexOf(allergy.getAllergy()),true);
                allergyAdapter.notifyDataSetChanged();
            }
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