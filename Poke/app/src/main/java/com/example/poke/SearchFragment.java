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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
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
    RecentAdapter recentAdapter;
    private String cate="전체";
    private ArrayList<Recipe_get> SearchArrayList = new ArrayList<>();
    private List<String> slist;
    AutoCompleteTextView editText;
    public static SearchView searchView;
    private Chip chip;
    String uid;
    Button clear_btn;
    private ChipGroup tag_chip;
    private TabLayout tabLayout;
    private RecyclerView recentView;
    ProgressDialog progressDialog;
    private FrameLayout frameLayout;
    private SearchAdapter SearchAdapter = new SearchAdapter(searchList);
    private RealtimeBlurView rtv;
   // private RecentAdapter RecentAdapter = new RecentAdapter(searchedList);

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
        tag_chip = (ChipGroup) view.findViewById(R.id.search_tag);
        recentView = view.findViewById(R.id.RecentView);
        recentView.setVisibility(view.INVISIBLE);
        frameLayout = view.findViewById(R.id.frame);
        rtv = view.findViewById(R.id.rtv);
        rtv.setVisibility(view.INVISIBLE);

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
                                        List<String> tags = (List<String>) doc.get("tag");
                                        Recipe_get r = new Recipe_get(rcp_id, thumbnail, title, tags);

//                                        String[] string;
//                                        for (String t : tags) {
//                                            string = (t.split(","));
//                                            for (String s : string) {
//                                                Log.w(TAG, s);
//                                                addChip(s);
//                                            }
//                                        }

                                        searchList.add(r);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        });

            }
        }.start();

        ArrayList<String> recent = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.search_rv);
        RecyclerView recyclerView2 = view.findViewById(R.id.RecentView) ;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView2.setHasFixedSize(true);
        adapter = new SearchAdapter(searchList);
        recyclerView.setAdapter(adapter);
        recentAdapter = new RecentAdapter(recent);
        recyclerView2.setAdapter(recentAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, searchView.getQuery().toString());

                recentView.bringToFront();

                recentView.setVisibility(view.VISIBLE);

                rtv.setVisibility(view.VISIBLE);

                hideKeyboard(getActivity());

                recent.add(query.toString());

                recentAdapter.notifyDataSetChanged();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String searchText = searchView.getQuery().toString();
                searchFilter(searchText);
                recentView.setVisibility(View.INVISIBLE);
                rtv.setVisibility(view.INVISIBLE);
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

    public void addChip(String text){
        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 15,
                getResources().getDisplayMetrics()
        );
        chip =(Chip) this.getLayoutInflater().inflate(R.layout.tag_chip, null, false);
        chip.setText(text);
        chip.setClickable(false);
        chip.setPadding(paddingDp, 0, paddingDp, 0);
        chip.setCheckable(false);
        Log.d("dadf",chip.getText().toString());
        tag_chip.addView(chip);
    }

}
