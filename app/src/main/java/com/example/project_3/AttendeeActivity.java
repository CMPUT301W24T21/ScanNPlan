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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        getFcmToken();

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
                                        //Log.d("DEBUG", "DocumentSnapshot data: " + document.getData());

                                        eventArray.add(new Event(document.getId().toString(),
                                                (String) eventDetails.get("Date"),
                                                (String) eventDetails.get("Time"),
                                                (String) eventDetails.get("Location"),
                                                (String) eventDetails.get("Details"),
                                                (boolean) eventDetails.get("Reuse"),
                                                (String) eventDetails.get("Image"),
                                                null,
                                                null,
                                                null,null));
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

//        //test events
//
////        String[] EventNames = {"concert1", "party2", "boardgames3"};
////        String[] EventLocation = {"123 4th st", "567 8th avenue", "91011 12blv"};
////
////
//        ArrayList<Event> dataList= new ArrayList<Event>();
////        for (int i = 0; i < EventNames.length; i++) {
////            boolean details = dataList.add(new Event(EventNames[i], "DATE123", "TIME456", EventLocation[i], "DETAILS789"));
////        }
////
////
////        eventAdapter = new EventArrayAdapter(this, dataList);
////        eventList = findViewById(R.id.event_listView);
////        eventList.setAdapter(eventAdapter);
////        eventList.setOnItemClickListener(listSelector);
//
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("events")
//                //.whereEqualTo("ProfileId", "ProfileEvent ID") // just a template to compare
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                String eventName = document.getString("eventName");
//                                String eventDate = document.getString("eventDate");
//                                String eventTime = document.getString("eventTime");
//                                String eventLocation = document.getString("eventLocation");
//                                String eventDetails = document.getString("eventDetails");
//
//                                Event event = new Event(eventName, eventDate, eventTime, eventLocation, eventDetails);
//                                dataList.add(event); // Add the event to the dataList
//                            }
//                            eventAdapter = new EventArrayAdapter(AttendeeActivity.this, dataList); // Create a new adapter with the updated dataList
//                            eventListView = findViewById(R.id.event_listView);
//                            eventListView.setAdapter(eventAdapter);
//                            eventListView.setOnItemClickListener(listSelector);
//                            eventAdapter.notifyDataSetChanged(); // Notify the adapter that the data set has changed
//                        } else {
//                            //Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//
//
    }

    /**
     * Toggles the visibility of the rest of the page when editing the profile.
     */
    void getFcmToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener((OnCompleteListener<String>) task -> {
            if(task.isSuccessful()){
                String token = task.getResult();
                Log.i("My token", token);
                saveTokenToFirestore(token);
            }
        });
    }
//    void saveTokenToFirestore(String token) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference tokenRef = db.collection("Tokens");
//
//        // Check if document exists and retrieve its data
//        tokenRef.document("tokenz").get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                    // Document exists, retrieve token list and update
//                    List<String> tokensList = (List<String>) document.get("AttendeesTokens");
//                    if (tokensList != null && !tokensList.isEmpty()) {
//                        // Add the new token to the end of the list
//                        tokensList.add(token);
//                    } else {
//                        // Token list is empty, create a new list with the token
//                        tokensList = new ArrayList<>();
//                        tokensList.add(token);
//                    }
//                    // Update Firestore with the modified token list
//                    updateTokenListInFirestore(tokenRef, tokensList);
//                } else {
//                    // Document doesn't exist, create a new one with the token
//                    List<String> tokensList = new ArrayList<>();
//                    tokensList.add(token);
//                    // Update Firestore with the new token list
//                    updateTokenListInFirestore(tokenRef, tokensList);
//                }
//            } else {
//                Log.e("Firestore", "Error getting document", task.getException());
//            }
//        });
//    }
void saveTokenToFirestore(String token) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference tokenRef = db.collection("Tokens");

    // Check if document exists and retrieve its data
    tokenRef.document("tokenz").get().addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                // Document exists, retrieve token list and update
                List<String> tokensList = (List<String>) document.get("AttendeesTokens");
                if (tokensList != null) {
                    // Add the new token to the end of the list
                    tokensList.add(token);
                } else {
                    // Token list is null, create a new list with the token
                    tokensList = new ArrayList<>();
                    tokensList.add(token);
                }
                // Update Firestore with the modified token list
                tokenRef.document("tokenz").update("AttendeesTokens", tokensList)
                        .addOnSuccessListener(aVoid -> Log.d("Firestore", "Token added successfully"))
                        .addOnFailureListener(e -> Log.e("Firestore", "Error adding token", e));
            } else {
                // Document doesn't exist, create a new one with the token
                Map<String, Object> data = new HashMap<>();
                List<String> tokensList = new ArrayList<>();
                tokensList.add(token);
                data.put("OrganizerTokens", tokensList);
                // Update Firestore with the new token list
                tokenRef.document("tokenz").set(data)
                        .addOnSuccessListener(aVoid -> Log.d("Firestore", "New document created with token"))
                        .addOnFailureListener(e -> Log.e("Firestore", "Error creating document", e));
            }
        } else {
            Log.e("Firestore", "Error getting document", task.getException());
        }
    });
}
    void updateTokenListInFirestore(CollectionReference tokenRef, List<String> tokensList) {
        // Create a HashMap to store the token list
        HashMap<String, Object> data = new HashMap<>();
        data.put("AttendeesTokens", tokensList);

        // Update Firestore with the token list
        tokenRef.document("tokenz").set(data)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Token list updated in Firestore"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error updating token list in Firestore", e));
    }


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
