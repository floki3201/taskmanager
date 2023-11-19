package com.example.tttn2023;

//import static android.os.Build.VERSION_CODES.R;



import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.tttn2023.adapter.BottomNavigationPerTask;
import com.example.tttn2023.model.PerPro;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class TaskActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;
    private ViewPager viewPager;
    private FloatingActionButton fab, fab_back;
    private TabLayout tabLayout;
    private String projectId = "";
    private FirebaseDatabase database;
    private DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        // Check if the Intent contains the "userPerPro" extra
        if (intent.hasExtra("userPerPro")) {
            // Retrieve the Serializable object
            Serializable serializable = intent.getSerializableExtra("userPerPro");

            // Check if the Serializable object is of type PerPro
            if (serializable instanceof PerPro) {
                // Cast it to PerPro
                PerPro perPro = (PerPro) serializable;

                // Now you can use the 'perPro' object as needed
                // For example, you can access its properties:
                projectId = perPro.getId();
                // Do something with perProName
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
                Intent intent = new Intent(TaskActivity.this, AddTaskActivity.class);
                intent.putExtra("projectId", projectId);
                startActivity(intent);
            }
        });
        fab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(TaskActivity.this,Main0Activity.class);
                startActivity(intent);
            }
        });
        BottomNavigationPerTask adapter = new BottomNavigationPerTask(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, projectId);
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