package com.example.project_3;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AttendeeFirebase extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private EditText addEventEditText;
    private ListView eventListView;
    private ArrayList<Event> eventNamesList;
    private EventArrayAdapter eventNamesAdapter;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_homepage);

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");

        eventListView = findViewById(R.id.event_listView);
        eventNamesList = new ArrayList<>();
        eventNamesAdapter = new EventArrayAdapter(this, eventNamesList);
        eventListView.setAdapter(eventNamesAdapter);

        //user = new User(); // Assuming "John Doe" is the user's name

        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    eventNamesList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        String eventName = doc.getId();
                        String date = doc.getString("date"); // Assuming you have a "date" field in your document
                        Event event = new Event(eventName, date); // Use the appropriate constructor

                        // Add event to the user
                        //user.addEvent(event);

                        eventNamesList.add(event);
                    }
                    eventNamesAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}

