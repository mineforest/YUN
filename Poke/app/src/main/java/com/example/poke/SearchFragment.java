package com.example.poke;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.os.Bundle;

import android.provider.SearchRecentSuggestions;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArraySet;
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
import androidx.recyclerview.widget.GridLayoutManager;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchFragment extends Fragment {
    private static final String TAG = "tag";
    private ArrayList<Recipe_get> searchList = new ArrayList<>();
    private ArrayList<Recipe_get> searchedList = new ArrayList<>();
    private TagsAdapter adapter;
    private ArrayList<Recipe_get> SearchArrayList = new ArrayList<>();
    public static SearchView searchView;
    private FrameLayout frameLayout;
    private SearchAdapter SearchAdapter = new SearchAdapter(searchList);
    private ArrayList<String> tag_names = new ArrayList<>();
    private ArrayList<ArrayList<Recipe_get>> tag_contents = new ArrayList<>();

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
        frameLayout = view.findViewById(R.id.frame);
        tag_names.add("전체");
        tag_contents.add(new ArrayList<Recipe_get>());

        new Thread() {
            @Override
            public void run() {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("recipe")
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
                                        searchList.add(r);
                                        for (String tag : tags.get(0).split(", ")){
                                            int idx=tag_names.indexOf(tag);
                                            if(idx==-1){
                                                ArrayList<Recipe_get> rcp = new ArrayList<>();
                                                rcp.add(r);
                                                tag_names.add(tag);
                                                tag_contents.add(rcp);
                                            }
                                            else {
                                                ArrayList<Recipe_get> rcps=tag_contents.get(idx);
                                                rcps.add(r);
                                                tag_contents.set(idx,rcps);
                                            }
                                        }
                                        adapter.notifyDataSetChanged();
                                        Log.d("DFDFDF",adapter.getItemCount()+"");
                                    }
                                }
                            }
                        });

            }
        }.start();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.search_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        adapter = new TagsAdapter(tag_names,tag_contents);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, searchView.getQuery().toString());

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
        return view;
    }


    public void searchFilter(String searchText) {
        searchedList.clear();

        for (int i = 0; i < searchList.size(); i++) {
            if (searchList.get(i).getName().toLowerCase().contains(searchText.toLowerCase())) {
                searchedList.add(searchList.get(i));
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }


    public void clicktag(@NonNull String cate, ArrayList<Recipe_get> al) {
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
