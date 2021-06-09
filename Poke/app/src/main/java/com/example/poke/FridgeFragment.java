package com.example.poke;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.util.Log.d;

public class FridgeFragment extends Fragment{
    private IngredientAdapter ingredientAdapter;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fridge,container,false);
        setHasOptionsMenu(true);
        tabLayout = view.findViewById(R.id.fridgeTab);
        addButton = view.findViewById(R.id.ingredientAddBtn);
        searchView = view.findViewById(R.id.menu_search);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(user != null)
            uid=user.getUid();

        recyclerView = (RecyclerView)view.findViewById(R.id.ingredientRecyclerView);
        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        tabArrayList = new ArrayList<>();
        ingredientArrayList = new ArrayList<>();

        mDatabase.child("ingredient").child(uid).addChildEventListener(childEventListener);

        ingredientAdapter = new IngredientAdapter(tabArrayList);
        ingredientAdapter.setOnItemClickListener(onItemClickListener);
        addButton.setOnClickListener(addClickListener);
        recyclerView.setAdapter(ingredientAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
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
        searchView.setQueryHint("재료 검색");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(onQueryTextListener);
        searchView.setQuery(null,false);
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
                Collections.sort(ingredientArrayList, new Comparator<UserIngredient>() {
                    @Override
                    public int compare(UserIngredient o1, UserIngredient o2) {
                        return o1.getIngredientTitle().compareTo(o2.getIngredientTitle());
                    }
                });
                ingredientAdapter.notifyDataSetChanged();
                break;

            case R.id.day:
                Collections.sort(ingredientArrayList, new Comparator<UserIngredient>() {
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

            if(cate.equals("전체") || cate.equals(ingredient.getCategory())){
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
//        ingredientAdapter = new IngredientAdapter(tab);
//        ingredientAdapter.setOnItemClickListener(onItemClickListener);
//        recyclerView.setAdapter(ingredientAdapter);
        ingredientAdapter.notifyDataSetChanged();
    }

    IngredientAdapter.OnItemClickListener onItemClickListener =new IngredientAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            Bundle args = new Bundle();
            pos = position;
//            if(cate.equals("전체")){
//                args.putString("title",ingredientArrayList.get(position).getIngredientTitle());
//                args.putString("category", ingredientArrayList.get(position).getCategory());
//                args.putString("date", ingredientArrayList.get(position).getExpirationDate());
//                args.putString("key",ingredientArrayList.get(position).getIngredientKey());
//            }
//            else{
                args.putString("title",tabArrayList.get(position).getIngredientTitle());
                args.putString("category", tabArrayList.get(position).getCategory());
                args.putString("date", tabArrayList.get(position).getExpirationDate());
                args.putString("key", tabArrayList.get(position).getIngredientKey());
//            }

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

}