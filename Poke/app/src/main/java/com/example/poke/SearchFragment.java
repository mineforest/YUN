package com.example.poke;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import com.arasthel.spannedgridlayoutmanager.SpanSize;
import com.arasthel.spannedgridlayoutmanager.SpannedGridLayoutManager;
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

import kotlin.jvm.functions.Function1;

public class SearchFragment extends Fragment {
    private ArrayList<Recipe_get> searchList = new ArrayList<>();
    private TagsAdapter adapter;
    private ArrayList<String> tag_names = new ArrayList<>();
    private ArrayList<ArrayList<Recipe_get>> tag_contents = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);
        tag_names.add("전체");
        tag_contents.add(searchList);

        new Thread() {
            @Override
            public void run() {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("recipe")
                        .addSnapshotListener((value, error) -> {
                            for (QueryDocumentSnapshot doc : value) {
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
                            }
                        });

            }
        }.start();

        RecyclerView recyclerView = view.findViewById(R.id.search_rv);
        recyclerView.setHasFixedSize(true);
        SpannedGridLayoutManager spannedGridLayoutManager = new SpannedGridLayoutManager(SpannedGridLayoutManager.Orientation.VERTICAL, 4);
        spannedGridLayoutManager.setItemOrderIsStable(true);
        spannedGridLayoutManager.setSpanSizeLookup(new SpannedGridLayoutManager.SpanSizeLookup(position -> {
            if(position%7 == 0) return new SpanSize(2,2);
            return new SpanSize(1, 1);
        }));
        recyclerView.setLayoutManager(spannedGridLayoutManager);
        adapter = new TagsAdapter(tag_names,tag_contents);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            String tname = tag_names.get(position);
            ArrayList<Recipe_get> trcps = tag_contents.get(position);
            Intent intent = new Intent(v.getContext(), SearchMoreViewActivity.class);
            if(position == 0) {
                Singleton_global_recipe singleton_global_recipe = Singleton_global_recipe.getInstance();
                singleton_global_recipe.setData(trcps);
            }
            else{
                intent.putExtra("t_rcps",trcps);
            }
            intent.putExtra("t_name",tname);
            v.getContext().startActivity(intent);
        });
        return view;
    }
}
