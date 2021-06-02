package com.example.poke;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StartFragment extends Fragment {
    Button startButton;
    PreferenceActivity preferenceActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        preferenceActivity = (PreferenceActivity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        preferenceActivity=null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preference_fragment_start, container, false);
        startButton = view.findViewById(R.id.startButton);

        startButton.setOnClickListener(onClickListener);
        return view;
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            preferenceActivity.next(0);
        }
    };


}
