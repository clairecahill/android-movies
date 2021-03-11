package com.example.android.sqliteweather;

import android.graphics.Movie;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PageAdapter extends FragmentStateAdapter {
    public PageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new MovieFragment();
            default:
                return new TVShowFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
