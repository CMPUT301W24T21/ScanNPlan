package com.example.project_3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

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

        // Getting reference to the back button
        Button backButton = findViewById(R.id.back_button);
        Button createMessageButton = findViewById(R.id.create_message);
        Button qrViewButton = findViewById(R.id.view_qrcode);

        // https://www.youtube.com/watch?v=vyt20Gg2Ckg
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

                // Check if title or description are not empty
                if (!title.isEmpty() || !description.isEmpty()) {
                    // Create notification with title and description
                    makeNotification(title, description);
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

    public void makeNotification(String title, String description) {
        String channelId = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), channelId);
        builder.setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(description)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("data", "Some Value to be passed here");

        PendingIntent pendingIntent =
                PendingIntent.getActivity(getApplicationContext(),
                        0, intent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    notificationManager.getNotificationChannel(channelId);
            if(notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(channelId, "Some Description", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0,builder.build());

    }
}
