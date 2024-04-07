package com.example.project_3;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

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
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 102;
    private CollectionReference tokenRef;
    private static final String TAG = "MainActivity";

    private void checkAndRequestNotificationPermission() {
        if (!hasNotificationPermission()) {
            requestNotificationPermission();
        }
        if (!hasLocationPermissions()) {
            requestLocationPermission();
        }
    }

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
            startActivity(OrganizerIntent);
        });
        Admin_button.setOnClickListener(V -> {
            AdminIntent = new Intent(this, AdminActivity.class);
            startActivity(AdminIntent);
        });
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        getFcmToken();

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
                    if (!idFound) {
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

    void getFcmToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener((OnCompleteListener<String>) task -> {
            if (task.isSuccessful()) {
                String token = task.getResult();
                Log.i("My token", token);
                saveTokenToFirestore(token);
            }
        });
    }

    void saveTokenToFirestore(String token) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tokenRef = db.collection("Tokens");

        // Check if document exists and retrieve its data
        tokenRef.document("tokenz").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Document exists, retrieve token list and update
                    List<String> tokensList = (List<String>) document.get("TokenList");
                    if (tokensList != null && !tokensList.isEmpty()) {
                        // Add the new token to the end of the list
                        tokensList.add(token);
                    } else {
                        // Token list is empty, create a new list with the token
                        tokensList = new ArrayList<>();
                        tokensList.add(token);
                    }
                    // Update Firestore with the modified token list
                    updateTokenListInFirestore(tokenRef, tokensList);
                } else {
                    // Document doesn't exist, create a new one with the token
                    List<String> tokensList = new ArrayList<>();
                    tokensList.add(token);
                    // Update Firestore with the new token list
                    updateTokenListInFirestore(tokenRef, tokensList);
                }
            } else {
                Log.e("Firestore", "Error getting document", task.getException());
            }
        });
    }

    void updateTokenListInFirestore(CollectionReference tokenRef, List<String> tokensList) {
        // Create a HashMap to store the token list
        HashMap<String, Object> data = new HashMap<>();
        data.put("TokenList", tokensList);

        // Update Firestore with the token list
        tokenRef.document("tokenz").set(data)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Token list updated in Firestore"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error updating token list in Firestore", e));
    }

    private boolean hasNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            return notificationManager != null && notificationManager.getNotificationChannel(String.valueOf(NotificationManager.IMPORTANCE_DEFAULT)) != null;
        }
        return true; // On versions lower than Oreo, no permission is required.
    }

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

    private boolean hasLocationPermissions() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        //Microsoft Copilot. (2024), April 6th
        //Prompt:it just says location permissions denied as soon as the app opens with this code and no prompt is showing up.
        //is it possible to send the user into settings and turn the permission on:
        // (the boolean function works)
        // private void checkAndRequestNotificationPermission() {
        //        if (!hasNotificationPermission()) {
        //            requestNotificationPermission();
        //        }
        //        if (!hasLocationPermissions()) {
        //            requestLocationPermission();
        //        }
        //
        //    }
        //private void requestLocationPermission(){
        // ActivityCompat.requestPermissions(this,
        //            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
        //            REQUEST_PERMISSIONS_REQUEST_CODE);
        //}
        new AlertDialog.Builder(this)
                .setTitle("Location Permission Needed")
                .setMessage("Please enable location permission in settings")
                .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Take the user to the app settings
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .create()
                .show();
    }


    }


