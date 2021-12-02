package com.example.poke;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;

public class PreferenceActivity extends AppCompatActivity implements AllergyFragment.AllergyListener, PreferenceFragment.PreListener{
    private FragmentManager fragmentManager;
    private DatabaseReference mDatabase;
    private String uid;
    private ArrayList<String> preList, allergyList = new ArrayList<>();
    private ViewPager2 viewPager2;
    private FragmentStateAdapter pagerAdapter;
    private SpringDotsIndicator dotsIndicator;
    private StartFragment startFragment;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_main_fragment);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
            uid = user.getUid();
        dotsIndicator = findViewById(R.id.dots_indicator);
        viewPager2 = findViewById(R.id.pager2);
        pagerAdapter=new PreferenceFragmentAdapter(this);
        viewPager2.registerOnPageChangeCallback(pageChangeCallback);
        startFragment = new StartFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container2,startFragment).commit();
        viewPager2.setUserInputEnabled(false);
    }

    public void next(int page){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch(page){
            case 0:
                fragmentManager.beginTransaction().remove(startFragment).commit();
                viewPager2.setAdapter(pagerAdapter);
                dotsIndicator.setViewPager2(viewPager2);
                dotsIndicator.setVisibility(View.VISIBLE);
                viewPager2.setCurrentItem(0);
                break;

            case 1:
                viewPager2.setCurrentItem(1);
                break;

            case 2:
                if(preList.size() <= 1){
                   startToast("좋아하는 음식을 2가지 이상 선택해주세요.");
                }
                else {
                    for (String list : preList)
                        mDatabase.child("preference").child(uid).push().setValue(new UserPreference(list));
                    for (String list : allergyList)
                        mDatabase.child("allergy").child(uid).child(list).setValue(new UserAllergy(list));
                    myStartActivity(MainActivity.class);
                }
        }
    }

    public void pre(int page){
        switch (page){
            case 0:
                viewPager2.setCurrentItem(0);
                break;
            case -1:
                dotsIndicator.setVisibility(View.GONE);
                viewPager2.setAdapter(null);
                fragmentManager.beginTransaction().replace(R.id.frame_container2,startFragment).commit();
                break;
        }
    }

    @Override
    public void allergyListener(ArrayList allergy) {
        allergyList = allergy;
    }

    @Override
    public void preListener(ArrayList pre) {
        preList = pre;
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        int vp =viewPager2.getCurrentItem();
        if (vp == 0) {
            super.onBackPressed();
            dotsIndicator.setVisibility(View.GONE);
            viewPager2.setAdapter(null);
//            fragmentManager.beginTransaction().replace(R.id.frame_container2,startFragment).commit();
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } else if(vp == 1 ) {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1);
        }
        else{
//            myStartActivity(MainActivity.class);
//            super.onBackPressed();
//            moveTaskToBack(true);

        }
    }

    ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
        }
    };

    private void startToast(String msg){
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }
}
