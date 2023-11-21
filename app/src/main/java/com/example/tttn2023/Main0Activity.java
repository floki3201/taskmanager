package com.example.tttn2023;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Main0Activity extends AppCompatActivity {
    private CardView jointPro, personalPro;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main0);
        jointPro = findViewById(R.id.JointProject);
        personalPro = findViewById(R.id.PersonalProject);
        jointPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main0Activity.this, JointProActivity.class);
                startActivity(intent);
            }
        });
        personalPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main0Activity.this, PerProActivity.class);
                startActivity(intent);
            }
        });
    }

}
