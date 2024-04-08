package com.example.project_3;

/**
 * This activity displays the list of attendees who have checked in to a particular event.
 */

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AttendeesCheckedInActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private ListView attendeesListView;
    private String eventName;
    private static final String TAG = "AttendeesCheckedInActivity";
    private TextView attendanceCountTextView;
    private ListenerRegistration eventListener;
    private boolean notificationSent = false;

    /**
     * Initializes the activity layout, Firestore instance, and necessary references.
     * Retrieves the event name from the intent and sets up UI components.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendees_checked_in);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");

        // Get event name from intent
        eventName = getIntent().getStringExtra("event_name");

        // Find views
        Button backButton = findViewById(R.id.back_button);
        attendeesListView = findViewById(R.id.attendees_list_view);
        TextView appbar = findViewById(R.id.appbar_title);
        appbar.setTextSize(22);
        appbar.setText("Attendees Checked In");
        attendanceCountTextView = findViewById(R.id.attendance_count_text);

        // Set click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the AttendeesCheckedInActivity and return to the previous activity
                finish();
            }
        });

        // Fetch checked-in attendees and display them in the ListView
        fetchCheckedInAttendees();
    }


    /**
     * Fetches the list of checked-in attendees from Firestore for the current event.
     * Updates the UI with the attendee names and real-time attendance count.
     */
    private void fetchCheckedInAttendees() {
        // Check if eventName is null
        if (eventName == null) {
            Log.d(TAG, "Event name is null");
            Toast.makeText(this, "Event name is null", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference eventDocRef = eventsRef.document(eventName);

        // Listen for changes in the event document to update attendee list and attendance count in real-time
        eventListener = eventDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Retrieve the list of checked-in attendees
                    List<DocumentReference> checkedIn = (List<DocumentReference>) documentSnapshot.get("checked_in");
                    if (checkedIn != null) {
                        List<String> attendeeNames = new ArrayList<>();
                        for (DocumentReference attendeeRef : checkedIn) {
                            // Get the name of the attendee directly from the reference

                            String attendeeName = attendeeRef.getId();

                            if (attendeeName != null) {
                                attendeeNames.add(attendeeName);
                            }
                        }
                        // Update the UI with the attendee names
                        displayAttendees(attendeeNames);
                        // Update real-time attendance count
                        updateRealTimeAttendance(attendeeNames.size());

                        if (attendeeNames.size() == 1 && !notificationSent) {
                            notificationSent = true;    // Set the flag to true to indicate that the notification has been sen
                            showAlertDialog("First Attendee Checked IN!", eventName);
                        }
                        else if ((attendeeNames.size() == 5 || attendeeNames.size() == 25 || attendeeNames.size() == 50
                                || attendeeNames.size() == 100 || attendeeNames.size() == 200 || attendeeNames.size() == 300)
                                && !notificationSent) {
                            notificationSent = true;    // Set the flag to true to indicate that the notification has been sent
                            showAlertDialog("Checked-in attendees: " + attendeeNames.size(), eventName);
                        }
                    }
                } else {
                    Log.d(TAG, "Event document not found");
                }
            }
        });
    }

    private void showAlertDialog(final String message, final String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Call a method to handle Firestore update
                        updateFirestoreWithAnnouncement(message, title);
                        // Close the dialog
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void updateFirestoreWithAnnouncement(String title , String message) {
        Map<String, Object> announcement = new HashMap<>();
        announcement.put("content", title + " - " + message); // Assuming title is a member variable of your class
        announcement.put("event_name", eventName);
        announcement.put("timestamp", new Timestamp(new Date()));
        db.collection("Events").document(eventName).update("Milestones", FieldValue.arrayUnion(announcement))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Update successful
                        Toast.makeText(getApplicationContext(), "Milestone added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(getApplicationContext(), "Failed to add announcement: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to show Milestones
//    private void showAlertDialog(String message) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(message)
//                .setCancelable(false)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int id) {
//                        // Close the dialog if needed
//                        dialog.dismiss();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }

    /**
     * Displays the list of attendees in a ListView and sets click listeners for each item.
     *
     * @param attendees List of attendee names to be displayed
     */

    private void displayAttendees(List<String> attendees) {
        // Create an ArrayAdapter to fill the ListView with attendee names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, attendees);
        attendeesListView.setAdapter(adapter);

        // Set click listener for the attendees list items to display details of the clicked attendee
        attendeesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedAttendee = attendees.get(position);
                // Display a dialog with details of the clicked attendee
                showAttendeeDetailsDialog(clickedAttendee);
            }
        });
    }
    /**
     * Displays a dialog with details of the selected attendee.
     * Fetches the attendee details from Firestore and presents them in the dialog.
     *
     * @param attendeeName Name of the selected attendee
     */
    private void showAttendeeDetailsDialog(String attendeeName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Fetch the attendee document from Firestore
        db.collection("Profiles")
                .document(attendeeName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot profileSnapshot = task.getResult();
                            if (profileSnapshot.exists()) {
                                // Retrieve the checked-in events map from the profile document
                                Object checkedInEventsObj = profileSnapshot.get("checked_in_events");
                                if (checkedInEventsObj instanceof Map) {
                                    Map<String, Long> checkedInEvents = (Map<String, Long>) checkedInEventsObj;
                                    Long checkInCount = checkedInEvents.get(eventName);
                                    if (checkInCount != null) {
                                        // Retrieve the name field from the profile document
                                        int count = checkInCount.intValue();
                                        String profileName = profileSnapshot.getString("name"); // Retrieve the 'name' field
                                        builder.setTitle("Attendee Details")
                                                .setMessage("Name: " + profileName + "\nCheck-in Count: " + count)
                                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .show();
                                        return;
                                    }
                                }
                                // If the checked_in_events field is not a map or if the event name is not found
                                // Show an error message
                                Toast.makeText(AttendeesCheckedInActivity.this, "Check-in count not found for this event", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "Profile document not found");
                                // Show an error message if the profile document cannot be found
                                Toast.makeText(AttendeesCheckedInActivity.this, "Profile document not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "Error fetching profile details", task.getException());
                            // Show an error message if profile details cannot be fetched
                            Toast.makeText(AttendeesCheckedInActivity.this, "Failed to fetch profile details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /**
     * Updates the real-time attendance count displayed in the UI.
     *
     * @param count The number of attendees currently checked in
     */
    private void updateRealTimeAttendance(int count) {
        attendanceCountTextView.setText("Real time attendance: " + count);
    }
    /**
     * Cleans up resources when the activity is destroyed.
     * Removes the Firestore listener to avoid memory leaks.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the Firestore listener to avoid memory leaks
        if (eventListener != null) {
            eventListener.remove();
        }
    }
    // Alphabet Inc., 2024, YouTube, https://www.youtube.com/watch?v=YjNZO90yVsE&t=1s
    // describes how to use Firebase Messaging and FCM
//    void sendNotification(String title, String message) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference tokenRef = db.collection("Tokens");
//
//        tokenRef.document("tokenz").get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                    // Get the TokenList field as a List
//                    List<String> tokenList = (List<String>) document.get("OrganizerTokens");
//                    if (tokenList != null) {
//                        // Iterate over the TokenList and send notification to each token
//                        for (String token : tokenList) {
//                            try {
//                                JSONObject jsonObject = new JSONObject();
//
//                                JSONObject notificationObj = new JSONObject();
//                                notificationObj.put("title", title);
//                                notificationObj.put("body", message);
//
//                                JSONObject dataObj = new JSONObject();
//                                dataObj.put("userId", title);
//
//                                jsonObject.put("notification", notificationObj);
//                                jsonObject.put("data", dataObj);
//                                jsonObject.put("to", token);
//
//                                callApi(jsonObject);
//
//                            } catch (Exception e) {
//                                Log.e(TAG, "Error sending notification to token: " + token, e);
//                            }
//                        }
//                    } else {
//                        Log.d(TAG, "OrganizerTokens is null");
//                    }
//                } else {
//                    Log.d(TAG, "No such document");
//                }
//            } else {
//                Log.d(TAG, "get failed with ", task.getException());
//            }
//        });
//    }
//    void sendNotification(String title, String message) {
//        // Taken from: https://stackoverflow.com/questions/55948318/how-to-send-a-firebase-message-to-topic-from-android
//
//        RequestQueue mRequestQue = Volley.newRequestQueue(this);
//
//        JSONObject json = new JSONObject();
//        try {
//            json.put("to", "/topics/" + ("Events/" + eventName).hashCode());
//            JSONObject notificationObj = new JSONObject();
//            notificationObj.put("title", title);
//            notificationObj.put("body", message);
//            //replace notification with data when went send data
//            json.put("notification", notificationObj);
//
//            String URL = "https://fcm.googleapis.com/fcm/send";
//            JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL,
//                    json,
//                    response -> Log.d("MUR", "onResponse: "),
//                    error -> Log.d("MUR", "onError: " + error.networkResponse)
//            ) {
//                @Override
//                public Map<String, String> getHeaders() {
//                    Map<String, String> header = new HashMap<>();
//                    header.put("content-type", "application/json");
//                    header.put("authorization", "key=AAAA_Dv0cdM:APA91bES7JC6yoQaMnguKlQUwdd6ac9uT3m3hMPRGVEMKn44frxPFLLmzKZHjH38m6sGsBN4pkUoe4Vt5VKMjxN3UWahrv6oyTPrVbmUD2-RudLD0DpzodDseZpEjnPs3zc044THL6ht");
//                    return header;
//                }
//            };
//
//
//            mRequestQue.add(request);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Map<String, Object> announcement = new HashMap<String, Object>();
//        announcement.put("content", title + " - " + message);
//        announcement.put("event_name", eventName);
//        announcement.put("timestamp", new Timestamp(new Date()));
//        db.collection("Events").document(eventName).update("announcements", FieldValue.arrayUnion(announcement));
//    }
//    // Alphabet Inc., 2024, YouTube, https://www.youtube.com/watch?v=YjNZO90yVsE&t=1s
//    // describes how to use Firebase Messaging and FCM
//    void callApi(JSONObject jsonObject){
//        MediaType JSON = MediaType.get("application/json; charset=utf-8");
//        OkHttpClient client = new OkHttpClient();
//        String url = "https://fcm.googleapis.com/fcm/send";
//        RequestBody body = RequestBody.create(jsonObject.toString(),JSON);
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .header("Authorization","Bearer AAAA_Dv0cdM:APA91bES7JC6yoQaMnguKlQUwdd6ac9uT3m3hMPRGVEMKn44frxPFLLmzKZHjH38m6sGsBN4pkUoe4Vt5VKMjxN3UWahrv6oyTPrVbmUD2-RudLD0DpzodDseZpEjnPs3zc044THL6ht") // you need to paste in the API KEY HERE. I removed it for safety purposes
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//            }
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//            }
//        });
//    }
}
