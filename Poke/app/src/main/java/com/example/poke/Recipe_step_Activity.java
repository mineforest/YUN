package com.example.poke;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;

public class Recipe_step_Activity extends AppCompatActivity {

    private Recipe_get rcp;
    private RecipeStep_Adapter recipeStep_adapter;
    private SpringDotsIndicator springDotsIndicator;
    private Button nextbutton;
    private ViewPager2 viewPager;
    private ArrayList<String> ingres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_step_viewpager);
        Intent intent = getIntent();
        rcp = intent.getParcelableExtra("rcp");
        ingres = intent.getStringArrayListExtra("ingre");

        springDotsIndicator = findViewById(R.id.dots_indicator);
        nextbutton = findViewById(R.id.nextButton);
        viewPager = findViewById(R.id.recipe_viewpager);

        nextbutton.setOnClickListener(nextButtonClickListener);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == recipeStep_adapter.getItemCount()-1) {
                    nextbutton.setText("요리 완료");
                }
                else {
                    nextbutton.setText("다음");
                }
            }
        });

        viewPager.setClipToPadding(false);
        viewPager.setPadding(50,0,50,0);
        viewPager.setOffscreenPageLimit(3);
        viewPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float v = 1-Math.abs(position);
                page.setScaleY(0.8f + v * 0.2f);
            }
        });
        viewPager.setPageTransformer(transformer);
        recipeStep_adapter = new RecipeStep_Adapter(rcp.getRecipe_img(), rcp.getRecipe());
        viewPager.setAdapter(recipeStep_adapter);
        springDotsIndicator.setViewPager2(viewPager);

    }

    View.OnClickListener nextButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int curr = viewPager.getCurrentItem();
            if(curr == recipeStep_adapter.getItemCount()-1) {
                Intent intent = new Intent(getApplicationContext(), RatingActivity.class);

                intent.putExtra("ingre", ingres);
                intent.putExtra("rcp", rcp);
                startActivity(intent);
            }
            else viewPager.setCurrentItem(curr+1,true);
        }
    };
}
