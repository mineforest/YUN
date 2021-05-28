package com.example.poke;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FridgeFragment extends Fragment {
    private IngredientAdapter ingredientAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<UserIngredient> ingredientArrayList;
    private ImageButton btn;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String uid;
    private ImageView imageView;
    long date;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date now= new Date();
    String getTime = simpleDateFormat.format(now);

    Calendar today = Calendar.getInstance();
    Calendar cal = Calendar.getInstance();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fridge,container,false);
        view.findViewById(R.id.barcodeButton).setBackgroundColor(Color.rgb(255,255,255));
        imageView = view.findViewById(R.id.categoryView);
        btn = view.findViewById(R.id.ingreAdd);
        btn.setBackgroundColor(Color.rgb(255,255,255));
        btn.setOnClickListener(onClickListener);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uid=user.getUid();

        recyclerView = (RecyclerView)view.findViewById(R.id.ingredientRecyclerView);
        layoutManager = new GridLayoutManager(getActivity(),4);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        ingredientArrayList = new ArrayList<>();

        mDatabase.child("ingredient").child(uid).addChildEventListener(childEventListener);

        ingredientAdapter = new IngredientAdapter(ingredientArrayList);
        recyclerView.setAdapter(ingredientAdapter);

        return view;
    }

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                UserIngredient ingredient = snapshot.getValue(UserIngredient.class);
                String[] dday=ingredient.getExpirationDate().split("-");

                int[] days=new int[3];
                for(int i=0;i<3;i++){
                    days[i]=Integer.parseInt(dday[i]);
                }

                cal.set(days[0],days[1]-1,days[2]);
                date = (cal.getTimeInMillis() - today.getTimeInMillis());

                ingredientArrayList.add(new UserIngredient(ingredient.getIngredientTitle(), "D-"+Long.toString(date/86400000), ingredient.getCategory()));
                ingredientAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            UserIngredient ingredient = snapshot.getValue(UserIngredient.class);
            String[] dday=ingredient.getExpirationDate().split("-");
            ingredientArrayList.remove(ingredientArrayList.size());
        }
        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)  {}
        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };

View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(v==btn)
        myStartActivity(IngredientAddActivity.class);
        else{
            
        }

    }
};

    private void myStartActivity(Class c){
        Intent intent = new Intent(getActivity(),c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}