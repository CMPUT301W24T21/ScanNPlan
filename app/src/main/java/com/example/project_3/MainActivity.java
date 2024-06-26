package com.example.project_3;

import android.Manifest;
import android.app.NotificationManager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;
import java.util.Objects;

/**
 * Main activity for the application, responsible for handling permissions and initializing the user profile.
 */
public class MainActivity extends AppCompatActivity {
    private Intent AttendeeIntent;
    private Intent AdminIntent;
    private Intent OrganizerIntent;
    private String profileId;
    private User user;
    private FirebaseFirestore db;
    private CollectionReference profilesRef;
    private Map<String, Object> profileDocDetails;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 101;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private CollectionReference tokenRef;
    private static final String TAG = "MainActivity";

    /**
     * Checks and requests notification permission if not granted.
     */
    private void checkAndRequestNotificationPermission() {
        if (!hasNotificationPermission()) {
            requestNotificationPermission();
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!hasLocationPermissions()) {
                        requestLocationPermission();
                    }
                }
                //Adding a delay to allow processing time between prompts
            }, 200);
        }
    }

    /**
     * Initializes the activity, checks and requests notification permission, and sets up button listeners.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkAndRequestNotificationPermission();

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
            OrganizerIntent.putExtra("profileID", profileId);
            startActivity(OrganizerIntent);
        });
        Admin_button.setOnClickListener(V -> {
            AdminIntent = new Intent(this, AdminActivity.class);
            startActivity(AdminIntent);
        });
        FirebaseMessaging.getInstance().subscribeToTopic("all");

//        getFcmToken();

        profileId = "This is an admin profile";
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
                        profileData.put("events", new ArrayList<DocumentReference>());
                        profileData.put("locationEnabled", Boolean.FALSE);
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

//    void getFcmToken(){
//        FirebaseMessaging.getInstance().getToken().addOnCompleteListener((OnCompleteListener<String>) task -> {
//            if(task.isSuccessful()){
//                String token = task.getResult();
//                Log.i("My token", token);
//                saveTokenToFirestore(token);
//            }
//        });
//    }
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
//                    List<String> tokensList = (List<String>) document.get("TokenList");
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
//    void updateTokenListInFirestore(CollectionReference tokenRef, List<String> tokensList) {
//        // Create a HashMap to store the token list
//        HashMap<String, Object> data = new HashMap<>();
//        data.put("TokenList", tokensList);
//
//        // Update Firestore with the token list
//        tokenRef.document("tokenz").set(data)
//                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Token list updated in Firestore"))
//                .addOnFailureListener(e -> Log.e("Firestore", "Error updating token list in Firestore", e));
//    }
    /**
     * Checks if notification permission is granted.
     *
     * @return True if notification permission is granted, false otherwise.
     */
    private boolean hasNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            return notificationManager != null && notificationManager.getNotificationChannel(String.valueOf(NotificationManager.IMPORTANCE_DEFAULT)) != null;
        }
        return true; // On versions lower than Oreo, no permission is required.
    }

    /**
     * Requests notification permission.
     */
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Request permission for notification
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    NOTIFICATION_PERMISSION_REQUEST_CODE);
        } else {
            // No permission required for pre-Oreo devices
            Log.d("MainActivity", "Notification permission not required for pre-Oreo devices.");
        }
    }

    /**
     * Handles the result of permission requests.
     *
     * @param requestCode  The request code.
     * @param permissions  The requested permissions.
     * @param grantResults The results of the permission requests.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            // After notification permission has been requested then check location permission
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!hasLocationPermissions()) {
                        requestLocationPermission();
                    }
                }
            }, 200);
        }
    }

    /**
     * Requests location permission.
     */
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

    }

    /**
     * Checks if location permissions are granted.
     *
     * @return True if location permissions are granted, false otherwise.
     */

    private boolean hasLocationPermissions() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
