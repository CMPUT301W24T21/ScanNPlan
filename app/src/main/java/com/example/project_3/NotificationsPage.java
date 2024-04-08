package com.example.project_3;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Activity for displaying notifications received from Firebase Firestore.
 */

public class NotificationsPage extends AppCompatActivity {
    private ListView notificationsListView;
    private ArrayAdapter<String> adapter;
    private List<String> fieldNames;

    private static final String TAG = "NotifActivity";

    /**
     * Called when the activity is created. Initializes the ListView and fetches notification field names from Firestore.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_notifications_page);

        // Initialize ListView and adapter
        notificationsListView = findViewById(R.id.notifications_list);
        fieldNames = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fieldNames);
        notificationsListView.setAdapter(adapter);

        // Fetch field names
        fetchAnnouncements();

        Button backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Fetches notification field names from Firebase Firestore and updates the ListView.
     */
    private void fetchAnnouncements() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference eventsCollection = db.collection("Events");
        eventsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                fieldNames.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Access the "announcements" field from each document
                    List<Map<String, Object>> milestones = (List<Map<String, Object>>) document.get("Milestones");
                    if (milestones != null) {
                        for (Map<String, Object> milestone : milestones) {
                            // Access each announcement
                            String content = (String) milestone.get("content");
                            String eventName = (String) milestone.get("event_name");
                            Timestamp timestamp = (Timestamp) milestone.get("timestamp");

                            // Format the timestamp to display both date and time
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            String dateTimeString = sdf.format(timestamp.toDate());

                            // Format the announcement data for display
                            String announcementText = "Event: " + eventName + "\n"
                                    + content + "\n"
                                    + "Date & Time: " + dateTimeString;


                            // Add the formatted announcement to the list
                            fieldNames.add(announcementText);
                        }
                    }
                }
                adapter.notifyDataSetChanged(); // Update UI with the new data
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }
}
