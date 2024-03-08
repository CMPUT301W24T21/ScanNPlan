package com.example.project_3;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public class OrganizerActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private EditText addEventEditText;
    private ListView eventList;
    private ArrayList<Event> eventDataList;
    private EventArrayAdapter eventArrayAdapter;
    private static final int ADD_EVENT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_events);

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");

        eventDataList = new ArrayList<>();
        eventList = findViewById(R.id.event_list);
        eventArrayAdapter = new EventArrayAdapter(this, eventDataList);
        eventList.setAdapter(eventArrayAdapter);

        // This info is brought up to the Activity once you click on the event
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event selectedEvent = (Event) eventArrayAdapter.getItem(position);
                if (selectedEvent != null) {
                    Intent intent = new Intent(OrganizerActivity.this, EventDetailsActivity.class);
                    intent.putExtra("eventName", selectedEvent.getName());
                    intent.putExtra("eventLocation", selectedEvent.getLocation());
                    intent.putExtra("eventDate", selectedEvent.getDate());
                    intent.putExtra("eventTime", selectedEvent.getTime());
                    intent.putExtra("imageUri", selectedEvent.getImage());

                    startActivity(intent);
                }
            }
        });

        FloatingActionButton fabAddEvent = findViewById(R.id.fab_add_event);
        fabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEventDialog();
            }
        });

        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    eventDataList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        String event = doc.getId();// for displaying event name in ListView
                        boolean promo = false;
                        boolean reuse = false;
                        String date = "No Date";
                        String time = "No Time";
                        String location = doc.getString("Location");// for displaying location in ListView
                        String details = "No Details";
                        String imageUri = doc.getString("Image");// for displaying location in ListView

                        eventDataList.add(new Event(event, promo, reuse,
                                date, time, location, details,imageUri));
                    }
                    eventArrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void addNewEvent(Event event) {
        eventDataList.add(event);
        eventArrayAdapter.notifyDataSetChanged();

        // Prepare data to be stored in Firebase
        HashMap<String, Object> data = new HashMap<>();
        data.put("Promo", event.getPromo());
        data.put("Reuse", event.getReuse());
        data.put("Date", event.getDate());
        data.put("Time", event.getTime());
        data.put("Location", event.getLocation());
        data.put("Details", event.getDetails());
        data.put("Image", event.getImage());

        // Add the Image field

        // Store data in Firebase
        eventsRef.document(event.getName()).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "Event added with checkbox selections");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error adding event with checkbox selections", e);
                    }
                });
    }
    private String imageUri;
    private void showAddEventDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_add_event, null);

        final EditText addEventEditText = view.findViewById(R.id.add_event_editText);
        final CheckBox promoCheck = view.findViewById(R.id.promoChecker);
        final CheckBox reuseCheck = view.findViewById(R.id.reuseChecker);
        final EditText addEventEditDate = view.findViewById(R.id.add_date_editText);
        final EditText addEventEditTime = view.findViewById(R.id.add_time_editText);
        final EditText addEventEditLocation = view.findViewById(R.id.add_location_editText);
        final EditText addEventEditDetails = view.findViewById(R.id.add_details_editText);

        Button buttonPoster = view.findViewById(R.id.buttonPoster);
        Button buttonLink = view.findViewById(R.id.buttonLink);

        promoCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                buttonPoster.setVisibility(View.VISIBLE);
                buttonLink.setVisibility(View.VISIBLE);
            } else {
                buttonPoster.setVisibility(View.GONE);
                buttonLink.setVisibility(View.GONE);
            }
        });

        // adding a download poster activity
        buttonPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerActivity.this, DownloadPoster.class);
                startActivityForResult(intent, ADD_EVENT_REQUEST);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setTitle("Add an Event")
                .setPositiveButton("OK", (dialog, which) -> {
                    String eventName = addEventEditText.getText().toString();
                    boolean promo_check = promoCheck.isChecked();
                    boolean reuse_check = reuseCheck.isChecked();
                    String eventDate = addEventEditDate.getText().toString();
                    String eventTime = addEventEditTime.getText().toString();
                    String eventLocation = addEventEditLocation.getText().toString();
                    String eventDetails = addEventEditDetails.getText().toString();

                    if (!eventName.isEmpty()) {
                        Event newEvent = new Event(eventName, promo_check, reuse_check,
                                eventDate, eventTime, eventLocation, eventDetails, "");
                        newEvent.setPromo(promo_check);
                        newEvent.setReuse(reuse_check);
                        newEvent.setImage(imageUri);
                        addNewEvent(newEvent);
                    }
                    eventArrayAdapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EVENT_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getStringExtra("imageUri"); // Retrieve the image URI
            }
        }
    }
}
