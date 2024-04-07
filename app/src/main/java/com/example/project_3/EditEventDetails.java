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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private String info_5;

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
        ImageButton detailsButton = findViewById(R.id.edit_details);


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
        detailsButton.setOnClickListener(v -> {
            showEditDetailsDialog(eventName);
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
    public void showEditDetailsDialog(String realName){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_edit_details, null);
        final EditText editDate = view.findViewById(R.id.edit_date);
        final EditText editTime = view.findViewById(R.id.edit_time);
        final EditText editLocation = view.findViewById(R.id.edit_location);
        final EditText editDetails = view.findViewById(R.id.edit_details);
        final EditText editMax = view.findViewById(R.id.edit_max_peeps);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setTitle("Edit Details for the Event")
                .setPositiveButton("OK", (dialog, which) -> {
                    String eventNewDate = editDate.getText().toString();
                    String eventNewTime = editTime.getText().toString();
                    String eventNewLocation = editLocation.getText().toString();
                    String eventNewDetails = editDetails.getText().toString();
                    String eventNewMax = editMax.getText().toString();

                    info_1 = eventNewDate;
                    info_2 = eventNewTime;
                    info_3 = eventNewLocation;
                    info_4 = eventNewDetails;
                    info_5 = eventNewMax;

                    updateDetailsInDatabase(info_1, info_2, info_3,info_4,info_5);

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
                    addNotifToFb(title, description);
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
    void addNotifToFb(String title, String description){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("Events").document(eventName);
        eventRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    List<Map<String, Object>> existingAnnouncements = (List<Map<String, Object>>) document.get("Announcements");

                    // Add the new announcement to the existing list
                    Map<String, Object> newAnnouncement = new HashMap<>();

                    newAnnouncement.put("title", title);
                    newAnnouncement.put("content", description);
                    newAnnouncement.put("timestamp", new Date()); // Use current date/time
                    existingAnnouncements.add(newAnnouncement);

                    // Update the document with the updated list of announcements
                    eventRef.update("Announcements", existingAnnouncements)
                            .addOnSuccessListener(aVoid -> Log.d("Firestore", "Document updated successfully"))
                            .addOnFailureListener(e -> Log.e("Firestore", "Error updating document", e));
                } else {
                    Log.d("Firestore", "No such document");
                }
            } else {
                Log.e("Firestore", "Error getting document", task.getException());
            }
        });

    }
    // Alphabet Inc., 2024, YouTube, https://www.youtube.com/watch?v=YjNZO90yVsE&t=1s
    // describes how to use Firebase Messaging and FCM
    void sendNotification(String title, String message) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tokenRef = db.collection("Tokens");

        tokenRef.document("tokenz").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Get the TokenList field as a List
                    List<String> tokenList = (List<String>) document.get("OrganizerTokens");
                    if (tokenList != null) {
                        // Iterate over the TokenList and send notification to each token
                        for (String token : tokenList) {
                            try {
                                JSONObject jsonObject = new JSONObject();

                                JSONObject notificationObj = new JSONObject();
                                notificationObj.put("title", title);
                                notificationObj.put("body", message);

                                JSONObject dataObj = new JSONObject();
                                dataObj.put("userId", title);

                                jsonObject.put("notification", notificationObj);
                                jsonObject.put("data", dataObj);
                                jsonObject.put("to", token);

                                callApi(jsonObject);

                            } catch (Exception e) {
                                Log.e(TAG, "Error sending notification to token: " + token, e);
                            }
                        }
                    } else {
                        Log.d(TAG, "TokenList is null");
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

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
                    addNotifToFb(eventName, "Poster: Updated");

                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(EditEventDetails.this, "Failed to update image", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error updating image", e);
                });
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
                    addNotifToFb(eventName, "Link: Updated");
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(EditEventDetails.this, "Failed to update link", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error updating link", e);
                });
    }
    private void updateDetailsInDatabase(String date, String time, String location
            , String details, String max) {

        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventsRef = db.collection("Events").document(eventName);

        if(date != null && !date.isEmpty()){
            Map<String, Object> updates = new HashMap<>();
            updates.put("Date", date);

            // Update the document in the database
            eventsRef.update(updates)
                    .addOnSuccessListener(aVoid -> {
                        // Handle successful update
                        sendNotification(eventName , "Date: Updated");
                        addNotifToFb(eventName, "Date: Updated");
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(EditEventDetails.this, "Failed to update Date", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error updating Date", e);
                    });
        }
        if(time != null && !time.isEmpty()){
            Map<String, Object> updates = new HashMap<>();
            updates.put("Time", time);

            // Update the document in the database
            eventsRef.update(updates)
                    .addOnSuccessListener(aVoid -> {
                        // Handle successful update
                        sendNotification(eventName , "Time: Updated");
                        addNotifToFb(eventName, "Time: Updated");
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(EditEventDetails.this, "Failed to update Time", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error updating Time", e);
                    });
        }
        if(location != null && !location.isEmpty()){
            Map<String, Object> updates = new HashMap<>();
            updates.put("Location", location);

            // Update the document in the database
            eventsRef.update(updates)
                    .addOnSuccessListener(aVoid -> {
                        // Handle successful update
                        sendNotification(eventName , "Location: Updated");
                        addNotifToFb(eventName, "Location: Updated");
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(EditEventDetails.this, "Failed to update Location", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error updating Location", e);
                    });
        }
        if(details != null && !details.isEmpty()){
            Map<String, Object> updates = new HashMap<>();
            updates.put("Details", details);

            // Update the document in the database
            eventsRef.update(updates)
                    .addOnSuccessListener(aVoid -> {
                        // Handle successful update
                        sendNotification(eventName , "Details: Updated");
                        addNotifToFb(eventName, "Details: Updated");
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(EditEventDetails.this, "Failed to update Details", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error updating Details", e);
                    });
        }
    }
}
