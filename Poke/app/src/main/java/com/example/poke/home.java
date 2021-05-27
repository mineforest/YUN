package com.example.poke;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.os.Bundle;

public class home extends AppCompatActivity {
    private static final String TAG = "Main_Activity";

    private FragmentManager fragmentManager;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btm_nav);

        FragmentManager fragmentManager = getSupportFragmentManager();

        mBottomNavigationView=findViewById(R.id.bottom_navigation);

        //첫 화면 띄우기
        mBottomNavigationView.setSelectedItemId(R.id.nav_home);
        fragmentManager.beginTransaction().add(R.id.frame_container,new MainActivity()).commit();

        //case 함수를 통해 클릭 받을 때마다 화면 변경하기
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (item.getItemId()){
                    case R.id.nav_info :
                        fragmentTransaction.replace(R.id.frame_container,new MyInfoActivity()).commit();
                        break;
                    case R.id.nav_home:
                        fragmentTransaction.replace(R.id.frame_container,new MainActivity()).commit();
                        break;
                    case R.id.nav_ingredient:
                        fragmentTransaction.replace(R.id.frame_container,new Frag3()).commit();
                        break;
                    case R.id.nav_search:
                        fragmentTransaction.replace(R.id.frame_container,new Frag4()).commit();
                        break;

                }
                return true;
            }
        });
    }
}
