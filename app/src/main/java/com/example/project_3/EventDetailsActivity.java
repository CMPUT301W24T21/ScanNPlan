package com.example.project_3;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_3.R;

public class EventDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // Get the selected city name passed from MainActivity
        String eventName = getIntent().getStringExtra("eventName");


        // Set the selected city name to the TextView
        TextView eventTextView = findViewById(R.id.event_name_text_view);
        eventTextView.setText(eventName);

        // Set click listener for the back button
        Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish this activity to go back to MainActivity
                finish();
            }
        });
    }
}