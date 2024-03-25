package com.example.project_3;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This class represents the main activity for attendees, displaying a list of events
 * and allowing them to open the camera for scanning QR codes and edit their profile.
 */

public class AttendeeActivity extends AppCompatActivity {
    private Button openCameraButton;

    private Intent QRIntent;

    private ListView eventList;
    private EventArrayAdapter eventAdapter;

    private ExtendedFloatingActionButton editProfileButton;

    /**
     * Initializes the activity, sets up the layout, and initializes UI elements.
     * @param savedInstanceState The saved instance state bundle.
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_homepage);
        this.openCameraButton = findViewById(R.id.openCameraButton);
        QRIntent = new Intent(this, QRScan.class);
        //sends to the QRscan class

        editProfileButton = findViewById(R.id.EditProfile);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRestOfPageVisibility();
                replaceFragment(new AttendeeEditProfileFragment());
                //turn the visibility of the page off and turns the other one on
            }
        });


        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(QRIntent);
                //start the qrscanning page
            }
        });

        //test events
        
//        String[] EventNames = {"concert1", "party2", "boardgames3"};
//        String[] EventLocation = {"123 4th st", "567 8th avenue", "91011 12blv"};
//
//
        ArrayList<Event> dataList= new ArrayList<Event>();
//        for (int i = 0; i < EventNames.length; i++) {
//            boolean details = dataList.add(new Event(EventNames[i], "DATE123", "TIME456", EventLocation[i], "DETAILS789"));
//        }
//
//
//        eventAdapter = new EventArrayAdapter(this, dataList);
//        eventList = findViewById(R.id.event_listView);
//        eventList.setAdapter(eventAdapter);
//        eventList.setOnItemClickListener(listSelector);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events")
                //.whereEqualTo("ProfileId", "ProfileEvent ID") // just a template to compare
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String eventName = document.getString("eventName");
                                String eventDate = document.getString("eventDate");
                                String eventTime = document.getString("eventTime");
                                String eventLocation = document.getString("eventLocation");
                                String eventDetails = document.getString("eventDetails");

                                Event event = new Event(eventName, eventDate, eventTime, eventLocation, eventDetails);
                                dataList.add(event); // Add the event to the dataList
                            }
                            eventAdapter = new EventArrayAdapter(AttendeeActivity.this, dataList); // Create a new adapter with the updated dataList
                            eventList = findViewById(R.id.event_listView);
                            eventList.setAdapter(eventAdapter);
                            eventList.setOnItemClickListener(listSelector);
                            eventAdapter.notifyDataSetChanged(); // Notify the adapter that the data set has changed
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    /**
     * Toggles the visibility of the rest of the page when editing the profile.
     */



    private void toggleRestOfPageVisibility() {
        View restOfPage = findViewById(R.id.REST_OF_PAGE);
        if (restOfPage.getVisibility() == View.VISIBLE) {
            restOfPage.setVisibility(View.INVISIBLE);
        } else {
            restOfPage.setVisibility(View.VISIBLE);
        }
    }

    /**
     *Replaces the current fragment with a new fragment for editing the current attendee's profile.
     * @param fragment The fragment to replace the current fragment with.
     */

    // Added replaceFragment method
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.attendee_fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Handles the click event for items in the event list.
     */

    AdapterView.OnItemClickListener listSelector  = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Event selectedEvent = (Event) eventList.getItemAtPosition(position);
            Integer EventIndex = position;

            findViewById(R.id.REST_OF_PAGE).setVisibility(View.INVISIBLE);

            AttendeeEventDetailsFragment fragment = new AttendeeEventDetailsFragment(selectedEvent);
            getSupportFragmentManager().beginTransaction()
            .add(R.id.attendee_fragment_container, fragment, null).addToBackStack("test").commit();
        }
    };

}
