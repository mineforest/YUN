package com.example.poke;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AllergyFragment extends Fragment {
    ArrayList<UserPreference> preferenceArrayList = new ArrayList<>();
    ArrayList<UserDiet> dietArrayList = new ArrayList<>();


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_allergy,container,false);
        ChipGroup chip = view.findViewById(R.id.allergyOption);


        if(getArguments() != null){
            for(String list : getArguments().getStringArrayList("preference")){
                preferenceArrayList.add(new UserPreference(list));
            }

            for(String list : getArguments().getStringArrayList("diet")){
                dietArrayList.add(new UserDiet(list));
            }

        }

        return view;
    }

    public interface OnMyListener{
        public void onReceivedData(ArrayList pre, ArrayList diet);
    }

    private OnMyListener onMyListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(getActivity() != null && getActivity() instanceof OnMyListener){
            onMyListener = (OnMyListener)getActivity();
        }
    }


}