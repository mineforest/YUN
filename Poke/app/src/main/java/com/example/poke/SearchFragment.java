package com.example.poke;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import java.util.Collections;
import java.util.List;

public class SearchFragment extends Fragment implements TextWatcher {
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
    EditText editText;
    String uid;
    Button clear_btn;
    private TabLayout tabLayout;
    ProgressDialog progressDialog;
    private SearchAdapter SearchAdapter;

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);
        editText = (EditText) view.findViewById(R.id.rec_search);
        editText.addTextChangedListener(this);
        clear_btn = (Button) view.findViewById(R.id.clear_btn);
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
                                Log.d("dadf",editText.getText().toString());
                                if (error != null) {
                                    Log.w(TAG, "Oh ~ no ~");
                                    return;
                                }

                                for (QueryDocumentSnapshot doc : value) {
                                    if(doc.getData().get("name").toString().contains(editText.getText().toString()))
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
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.search_rv);
        recyclerView.setHasFixedSize(true);
        clearText();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SearchAdapter(searchList);
        recyclerView.setAdapter(adapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        editText.setText("");
//                        update(cate, searchList);
                        break;
                    case 1:
                        editText.setText("강정");
//                        cate = tab.getText().toString();
//                        Log.d("dadf",tab.getText().toString());
//                        update(cate, searchList);
                        break;
                    case 2:
                        editText.setText("찜");
                        break;
                    case 3:
                        editText.setText("구이");;
                        break;
                    case 4:
                        editText.setText("조림");
                        break;
                    case 5:
                        editText.setText("케이크");
                        break;
                    case 6:
                        editText.setText("치킨");
                        break;
                    case 7:
                        editText.setText("볶음");
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
                        editText.setText("");
                        break;
                    case 1:
                        editText.setText("강정");
                        break;
                    case 2:
                        editText.setText("찜");
                        break;
                    case 3:
                        editText.setText("구이");;
                        break;
                    case 4:
                        editText.setText("조림");
                        break;
                    case 5:
                        editText.setText("케이크");
                        break;
                    case 6:
                        editText.setText("치킨");
                        break;
                    case 7:
                        editText.setText("볶음");
                        break;


                }
            }
        });
        return view;
    }



    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        String searchText = editText.getText().toString();
        searchFilter(searchText);

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


    private void clearText() {
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==clear_btn)
                {
                    editText.setText(null);
                }
            }
        });
    }

    public void update(String cate, ArrayList<Recipe_get> al){
        //String[] tags = al.toArray(new String[al.size()]);

        if(!cate.equals("전체")){
            SearchArrayList.clear();
            for (Recipe_get rcp : al) {
                if (rcp.getTag().contains(cate)) {
                    //al.add(al.get(i));
                    SearchArrayList.add(rcp);
                    SearchAdapter.notifyDataSetChanged();
                }
            }
        }


    }

}
