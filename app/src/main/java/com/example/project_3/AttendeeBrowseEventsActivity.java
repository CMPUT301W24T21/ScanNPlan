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
                    AttendeeNewEventDetailsFragment fragment = new AttendeeNewEventDetailsFragment(docPath);
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
                        String event = doc.getId();
                        boolean reuse = false;
                        String date = doc.getString("Date");
                        String time = "No Time";
                        String location = doc.getString("Location");
                        String details = doc.getString("Details");
                        String imageUri = doc.getString("Image");
                        String qrCode = doc.getString("QRCode");
                        String qrPromoCode = doc.getString("QRPromoCode");
                        String link = doc.getString("link");
                        eventDataList.add(new Event(event, date, time, location,
                                details, reuse, imageUri, qrCode, qrPromoCode, link));
                    }
                    eventArrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
