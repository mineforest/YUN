package com.example.poke;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class AllergyFragment extends Fragment {
    private ArrayList<String> allergyList = new ArrayList<>();
    private ChipGroup allergyGroup;
    private Chip chip;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allergy,container,false);
        allergyGroup =view.findViewById(R.id.allergyOption);

        for(int i = 0; i < allergyGroup.getChildCount(); i++) {
            int id = allergyGroup.getChildAt(i).getId();
            chip = view.findViewById(id);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        allergyList.add(buttonView.getText().toString());
                    }
                    else
                        allergyList.remove(buttonView.getText().toString());
                }
            });
        }

        allergyListener.allergyListener(allergyList);
        return view;
    }

    public interface AllergyListener {
        void allergyListener(ArrayList<String> allergy);
    }

    private AllergyListener allergyListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof AllergyListener) {
            allergyListener = (AllergyListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
            + "must implement AllergyListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        allergyListener=null;
    }
}