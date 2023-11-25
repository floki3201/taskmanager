package com.example.tttn2023.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tttn2023.fragment.FragmentInfo;
import com.example.tttn2023.fragment.FragmentListPerPro;
import com.example.tttn2023.fragment.FragmentSearch;

public class BottomNavigationPerPro extends FragmentPagerAdapter {

    public BottomNavigationPerPro(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
//                return new FragmentListTask();
                return new FragmentListPerPro();
            case 1:return new FragmentSearch();
            case 2:return new FragmentInfo();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
