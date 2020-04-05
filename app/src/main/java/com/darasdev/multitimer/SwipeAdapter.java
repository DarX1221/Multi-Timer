package com.darasdev.multitimer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class SwipeAdapter extends FragmentStatePagerAdapter {
    public SwipeAdapter(FragmentManager fm) {
        super (fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new TimerFragment();
        Bundle bundle = new Bundle();

        return fragment;

    }

    @Override
    public int getCount() {
        return 0;
    }
}
