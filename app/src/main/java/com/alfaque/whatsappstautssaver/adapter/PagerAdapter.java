package com.alfaque.whatsappstautssaver.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.alfaque.whatsappstautssaver.fragments.VideosFragment;
import com.alfaque.whatsappstautssaver.fragments.ImagesFragment;

import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {
    List<String> titles;

    public PagerAdapter(@NonNull FragmentManager fm, List<String> titles) {
        super(fm);
        this.titles = titles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ImagesFragment();
            case 1:
                return new VideosFragment();
            default:
                return new ImagesFragment();

        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
