package com.example.project_3;


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

public class AttendeesCheckedInActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private ListView attendeesListView;
    private String eventName;
    private static final String TAG = "AttendeesCheckedInActivity";
    private TextView attendanceCountTextView;
    private ListenerRegistration eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendees_checked_in);

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
        appbar.setText("Attendees Checked In");
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
        fetchCheckedInAttendees();
    }

    private void fetchCheckedInAttendees() {
        // Check if eventName is null
        if (eventName == null) {
            Log.d(TAG, "Event name is null");
            Toast.makeText(this, "Event name is null", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference eventDocRef = eventsRef.document(eventName);

        eventListener = eventDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    List<DocumentReference> checkedIn = (List<DocumentReference>) documentSnapshot.get("checked_in");
                    int realTimeAttendance = checkedIn != null ? checkedIn.size() : 0;

                    // Update real-time attendance count
                    updateRealTimeAttendance(realTimeAttendance);

                    if (checkedIn != null) {
                        List<String> attendeeNames = new ArrayList<>();
                        for (DocumentReference doc : checkedIn) {
                            // Fetch the attendee document from Firestore
                            doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot attendeeDoc = task.getResult();
                                        if (attendeeDoc.exists()) {
                                            // Get the name of the attendee
                                            String attendeeName = attendeeDoc.getString("name");
                                            if (attendeeName != null) {
                                                attendeeNames.add(attendeeName);
                                                displayAttendees(attendeeNames);

                                                // Update real-time attendance count after all attendee names are fetched
                                                updateRealTimeAttendance(attendeeNames.size());
                                            }
                                        } else {
                                            Log.d(TAG, "Attendee document not found");
                                        }
                                    } else {
                                        Log.d(TAG, "Error fetching attendee document", task.getException());
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

    private void displayAttendees(List<String> attendees) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, attendees);
        attendeesListView.setAdapter(adapter);

        // Set click listener for the attendees list items
        attendeesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedAttendee = attendees.get(position);
                // Display a dialog with details of the clicked attendee
                showAttendeeDetailsDialog(clickedAttendee);
            }
        });
    }


    private void showAttendeeDetailsDialog(String attendeeName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Fetch the attendee document from Firestore
        db.collection("Attendees")
                .whereEqualTo("name", attendeeName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int checkInCount = task.getResult().size();
                            builder.setTitle("Attendee Details")
                                    .setMessage("Name: " + attendeeName + "\nCheck-in Count: " + checkInCount)
                                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        } else {
                            Log.d(TAG, "Error fetching attendee details", task.getException());
                            // Show an error message if attendee details cannot be fetched
                            Toast.makeText(AttendeesCheckedInActivity.this, "Failed to fetch attendee details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void updateRealTimeAttendance(int count) {
        attendanceCountTextView.setText("Real time attendance: " + count);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the Firestore listener to avoid memory leaks
        if (eventListener != null) {
            eventListener.remove();
        }
    }
}

