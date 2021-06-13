package com.example.poke;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private BottomNavigationView mBottomNavigationView;
    private ViewPager2 viewPager2;
    private FragmentStateAdapter pagerAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btm_nav);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        ActionBar actionbar = getSupportActionBar();
        //로그인 되어 있지 않으면 로그인 화면으로
        if(user == null) {
            myStartActivity(LoginActivity.class);
        }
        //회원정보가 없으면 회원등록 화면 나옴
        else {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            uid = user.getUid();
            nullStartActivity(uid,"preference");
            nullStartActivity(uid,"users");

            viewPager2 = findViewById(R.id.pager);
            pagerAdapter = new MainPagerAdapter(this);
            viewPager2.setAdapter(pagerAdapter);
            viewPager2.registerOnPageChangeCallback(pageChangeCallback);
            viewPager2.setUserInputEnabled(false);
            FragmentManager fragmentManager = getSupportFragmentManager();
            mBottomNavigationView=findViewById(R.id.bottom_navigation);

            viewPager2.setCurrentItem(1);
            //첫 화면 띄우기
            mBottomNavigationView.setSelectedItemId(R.id.nav_home);

            //case 함수를 통해 클릭 받을 때마다 화면 변경하기
            mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    switch (item.getItemId()){
                        case R.id.nav_info :
                            actionbar.hide();
                            viewPager2.setCurrentItem(0);

                            //  fragmentTransaction.replace(R.id.frame_container,new MyInfoActivity()).commit();
                            break;
                        case R.id.nav_home:
                            actionbar.show();
                            viewPager2.setCurrentItem(1);
                            // fragmentTransaction.replace(R.id.frame_container,new MainActivity()).commit();
                            break;
                        case R.id.nav_ingredient:
                            actionbar.show();
                            viewPager2.setCurrentItem(2);
                            //fragmentTransaction.replace(R.id.frame_container,new FridgeFragment()).commit();
                            break;
                        case R.id.nav_search:
                            actionbar.hide();
                            viewPager2.setCurrentItem(3);
                            //fragmentTransaction.replace(R.id.frame_container,new Frag4()).commit();
                            break;

                    }
                    return true;
                }
            });
        }
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

    private void nullStartActivity(String uid, String child){
        mDatabase.child(child).child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if(dataSnapshot != null) {
                        if (dataSnapshot.exists()) {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        } else {
                            if(child.equals("preference")){
                                myStartActivity(PreferenceActivity.class);
                            }
                        }
                    }
                }
                else {
                    Log.e("firebase", "Error getting data", task.getException());
                }
            }
        });
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(getApplicationContext(),c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
