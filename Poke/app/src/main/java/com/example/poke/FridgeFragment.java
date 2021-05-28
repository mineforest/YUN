package com.example.poke;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
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
    private ArrayList<UserIngredient> tabArrayList;
    private ImageButton btn;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String uid;
    private ImageView imageView;
    private TabLayout tabLayout;
    long date;
    String cate;
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
        tabLayout = view.findViewById(R.id.fridgeTab);
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
        tabArrayList = new ArrayList<>();
        ingredientArrayList = new ArrayList<>();

        mDatabase.child("ingredient").child(uid).addChildEventListener(childEventListener);

        ingredientAdapter = new IngredientAdapter(ingredientArrayList);
        recyclerView.setAdapter(ingredientAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        ingredientAdapter = new IngredientAdapter(ingredientArrayList);
                        recyclerView.setAdapter(ingredientAdapter);
                        break;
                    case 1:
                        cate = tab.getText().toString();
                        update(cate, ingredientArrayList, tabArrayList);
                        break;
                    case 2:
                        cate = tab.getText().toString();
                        update(cate, ingredientArrayList, tabArrayList);
                        break;
                    case 3:
                        cate = tab.getText().toString();
                        update(cate, ingredientArrayList, tabArrayList);
                        break;
                    case 4:
                        cate = tab.getText().toString();
                        update(cate, ingredientArrayList, tabArrayList);
                        break;
                    case 5:
                        cate = tab.getText().toString();
                        update(cate, ingredientArrayList, tabArrayList);
                        break;
                    case 6:
                        cate = tab.getText().toString();
                        update(cate, ingredientArrayList, tabArrayList);
                        break;
                    case 7:
                        cate = tab.getText().toString();
                        update(cate, ingredientArrayList, tabArrayList);
                        break;
                    case 8:
                        cate = tab.getText().toString();
                        update(cate, ingredientArrayList, tabArrayList);
                        break;
                    case 9:
                        cate = tab.getText().toString();
                        update(cate, ingredientArrayList, tabArrayList);
                        break;
                    case 10:
                        cate = tab.getText().toString();
                        update(cate, ingredientArrayList, tabArrayList);
                        break;
                    case 11:
                        cate = tab.getText().toString();
                        update(cate, ingredientArrayList, tabArrayList);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
            });
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
            ingredientArrayList.remove(ingredient.getIngredientTitle());
            ingredientAdapter.notifyDataSetChanged();
        }
        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)  {}
        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };

View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
//        myStartActivity(IngredientAddActivity.class);
        Bundle args = new Bundle();

        args.putString("key", "value");

        IngredientDialog dialog = new IngredientDialog();
        dialog.setArguments(args); // 데이터 전달
        dialog.show(getActivity().getSupportFragmentManager(),"tag");


    }
};

    private void myStartActivity(Class c){
        Intent intent = new Intent(getActivity(),c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void update(String cate, ArrayList<UserIngredient> al, ArrayList<UserIngredient> tab){
        tab.clear();
        for (int i = 0; i < al.size(); i++) {
            if (cate.equals(al.get(i).getCategory())) {
                tab.add(al.get(i));
            }
            ingredientAdapter = new IngredientAdapter(tab);
            recyclerView.setAdapter(ingredientAdapter);
        }
    }
}