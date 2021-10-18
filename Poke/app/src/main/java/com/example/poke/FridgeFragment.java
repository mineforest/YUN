package com.example.poke;

import  android.app.ProgressDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
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
    private int swipePos;
    private ImageButton addButton;
    private SearchView searchView;
    ProgressDialog progressDialog;
    FridgeSwipe fridgeSwipe;

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
        searchView = view.findViewById(R.id.menu_search);

        tabArrayList = new ArrayList<>();
        ingredientArrayList = new ArrayList<>();

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.fridgeTab);
        int betweenSpace = 30;

        recyclerView = (RecyclerView)view.findViewById(R.id.ingredientRecyclerView);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.ingredientAddBtn);
        fab.attachToRecyclerView(recyclerView);

        ingredientAdapter = new FridgeAdapter(tabArrayList);
        ingredientAdapter.setOnItemClickListener(onItemClickListener);
        addButton.setOnClickListener(addClickListener);

        layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        fridgeSwipe = new FridgeSwipe(new FridgeSwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                swipePos = position;
                mDatabase.child("ingredient").child(uid).child(tabArrayList.get(swipePos).getIngredientKey()).setValue(null);
                startToast(tabArrayList.get(swipePos).getIngredientTitle() + " 삭제 ");
            }
        });

        fridgeSwipe.setClamp(200f);
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(fridgeSwipe);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                fridgeSwipe.onDraw(c);
                recyclerView.setOnTouchListener(new View.OnTouchListener() {
                   @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        fridgeSwipe.removePreviousClamp(recyclerView);
                        return false;
                    }
                });
            }
        });

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
                switch (tab.getPosition()) {
                    case 0:
                        cate = tab.getText().toString();
                        update(cate, ingredientArrayList, tabArrayList);
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
                switch (tab.getPosition()) {
                    case 0:
                        cate = tab.getText().toString();
                        update(cate, ingredientArrayList, tabArrayList);
//                        ingredientAdapter = new IngredientAdapter(ingredientArrayList);
//                        ingredientAdapter.setOnItemClickListener(onItemClickListener);
//                        recyclerView.setAdapter(ingredientAdapter);
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
            });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.ingredient_sort, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("냉장고 속 재료를 찾아보세요.");
        searchView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.search_radius));
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(onQueryTextListener);
        searchView.setQuery(null,false);
        EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(Color.parseColor("#6E7377"));
        editText.setHintTextColor(Color.parseColor("#6E7377"));
    }

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            tabArrayList.clear();
            for (int i = 0; i < ingredientArrayList.size(); i++) {
                String t = ingredientArrayList.get(i).getIngredientTitle();
                String c = ingredientArrayList.get(i).getCategory();
                if ((cate.equals("전체") || c.equals(cate)) && t.toLowerCase().contains(newText.toLowerCase())) {
                    tabArrayList.add(ingredientArrayList.get(i));
                }
            }

            ingredientAdapter.notifyDataSetChanged();
            return false;
        }
    };

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

//    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//        @Override
//        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            swipePos = viewHolder.getLayoutPosition();
//            mDatabase.child("ingredient").child(uid).child(tabArrayList.get(swipePos).getIngredientKey()).setValue(null);
//            startToast(tabArrayList.get(swipePos).getIngredientTitle() + " 삭제 ");
//        }
//    };

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
                        tabArrayList.remove(swipePos);
                    }

                    for (int i = 0; i < ingredientArrayList.size(); i++) {
                        if (ingredientArrayList.contains(snapshot.getKey())) {
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
        }
    };

    private void startToast(String msg){
        Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}