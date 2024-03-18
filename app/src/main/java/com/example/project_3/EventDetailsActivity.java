package com.example.project_3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
    private String all;
    private String imageUri;
    private Boolean promoChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // all this can be displayed on screen but so far we're only displaying
        // the event name
        eventName = getIntent().getStringExtra("eventName");
        eventLocation = getIntent().getStringExtra("eventLocation");
        eventDate = getIntent().getStringExtra("eventDate");
        eventTime = getIntent().getStringExtra("eventTime");
        imageUri = getIntent().getStringExtra("imageUri");

        // Creating a string containing all event details for QR code
        // for QR code purposes
        all = eventName + "_" + eventLocation + "_" + eventDate + "_" + eventTime;

        // displaying the event name
        TextView eventTextView = findViewById(R.id.event_name_text_view);
        eventTextView.setText(eventName);

        // defining the back button
        Button backButton = findViewById(R.id.button_back);

        // setting the back button listener to return to previous activity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // defining the QR buttons
        Button qrButton = findViewById(R.id.qr_button_event);
        Button qrPromoButton = findViewById(R.id.qr_promo_event);

        // hide the promo qr button
        qrPromoButton.setVisibility(View.GONE);

        // Load image URL from Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Events").document(eventName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String imageUrl = document.getString("Image");
                                if (imageUrl != null && !imageUrl.isEmpty()) {
                                    // Use Glide to load the image from the URL
                                    ImageView eventPosterImageView = findViewById(R.id.targetImage);
                                    Glide.with(EventDetailsActivity.this)
                                            .load(imageUrl)
                                            .apply(new RequestOptions().placeholder(R.drawable.baseline_image_24)) // Placeholder if image loading fails
                                            .into(eventPosterImageView);
                                    // Set the flag to indicate that the poster image is present
                                    promoChecker = true;
                                } else {
                                    // Set the flag to indicate that the poster image is not present
                                    promoChecker = false;
                                }
                                // Show or hide the promo QR button based on the flag
                                if (promoChecker) {
                                    qrPromoButton.setVisibility(View.VISIBLE);
                                } else {
                                    qrPromoButton.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            // Handle errors
                            Toast.makeText(EventDetailsActivity.this, "Failed to retrieve image URL", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // Define the floating action button for editing event details
        FloatingActionButton fabAddEvent = findViewById(R.id.floatingEditButton);
        fabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the EditEventDetails activity
                Intent intent = new Intent(EventDetailsActivity.this, EditEventDetails.class);
                startActivity(intent); // start the EditEventDetails activity
            }
        });


        // setting the listener for QR buttons s.t. the QR code gets generated depending
        // on the chosen button
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAndDisplayQRCode();
            }
        });
        qrPromoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAndDisplayQRPromoCode();
            }
        });
    }

    /**
     * Generates and displays a QR code based on the event details.
     */
    private void generateAndDisplayQRCode() {
        // Creating a QRCodeWriter instance
        QRCodeWriter writer = new QRCodeWriter();
        try {
            // Encoding the event details into a BitMatrix
            BitMatrix bitMatrix = writer.encode(all, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            // Creating a bitmap to store the QR code image
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            // Iterating over each pixel in the BitMatrix and set corresponding pixel in the bitmap
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    // Set pixel color to black if the corresponding BitMatrix element is true, otherwise set it to white
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            // Finding the ImageView to display the QR code
            ImageView qrCodeImageView = findViewById(R.id.qr_code_image_view);
            // Setting the bitmap as the image source for the ImageView
            qrCodeImageView.setImageBitmap(bmp);
            // Making the ImageView visible
            qrCodeImageView.setVisibility(View.VISIBLE);
        } catch (WriterException e) {
            // Handling exception if QR code generation fails
            e.printStackTrace();
        }
    }

    /**
     * Generates and displays a promotional QR code based on the event details.
     */
    private void generateAndDisplayQRPromoCode() {
        // Creating a QRCodeWriter instance
        QRCodeWriter writer = new QRCodeWriter();
        try {
            // Encoding the event details with "_promo" suffix into a BitMatrix
            BitMatrix bitMatrix = writer.encode(all+"_promo", BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            // Creating a bitmap to store the QR code image
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            // Iterating over each pixel in the BitMatrix and set corresponding pixel in the bitmap
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    // Setting pixel color to black if the corresponding BitMatrix element is true
                    // otherwise set it to white
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            // Finding the ImageView to display the QR code
            ImageView qrCodeImageView = findViewById(R.id.qr_code_image_view);
            // Setting the bitmap as the image source for the ImageView
            qrCodeImageView.setImageBitmap(bmp);
            // Making the ImageView visible
            qrCodeImageView.setVisibility(View.VISIBLE);
        } catch (WriterException e) {
            // Handling exception if QR code generation fails
            e.printStackTrace();
        }
    }
}