package com.example.poke;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainMoreViewActivity extends AppCompatActivity {

    ArrayList<Recipe_get> rcps = new ArrayList<>();
    ArrayList<Recipe_get> sub_rcps = new ArrayList<>();
    MainMoreRecipeAdapter adapter;
    RecyclerView recyclerView;
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_recipes);

        Intent intent = getIntent();
        rcps = intent.getParcelableArrayListExtra("rcp");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(intent.getCharSequenceExtra("more_title"));

        recyclerView = findViewById(R.id.more_recyclerview);
        recyclerView.setHasFixedSize(true);
        populateRcp();
        adapter = new MainMoreRecipeAdapter(sub_rcps);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        recyclerView.addItemDecoration(new MainGridItemDecoration(largePadding, smallPadding));
        recyclerView.setAdapter(adapter);
        initScrollListener();

    }

    private void populateRcp() {
        int i =0;
        while (i < 10) {
            sub_rcps.add(rcps.get(i));
            i++;
        }
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                if(!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == sub_rcps.size()-1) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        sub_rcps.add(null);
        adapter.notifyItemInserted(sub_rcps.size()-1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sub_rcps.remove(sub_rcps.size()-1);
                int scrollPosition = sub_rcps.size();
                adapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;
                while ( currentSize-1 < nextLimit && currentSize < rcps.size()) {
                    sub_rcps.add(rcps.get(currentSize));
                    currentSize++;
                }
                adapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }
}
