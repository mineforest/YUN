package com.example.poke;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RatingBottomSheetFrag extends BottomSheetDialogFragment {
    Context context;
    private Recipe_get rcp = new Recipe_get();
    private RatingBar ratingBar;
    private UserHistory userHistory;
    private final SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
    private Date date;
    private String now;
    private DatabaseReference mDatabase;
    private String uid;
    private ArrayList<String> deleteList = new ArrayList<>();

    public RatingBottomSheetFrag(Context context) {
        this.context = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rating_bottom_dialog, container, false);
        Bundle bundle = getArguments();
        if(bundle != null) {
            rcp = bundle.getParcelable("rcp");
            deleteList = bundle.getStringArrayList("delList");
        }

        Button doneBtn = view.findViewById(R.id.ratingDoneBtn);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uid = user.getUid();
        userHistory = new UserHistory();
        ratingBar = view.findViewById(R.id.ratingBar);


        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = new Date();
                now = form.format(date);
                long l = (long)ratingBar.getRating();
                userHistory = new UserHistory(rcp.getId(), rcp.getName(), rcp.getThumbnail(), now, l);
                mDatabase.child("history").child(uid).child(rcp.getId()).setValue(userHistory);
                deleteIngredient();

                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        return  view;
    }


    public void deleteIngredient() {
        mDatabase.child("ingredient").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (deleteList.contains(dataSnapshot.child("ingredientTitle").getValue(String.class))) {
                            dataSnapshot.getRef().removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
