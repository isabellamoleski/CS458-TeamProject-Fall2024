package com.example.habittrackerapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.habittrackerapp.BuildFragment;
import com.example.habittrackerapp.QuitFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new QuitFragment();
            case 0:
            default:
                return new BuildFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
