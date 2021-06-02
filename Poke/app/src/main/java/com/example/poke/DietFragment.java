package com.example.poke;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class DietFragment extends Fragment {
    private ArrayList<String> dietList = new ArrayList<>();
    private ChipGroup dietGroup;
    private Chip chip;
    private Button btn3;
    private Button btn4;
    private View bottomSheetBehavior;
    private CoordinatorLayout.Behavior behavior;
    PreferenceActivity preferenceActivity;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet,container,false);
        dietGroup =view.findViewById(R.id.dietOption);
        btn3 = view.findViewById(R.id.button3);
        btn4 = view.findViewById(R.id.button4);
        bottomSheetBehavior = view.findViewById(R.id.bottomSheet2);
        behavior = BottomSheetBehavior.from(bottomSheetBehavior);

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

        btn3.setOnClickListener(onClickListener);
        btn4.setOnClickListener(onClickListener);

        dietListener.dietListener(dietList);
        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button3:
                    preferenceActivity.pre(0);
                    break;
                case R.id.button4:
                    preferenceActivity.next(2);
                    break;
            }
        }
    };

    public interface DietListener {
        void dietListener(ArrayList<String> diet);
    }

    private DietFragment.DietListener dietListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        preferenceActivity = (PreferenceActivity)getActivity();
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
        preferenceActivity=null;
    }
}