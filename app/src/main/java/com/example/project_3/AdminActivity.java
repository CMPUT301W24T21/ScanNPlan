package com.example.project_3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.content.Intent;
import android.view.View;


import com.google.android.material.button.MaterialButton;

public class AdminActivity extends AppCompatActivity {
    private MaterialButton manage_profiles;
    private MaterialButton manage_images;
    private MaterialButton manage_events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_homepage);
        this.manage_events = findViewById(R.id.manage_events);
        this.manage_images = findViewById(R.id.manage_images);
        this.manage_profiles = findViewById(R.id.manage_profiles);

        manage_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
