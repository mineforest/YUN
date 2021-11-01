package com.example.poke;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddAllergy extends Fragment {
    private ArrayList<UserAllergy> allergyList = new ArrayList<>();
    String uid;
    private DatabaseReference mDatabase;
    public static ArrayList<String> AllergyList;
    private CheckBox checkBox;
    private AllergyAdapter allergyAdapter;
    RecyclerView rec_allergy;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.allergy_list, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
            uid = user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        AllergyList = new ArrayList<>();

        AllergyList.add("새우");
        AllergyList.add("굴");
        AllergyList.add("게");
        AllergyList.add("홍합");
        AllergyList.add("오징어");
        AllergyList.add("전복");
        AllergyList.add("고등어");
        AllergyList.add("조개");
        AllergyList.add("메밀");
        AllergyList.add("밀");
        AllergyList.add("대두");
        AllergyList.add("호두");
        AllergyList.add("땅콩");
        AllergyList.add("난류");
        AllergyList.add("우유");
        AllergyList.add("쇠고기");
        AllergyList.add("돼지고기");
        AllergyList.add("닭고기");
        AllergyList.add("복숭아");
        AllergyList.add("토마토");
        AllergyList.add("잣");

        allergyAdapter = new AllergyAdapter(AllergyList);
        rec_allergy = view.findViewById(R.id.list_allergy);
        rec_allergy.setHasFixedSize(true);
        //rec_allergy.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_allergy.setLayoutManager(linearLayoutManager);
        rec_allergy.setAdapter(allergyAdapter);

        DatabaseReference myRef = database.getReference("allergy");


        return view;
    }

}