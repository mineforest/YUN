package com.example.poke;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PreferenceFragmentAdapter extends FragmentStateAdapter {
    public PreferenceFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0 ) return new PreferenceFragment();
        else if(position == 1) return new DietFragment();
        else return new AllergyFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
