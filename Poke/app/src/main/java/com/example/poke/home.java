package com.example.poke;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class home extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private BottomNavigationView mBottomNavigationView;
    private ViewPager2 viewPager2;
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btm_nav);
        viewPager2 = findViewById(R.id.pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        pagerAdapter = new MainFragmentAdapter(this);
        viewPager2.setAdapter(pagerAdapter);
        viewPager2.registerOnPageChangeCallback(pageChangeCallback);
        mBottomNavigationView=findViewById(R.id.bottom_navigation);

        //첫 화면 띄우기
        mBottomNavigationView.setSelectedItemId(R.id.nav_home);
        fragmentManager.beginTransaction().add(R.id.frame_container,new MainRecyclerViewFragment()).commit();

        viewPager2.setCurrentItem(1);
        //case 함수를 통해 클릭 받을 때마다 화면 변경하기
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()){
                    case R.id.nav_info :
                        viewPager2.setCurrentItem(0);
                      //  fragmentTransaction.replace(R.id.frame_container,new MyInfoActivity()).commit();
                        break;
                    case R.id.nav_home:
                        viewPager2.setCurrentItem(1);
                       // fragmentTransaction.replace(R.id.frame_container,new MainActivity()).commit();
                        break;
                    case R.id.nav_ingredient:
                        viewPager2.setCurrentItem(2);
                        //fragmentTransaction.replace(R.id.frame_container,new FridgeFragment()).commit();
                        break;
                    case R.id.nav_search:
                        viewPager2.setCurrentItem(3);
                        //fragmentTransaction.replace(R.id.frame_container,new Frag4()).commit();
                        break;

                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager2.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1);
        }
    }

    ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            mBottomNavigationView.getMenu().getItem(position).setChecked(true);
        }
    };
}
