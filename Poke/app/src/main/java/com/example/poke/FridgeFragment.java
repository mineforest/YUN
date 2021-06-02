package com.example.poke;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.auth.User;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import static android.util.Log.d;

public class FridgeFragment extends Fragment {
    private IngredientAdapter ingredientAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<UserIngredient> ingredientArrayList;
    private ArrayList<UserIngredient> tabArrayList;
    private ImageButton btn;
    private ImageButton barcode_btn;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String uid;
    private ImageView imageView;
    private TabLayout tabLayout;
    String cate="전체";
    private int pos;

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
        btn.setOnClickListener(addClickListener);

        barcode_btn = view.findViewById(R.id.barcodeButton);
        barcode_btn.setOnClickListener(scanClickListener);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(user != null)
            uid=user.getUid();

        recyclerView = (RecyclerView)view.findViewById(R.id.ingredientRecyclerView);
        layoutManager = new GridLayoutManager(getActivity(),4);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        tabArrayList = new ArrayList<>();
        ingredientArrayList = new ArrayList<>();

        mDatabase.child("ingredient").child(uid).addChildEventListener(childEventListener);

        ingredientAdapter = new IngredientAdapter(ingredientArrayList);
        ingredientAdapter.setOnItemClickListener(onItemClickListener);

        recyclerView.setAdapter(ingredientAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        cate = tab.getText().toString();
                        ingredientAdapter = new IngredientAdapter(ingredientArrayList);
                        ingredientAdapter.setOnItemClickListener(onItemClickListener);
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

                ingredientArrayList.add(new UserIngredient(ingredient.getIngredientTitle(), ingredient.getExpirationDate(), ingredient.getCategory(),snapshot.getKey()));

                if(!cate.equals("전체") && cate.equals(ingredient.getCategory())){
                    tabArrayList.add(new UserIngredient(ingredient.getIngredientTitle(), ingredient.getExpirationDate(), ingredient.getCategory(),snapshot.getKey()));
                }

                ingredientAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            UserIngredient ingredient = snapshot.getValue(UserIngredient.class);
            ingredient = new UserIngredient(ingredient.getIngredientTitle(), ingredient.getExpirationDate(), ingredient.getCategory(), snapshot.getKey());

            if(!cate.equals("전체")){
                tabArrayList.set(pos, ingredient);
            }

                for(int i=0; i<ingredientArrayList.size(); i++){
                    if(snapshot.getKey().equals(ingredientArrayList.get(i).getIngredientKey())){
                        ingredientArrayList.set(i,ingredient);
                    }
                }

            ingredientAdapter.notifyDataSetChanged();
        }
        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//            UserIngredient ingredient = snapshot.getValue(UserIngredient.class);
//            ingredientArrayList.remove(ingredient.getIngredientTitle());
//            ingredientAdapter.notifyDataSetChanged();
        }
        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)  {}
        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };
    
    //+ 버튼 클릭
View.OnClickListener addClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Bundle args = new Bundle();

        IngredientDialog dialog = new IngredientDialog();
        dialog.setArguments(args); // 데이터 전달
        dialog.show(getActivity().getSupportFragmentManager(),"tag");
    }
};

View.OnClickListener scanClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        IntentIntegrator.forSupportFragment(FridgeFragment.this).initiateScan();
    }
};

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            String barcode_num = result.getContents();
            if(barcode_num == null) {
                Toast.makeText(getContext(), "취소됨",Toast.LENGTH_LONG).show();
             } else {
                Bundle args = new Bundle();

                BarcodeApiCaller barcodeApiCaller = new BarcodeApiCaller();
                barcodeApiCaller.getXmlData(barcode_num);
                String p_name = barcodeApiCaller.getP_name();
                String p_cate = barcodeApiCaller.getP_cate();
                String p_date = barcodeApiCaller.getP_date();

                if(p_name == null || p_cate == null || p_date == null) {
                    Toast.makeText(getContext(), "읽을 수 없습니다.\n다시 시도하거나 수동 입력해주세요.",Toast.LENGTH_LONG).show();
                } else {
                    args.putString("title", p_name); // 제품명으로 출력됨
                    args.putString("category", p_cate); // 우리가 가진 재료 카테고리로의 매핑 알고리즘 필요
                    args.putString("date", p_date);
                }
                IngredientDialog dialog = new IngredientDialog();
                dialog.setArguments(args);
                dialog.show(getActivity().getSupportFragmentManager(), "tag");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void update(String cate, ArrayList<UserIngredient> al, ArrayList<UserIngredient> tab){
        tab.clear();
        for (int i = 0; i < al.size(); i++) {
            if (cate.equals(al.get(i).getCategory())) {
                tab.add(al.get(i));
            }
            ingredientAdapter = new IngredientAdapter(tab);
            ingredientAdapter.setOnItemClickListener(onItemClickListener);
            recyclerView.setAdapter(ingredientAdapter);
        }
    }

    IngredientAdapter.OnItemClickListener onItemClickListener =new IngredientAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            Bundle args = new Bundle();
            pos = position;
            if(cate.equals("전체")){
                args.putString("title",ingredientArrayList.get(position).getIngredientTitle());
                args.putString("category", ingredientArrayList.get(position).getCategory());
                args.putString("date", ingredientArrayList.get(position).getExpirationDate());
                args.putString("key",ingredientArrayList.get(position).getIngredientKey());
            }
            else{
                args.putString("title",tabArrayList.get(position).getIngredientTitle());
                args.putString("category", tabArrayList.get(position).getCategory());
                args.putString("date", tabArrayList.get(position).getExpirationDate());
                args.putString("key", tabArrayList.get(position).getIngredientKey());
            }

            IngredientDialog dialog = new IngredientDialog();
            dialog.setArguments(args); // 데이터 전달
            dialog.show(getActivity().getSupportFragmentManager(),"tag");
        }
    };
}