package com.example.project_3;

/**
 * This activity displays the list of attendees who have checked in to a particular event.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttendeesSignedUpActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private ListView attendeesListView;
    private String eventName;
    private static final String TAG = "AttendeesSignedUpActivity";
    private TextView attendanceCountTextView;
    private ListenerRegistration eventListener;
    private Button confirmButton;
    private TextView maxAttendeesEditText;

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

        // Get event name from intent
        eventName = getIntent().getStringExtra("event_name");

        // Find views
        Button backButton = findViewById(R.id.back_button);
        maxAttendeesEditText = findViewById(R.id.max_attendees_edittext);
        confirmButton = findViewById(R.id.confirm_button);
        attendeesListView = findViewById(R.id.attendees_list_view);
        TextView appbar = findViewById(R.id.appbar_title);
        appbar.setTextSize(22);
        appbar.setText("Attendees Signed Up");
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
     * Fetches the list of checked-in attendees from Firestore for the current event.
     * Updates the UI with the attendee names and real-time attendance count.
     */
    private void fetchSignedUpAttendees() {
        // Check if eventName is null
        if (eventName == null) {
            Log.d(TAG, "Event name is null");
            Toast.makeText(this, "Event name is null", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference eventDocRef = db.collection("Events").document(eventName);

        // Listen for changes in the event document to update attendee list and attendance count in real-time
        eventListener = eventDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Retrieve the list of checked-in attendees
                    List<DocumentReference> signedUp = (List<DocumentReference>) documentSnapshot.get("attendees");
                    if (signedUp != null) {
                        List<String> attendeeNames = new ArrayList<>();
                        for (DocumentReference attendeeRef : signedUp) {
                            // Fetch the attendee document to get the 'name' field
                            attendeeRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot attendeeSnapshot = task.getResult();
                                        if (attendeeSnapshot.exists()) {
                                            String attendeeName = attendeeSnapshot.getString("name");
                                            if (attendeeName != null) {
                                                attendeeNames.add(attendeeName);
                                                // Update the UI with the attendee names
                                                displayAttendees(attendeeNames);
                                                // Update real-time attendance count
                                                updateRealTimeAttendance(attendeeNames.size());
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                } else {
                    Log.d(TAG, "Event document not found");
                }
            }
        });
    }



    /**
     * Displays the list of attendees in a ListView and sets click listeners for each item.
     *
     * @param attendees List of attendee names to be displayed
     */

    private void displayAttendees(List<String> attendees) {
        // Create an ArrayAdapter to fill the ListView with attendee names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, attendees);
        attendeesListView.setAdapter(adapter);

        // Set click listener for the attendees list items to display details of the clicked attendee
        attendeesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedAttendee = attendees.get(position);
                // Display a dialog with details of the clicked attendee
                showAttendeeDetailsDialog(clickedAttendee);
            }
        });
    }


    /**
     * Displays a dialog with details of the selected attendee.
     * Fetches the attendee details from Firestore and presents them in the dialog.
     *
     * @param attendeeName Name of the selected attendee
     */
    private void showAttendeeDetailsDialog(String attendeeName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Fetch the attendee document from Firestore
        db.collection("Profiles")
                .document(attendeeName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot profileSnapshot = task.getResult();
                            if (profileSnapshot.exists()) {
                                // Retrieve the checked-in events map from the profile document
                                Object checkedInEventsObj = profileSnapshot.get("checked_in_events");
                                if (checkedInEventsObj instanceof Map) {
                                    Map<String, Long> checkedInEvents = (Map<String, Long>) checkedInEventsObj;
                                    Long checkInCount = checkedInEvents.get(eventName);
                                    if (checkInCount != null) {
                                        // Retrieve the name field from the profile document
                                        int count = checkInCount.intValue();
                                        String profileName = profileSnapshot.getString("name"); // Retrieve the 'name' field
                                        builder.setTitle("Attendee Details")
                                                .setMessage("Name: " + profileName + "\nCheck-in Count: " + count)
                                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .show();
                                        return;
                                    }
                                }
                                // If the checked_in_events field is not a map or if the event name is not found
                                // Show an error message
                                Toast.makeText(AttendeesSignedUpActivity.this, "Check-in count not found for this event", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "Profile document not found");
                                // Show an error message if the profile document cannot be found
                                Toast.makeText(AttendeesSignedUpActivity.this, "Profile document not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "Error fetching profile details", task.getException());
                            // Show an error message if profile details cannot be fetched
                            Toast.makeText(AttendeesSignedUpActivity.this, "Failed to fetch profile details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /**
     * Updates the real-time attendance count displayed in the UI.
     *
     * @param count The number of attendees currently checked in
     */
    private void updateRealTimeAttendance(int count) {
        attendanceCountTextView.setText("Total sign ups: " + count);
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
