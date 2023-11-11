package com.example.tttn2023.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tttn2023.fragment.FragmentInfo;
import com.example.tttn2023.fragment.FragmentListTask;
import com.example.tttn2023.fragment.FragmentSearch;

public class BottomNavigationPerTask extends FragmentPagerAdapter {
    private String projectId;

    public BottomNavigationPerTask(@NonNull FragmentManager fm, int behavior, String projectId) {
        super(fm, behavior);
        this.projectId = projectId;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return new FragmentListTask(projectId);
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
