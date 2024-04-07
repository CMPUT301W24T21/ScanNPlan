package com.example.project_3;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * This class represents the main activity for attendees, displaying a list of events
 * and allowing them to open the camera for scanning QR codes and edit their profile.
 */

public class AttendeeActivity extends AppCompatActivity {
    private FloatingActionButton openCameraButton;

    private Intent QRIntent;
    ArrayList<Event> eventArray;

    private ListView eventListView;
    private EventArrayAdapter eventAdapter;

    private FloatingActionButton editProfileButton;
    private FloatingActionButton announcementsButton;
    private Profile profile;
    private FirebaseFirestore db;
    private String profileID;


    /**
     * Initializes the activity, sets up the layout, and initializes UI elements.
     * @param savedInstanceState The saved instance state bundle.
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_homepage);
        eventListView = findViewById(R.id.event_listView);
        eventArray = new ArrayList<Event>();
        eventAdapter = new EventArrayAdapter(this,  eventArray);
        eventListView.setAdapter(eventAdapter);
        eventListView.setOnItemClickListener(listSelector);


        this.openCameraButton = findViewById(R.id.openCameraButton);

        //sends to the QRscan class

        editProfileButton = findViewById(R.id.EditProfile);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRestOfPageVisibility();
                replaceFragment(new AttendeeEditProfileFragment(profileID));
                //turn the visibility of the page off and turns the other one on
            }
        });

        // Assuming attendeebrowseevents is a button
        FloatingActionButton browseEventsButton = findViewById(R.id.BrowseEventsButton);
        browseEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start the AttendeeBrowseEventsActivity
                Intent intent = new Intent(AttendeeActivity.this, AttendeeBrowseEventsActivity.class);
                startActivity(intent);

                // Optionally, you can finish the current activity if you don't want it to remain in the back stack
                finish();
            }
        });

        announcementsButton = findViewById(R.id.announcementsButton);
        announcementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationIntent = new Intent(getBaseContext(), NotificationActivity.class);
                notificationIntent.putExtra("eventArray", eventArray);
                startActivity(notificationIntent);
            }
        });


        db = FirebaseFirestore.getInstance();
        //get profile details
        profileID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//        profileID = "9a747b30be9a8ed2";
        QRIntent = new Intent(this, QRScan.class);
        QRIntent.putExtra("profileName", profileID);
        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(QRIntent);
                //start the qrscanning page
            }
        });
        db.collection("Profiles").document(profileID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    final int firestore = Log.e("Firestore", error.toString());
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    String contactInfo = documentSnapshot.getString("contact_info");
                    String socialLink = documentSnapshot.getString("social_link");

                    //user = new User(name, contactInfo, socialLink);
                    String profileType = documentSnapshot.getString("profile_type");
                    Log.d("DEBUG", name+contactInfo+socialLink+profileType);
                    ArrayList<DocumentReference> eventRefs = (ArrayList<DocumentReference>) documentSnapshot.getData().get("events");
                    if (eventRefs == null) {
                        eventRefs = new ArrayList<DocumentReference>();
                    }
                    profile = new Profile(name, contactInfo, socialLink, profileType, eventRefs);

                    for (int i = 0; i < (eventRefs.size()); i++) {
                        DocumentReference event = eventRefs.get(i);
                        event.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Map<String, Object> eventDetails = document.getData();
                                        Log.d("DEBUG", "DocumentSnapshot data: " + document.getData());
                                        ArrayList<Map<String, Object>> firebaseAnnouncements = (ArrayList<Map<String, Object>>) eventDetails.get("announcements");
                                        ArrayList<Announcement> localAnnouncementsList = new ArrayList<Announcement>();
                                        for (int i = 0; i < firebaseAnnouncements.size(); i++) {
                                            com.google.firebase.Timestamp firebaseTimestamp = (com.google.firebase.Timestamp) firebaseAnnouncements.get(i).get("timestamp");
                                            Date date = firebaseTimestamp.toDate();
                                            Timestamp timestamp = new Timestamp(date.getTime());
                                            String content = (String) firebaseAnnouncements.get(i).get("content");
                                            String eventName = (String) firebaseAnnouncements.get(i).get("event_name");
                                            localAnnouncementsList.add(new Announcement(timestamp, content, eventName));
                                        }
                                        Log.d("DEBUG", "Announcement Content:" + localAnnouncementsList.toString());
                                        eventArray.add(new Event(document.getId().toString(),
                                                (String) eventDetails.get("Date"),
                                                (String) eventDetails.get("Time"),
                                                (String) eventDetails.get("Location"),
                                                (String) eventDetails.get("Details"),
                                                localAnnouncementsList,
                                                (boolean) eventDetails.get("Reuse"),
                                                (String) eventDetails.get("Image"),
                                                null,
                                                null,
                                                null));
                                        eventAdapter.notifyDataSetChanged();

                                    } else {
                                        Log.d("DEBUG", "No such document");
                                    }
                                } else {
                                    Log.d("DEBUG", "get failed with ", task.getException());
                                }
                            }
                        });

                    }



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
            Event selectedEvent = (Event) eventListView.getItemAtPosition(position);
            Integer EventIndex = position;

            findViewById(R.id.REST_OF_PAGE).setVisibility(View.INVISIBLE);

            AttendeeEventDetailsFragment fragment = new AttendeeEventDetailsFragment(selectedEvent);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.attendee_fragment_container, fragment, null).addToBackStack("test").commit();
        }
    };

}