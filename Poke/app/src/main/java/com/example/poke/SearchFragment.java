package com.example.poke;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements TextWatcher {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private ArrayList<Recipe_get> searchList;
    private FirebaseAuth mAuth;
    SearchAdapter adapter;
    String uid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);
        searchList = new ArrayList<>();
        String[] test_ids = {"1762278", "1762498","1894779", "1899131", "1978049", "2001746",
                "2017354", "2442087", "2528933", "2442087", "2803587", "3568149"};
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for(int i =0;i<test_ids.length; i++) {
            DocumentReference docRef = db.collection("recipe").document(test_ids[i]);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String title = documentSnapshot.getData().get("name").toString();
                    String thumbnail = documentSnapshot.getData().get("thumbnail").toString();
                    Recipe_get r = new Recipe_get(thumbnail, title);
                    searchList.add(r);
                    adapter.notifyDataSetChanged();
                }
            });
        }


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.search_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SearchAdapter(searchList);
        recyclerView.setAdapter(adapter);

        return view;

    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        adapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}

