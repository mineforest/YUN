package com.example.poke;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IngredientDialog extends DialogFragment {
    private Fragment fragment;
    private Button okBtn;
    private EditText nameText;
    private EditText cateText;
    private EditText dateText;
    private String title;
    private String cate;
    private String date;
    private String key;
    private DatabaseReference mDatabase;
    String uid;

    public IngredientDialog(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ingre_popup,container,false);
        okBtn = view.findViewById(R.id.dialogOkBtn);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uid = user.getUid();
        okBtn.setOnClickListener(onClickListener);
        Bundle args = getArguments();
        nameText = view.findViewById(R.id.prod_name_txt);
        cateText = view.findViewById(R.id.prod_cat_txt);
        dateText = view.findViewById(R.id.daycnt_txt);

        if(args != null) {
            title = args.getString("title");
            date = args.getString("date");
            cate = args.getString("category");
            key = args.getString("key");
            nameText.setText(title);
            cateText.setText(cate);
            dateText.setText(date);
        }

        fragment = getActivity().getSupportFragmentManager().findFragmentByTag("tag");
        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            title = nameText.getText().toString();
            cate = cateText.getText().toString();
            date = dateText.getText().toString();
            UserIngredient userIngredient;

            if(title.length() >=2 && cate.length() >=2 ){
                if(key != null){
                    userIngredient = new UserIngredient(title, date, cate);
                    mDatabase.child("ingredient").child(uid).child(key).setValue(userIngredient);
                }
                else{
                    userIngredient = new UserIngredient(title, date, cate);
                    mDatabase.child("ingredient").child(uid).push().setValue(userIngredient);
                }
                if (fragment != null) {
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    dialogFragment.dismiss();
                }
            }
            else{
                    startToast("다시 입력해주세요");
            }
        }
    };

    private void startToast(String msg){
        Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();
    }
}
