package com.example.project_3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
/*
        // create a new user and set its id to be the ANDROID_ID
        //userId = Settings.Secure.ANDROID_ID;
        profileId = "Test1";
        //user = new User();

        // get the user's profile details from firestore
        db =FirebaseFirestore.getInstance();
        profilesRef = db.collection("Profiles");
        profilesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {

                    for (QueryDocumentSnapshot doc: querySnapshots) {
                        String profileDocId = doc.getId();
                        if (profileDocId.equals(profileId)) {
                            profileDocDetails = doc.getData();
                            //Log.d("DEBUG", "FinalValue" + profileDocDetails);
                            user = new User(new Profile(null,
                                    (String) profileDocDetails.get("name"),
                                    (String) profileDocDetails.get("contact_info"),
                                    (String) profileDocDetails.get("social_link"), (String) profileDocDetails.get("profile_type")));
                            user.getUserProfile().setProfileID(doc.getId());

                        }

                    }
                }
            }
        });
*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AttendeeIntent = new Intent(this, AttendeeActivity.class);
        AdminIntent = new Intent(this, AdminActivity.class);
        OrganizerIntent = new Intent(this, OrganizerActivity.class);

        //This line starts the attendee activity
//        startActivity(AdminIntent);
        startActivity(AttendeeIntent);
//        startActivity(OrganizerIntent);

    }
}

