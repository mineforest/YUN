package com.example.poke;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.os.Bundle;

import android.provider.SearchRecentSuggestions;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private static final String TAG = "tag";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private ArrayList<Recipe_get> searchList = new ArrayList<>();
    private ArrayList<Recipe_get> searchedList = new ArrayList<>();
    private FirebaseAuth mAuth;
    SearchAdapter adapter;
    private String cate="전체";
    private ArrayList<Recipe_get> SearchArrayList = new ArrayList<>();
    private List<String> slist;
    AutoCompleteTextView editText;
    SearchView searchView;
    String uid;
    Button clear_btn;
    private TabLayout tabLayout;
    ProgressDialog progressDialog;
    private SearchAdapter SearchAdapter = new SearchAdapter(searchList);

    public SearchFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);
        searchView = (SearchView) view.findViewById(R.id.rec_search);
        tabLayout = view.findViewById(R.id.searchTab);

        //SearchAdapter = new SearchAdapter(tabArrayList);
        ArrayList<Object> filteredList = new ArrayList<>();


//        progressDialog = new ProgressDialog(getActivity());
//
//        progressDialog.show();
//        progressDialog.setContentView(R.layout.progress_dialog);
//        progressDialog.getWindow().setBackgroundDrawableResource(
//                android.R.color.transparent
//        );
//        progressDialog.setCancelable(false);

        ViewGroup slidingTabStrip = (ViewGroup) tabLayout.getChildAt(0);
        for (int i = 0; i < slidingTabStrip.getChildCount() - 1; i++) {
            View v = slidingTabStrip.getChildAt(i);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.rightMargin = 30;
        }

        new Thread() {
            @Override
            public void run() {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("recipe")
                       // .whereEqualTo("name",editText.getText().toString())
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                Log.d("dadf",searchView.getQuery().toString());
                                if (error != null) {
                                    Log.w(TAG, "Oh ~ no ~");
                                    return;
                                }

                                for (QueryDocumentSnapshot doc : value) {
                                    if(doc.getData().get("name").toString().contains(searchView.getQuery().toString()))
                                    {
                                        String rcp_id = doc.getData().get("id").toString();
                                        String title = doc.getData().get("name").toString();
                                        String thumbnail = doc.getData().get("thumbnail").toString();
                                        List<String> tag = (List<String>) doc.get("tag");
                                        Recipe_get r = new Recipe_get(rcp_id, thumbnail, title, tag);
                                        searchList.add(r);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        });

            }
        }.start();

        //editText.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, slist));
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.search_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SearchAdapter(searchList);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, searchView.getQuery().toString());
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getActivity(),
                        MySuggestionProvider.AUTHORITY,
                        MySuggestionProvider.MODE);


                suggestions.saveRecentQuery(query, null);

//                SearchView searchView = (SearchView)menu.findItem(R.id.menu_search).getActionView();

                displaySearchResults(query);

                hideKeyboard(getActivity());

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String searchText = searchView.getQuery().toString();
                searchFilter(searchText);
                return false;
            }
        });




        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        searchView.setQuery("",false);
                        break;
                    case 1:
                        cate = tab.getText().toString();
                        Log.d("dadf",tab.getText().toString());
                        clicktag(cate, SearchArrayList);
                        break;
                    case 2:
                        cate = tab.getText().toString();
                        clicktag(cate, SearchArrayList);
                        break;
                    case 3:
                        cate = tab.getText().toString();
                        clicktag(cate, SearchArrayList);
                        break;
                    case 4:
                        cate = tab.getText().toString();
                        clicktag(cate, SearchArrayList);
                        break;
                    case 5:
                        cate = tab.getText().toString();
                        clicktag(cate, SearchArrayList);
                        break;
                    case 6:
                        cate = tab.getText().toString();
                        clicktag(cate, SearchArrayList);
                        break;
                    case 7:
                        cate = tab.getText().toString();
                        clicktag(cate, SearchArrayList);
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
                        searchView.setQuery("",false);
                        break;
                    case 1:
                        searchView.setQuery("강정",false);
                        break;
                    case 2:
                        searchView.setQuery("찜",false);
                        break;
                    case 3:
                        searchView.setQuery("구이",false);
                        break;
                    case 4:
                        searchView.setQuery("조림",false);
                        break;
                    case 5:
                        searchView.setQuery("케이크",false);
                        break;
                    case 6:
                        searchView.setQuery("치킨",false);
                        break;
                    case 7:
                        searchView.setQuery("볶음",false);
                        break;


                }
            }
        });
        return view;
    }

    private void displaySearchResults(String query) {

    }


    public void searchFilter(String searchText) {
        searchedList.clear();

        for (int i = 0; i < searchList.size(); i++) {
            if (searchList.get(i).getName().toLowerCase().contains(searchText.toLowerCase())) {
                searchedList.add(searchList.get(i));
            }
        }

        adapter.filterList(searchedList);
    }
    @Override
    public void onResume() {
        super.onResume();
    }


    public void clicktag(@NonNull String cate, ArrayList<Recipe_get> al) {
        //String[] tags = al.toArray(new String[al.size()]);

        SearchArrayList.clear();

        if(cate.equals("전체"))
        {
            SearchArrayList.addAll(searchList);
            Log.d(TAG, "clicktag: done");
        }
        else {
            for (Recipe_get rcp : new ArrayList<>(al)) {
                if (rcp.getTag().contains(cate)) {
                    SearchArrayList.add(rcp);
                    SearchAdapter.notifyDataSetChanged();
                    Log.d("dadf",rcp.getTag().toString());
                }
            }
        }

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();

        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }




}
