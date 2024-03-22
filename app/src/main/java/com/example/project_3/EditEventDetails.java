package com.example.project_3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for editing event details.
 */
public class EditEventDetails extends AppCompatActivity {

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle).
     */

    // Declare ImageView for QR code
    private ImageView qrCodeImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event);

        // Getting reference to the back button
        Button backButton = findViewById(R.id.back_button);
        // Setting click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finishing the activity when the back button is clicked
                finish();
            }
        });

        // Getting reference to the "View QR" button
        Button viewQRButton = findViewById(R.id.view_qrcode);

        // Setting click listener for the "View QR" button
        viewQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the QRCodeDisplayActivity
                Intent intent = new Intent(EditEventDetails.this, QRCodeDisplayActivity.class);
                // Pass any necessary data to QRCodeDisplayActivity if needed
                startActivity(intent);
            }
        });
    }
}
