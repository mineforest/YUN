package com.example.poke;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class SearchMoreViewActivity extends AppCompatActivity {

    ArrayList<Recipe_get> rcps = new ArrayList<>();
    ArrayList<Recipe_get> filteredList;
    SearchMoreViewAdapter adapter;
    RecyclerView recyclerView;
    TextInputLayout editText;
    TextView tag_name;
    TextView tag_cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_recipes_insearch);
        Intent intent = getIntent();
        rcps = intent.getParcelableArrayListExtra("t_rcps");

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        tag_name = findViewById(R.id.tag_tv);
        tag_cnt = findViewById(R.id.count_result);

        filteredList = new ArrayList<>();
        editText = findViewById(R.id.search_edit);
        editText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchFilter(editText.getEditText().getText().toString());
            }
        });

        recyclerView = findViewById(R.id.more_recyclerview_insearch);
        recyclerView.setHasFixedSize(true);
        adapter = new SearchMoreViewAdapter(rcps);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        int largePadding = getResources().getDimensionPixelSize(R.dimen.more_view_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.more_view_spacing_small);
        recyclerView.addItemDecoration(new MainGridItemDecoration(largePadding, smallPadding));
        recyclerView.setAdapter(adapter);

        tag_name.setText(intent.getCharSequenceExtra("t_name"));
        tag_cnt.setText(adapter.getItemCount() + "개의 레시피");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    public void searchFilter(String searchText) {
        filteredList.clear();
        for (Recipe_get rcp : rcps) {
            if(rcp.getName().contains(searchText)) filteredList.add(rcp);
        }
        adapter.filterList(filteredList);
    }
}