package com.example.project_3;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.RemoteMessageCreator;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Activity for editing event details.
 */
public class EditEventDetails extends AppCompatActivity {
    private String eventName;
    private String eventLocation;
    private String eventDate;
    private String eventTime;
    private String qrCode;
    private String qrPromoCode;
    private String imageUri;
    private String imageUry;
    private String link;
    private String info;
    private String info_1;
    private String info_2;
    private String info_3;
    private String info_4;
    private FirebaseFirestore db;


    private static final int ADD_EVENT_REQUEST = 1;
    private static final String TAG = "EditEventDetails";



    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event);
        db = FirebaseFirestore.getInstance();

        eventName = getIntent().getStringExtra("eventName");
        eventLocation = getIntent().getStringExtra("eventLocation");
        eventDate = getIntent().getStringExtra("eventDate");
        eventTime = getIntent().getStringExtra("eventTime");
        qrCode = getIntent().getStringExtra("QRCode");
        qrPromoCode = getIntent().getStringExtra("QRPromoCode");
        imageUri = getIntent().getStringExtra("imageUri");
        link = getIntent().getStringExtra("link");

        // Getting reference to the back button
        Button backButton = findViewById(R.id.back_button);

        ImageButton createMessageButton = findViewById(R.id.create_message);
        ImageButton addPosterButton = findViewById(R.id.add_poster);
        ImageButton addLink = findViewById(R.id.add_link);
        ImageButton qrViewButton = findViewById(R.id.view_qrcode);
        ImageButton editDetailsButton = findViewById(R.id.edit_details);


        // Alphabet Inc, 2024, YouTube, https://www.youtube.com/watch?v=vyt20Gg2Ckg
        // describes how to set up notification view
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(EditEventDetails.this,
                    android.Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(EditEventDetails.this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        // Setting click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finishing the activity when the back button is clicked
                finish();
            }
        });

        createMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateMessageDialog();
            }
        });


        addPosterButton.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, ADD_EVENT_REQUEST);
        });
        addLink.setOnClickListener(v -> {
            showAddLinkDialog();
        });
        editDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });


        qrViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the QRCodeDisplayActivity
                Intent intent = new Intent(EditEventDetails.this, QRCodeDisplayActivity.class);
                intent.putExtra("eventName", eventName); // Pass event details if needed
                intent.putExtra("eventLocation", eventLocation);
                intent.putExtra("eventDate", eventDate);
                intent.putExtra("eventTime", eventTime);
                intent.putExtra("QRCode", qrCode);
                intent.putExtra("QRPromoCode", qrPromoCode);
                // Pass any necessary data to QRCodeDisplayActivity if needed
                startActivity(intent);
            }
        });

    }
    public void showEditDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_edit_details, null);
        final EditText editDateF= view.findViewById(R.id.edit_date);
        final EditText editTimeF= view.findViewById(R.id.edit_time);
        final EditText editLocationF= view.findViewById(R.id.edit_location);
        final EditText editDetailsF= view.findViewById(R.id.edit_details);
        final EditText editMaxF= view.findViewById(R.id.edit_max_peeps);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setTitle("Edit Details for the Event")
                .setPositiveButton("OK", (dialog, which) -> {
                    String eventNewDate = editDateF.getText().toString();
                    String eventNewTime = editTimeF.getText().toString();
                    String eventNewLocation = editLocationF.getText().toString();
                    String eventNewDetails = editDetailsF.getText().toString();
                    String eventNewMax = editMaxF.getText().toString();

                    info = eventNewDate;
                    info_1 = eventNewTime;
                    info_2 = eventNewLocation;
                    info_3 = eventNewDetails;
                    info_4 = eventNewMax;

                    updateDetails(info, info_1, info_2, info_3, info_4);

                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    public void showAddLinkDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_add_link, null);
        final EditText addLinkEditText = view.findViewById(R.id.add_link_editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setTitle("Add a Link for the Event")
                .setPositiveButton("OK", (dialog, which) -> {
                    String eventLinc = addLinkEditText.getText().toString();
                    info = eventLinc;
                    updateLinkInDatabase(info);

                })
                .setNegativeButton("Cancel", null)
                .show();


    }
    private void showCreateMessageDialog() {
        // Create AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Create a parent layout to hold both EditText fields
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Set up the input for title EditText
        final EditText titleEditText = new EditText(this);
        titleEditText.setHint("Title:");
        // Set text size for title EditText (adjust as needed)
        titleEditText.setTextSize(20); // Set text size in sp (adjust as needed)
        titleEditText.setBackgroundColor(Color.WHITE);
        titleEditText.setHintTextColor(Color.DKGRAY);
        titleEditText.setTextColor(Color.BLACK);
        // Set padding for title EditText (adjust as needed)
        titleEditText.setPadding(25, 20, 20, 25); // Left, Top, Right, Bottom
        layout.addView(titleEditText); // Add title EditText to the layout
        // Set up the input for description EditText
        final EditText descriptionEditText = new EditText(this);
        descriptionEditText.setHint("Message:");
        // Set text size for description EditText (adjust as needed)
        descriptionEditText.setTextSize(20); // Set text size in sp (adjust as needed)
        descriptionEditText.setBackgroundColor(Color.WHITE);
        descriptionEditText.setHintTextColor(Color.DKGRAY);
        descriptionEditText.setTextColor(Color.BLACK);
        // Set padding for description EditText (adjust as needed)
        descriptionEditText.setPadding(25, 20, 20, 25); // Left, Top, Right, Bottom
        layout.addView(descriptionEditText); // Add description EditText to the layout

        // Set the custom layout as the view of the AlertDialog
        builder.setView(layout);

        // Set positive button with text size
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve title and description from EditText fields
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                if (!title.isEmpty() || !description.isEmpty()) {
                    sendNotification(title , description);
                } else {
                    // Display a toast or alert indicating that title and description are required
                    Toast.makeText(EditEventDetails.this, "Title and description are required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set negative button with text size
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        // Set the custom title view with text size
        TextView titleView = new TextView(this);
        titleView.setText("Create Message");
        titleView.setTextSize(25); // Set text size for the title (adjust as needed)
        titleView.setPadding(20, 20, 20, 20); // Add padding (adjust as needed)
        dialog.setCustomTitle(titleView);

        // Show the dialog
        dialog.show();
    }

    // Alphabet Inc., 2024, YouTube, https://www.youtube.com/watch?v=YjNZO90yVsE&t=1s
    // describes how to use Firebase Messaging and FCM
    void sendNotification(String title, String message) {
        // Taken from: https://stackoverflow.com/questions/55948318/how-to-send-a-firebase-message-to-topic-from-android

        RequestQueue mRequestQue = Volley.newRequestQueue(this);

        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/" + ("Events/" + eventName).hashCode());
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", title);
            notificationObj.put("body", message);
            //replace notification with data when went send data
            json.put("notification", notificationObj);

            String URL = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL,
                    json,
                    response -> Log.d("MUR", "onResponse: "),
                    error -> Log.d("MUR", "onError: " + error.networkResponse)
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAA_Dv0cdM:APA91bES7JC6yoQaMnguKlQUwdd6ac9uT3m3hMPRGVEMKn44frxPFLLmzKZHjH38m6sGsBN4pkUoe4Vt5VKMjxN3UWahrv6oyTPrVbmUD2-RudLD0DpzodDseZpEjnPs3zc044THL6ht");
                    return header;
                }
            };


            mRequestQue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, Object> announcement = new HashMap<String, Object>();
        announcement.put("content", title + " - " + message);
        announcement.put("event_name", eventName);
        announcement.put("timestamp", new Timestamp(new Date()));
        db.collection("Events").document(eventName).update("announcements", FieldValue.arrayUnion(announcement));
    }

//        void sendNotification(String title , String message){
//        try{
//            JSONObject jsonObject  = new JSONObject();
//
//            JSONObject notificationObj = new JSONObject();
//            notificationObj.put("title", title);
//            notificationObj.put("body",message);
//
//            JSONObject dataObj = new JSONObject();
//            dataObj.put("userId", title);
//
//            jsonObject.put("notification",notificationObj);
//            jsonObject.put("data",dataObj);
////            jsonObject.put("to","dhAQk8JEQEOTXPNLLLvAUF:APA91bGlmtSDnAZoEurlAWHj6iKWqtwZmnir6TdOvSc1yCIay3nHPoyv7BgGsPPmN_9pGoROx_viPqJe7LLdkVAGSDpb2yJVrZ-L9B81vT7lhjqrbSd6F9Th6q_Hx60KsOnV7tO6RFg9");
//
////            dhAQk8JEQEOTXPNLLLvAUF:APA91bGlmtSDnAZoEurlAWHj6iKWqtwZmnir6TdOvSc1yCIay3nHPoyv7BgGsPPmN_9pGoROx_viPqJe7LLdkVAGSDpb2yJVrZ-L9B81vT7lhjqrbSd6F9Th6q_Hx60KsOnV7tO6RFg9
//            jsonObject.put("to","c2yFLYtWRAKa9Hp3xxbmnn:APA91bE7tLZ7Wh1uB1WiU0vpzgwEMX19wc1SoLCYxMKW2QF23H6YbNq2CLNH5HcrVli8GMzWN2kpMaawb15UIVMpe5cYYtH9_j1Je8Pe41WFxrHKUmJAUDcT4_P_WvYVpOaujzd_-1tV");
//
//            callApi(jsonObject);
//
//        }catch (Exception e){
//
//        }
//    }
    // Alphabet Inc., 2024, YouTube, https://www.youtube.com/watch?v=YjNZO90yVsE&t=1s
    // describes how to use Firebase Messaging and FCM
    void callApi(JSONObject jsonObject){
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(),JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization","Bearer AAAA_Dv0cdM:APA91bES7JC6yoQaMnguKlQUwdd6ac9uT3m3hMPRGVEMKn44frxPFLLmzKZHjH38m6sGsBN4pkUoe4Vt5VKMjxN3UWahrv6oyTPrVbmUD2-RudLD0DpzodDseZpEjnPs3zc044THL6ht") // you need to paste in the API KEY HERE. I removed it for safety purposes
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EVENT_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                // Get the URI of the selected image
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        // Decode the selected image to obtain its dimensions
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri), null, options);

                        // Calculate the desired width and height for the resized image
                        int desiredWidth = 250; // Adjust the width
                        int desiredHeight = 250; // Adjust the height

                        // Calculate the scale factor to maintain aspect ratio
                        int scaleFactor = Math.min(options.outWidth / desiredWidth, options.outHeight / desiredHeight);

                        // Set the options to decode the image with the calculated scale factor
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = scaleFactor;

                        // Decode the image with the specified options
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri), null, options);

                        // Convert the resized image to base64 string
                        if (bitmap != null) {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            imageUry = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        }
                        updateImageInDatabase(selectedImageUri);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }
        }
    }

    // Method to update the imageUry field in the database
    private void updateImageInDatabase(Uri selectedImageUri) {
        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get the reference to the specific event document
        DocumentReference eventRef = db.collection("Events").document(eventName);

        // Create a map to update the image field
        Map<String, Object> updates = new HashMap<>();
        updates.put("Image", imageUry);

        // Update the document in the database
        eventRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    // Handle successful update
                    sendNotification(eventName , "Poster: Updated");
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(EditEventDetails.this, "Failed to update image", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error updating image", e);
                });
    }
    public void updateDetails(String date, String time, String location, String details
    , String max){
        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get the reference to the specific event document
        DocumentReference eventRef = db.collection("Events").document(eventName);

        // Create a map to update the image field
        Map<String, Object> updates = new HashMap<>();
        if(date!= null && !date.isEmpty()) {
            updates.put("Date", date);
            eventRef.update(updates)
                    .addOnSuccessListener(aVoid -> {
                        // Handle successful update
                        sendNotification(eventName , "Date: Updated");
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(EditEventDetails.this, "Failed to update Date", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error updating Date", e);
                    });
        }
        if(time!= null && !time.isEmpty()) {
            updates.put("Time", time);
            eventRef.update(updates)
                    .addOnSuccessListener(aVoid -> {
                        // Handle successful update
                        sendNotification(eventName , "Time: Updated");
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(EditEventDetails.this, "Failed to update Time", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error updating Time", e);
                    });
        }
        if(location!= null && !location.isEmpty()) {
            updates.put("Location", location);
            eventRef.update(updates)
                    .addOnSuccessListener(aVoid -> {
                        // Handle successful update
                        sendNotification(eventName , "Location: Updated");
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(EditEventDetails.this, "Failed to update Location", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error updating Location", e);
                    });
        }
        if(details!= null && !details.isEmpty()) {
            updates.put("Details", details);
            eventRef.update(updates)
                    .addOnSuccessListener(aVoid -> {
                        // Handle successful update
                        sendNotification(eventName , "Details: Updated");
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(EditEventDetails.this, "Failed to update Details", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error updating Details", e);
                    });
        }
        if(max!= null && !max.isEmpty()) {
            updates.put("max_attendees", max);
            eventRef.update(updates)
                    .addOnSuccessListener(aVoid -> {
                        // Handle successful update
                        sendNotification(eventName , "Max: Updated");
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(EditEventDetails.this, "Failed to update Max", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error updating Max", e);
                    });
        }


        // Update the document in the database
//        eventRef.update(updates)
//                .addOnSuccessListener(aVoid -> {
//                    // Handle successful update
//                    sendNotification(eventName , "Link: Updated");
//                })
//                .addOnFailureListener(e -> {
//                    // Handle failure
//                    Toast.makeText(EditEventDetails.this, "Failed to update link", Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, "Error updating link", e);
//                });

    }
    private void updateLinkInDatabase(String link) {
        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get the reference to the specific event document
        DocumentReference eventRef = db.collection("Events").document(eventName);

        // Create a map to update the image field
        Map<String, Object> updates = new HashMap<>();
        updates.put("Link", link);

        // Update the document in the database
        eventRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    // Handle successful update
                    sendNotification(eventName , "Link: Updated");
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(EditEventDetails.this, "Failed to update link", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error updating link", e);
                });
    }
}
