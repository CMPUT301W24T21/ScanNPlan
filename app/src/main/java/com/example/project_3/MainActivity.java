package com.example.project_3;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Intent AttendeeIntent;
    private Intent AdminIntent;
    private Intent OrganizerIntent;
    private String profileId;
    private User user;
    private FirebaseFirestore db;
    private CollectionReference profilesRef;
    private Map<String, Object> profileDocDetails;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AttendeeIntent = new Intent(this, AttendeeActivity.class);
        OrganizerIntent = new Intent(this, OrganizerActivity.class);
        AdminIntent = new Intent(this, AdminActivity.class);
//
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


//
        //profileId = "This is an admin profile";
        profileId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("DEBUG", profileId);

        db = FirebaseFirestore.getInstance();
        profilesRef = db.collection("Profiles");
        Log.d("DEBUG", "about to connect to DB");

        profilesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    boolean idFound = false;
                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        String profileDocId = doc.getId();
                        if (profileDocId.equals(profileId)) {
                            idFound = true;
                            profileDocDetails = doc.getData();
                            user = new User(new Profile(null,
                                    (String) profileDocDetails.get("name"),
                                    (String) profileDocDetails.get("contact_info"),
                                    (String) profileDocDetails.get("social_link"),
                                    (String) profileDocDetails.get("profile_type")));
                            user.getUserProfile().setProfileID(doc.getId());
                            //get the id and from here start the valid intent lol?
                            //please work????
                            Log.d("DEBUG", user.getUserProfile().getProfileType());
                            if (Objects.equals(user.getUserProfile().getProfileType(), "Attendee")){
                                startActivity(AttendeeIntent);
                            } else if (Objects.equals(user.getUserProfile().getProfileType(), "Organizer")) {
                                startActivity(OrganizerIntent);
                            } else if (Objects.equals(user.getUserProfile().getProfileType(), "Admin")) {
                                startActivity(AdminIntent);
                            }

                            // Start the AttendeeIntent here

//                            AttendeeIntent = new Intent(MainActivity.this, AttendeeActivity.class);
//                            startActivity(AttendeeIntent);
                        }
                    }
                    if (!idFound){
                        user = new User(new Profile(null,
                                                "New User!",
                                                "",
                                                "",
                                                "Attendee"));
                        user.getUserProfile().setProfileID(profileId);

                        HashMap<String, Object> profileData = new HashMap<>();
                        profileData.put("profile_picture", user.getUserProfile().getProfile_picture());
                        profileData.put("name", user.getUserProfile().getName());
                        profileData.put("contact_info", user.getUserProfile().getContact_info());
                        profileData.put("social_link", user.getUserProfile().getSocial_link());
                        profileData.put("profile_type", user.getUserProfile().getProfileType());
                        profilesRef.document(profileId).set(profileData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Firestore", "Successfully created profile!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("Firestore", "Failed to create profile!", e);
                                    }
                                });
                    }
                }
            }
        });
    }
}
//
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.util.Log;
//import android.widget.Button;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.Map;
//
//public class MainActivity extends AppCompatActivity {
//    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 101;
//    private Intent AttendeeIntent;
//    private Intent AdminIntent;
//    private Intent OrganizerIntent;
//    private String profileId;
//    private User user;
//    private FirebaseFirestore db;
//    private CollectionReference profilesRef;
//    private Map<String, Object> profileDocDetails;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.account_selector);
//
//        // Check if the app has notification permission
//        if (!hasNotificationPermission()) {
//            // Request notification permission
//            requestNotificationPermission();
//        }
//
//
//        AttendeeIntent = new Intent(this, AttendeeActivity.class);
//        OrganizerIntent = new Intent(this, OrganizerActivity.class);
//        AdminIntent = new Intent(this, AdminActivity.class);
////
//        setContentView(R.layout.account_selector);
//        Button Attendee_button = findViewById(R.id.Attendee_button);
//        Button Organizer_button = findViewById(R.id.Organizer_button);
//        Button Admin_button = findViewById(R.id.Admin_button);
//
//        Attendee_button.setOnClickListener(V -> {
//            AttendeeIntent = new Intent(this, AttendeeActivity.class);
//            startActivity(AttendeeIntent);
//        });
//        Organizer_button.setOnClickListener(V -> {
//            OrganizerIntent = new Intent(this, OrganizerActivity.class);
//            startActivity(OrganizerIntent);
//        });
//        Admin_button.setOnClickListener(V -> {
//            AdminIntent = new Intent(this, AdminActivity.class);
//            startActivity(AdminIntent);
//        });
//    }
//
//    // Method to check if the app has notification permission
//    private boolean hasNotificationPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            return notificationManager != null && notificationManager.getNotificationChannel(String.valueOf(NotificationManager.IMPORTANCE_DEFAULT)) != null;
//        }
//        return true; // On versions lower than Oreo, no permission is required.
//    }
//
//    // Method to request notification permission
//    private void requestNotificationPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Request permission for notification
//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
//                    NOTIFICATION_PERMISSION_REQUEST_CODE);
//        } else {
//            // No permission required for pre-Oreo devices
//            Log.d("MainActivity", "Notification permission not required for pre-Oreo devices.");
//        }
//    }
//
//    // Override onRequestPermissionsResult to handle permission request result
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Notification permission granted
//                Log.d("MainActivity", "Notification permission granted.");
//            } else {
//                // Notification permission denied
//                Log.d("MainActivity", "Notification permission denied.");
//            }
//        }
//    }
//
//    // Rest of your MainActivity class...
//}
