package com.example.project_3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


// for the list of attendees who have checked in
// to be implemented later
public class OrganizerListActivity extends AppCompatActivity {
    private String eventName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        eventName = extras.getString("event_name");
        setContentView(R.layout.attendees_list);
        // Find the back button
        Button backButton = findViewById(R.id.back_button);
        Button test_map = findViewById(R.id.map_button);
        test_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventMapFragment map = new EventMapFragment(eventName);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.organizer_events_fragment_container, map).addToBackStack(null)
                        .commit();
                findViewById(R.id.rest_attendees_list).setVisibility(View.INVISIBLE);
            }
        });
        // Set click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the AttendeesActivity and return to the previous activity
                finish();
            }
        });
    }
}
