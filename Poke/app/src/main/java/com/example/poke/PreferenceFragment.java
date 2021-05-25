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

public class PreferenceFragment extends Fragment {
    private ArrayList<String> preList = new ArrayList<>();
    private ChipGroup preGroup;
    private Chip chip;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preference,container,false);
        preGroup=view.findViewById(R.id.preOption);

        for(int i=0; i < preGroup.getChildCount(); i++) {
            int id = preGroup.getChildAt(i).getId();
            chip = view.findViewById(id);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        preList.add(buttonView.getText().toString());
                    }
                    else
                        preList.remove(buttonView.getText().toString());
                }
            });
        }
        preListener.preListener(preList);
        return view;
    }


    public interface PreListener{
        void preListener(ArrayList pre);
    }

    private PreListener preListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof PreListener) {
            preListener = (PreListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + "must implement PreListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        preListener=null;
    }
}
