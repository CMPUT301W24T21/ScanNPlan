package com.example.project_3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Intent AttendeeIntent;
    private Intent AdminIntent;
    private Intent OrganizerIntent;
    private String profileId;
    private User user;
    private Profile usersProfile;

    private FirebaseFirestore db;
    private CollectionReference profilesRef;
    private ArrayList<DocumentReference> EventRefArray;
    private Map<String, Object> profileDocDetails;
    private boolean doneAsync = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_selector);
        Button Attendee_button = findViewById(R.id.Attendee_button);
        Button Organizer_button = findViewById(R.id.Organizer_button);
        Button Admin_button = findViewById(R.id.Admin_button);

        Attendee_button.setOnClickListener(V -> {
            AttendeeIntent = new Intent(this, AttendeeActivity.class);
            startActivity(AttendeeIntent);
        });
        Organizer_button.setOnClickListener(V -> {
            OrganizerIntent = new Intent(this, OrganizerActivity.class);
            startActivity(OrganizerIntent);
        });
        Admin_button.setOnClickListener(V -> {
            AdminIntent = new Intent(this, AdminActivity.class);
            startActivity(AdminIntent);
        });



//        profileId = "Test1";
//        db = FirebaseFirestore.getInstance();
//        profilesRef = db.collection("Profiles");
//        profilesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot querySnapshots,
//                                @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    Log.e("Firestore", error.toString());
//                    return;
//                }
//                if (querySnapshots != null) {
//                    for (QueryDocumentSnapshot doc : querySnapshots) {
//                        String profileDocId = doc.getId();
//                        if (profileDocId.equals(profileId)) {
//                            profileDocDetails = doc.getData();
//                            user = new User(new Profile(null,
//                                    (String) profileDocDetails.get("name"),
//                                    (String) profileDocDetails.get("contact_info"),
//                                    (String) profileDocDetails.get("social_link"), (String) profileDocDetails.get("profile_type")));
//                            user.getUserProfile().setProfileID(doc.getId());
//                            //get the id and from here start the valid intent lol?
//                            //please work????
//
//                            // Start the AttendeeIntent here
//                            AttendeeIntent = new Intent(MainActivity.this, AttendeeActivity.class);
//                            startActivity(AttendeeIntent);
//                        }
//                    }
//                }
//            }
//        });
    }
}