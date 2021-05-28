package com.example.poke;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class IngredientDialog extends DialogFragment {
    private Fragment fragment;
    private Button okBtn;

    public IngredientDialog(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ingre_popup,container,false);
        okBtn = view.findViewById(R.id.dialogOkBtn);

        Bundle args = getArguments();
        String value = args.getString("key");

        fragment = getActivity().getSupportFragmentManager().findFragmentByTag("tag");
        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (fragment != null) {
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();
            }

        }
    };
}
