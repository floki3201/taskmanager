package com.example.tttn2023.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tttn2023.fragment.FragmentInfo;
import com.example.tttn2023.fragment.FragmentListJointTask;
import com.example.tttn2023.fragment.FragmentSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BottomNavigationJointTask extends FragmentPagerAdapter {
    private String projectId, ownerId;
    private List<Map<String, String>> listMember = new ArrayList<>();
    public BottomNavigationJointTask(@NonNull FragmentManager fm, int behavior, String projectId, String ownerId, List<Map<String, String>> listMember) {
        super(fm, behavior);
        this.projectId = projectId;
        this.ownerId = ownerId;
        this.listMember = listMember;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return new FragmentListJointTask(projectId, ownerId, listMember);
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
