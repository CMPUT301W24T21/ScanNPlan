package com.example.project_3;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This is the organizer activity for organizing the events.
 */

public class OrganizerActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private CollectionReference qrRef;
    private EditText addEventEditText;
    private ListView eventList;
    private ArrayList<Event> eventDataList;
    private EventArrayAdapter eventArrayAdapter;
    private static final int ADD_EVENT_REQUEST = 1;
    private String base64QRCode;
    private String base64QRPromoCode;
    private String previousId = null;
    private String info;

    /**
     * Called when the activity is first created. This method initializes the activity by setting its layout,
     * initializing Firebase, setting up UI components, setting click listeners for buttons, and listening for
     * changes in the Firebase database.
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state,
     *                             or null if the activity is being started fresh.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_events);

        // Initializing Firebase
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");
        qrRef = db.collection("QrCodes");

        // Initializing the UI components
        eventDataList = new ArrayList<>();
        eventList = findViewById(R.id.event_list);
        eventArrayAdapter = new EventArrayAdapter(this, eventDataList);
        eventList.setAdapter(eventArrayAdapter);

        // Displaying the event details when clicked
        // This info is brought up to the Activity once you click on the event
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Opening event details activity
                Event selectedEvent = (Event) eventArrayAdapter.getItem(position);
                if (selectedEvent != null) {
                    Intent intent = new Intent(OrganizerActivity.this, EventDetailsActivity.class);
                    intent.putExtra("eventName", selectedEvent.getName());
                    intent.putExtra("eventLocation", selectedEvent.getLocation());
                    intent.putExtra("eventDetails", selectedEvent.getDetails());
                    intent.putExtra("eventDate", selectedEvent.getDate());
                    intent.putExtra("eventTime", selectedEvent.getTime());
                    intent.putExtra("imageUri", selectedEvent.getImage());
                    intent.putExtra("QRCode", selectedEvent.getQrCode());
                    intent.putExtra("QRPromoCode", selectedEvent.getQrPromoCode());
                    intent.putExtra("link", selectedEvent.getLink());
                    startActivity(intent);
                }
            }
        });

        // Getting the button for navigating back to the main activity
        Button back = findViewById(R.id.button);

        // Setting on click listener for the back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating an intent to navigate back to the main activity
                Intent intent = new Intent(OrganizerActivity.this, MainActivity.class);
                startActivity(intent); // Starting the main activity
            }
        });

        // This button is to add a new event
        FloatingActionButton fabAddEvent = findViewById(R.id.fab_add_event);
        fabAddEvent.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_green_100)));

        fabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEventDialog();
            }
        });

        // Listening for changes in the Firebase database
        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    // Clearing the list and filling it with retrieved data
                    eventDataList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        // Retrieving the event data from Firestore and adding it to the list
                        String event = doc.getId();// for displaying event name in ListView
                        boolean reuse = false;
                        String date = doc.getString("Date");
                        String time = "No Time";
                        String location = doc.getString("Location");// for displaying location in ListView
                        String details = doc.getString("Details");
                        String imageUri = doc.getString("Image");
                        String qrCode = doc.getString("QRCode");
                        String qrPromoCode = doc.getString("QRPromoCode");
//                        String link = doc.getId();
                        String link = doc.getString("link");
//                        if(imageUri == null){
//                            imageUri = "";
//                        }
                        eventDataList.add(new Event(event, date, time, location,
                                details, reuse, imageUri, qrCode, qrPromoCode, link));
                    }
                    eventArrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    /**
     * Adding a new event to the list and Firebase database.
     *
     * @param event The event to add.
     */
    private void addNewEvent(Event event) {
        // Adding the event to the list
        eventDataList.add(event);
        eventArrayAdapter.notifyDataSetChanged();

        // Prepare data to be stored in Firebase
        HashMap<String, Object> data = new HashMap<>();
        data.put("Reuse", event.getReuse());
        data.put("Date", event.getDate());
        data.put("Time", event.getTime());
        data.put("Location", event.getLocation());
        data.put("Details", event.getDetails());
        data.put("Image", event.getImage());
        data.put("QR Code", event.getQrCode());
        data.put("QR Promo Code", event.getQrPromoCode());
        data.put("Link", event.getLink());

        // Store data in Firebase
        eventsRef.document(event.getName()).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "Event added with checkbox selections");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error adding event with checkbox selections", e);
                    }
                });
    }

    // URI for the event image
    private String imageUri;
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
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    /**
     * Displays a dialog for adding a new event. The dialog includes input fields for event details
     * such as name, date, time, location, and details. Additionally, it provides checkboxes for marking
     * the event as a promotional event or for indicating if the event can be reused. The dialog also
     * allows the user to attach a poster image to the event. After filling in the details and confirming,
     * the new event is added to the list.
     */
    private void showAddEventDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_add_event, null);

        // Initializing views from the dialog layout
        final EditText addEventEditText = view.findViewById(R.id.add_event_editText);
        final CheckBox reuseCheck = view.findViewById(R.id.reuseChecker);
        final EditText addEventEditDate = view.findViewById(R.id.add_date_editText);
        final EditText addEventEditTime = view.findViewById(R.id.add_time_editText);
        final EditText addEventEditLocation = view.findViewById(R.id.add_location_editText);
        final EditText addEventEditDetails = view.findViewById(R.id.add_details_editText);
        Button buttonPoster = view.findViewById(R.id.buttonPoster);
        Button buttonLink = view.findViewById(R.id.buttonLink);

        buttonPoster.setBackgroundColor(getResources().getColor(R.color.light_blue_100));
        buttonPoster.setTextColor(getResources().getColor(R.color.white));
        buttonLink.setBackgroundColor(getResources().getColor(R.color.light_blue_100));
        buttonLink.setTextColor(getResources().getColor(R.color.white));

        // Setting click listener for the poster button to select an image from the gallery
        buttonPoster.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, ADD_EVENT_REQUEST);
        });

        buttonLink.setOnClickListener(v -> {
            showAddLinkDialog();
        });


        // Building and displaying the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setTitle("New Event")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Retrieving the input values
                    String eventName = addEventEditText.getText().toString();
                    boolean reuse_check = reuseCheck.isChecked();
                    String eventDate = addEventEditDate.getText().toString();
                    String eventTime = addEventEditTime.getText().toString();
                    String eventLocation = addEventEditLocation.getText().toString();
                    String eventDetails = addEventEditDetails.getText().toString();

                    // Creating a new event object and adding it if event name is not empty
                    if (!eventName.isEmpty()) {
                        Event newEvent = new Event(eventName, eventDate, eventTime, eventLocation,
                                eventDetails, reuse_check, imageUri, "", "", "");

                        newEvent.setReuse(reuse_check);
                        newEvent.setImage(imageUri);

                        generateQRCode(eventName, reuse_check);

                        newEvent.setQrCode(base64QRCode);
                        generatePROMOCode(eventName);
                        newEvent.setQrPromoCode(base64QRPromoCode);
                        if (info != null && !info.isEmpty()) {
                            newEvent.setLink(info);
                        }

                        addNewEvent(newEvent);


                        Map<String, Object> data = new HashMap<>();
                        DocumentReference documentReference = eventsRef.document(eventName);
                        data.put("event", documentReference);
                        if(reuse_check){
                            qrRef.document("RE_USE").set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Firestore", "QR added");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("Firestore", "Error adding QR", e);
                                        }
                                    });
                        } else {
                            qrRef.document(previousId).set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Firestore", "QR added");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("Firestore", "Error adding QR", e);
                                        }
                                    });
                        }
                    }
                    // Refreshing event list
                    eventArrayAdapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void generateQRCode(String eventName, boolean reuse) {

        try {
            generateRandomId(reuse);
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode("QrCodes/"+previousId, BarcodeFormat.QR_CODE, 250, 250);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            // Convert the resized bitmap to byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            // Convert byte array to base64 string
            base64QRCode = Base64.encodeToString(byteArray, Base64.DEFAULT);
//            uploadIdToFirebase(previousId, eventName);
        } catch (WriterException e) {
            // Handle QR code generation failure
            e.printStackTrace();
            Toast.makeText(this, "Failed to generate QR code.", Toast.LENGTH_SHORT).show();
        }
    }
    public String generateRandomId(boolean reusePrevious) {
        if (reusePrevious && previousId != null) {
            return previousId;
        } else {
            String newId = UUID.randomUUID().toString(); // Generate new random ID
            previousId = newId; // Store the new random ID
            return newId;
        }
    }
    private void generatePROMOCode(String eventName) {
        try {
            String all = "Events/" + eventName;

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(all, BarcodeFormat.QR_CODE, 250, 250);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            // Convert the resized bitmap to byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            // Convert byte array to base64 string
            base64QRPromoCode = Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (WriterException e) {
            // Handle QR code generation failure
            e.printStackTrace();
            Toast.makeText(this, "Failed to generate QR code.", Toast.LENGTH_SHORT).show();
        }
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
                        int desiredWidth = 250; // Adjust the width as needed
                        int desiredHeight = 250; // Adjust the height as needed

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
                            imageUri = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }
        }
    }
}

