package com.example.project_3;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

public class AttendeesCheckedInActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private ListView attendeesListView;
    private String eventName;
    private static final String TAG = "AttendeesCheckedInActivity";

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

        eventsRef.document(eventName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<DocumentReference> checkedIn = (List<DocumentReference>) document.get("checked_in");
                                List<String> attendeeNames = new ArrayList<>();
                                if (checkedIn != null) {
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
                        } else {
                            Log.d(TAG, "Error fetching event document", task.getException());
                        }
                    }
                });
    }

    private void displayAttendees(List<String> attendees) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, attendees);
        attendeesListView.setAdapter(adapter);
    }
}

