package com.example.poke;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    private ViewPager2 viewPager2;
    private FragmentStateAdapter pagerAdapter;
    private FirebaseAuth mAuth;
    private long backKey = 0;
    public static Context mContext;
    public static boolean alarm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btm_nav);
        mAuth = FirebaseAuth.getInstance();
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("POKE");

        Intent alarmintent1 = getIntent();
        alarm = alarmintent1.getBooleanExtra("flag",false);
        mContext = this;

        viewPager2 = findViewById(R.id.pager);
        pagerAdapter = new MainPagerAdapter(this);
        viewPager2.setAdapter(pagerAdapter);
        viewPager2.registerOnPageChangeCallback(pageChangeCallback);
        viewPager2.setUserInputEnabled(false);
        mBottomNavigationView=findViewById(R.id.bottom_navigation);

        viewPager2.setCurrentItem(1);
        //첫 화면 띄우기
        mBottomNavigationView.setSelectedItemId(R.id.nav_home);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
        if(System.currentTimeMillis() > backKey + 2000) {
            backKey = System.currentTimeMillis();
            Toast.makeText(MainActivity.this,"한번 더 눌러 종료합니다.", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
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