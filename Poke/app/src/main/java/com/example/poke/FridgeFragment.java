package com.example.poke;

import  android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
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

import static android.content.Context.INPUT_METHOD_SERVICE;

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
    private ProgressDialog progressDialog;
    private LottieAnimationView lottieView;
    private TextView lot_txt;
    FloatingActionButton fab;
    View view;
    EditText ingreSearch;
    InputMethodManager imm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fridge,container,false);
        setHasOptionsMenu(true);
        ((MainActivity)getActivity()).getSupportActionBar().setElevation(0);
        imm = (InputMethodManager)getContext().getSystemService(INPUT_METHOD_SERVICE);

        lottieView = view.findViewById(R.id.lottieView);
        lot_txt = view.findViewById(R.id.lot_txt);
        lottieView.setVisibility(View.INVISIBLE);
        lot_txt.setVisibility(View.INVISIBLE);

        tabLayout = view.findViewById(R.id.fridgeTab);
        addButton = view.findViewById(R.id.ingredientAddBtn);

        textInputLayout = view.findViewById(R.id.input_box);
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                searchFilter(textInputLayout.getEditText().getText().toString());
            }

        });

        ingreSearch = view.findViewById(R.id.fridgeIngredientSearch);
        ingreSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);
        tabArrayList = new ArrayList<>();
        ingredientArrayList = new ArrayList<>();

        TabLayout tabLayout = view.findViewById(R.id.fridgeTab);
        int betweenSpace = 30;

        recyclerView = view.findViewById(R.id.ingredientRecyclerView);
        fab = view.findViewById(R.id.ingredientAddBtn);

        ingredientAdapter = new FridgeAdapter(tabArrayList);
        ingredientAdapter.setOnItemClickListener(onItemClickListener);
        addButton.setOnClickListener(addClickListener);

        layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnTouchListener(touchListener);

        ViewGroup slidingTabStrip = (ViewGroup) tabLayout.getChildAt(0);

        for(int i = 0; i < slidingTabStrip.getChildCount() - 1; i++) {
            View v = slidingTabStrip.getChildAt(i);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.rightMargin = betweenSpace;
        }

        fab.attachToRecyclerView(recyclerView);
        iLoveLottie(getView());

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
                }
            }
        }
        
        StartRunnable sr = new StartRunnable();
        Thread stop = new Thread(sr);
        stop.start();

        try{
            Thread.sleep(500);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        stop.interrupt();

//        recyclerView.setOnTouchListener(touchListener);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int tmp = 1;
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                if(tmp == 0) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    fab.hide();
//                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                fab.show();

                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;

                if (lastVisibleItemPosition == itemTotalCount && itemTotalCount != -1 && recyclerView.canScrollVertically(-1)) {
                    fab.hide();
                }

                if(newState==0 && lastVisibleItemPosition != itemTotalCount)
                    fab.show();
                else if (newState == 1)
                    fab.hide();

                tmp = 0;
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                cate = tab.getText().toString();
                update(cate, ingredientArrayList, tabArrayList);
                fab.show();
                ingreSearch.clearFocus();
                imm.hideSoftInputFromWindow(ingreSearch.getWindowToken(), 0);
                iLoveLottie(getView());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                cate = tab.getText().toString();
                update(cate, ingredientArrayList, tabArrayList);
                fab.show();
                ingreSearch.clearFocus();
                imm.hideSoftInputFromWindow(ingreSearch.getWindowToken(), 0);
                iLoveLottie(getView());
            }
        });

        return view;
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ingreSearch.clearFocus();
            imm.hideSoftInputFromWindow(ingreSearch.getWindowToken(), 0);
            return false;
        }
    };

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
        iLoveLottie(getView());
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
            iLoveLottie(getView());
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
            iLoveLottie(getView());
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

            iLoveLottie(getView());
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

    View.OnClickListener addClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle args = new Bundle();
            IngredientDialog dialog = new IngredientDialog();
            dialog.setArguments(args); // 데이터 전달
            dialog.show(getActivity().getSupportFragmentManager(),"tag");
//            dialog.getWindow().setBackgroundDrawableResource(
//                    android.R.color.transparent
//            );
        }
    };

    private void startToast(String msg){
        Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();
    }
    private void iLoveLottie(View view){
        if(tabArrayList.size()==0){
            lottieView.setVisibility(View.VISIBLE);
            lot_txt.setVisibility(View.VISIBLE);
        }
        else{
            lottieView.setVisibility(View.INVISIBLE);
            lot_txt.setVisibility(View.INVISIBLE);
        }

    }
}