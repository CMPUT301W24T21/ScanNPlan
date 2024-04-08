package com.example.project_3;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class AttendeeBrowseEventsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private ListView eventList;
    private ArrayList<Event> eventDataList;
    private EventArrayAdapter eventArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_browse_events);

        // Initializing Firebase
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");

        // Initializing the UI components
        eventDataList = new ArrayList<>();
        eventList = findViewById(R.id.event_list);
        eventArrayAdapter = new EventArrayAdapter(this, eventDataList);
        eventList.setAdapter(eventArrayAdapter);

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Event selectedEvent = (Event) eventArrayAdapter.getItem(position);
                if (selectedEvent != null) {
                    String docPath = "Events/" + selectedEvent.getName();
                    AttendeeNewEventDetailsFragment fragment = new AttendeeNewEventDetailsFragment(docPath, Boolean.FALSE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();

                    eventList.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Listening for changes in the Firebase database
        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    // Clearing the list and filling it with retrieved data
                    eventDataList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        // Retrieving the event data from Firestore and adding it to the list
                        String eventId = doc.getId();
                        boolean reuse = doc.getBoolean("Reuse"); // Assuming there's a field named "Reuse" indicating whether the event can be reused
                        String date = doc.getString("Date");
                        String time = doc.getString("Time"); // Assuming there's a field named "Time" for event time
                        String location = doc.getString("Location");
                        String details = doc.getString("Details");
                        String imageUri = doc.getString("Image");
                        String qrCode = doc.getString("QRCode");
                        String qrPromoCode = doc.getString("QRPromoCode");
                        String link = doc.getString("Link");

                        // Initialize the announcements ArrayList if needed
                        ArrayList<Map<String, Object>> announcements = new ArrayList<>();

                        eventDataList.add(new Event(eventId, date, time, location,
                                details, reuse, imageUri, qrCode, qrPromoCode, link, announcements));
                    }
                    eventArrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
