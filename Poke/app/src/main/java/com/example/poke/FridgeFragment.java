package com.example.poke;

import  android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FridgeFragment extends Fragment{
    private FridgeAdapter ingredientAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<UserIngredient> ingredientArrayList;
    private ArrayList<UserIngredient> tabArrayList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String uid;
    private TabLayout tabLayout;
    private String cate="전체";
    private int pos;
    private ImageButton addButton;
    private SearchView searchView;
    private TextInputLayout textInputLayout;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fridge,container,false);
        setHasOptionsMenu(true);
        ((MainActivity)getActivity()).getSupportActionBar().setElevation(0);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        progressDialog.setCancelable(false);

        tabLayout = view.findViewById(R.id.fridgeTab);
        addButton = view.findViewById(R.id.ingredientAddBtn);

        textInputLayout = view.findViewById(R.id.input_box);
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchFilter(textInputLayout.getEditText().getText().toString());
            }
        });

        tabArrayList = new ArrayList<>();
        ingredientArrayList = new ArrayList<>();

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.fridgeTab);
        int betweenSpace = 30;

        recyclerView = (RecyclerView)view.findViewById(R.id.ingredientRecyclerView);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.ingredientAddBtn);
        fab.attachToRecyclerView(recyclerView);

        ingredientAdapter = new FridgeAdapter(tabArrayList);
        //ingredientAdapter.setOnItemClickListener(onItemClickListener);
        addButton.setOnClickListener(addClickListener);

        layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        ViewGroup slidingTabStrip = (ViewGroup) tabLayout.getChildAt(0);
        for(int i = 0; i < slidingTabStrip.getChildCount() - 1; i++) {
            View v = slidingTabStrip.getChildAt(i);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.rightMargin = betweenSpace;
        }

        class StartRunnable implements Runnable{
            @Override
            public void run() {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        mAuth = FirebaseAuth.getInstance();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        if (user != null)
                            uid = user.getUid();
                    }
                }catch (Exception e){
                }finally {
                    mDatabase.child("ingredient").child(uid).addChildEventListener(childEventListener);
                    recyclerView.setAdapter(ingredientAdapter);
                    progressDialog.dismiss();
                }
            }
        }
        
        StartRunnable sr = new StartRunnable();
        Thread stop = new Thread(sr);
        stop.start();

        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        stop.interrupt();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy<0){
                    fab.show();
                }
                else if(dy>0){
                    fab.hide();
                }
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                cate = tab.getText().toString();
                update(cate, ingredientArrayList, tabArrayList);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                cate = tab.getText().toString();
                update(cate, ingredientArrayList, tabArrayList);
            }
        });

        return view;
    }


    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.ingredient_sort,menu);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    public void searchFilter(String searchText) {
        tabArrayList.clear();
        for (UserIngredient userIngredient : ingredientArrayList) {
            if ((cate.equals("전체") || userIngredient.getCategory().equals(cate)) && userIngredient.getIngredientTitle().contains(searchText)) tabArrayList.add(userIngredient);
        }
        ingredientAdapter.filterList(tabArrayList);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.id:
                Collections.sort(tabArrayList, new Comparator<UserIngredient>() {
                    @Override
                    public int compare(UserIngredient o1, UserIngredient o2) {
                        return o1.getIngredientTitle().compareTo(o2.getIngredientTitle());
                    }
                });
                ingredientAdapter.notifyDataSetChanged();
                break;

            case R.id.day:
                Collections.sort(tabArrayList, new Comparator<UserIngredient>() {
                    @Override
                    public int compare(UserIngredient o1, UserIngredient o2) {
                        return o1.getExpirationDate().compareTo(o2.getExpirationDate());
                    }
                });
                ingredientAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            UserIngredient ingredient = snapshot.getValue(UserIngredient.class);

            ingredientArrayList.add(new UserIngredient(ingredient.getIngredientTitle(), ingredient.getExpirationDate(), ingredient.getCategory(),snapshot.getKey()));

            if(cate.equals("전체") || cate.equals(ingredient.getCategory())){
                tabArrayList.add(new UserIngredient(ingredient.getIngredientTitle(), ingredient.getExpirationDate(), ingredient.getCategory(),snapshot.getKey()));
            }

            ingredientAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            UserIngredient ingredient = snapshot.getValue(UserIngredient.class);
            ingredient = new UserIngredient(ingredient.getIngredientTitle(), ingredient.getExpirationDate(), ingredient.getCategory(), snapshot.getKey());

            if (cate.equals("전체") || cate.equals(ingredient.getCategory())) {
                tabArrayList.set(pos, ingredient);
            }

            for (int i = 0; i < ingredientArrayList.size(); i++) {
                if (snapshot.getKey().equals(ingredientArrayList.get(i).getIngredientKey())) {
                    ingredientArrayList.set(i, ingredient);
                }
            }

            ingredientAdapter.notifyDataSetChanged();
        }
        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            UserIngredient ingredient = snapshot.getValue(UserIngredient.class);
            ingredient = new UserIngredient(ingredient.getIngredientTitle(), ingredient.getExpirationDate(), ingredient.getCategory(), snapshot.getKey());

            if (cate.equals("전체") || cate.equals(ingredient.getCategory())) {
                for (int i = 0; i < tabArrayList.size(); i++) {
                    if (snapshot.getKey().equals(tabArrayList.get(i).getIngredientKey())) {
                        tabArrayList.remove(i);
                    }
                }
            }

            for (int i = 0; i < ingredientArrayList.size(); i++) {
                if (snapshot.getKey().equals(ingredientArrayList.get(i).getIngredientKey())) {
                    ingredientArrayList.remove(i);
                }
            }
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)  {}
        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };

    public void update(String cate, ArrayList<UserIngredient> al, ArrayList<UserIngredient> tab){
        tab.clear();

        if(cate.equals("전체")){
            tab.addAll(al);
        }
        else {
            for (int i = 0; i < al.size(); i++) {
                if (cate.equals(al.get(i).getCategory())) {
                    tab.add(al.get(i));
                }
            }
        }
        ingredientAdapter.notifyDataSetChanged();
    }

    /*
    FridgeAdapter.OnItemClickListener onItemClickListener =new FridgeAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            Bundle args = new Bundle();
            pos = position;

            args.putString("title",tabArrayList.get(position).getIngredientTitle());
            args.putString("category", tabArrayList.get(position).getCategory());
            args.putString("date", tabArrayList.get(position).getExpirationDate());
            args.putString("key", tabArrayList.get(position).getIngredientKey());

            IngredientDialog dialog = new IngredientDialog();
            dialog.setArguments(args); // 데이터 전달
            dialog.show(getActivity().getSupportFragmentManager(),"tag");
        }
    };
*/
    View.OnClickListener addClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle args = new Bundle();

            IngredientDialog dialog = new IngredientDialog();
            dialog.setArguments(args); // 데이터 전달
            dialog.show(getActivity().getSupportFragmentManager(),"tag");
        }
    };

    private void startToast(String msg){
        Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();
    }

}