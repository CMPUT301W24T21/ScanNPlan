package com.example.project_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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

public class QRCodeDisplayActivity extends AppCompatActivity {

    // Declare ImageView for QR code
    private ImageView qrCodeImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_display);

        // Initialize ImageView
        qrCodeImageView = findViewById(R.id.targetImage);


        // Getting reference to the back button
        Button backButton = findViewById(R.id.back_button);

        // Setting click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the QRCodeDisplayActivity and return to the EditEventDetails activity
                finish();
            }
        });



        // Retrieve QR code string from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Events").document("YOUR_EVENT_NAME").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String qrCodeString = document.getString("QR Code");
                                if (qrCodeString != null && !qrCodeString.isEmpty()) {
                                    // Decode Base64 string to Bitmap
                                    Bitmap qrCodeBitmap = decodeBase64(qrCodeString);
                                    if (qrCodeBitmap != null) {
                                        // Display QR code image
                                        qrCodeImageView.setImageBitmap(qrCodeBitmap);
                                    } else {
                                        // Handle decoding failure
                                        Toast.makeText(QRCodeDisplayActivity.this, "Failed to decode QR code.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Handle missing or empty QR code string
                                    Toast.makeText(QRCodeDisplayActivity.this, "QR code string is empty or missing.", Toast.LENGTH_SHORT).show();
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

    // Method to decode Base64 string to Bitmap
    private Bitmap decodeBase64(String base64String) {
        try {
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
