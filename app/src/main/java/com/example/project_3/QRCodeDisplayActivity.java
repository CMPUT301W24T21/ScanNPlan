package com.example.project_3;

/**
 * This activity displays the QR code for a specific event along with event details.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
/**
 * This activity displays the QR code for a specific event along with event details.
 */
public class QRCodeDisplayActivity extends AppCompatActivity {
    private String eventName;
    private String eventLocation;
    private String eventDate;
    private String eventTime;
    private String qrCode;
    private String qrPromoCode;
    private String imageUri;
    private String link;

    /**
     * Initializes the activity layout and retrieves event details from the intent.
     * Retrieves QR code image and event details from Firestore and displays them.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_display);

        // Retrieve event details from the intent
        eventName = getIntent().getStringExtra("eventName");
        eventLocation = getIntent().getStringExtra("eventLocation");
        eventDate = getIntent().getStringExtra("eventDate");
        eventTime = getIntent().getStringExtra("eventTime");
        qrCode = getIntent().getStringExtra("QRCode");
        qrPromoCode = getIntent().getStringExtra("QRPromoCode");
        imageUri = getIntent().getStringExtra("imageUri");
        link = getIntent().getStringExtra("link");

        // Getting reference to the back button
        Button backButton = findViewById(R.id.back_qr_button);

        // Setting click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the QRCodeDisplayActivity and return to the EditEventDetails activity
                finish();
            }
        });

        Button nextButton = findViewById(R.id.next_button);
        // Setting click listener for the next button to navigate to the PromoQRCodeDisplayActivity
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the PromoQRCodeDisplayActivity
                Intent intent = new Intent(QRCodeDisplayActivity.this, PromoQRCodeDisplayActivity.class);
                // Pass event details to PromoQRCodeDisplayActivity if needed
                intent.putExtra("eventName", eventName);
                intent.putExtra("eventLocation", eventLocation);
                intent.putExtra("eventDate", eventDate);
                intent.putExtra("eventTime", eventTime);
                intent.putExtra("QRCode", qrCode);
                intent.putExtra("QRPromoCode", qrPromoCode);
                // Pass any necessary data to PromoQRCodeDisplayActivity if needed
                startActivity(intent);
            }
        });

        Button shareButton = findViewById(R.id.share_button);
        // Setting click listener for the share button to share the QR code image
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the QR code bitmap from the ImageView
                ImageView qrCodeImageView = findViewById(R.id.qr_code_image);
                qrCodeImageView.setDrawingCacheEnabled(true);
                Bitmap qrCodeBitmap = Bitmap.createBitmap(qrCodeImageView.getDrawingCache());
                qrCodeImageView.setDrawingCacheEnabled(false);

                // Create an intent with ACTION_SEND to share the QR code image
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");

                // Add the QR code bitmap as an extra to the intent
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), qrCodeBitmap, "QR Code", null);
                Uri qrCodeUri = Uri.parse(path);
                shareIntent.putExtra(Intent.EXTRA_STREAM, qrCodeUri);

                // Start the activity chooser to allow the user to select where to share the image
                startActivity(Intent.createChooser(shareIntent, "Share QR Code"));
            }
        });

        // Retrieve QR code image from Firestore and display it in the ImageView
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Events").document(eventName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String base64Image = document.getString("QR Code");
                                if (base64Image != null && !base64Image.isEmpty()) {
                                    // Decode base64 string to bitmap
                                    byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                    if (decodedByte != null) {
                                        // Set bitmap to ImageView
                                        ImageView eventPosterImageView = findViewById(R.id.qr_code_image);
                                        eventPosterImageView.setImageBitmap(decodedByte);
                                    } else {
                                        Log.e("Image Decoding", "Failed to decode base64 string into bitmap.");
                                    }
                                } else {
                                }

                            } else {
                                // Handle document not found
                                Toast.makeText(QRCodeDisplayActivity.this, "Event details not found.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Handle Firestore query failure
                            Toast.makeText(QRCodeDisplayActivity.this, "Failed to retrieve event details.", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }
}
