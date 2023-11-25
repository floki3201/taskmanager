package com.example.tttn2023;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.tttn2023.adapter.BottomNavigationJointTask;
import com.example.tttn2023.model.JointProject;
import com.example.tttn2023.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JointTaskActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;
    private ViewPager viewPager;
    private FloatingActionButton fab, fab_back;
    private TabLayout tabLayout;
    private String projectId = "";
    private List<Map<String, String>> listMember = new ArrayList<>();
    private String ownerId = "";
    private FirebaseDatabase database;
    private DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        if (intent.hasExtra("userJointPro")) {

            Parcelable parcelable = intent.getParcelableExtra("userJointPro");
            if (parcelable instanceof JointProject) {
                JointProject jointPro = (JointProject) parcelable;
                projectId = jointPro.getId();
                ownerId = jointPro.getOwnerId();
                listMember = jointPro.getListMember();
            }
        }
        if (intent.hasExtra("memberList")) {
            Serializable serializable = intent.getSerializableExtra("memberList");
            if (serializable instanceof List<?>) {
                listMember = (List<Map<String, String>>) serializable;
            }

        }
        if (intent.hasExtra("ownerId")) {
            Serializable serializable = intent.getSerializableExtra("ownerId");
            if (serializable instanceof String) {
                ownerId = (String) serializable;
            }
        }

        navigationView = findViewById(R.id.bottom_nav);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        fab = findViewById(R.id.fab);
        fab_back = findViewById(R.id.fab_back);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ownerId.equals(User.getCurrent_user().getUid())) {
                    Intent intent = new Intent(JointTaskActivity.this, AddJointTaskActivity.class);
                    intent.putExtra("projectId", projectId);
                    intent.putExtra("memberList", (Serializable) listMember);
                    startActivity(intent);
                }else{
                    Toast.makeText(JointTaskActivity.this, "Bạn không có quyền thêm công việc", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(JointTaskActivity.this, JointProActivity.class);
                startActivity(intent);
            }
        });
        BottomNavigationJointTask adapter = new BottomNavigationJointTask(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, projectId, ownerId, listMember);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0: navigationView.getMenu().findItem(R.id.mHome).setChecked(true);
                        break;
                    case 1: navigationView.getMenu().findItem(R.id.mSearch).setChecked(true);
                        break;
                    case 2: navigationView.getMenu().findItem(R.id.mInfo).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case  R.id.mHome:viewPager.setCurrentItem(0);
                        break;
                    case  R.id.mSearch:viewPager.setCurrentItem(1);
                        break;
                    case  R.id.mInfo:viewPager.setCurrentItem(2);
                        break;
                }
                return true;

            }
        });
    }

    public void showFab() {
        fab.show();
    }

    public void hideFab() {
        fab.hide();
    }

    @Override
    public void onBackPressed() {
        // Do nothing to prevent going back
    }
}
