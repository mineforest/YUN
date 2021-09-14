package com.example.poke;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

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
    EditText editText;
    String uid;
    ImageView clear_btn;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);
        editText = (EditText) view.findViewById(R.id.rec_search);
        editText.addTextChangedListener(this);
        ArrayList<Object> filteredList = new ArrayList<>();

//        progressDialog = new ProgressDialog(getActivity());
//
//        progressDialog.show();
//        progressDialog.setContentView(R.layout.progress_dialog);
//        progressDialog.getWindow().setBackgroundDrawableResource(
//                android.R.color.transparent
//        );
//        progressDialog.setCancelable(false);

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
                                        Recipe_get r = new Recipe_get(rcp_id, thumbnail, title);
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
                editText.setText(null);
            }
        });
    }


}
