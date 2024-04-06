package com.example.project_3;

/**
 * This activity displays the list of attendees who have checked in.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Initializes the activity layout and retrieves event name from intent extras.
 * Sets up the back button and map button click listeners.
 */
public class OrganizerListActivity extends AppCompatActivity {
    private String eventName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve event name from intent extras
        Bundle extras = getIntent().getExtras();
        eventName = extras.getString("event_name");

        // Set the layout for the activity
        setContentView(R.layout.attendees_list);

        // Find the back button
        Button backButton = findViewById(R.id.back_button);
        Button test_map = findViewById(R.id.map_button);

        // Set click listener for the map button to show event map
        test_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with the event map fragment
                EventMapFragment map = new EventMapFragment(eventName);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.organizer_events_fragment_container, map).addToBackStack(null)
                        .commit();
                // Hide the attendees list view
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
