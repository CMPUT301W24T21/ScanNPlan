package com.example.project_3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Activity displaying details of an event.
 */
public class EventDetailsActivity extends AppCompatActivity {
    private String eventName;
    private String eventDate;
    private String eventTime;
    private String eventLocation;
    private String qrCode;
    private String qrPromoCode;
    private String imageUri;
    private String link;
    private String all;
    private Boolean promoChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        eventName = getIntent().getStringExtra("eventName");
        eventLocation = getIntent().getStringExtra("eventLocation");
        eventDate = getIntent().getStringExtra("eventDate");
        eventTime = getIntent().getStringExtra("eventTime");
        qrCode = getIntent().getStringExtra("QRCode");
        qrPromoCode = getIntent().getStringExtra("QRPromoCode");
        imageUri = getIntent().getStringExtra("imageUri");
        link = getIntent().getStringExtra("link");

        if (eventName == null || eventLocation == null || eventDate == null || eventTime == null) {
            // Handle missing event details
            Toast.makeText(this, "Event details are missing.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView eventTextView = findViewById(R.id.event_name_text_view);
        eventTextView.setText(eventName);

        Button backButton = findViewById(R.id.button_back);
        FloatingActionButton editEventButton = findViewById(R.id.floatingEditButton);
        Button attendees = findViewById(R.id.attendees);
        Button checkIns = findViewById(R.id.check_ins);
        Button signUps = findViewById(R.id.sign_ups);

        signUps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEventDialog();
            }
        });
        

        attendees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OrganizerListActivity.class);
                intent.putExtra("event_name", eventName);
                startActivity(intent);
            }
        });


        // Set click listener for the "checked in" button
        checkIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the AttendeesCheckedInActivity when the button is clicked
                Intent intent = new Intent(v.getContext(), AttendeesCheckedInActivity.class);
                intent.putExtra("event_name", eventName); // Add event name as an extra
                startActivity(intent);
            }
        });


        // Set an OnClickListener for the FloatingActionButton
        editEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the EditEventDetails activity
                Intent intent = new Intent(EventDetailsActivity.this, EditEventDetails.class);
                intent.putExtra("eventName", eventName); // Pass event details if needed
                intent.putExtra("eventLocation", eventLocation);
                intent.putExtra("eventDate", eventDate);
                intent.putExtra("eventTime", eventTime);
                intent.putExtra("QRCode", qrCode);
                intent.putExtra("QRPromoCode", qrPromoCode);
                intent.putExtra("imageUri", imageUri);
                intent.putExtra("link", link);
                startActivity(intent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Events").document(eventName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String base64Image = document.getString("Image");
                                if (base64Image != null && !base64Image.isEmpty()) {
                                    // Decode base64 string to bitmap
                                    byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                    if (decodedByte != null) {
                                        // Set bitmap to ImageView
                                        ImageView eventPosterImageView = findViewById(R.id.targetImage);
                                        eventPosterImageView.setImageBitmap(decodedByte);
                                        promoChecker = true;
                                    } else {
                                        Log.e("Image Decoding", "Failed to decode base64 string into bitmap.");
                                        promoChecker = false;
                                    }
                                } else {
                                    promoChecker = false;
                                }

                            } else {
                                // Handle document not found
                                Toast.makeText(EventDetailsActivity.this, "Event details not found.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Handle Firestore query failure
                            Toast.makeText(EventDetailsActivity.this, "Failed to retrieve event details.", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

    private void showAddEventDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_sign_ups, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setTitle("Signed Up Attendees")
                .setNegativeButton("Cancel", null)
                .show();
    }

}
