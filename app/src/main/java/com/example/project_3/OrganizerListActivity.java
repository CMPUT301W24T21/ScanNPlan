package com.example.project_3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


// for the list of attendees who have checked in
// to be implemented later
public class OrganizerListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_attendee_list);

        // Find the back button
        Button backButton = findViewById(R.id.back_button);

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
