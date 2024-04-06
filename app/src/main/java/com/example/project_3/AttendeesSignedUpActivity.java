package com.example.project_3;

/**
 * This activity displays the list of attendees who have signed up for a particular event.
 */

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

public class AttendeesSignedUpActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private ListView attendeesListView;
    private String eventName;
    private static final String TAG = "AttendeesSignedUpActivity";
    private TextView attendanceCountTextView;
    private ListenerRegistration eventListener;

    /**
     * Initializes the activity layout, Firestore instance, and necessary references.
     * Retrieves the event name from the intent and sets up UI components.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendees_signed_up);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");

        // Get event name from intent
        eventName = getIntent().getStringExtra("event_name");

        // Find views
        Button backButton = findViewById(R.id.back_button);
        attendeesListView = findViewById(R.id.attendees_list_view);

        TextView appbar = findViewById(R.id.appbar_title);
        appbar.setTextSize(22);
        appbar.setText("Attendees Registered");

        attendanceCountTextView = findViewById(R.id.attendance_count_text);

        // Set click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the AttendeesCheckedInActivity and return to the previous activity
                finish();
            }
        });

        // Fetch checked-in attendees and display them in the ListView
        fetchSignedUpAttendees();
    }

    /**
     * Fetches the list of signed-up attendees from Firestore for the current event.
     * Updates the UI with the attendee names.
     */
    private void fetchSignedUpAttendees() {
        // Check if eventName is null
        if (eventName == null) {
            Log.d(TAG, "Event name is null");
            Toast.makeText(this, "Event name is null", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference eventDocRef = eventsRef.document(eventName);

        // Listen for changes in the event document to update attendee list in real-time
        eventListener = eventDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Retrieve the list of signed-up attendees
                    List<DocumentReference> checkedIn = (List<DocumentReference>) documentSnapshot.get("attendees");
                    if (checkedIn != null) {
                        List<String> attendeeNames = new ArrayList<>();
                        for (DocumentReference attendeeRef : checkedIn) {
                            // Get the name of the attendee directly from the reference
                            String attendeeName = attendeeRef.getId();
                            if (attendeeName != null) {
                                attendeeNames.add(attendeeName);
                            }
                        }
                        // Update the UI with the attendee names
                        displayAttendees(attendeeNames);

                    }
                } else {
                    Log.d(TAG, "Event document not found");
                }
            }
        });
    }


    /**
     * Displays the list of signed-up attendees in a ListView.
     *
     * @param attendees List of attendee names to be displayed
     */
    private void displayAttendees(List<String> attendees) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, attendees);
        attendeesListView.setAdapter(adapter);
    }

    /**
     * Cleans up resources when the activity is destroyed.
     * Removes the Firestore listener to avoid memory leaks.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the Firestore listener to avoid memory leaks
        if (eventListener != null) {
            eventListener.remove();
        }
    }
}

