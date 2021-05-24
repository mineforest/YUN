package com.example.poke;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class PreferenceFragment extends Fragment {
    LayoutInflater layoutInflater;
    View header;
    private ChipGroup preGroup;
    private ArrayList<String> preList = new ArrayList<>();
    Bundle bundle;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preference,container,false);
        preGroup = view.findViewById(R.id.preOption);
        bundle = new Bundle();
//        layoutInflater = getLayoutInflater();
//        header = layoutInflater.inflate(R.layout.preference,null);
//        preGroup = header.findViewById(R.id.preOption);
//
//        for(int list : preGroup.getCheckedChipIds()){
//            Chip chip = header.findViewById(list);
//            preList.add(chip.getText().toString());
//            //mDatabase.child("preference").child(uid).push().setValue(new UserPreference(chip.getText().toString()));
//        }

        return view;
    }

}
