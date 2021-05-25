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

public class DietFragment extends Fragment {
    private ArrayList<String> dietList = new ArrayList<>();
    private ChipGroup dietGroup;
    private Chip chip;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet,container,false);
        dietGroup =view.findViewById(R.id.dietOption);

        for(int i = 0; i < dietGroup.getChildCount(); i++) {
            int id = dietGroup.getChildAt(i).getId();
            chip = view.findViewById(id);

            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        dietList.add(buttonView.getText().toString());
                    }
                    else
                        dietList.remove(buttonView.getText().toString());

                }
            });
        }
        dietListener.dietListener(dietList);
        return view;
    }
    public interface DietListener {
        void dietListener(ArrayList diet);
    }

    private DietFragment.DietListener dietListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof DietFragment.DietListener) {
            dietListener = (DietFragment.DietListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + "must implement AllergyListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dietListener =null;
    }
}