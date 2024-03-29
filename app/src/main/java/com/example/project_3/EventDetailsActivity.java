package com.example.project_3;
//
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//
///**
// * Activity displaying details of an event.
// */
//public class EventDetailsActivity extends AppCompatActivity {
//    private Event event;
//    private String eventName;
//    private String eventDate;
//    private String eventTime;
//    private String eventLocation;
//    private String all;
//    private String imageUri;
//    private Boolean promoChecker;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_event_details);
//
//        // all this can be displayed on screen but so far we're only displaying
//        // the event name
//        eventName = getIntent().getStringExtra("eventName");
//        eventLocation = getIntent().getStringExtra("eventLocation");
//        eventDate = getIntent().getStringExtra("eventDate");
//        eventTime = getIntent().getStringExtra("eventTime");
//        imageUri = getIntent().getStringExtra("imageUri");
//
//        // Creating a string containing all event details for QR code
//        // for QR code purposes
//        all = eventName + "_" + eventLocation + "_" + eventDate + "_" + eventTime;
//
//        // displaying the event name
//        TextView eventTextView = findViewById(R.id.event_name_text_view);
//        eventTextView.setText(eventName);
//
//        // defining the back button
//        Button backButton = findViewById(R.id.button_back);
//
//        // setting the back button listener to return to previous activity
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        // defining the QR buttons
//        Button qrButton = findViewById(R.id.qr_button_event);
//        Button qrPromoButton = findViewById(R.id.qr_promo_event);
//
//        // hide the promo qr button
//        qrPromoButton.setVisibility(View.GONE);
//
//        // Load image URL from Firebase Firestore
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("Events").document(eventName)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document != null && document.exists()) {
//                                String imageUrl = document.getString("Image");
//                                if (imageUrl != null && !imageUrl.isEmpty()) {
//                                    // Use Glide to load the image from the URL
//                                    ImageView eventPosterImageView = findViewById(R.id.targetImage);
//                                    Glide.with(EventDetailsActivity.this)
//                                            .load(imageUrl)
//                                            .apply(new RequestOptions().placeholder(R.drawable.baseline_image_24)) // Placeholder if image loading fails
//                                            .into(eventPosterImageView);
//                                    // Set the flag to indicate that the poster image is present
//                                    promoChecker = true;
//                                } else {
//                                    // Set the flag to indicate that the poster image is not present
//                                    promoChecker = false;
//                                }
//                                // Show or hide the promo QR button based on the flag
//                                if (promoChecker) {
//                                    qrPromoButton.setVisibility(View.VISIBLE);
//                                } else {
//                                    qrPromoButton.setVisibility(View.GONE);
//                                }
//                            }
//                        } else {
//                            // Handle errors
//                            Toast.makeText(EventDetailsActivity.this, "Failed to retrieve image URL", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//        // Define the floating action button for editing event details
//        FloatingActionButton fabAddEvent = findViewById(R.id.floatingEditButton);
//        fabAddEvent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Start the EditEventDetails activity
//                Intent intent = new Intent(EventDetailsActivity.this, EditEventDetails.class);
//                startActivity(intent); // start the EditEventDetails activity
//            }
//        });
//
//
//        // setting the listener for QR buttons s.t. the QR code gets generated depending
//        // on the chosen button
//        qrButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                generateAndDisplayQRCode();
//            }
//        });
//        qrPromoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                generateAndDisplayQRPromoCode();
//            }
//        });
//    }
//
//    /**
//     * Generates and displays a QR code based on the event details.
//     */
//    private void generateAndDisplayQRCode() {
//        // Creating a QRCodeWriter instance
//        QRCodeWriter writer = new QRCodeWriter();
//        try {
//            // Encoding the event details into a BitMatrix
//            BitMatrix bitMatrix = writer.encode(all, BarcodeFormat.QR_CODE, 512, 512);
//            int width = bitMatrix.getWidth();
//            int height = bitMatrix.getHeight();
//            // Creating a bitmap to store the QR code image
//            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//            // Iterating over each pixel in the BitMatrix and set corresponding pixel in the bitmap
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    // Set pixel color to black if the corresponding BitMatrix element is true, otherwise set it to white
//                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
//                }
//            }
//            // Finding the ImageView to display the QR code
//            ImageView qrCodeImageView = findViewById(R.id.qr_code_image_view);
//            // Setting the bitmap as the image source for the ImageView
//            qrCodeImageView.setImageBitmap(bmp);
//            // Making the ImageView visible
//            qrCodeImageView.setVisibility(View.VISIBLE);
//        } catch (WriterException e) {
//            // Handling exception if QR code generation fails
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Generates and displays a promotional QR code based on the event details.
//     */
//    private void generateAndDisplayQRPromoCode() {
//        // Creating a QRCodeWriter instance
//        QRCodeWriter writer = new QRCodeWriter();
//        try {
//            // Encoding the event details with "_promo" suffix into a BitMatrix
//            BitMatrix bitMatrix = writer.encode(all+"_promo", BarcodeFormat.QR_CODE, 512, 512);
//            int width = bitMatrix.getWidth();
//            int height = bitMatrix.getHeight();
//            // Creating a bitmap to store the QR code image
//            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//            // Iterating over each pixel in the BitMatrix and set corresponding pixel in the bitmap
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    // Setting pixel color to black if the corresponding BitMatrix element is true
//                    // otherwise set it to white
//                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
//                }
//            }
//            // Finding the ImageView to display the QR code
//            ImageView qrCodeImageView = findViewById(R.id.qr_code_image_view);
//            // Setting the bitmap as the image source for the ImageView
//            qrCodeImageView.setImageBitmap(bmp);
//            // Making the ImageView visible
//            qrCodeImageView.setVisibility(View.VISIBLE);
//        } catch (WriterException e) {
//            // Handling exception if QR code generation fails
//            e.printStackTrace();
//        }
//    }
//}

//
//import android.graphics.BitmapFactory;
//import android.util.Base64;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//
//
///**
// * Activity displaying details of an event.
// */
//public class EventDetailsActivity extends AppCompatActivity {
//    private Event event;
//    private String eventName;
//    private String eventDate;
//    private String eventTime;
//    private String eventLocation;
//    private String all;
//    private String imageUri;
//    private Boolean promoChecker;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_event_details);
//
//        // all this can be displayed on screen but so far we're only displaying
//        // the event name
//        eventName = getIntent().getStringExtra("eventName");
//        eventLocation = getIntent().getStringExtra("eventLocation");
//        eventDate = getIntent().getStringExtra("eventDate");
//        eventTime = getIntent().getStringExtra("eventTime");
//        imageUri = getIntent().getStringExtra("imageUri");
//
//        // Creating a string containing all event details for QR code
//        // for QR code purposes
//        all = eventName + "_" + eventLocation + "_" + eventDate + "_" + eventTime;
//
//        // displaying the event name
//        TextView eventTextView = findViewById(R.id.event_name_text_view);
//        eventTextView.setText(eventName);
//
//        // defining the back button
//        Button backButton = findViewById(R.id.button_back);
//
//        // setting the back button listener to return to previous activity
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        // defining the QR buttons
//        Button qrButton = findViewById(R.id.qr_button_event);
//        Button qrPromoButton = findViewById(R.id.qr_promo_event);
//
//        // hide the promo qr button
//        qrPromoButton.setVisibility(View.GONE);
//
//        // Load image URL from Firebase Firestore
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("Events").document(eventName)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document != null && document.exists()) {
//                                String imageUrl = document.getString("Image");
//                                if (imageUrl != null && !imageUrl.isEmpty()) {
//                                    // Use Glide to load the image from the Base64 string
//                                    ImageView eventPosterImageView = findViewById(R.id.targetImage);
//                                    Glide.with(EventDetailsActivity.this)
//                                            .load(imageUrl)
//                                            .into(eventPosterImageView);
//                                    // Set the flag to indicate that the poster image is present
//                                    promoChecker = true;
//                                } else {
//                                    // Set the flag to indicate that the poster image is not present
//                                    promoChecker = false;
//                                }
//
//                                // Show or hide the promo QR button based on the flag
//                                if (promoChecker) {
//                                    qrPromoButton.setVisibility(View.VISIBLE);
//                                } else {
//                                    qrPromoButton.setVisibility(View.GONE);
//                                }
//                            }
//                        } else {
//                            // Handle errors
//                            Toast.makeText(EventDetailsActivity.this, "Failed to retrieve image URL", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//        // Define the floating action button for editing event details
//        FloatingActionButton fabAddEvent = findViewById(R.id.floatingEditButton);
//        fabAddEvent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Start the EditEventDetails activity
//                Intent intent = new Intent(EventDetailsActivity.this, EditEventDetails.class);
//                startActivity(intent); // start the EditEventDetails activity
//            }
//        });
//
//
//        // setting the listener for QR buttons s.t. the QR code gets generated depending
//        // on the chosen button
//        qrButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                generateAndDisplayQRCode();
//            }
//        });
//        qrPromoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                generateAndDisplayQRPromoCode();
//            }
//        });
//    }
//
//    /**
//     * Generates and displays a QR code based on the event details.
//     */
//    private void generateAndDisplayQRCode() {
//        // Creating a QRCodeWriter instance
//        QRCodeWriter writer = new QRCodeWriter();
//        try {
//            // Encoding the event details into a BitMatrix
//            BitMatrix bitMatrix = writer.encode(all, BarcodeFormat.QR_CODE, 512, 512);
//            int width = bitMatrix.getWidth();
//            int height = bitMatrix.getHeight();
//            // Creating a bitmap to store the QR code image
//            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//            // Iterating over each pixel in the BitMatrix and set corresponding pixel in the bitmap
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    // Set pixel color to black if the corresponding BitMatrix element is true, otherwise set it to white
//                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
//                }
//            }
//            // Finding the ImageView to display the QR code
//            ImageView qrCodeImageView = findViewById(R.id.qr_code_image_view);
//            // Setting the bitmap as the image source for the ImageView
//            qrCodeImageView.setImageBitmap(bmp);
//            // Making the ImageView visible
//            qrCodeImageView.setVisibility(View.VISIBLE);
//        } catch (WriterException e) {
//            // Handling exception if QR code generation fails
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Generates and displays a promotional QR code based on the event details.
//     */
//    private void generateAndDisplayQRPromoCode() {
//        // Creating a QRCodeWriter instance
//        QRCodeWriter writer = new QRCodeWriter();
//        try {
//            // Encoding the event details with "_promo" suffix into a BitMatrix
//            BitMatrix bitMatrix = writer.encode(all+"_promo", BarcodeFormat.QR_CODE, 512, 512);
//            int width = bitMatrix.getWidth();
//            int height = bitMatrix.getHeight();
//            // Creating a bitmap to store the QR code image
//            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//            // Iterating over each pixel in the BitMatrix and set corresponding pixel in the bitmap
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    // Setting pixel color to black if the corresponding BitMatrix element is true
//                    // otherwise set it to white
//                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
//                }
//            }
//            // Finding the ImageView to display the QR code
//            ImageView qrCodeImageView = findViewById(R.id.qr_code_image_view);
//            // Setting the bitmap as the image source for the ImageView
//            qrCodeImageView.setImageBitmap(bmp);
//            // Making the ImageView visible
//            qrCodeImageView.setVisibility(View.VISIBLE);
//        } catch (WriterException e){
//            // Handling exception if QR code generation fails
//            e.printStackTrace();
//        }
//    }
//}

//
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//
///**
// * Activity displaying details of an event.
// */
//public class EventDetailsActivity extends AppCompatActivity {
//    private String eventName;
//    private String eventDate;
//    private String eventTime;
//    private String eventLocation;
//    private String all;
//    private Boolean promoChecker;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_event_details);
//
//        eventName = getIntent().getStringExtra("eventName");
//        eventLocation = getIntent().getStringExtra("eventLocation");
//        eventDate = getIntent().getStringExtra("eventDate");
//        eventTime = getIntent().getStringExtra("eventTime");
//
//        if (eventName == null || eventLocation == null || eventDate == null || eventTime == null) {
//            // Handle missing event details
//            Toast.makeText(this, "Event details are missing.", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        all = eventName + "_" + eventLocation + "_" + eventDate + "_" + eventTime;
//
//        TextView eventTextView = findViewById(R.id.event_name_text_view);
//        eventTextView.setText(eventName);
//
//        Button backButton = findViewById(R.id.button_back);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        Button qrButton = findViewById(R.id.qr_button_event);
//        Button qrPromoButton = findViewById(R.id.qr_promo_event);
//        qrPromoButton.setVisibility(View.GONE);
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("Events").document(eventName)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document != null && document.exists()) {
//                                String imageUrl = document.getString("Image");
//                                if (imageUrl != null && !imageUrl.isEmpty()) {
//                                    ImageView eventPosterImageView = findViewById(R.id.targetImage);
//                                    Glide.with(EventDetailsActivity.this)
//                                            .load(imageUrl)
//                                            .apply(new RequestOptions().placeholder(R.drawable.baseline_image_24)) // Placeholder if image loading fails
//                                            .into(eventPosterImageView);
//                                    // Use Glide or your preferred image loading library here
//                                    // to load the image from the URL
//                                    // Glide.with(EventDetailsActivity.this).load(imageUrl).into(eventPosterImageView);
//                                    promoChecker = true;
//                                } else {
//                                    promoChecker = false;
//                                }
//
//                                if (promoChecker) {
//                                    qrPromoButton.setVisibility(View.VISIBLE);
//                                } else {
//                                    qrPromoButton.setVisibility(View.GONE);
//                                }
//                            } else {
//                                // Handle document not found
//                                Toast.makeText(EventDetailsActivity.this, "Event details not found.", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            // Handle Firestore query failure
//                            Toast.makeText(EventDetailsActivity.this, "Failed to retrieve event details.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//        qrButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                generateAndDisplayQRCode(all);
//            }
//        });
//        qrPromoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                generateAndDisplayQRPromoCode(all);
//            }
//        });
//    }
//
//    private void generateAndDisplayQRCode(String data) {
//        try {
//            QRCodeWriter writer = new QRCodeWriter();
//            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512);
//            int width = bitMatrix.getWidth();
//            int height = bitMatrix.getHeight();
//            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
//                }
//            }
//            ImageView qrCodeImageView = findViewById(R.id.qr_code_image_view);
//            qrCodeImageView.setImageBitmap(bmp);
//            qrCodeImageView.setVisibility(View.VISIBLE);
//        } catch (WriterException e) {
//            // Handle QR code generation failure
//            e.printStackTrace();
//            Toast.makeText(this, "Failed to generate QR code.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void generateAndDisplayQRPromoCode(String data) {
//        try {
//            QRCodeWriter writer = new QRCodeWriter();
//            BitMatrix bitMatrix = writer.encode(data + "_promo", BarcodeFormat.QR_CODE, 512, 512);
//            int width = bitMatrix.getWidth();
//            int height = bitMatrix.getHeight();
//            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
//                }
//            }
//            ImageView qrCodeImageView = findViewById(R.id.qr_code_image_view);
//            qrCodeImageView.setImageBitmap(bmp);
//            qrCodeImageView.setVisibility(View.VISIBLE);
//        } catch (WriterException e) {
//            // Handle QR code generation failure
//            e.printStackTrace();
//            Toast.makeText(this, "Failed to generate promotional QR code.", Toast.LENGTH_SHORT).show();
//        }
//    }
//}



//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Base64;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//
///**
// * Activity displaying details of an event.
// */
//public class EventDetailsActivity extends AppCompatActivity {
//    private String eventName;
//    private String eventDate;
//    private String eventTime;
//    private String eventLocation;
//    private String all;
//    private Boolean promoChecker;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_event_details);
//
//        eventName = getIntent().getStringExtra("eventName");
//        eventLocation = getIntent().getStringExtra("eventLocation");
//        eventDate = getIntent().getStringExtra("eventDate");
//        eventTime = getIntent().getStringExtra("eventTime");
//
//        if (eventName == null || eventLocation == null || eventDate == null || eventTime == null) {
//            // Handle missing event details
//            Toast.makeText(this, "Event details are missing.", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        all = eventName + "_" + eventLocation + "_" + eventDate + "_" + eventTime;
//
//        TextView eventTextView = findViewById(R.id.event_name_text_view);
//        eventTextView.setText(eventName);
//
//        Button backButton = findViewById(R.id.button_back);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        Button qrButton = findViewById(R.id.qr_button_event);
//        Button qrPromoButton = findViewById(R.id.qr_promo_event);
//        qrPromoButton.setVisibility(View.GONE);
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("Events").document(eventName)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document != null && document.exists()) {
//                                String base64Image = document.getString("Image");
//                                if (base64Image != null && !base64Image.isEmpty()) {
//                                    // Decode base64 string to bitmap
//                                    byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
//                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//
//                                    if (decodedByte != null) {
//                                        // Set bitmap to ImageView
//                                        ImageView eventPosterImageView = findViewById(R.id.targetImage);
//                                        eventPosterImageView.setImageBitmap(decodedByte);
//                                        promoChecker = true;
//                                    } else {
//                                        Log.e("Image Decoding", "Failed to decode base64 string into bitmap.");
//                                        promoChecker = false;
//                                    }
//                                } else {
//                                    promoChecker = false;
//                                }
//
//                                if (promoChecker) {
//                                    qrPromoButton.setVisibility(View.VISIBLE);
//                                } else {
//                                    qrPromoButton.setVisibility(View.GONE);
//                                }
//                            } else {
//                                // Handle document not found
//                                Toast.makeText(EventDetailsActivity.this, "Event details not found.", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            // Handle Firestore query failure
//                            Toast.makeText(EventDetailsActivity.this, "Failed to retrieve event details.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                });
//
//        qrButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                generateAndDisplayQRCode(all);
//            }
//        });
//        qrPromoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                generateAndDisplayQRPromoCode(all);
//            }
//        });
//    }
//
//    private void generateAndDisplayQRCode(String data) {
//        try {
//            QRCodeWriter writer = new QRCodeWriter();
//            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 250, 250);
//            int width = bitMatrix.getWidth();
//            int height = bitMatrix.getHeight();
//            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
//                }
//            }
//            ImageView qrCodeImageView = findViewById(R.id.qr_code_image_view);
//            qrCodeImageView.setImageBitmap(bmp);
//            qrCodeImageView.setVisibility(View.VISIBLE);
//        } catch (WriterException e) {
//            // Handle QR code generation failure
//            e.printStackTrace();
//            Toast.makeText(this, "Failed to generate QR code.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void generateAndDisplayQRPromoCode(String data) {
//        try {
//            QRCodeWriter writer = new QRCodeWriter();
//            BitMatrix bitMatrix = writer.encode(data + "_promo", BarcodeFormat.QR_CODE, 512, 512);
//            int width = bitMatrix.getWidth();
//            int height = bitMatrix.getHeight();
//            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
//                }
//            }
//            ImageView qrCodeImageView = findViewById(R.id.qr_code_image_view);
//            qrCodeImageView.setImageBitmap(bmp);
//            qrCodeImageView.setVisibility(View.VISIBLE);
//        } catch (WriterException e) {
//            // Handle QR code generation failure
//            e.printStackTrace();
//            Toast.makeText(this, "Failed to generate promotional QR code.", Toast.LENGTH_SHORT).show();
//        }
//    }
//}



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
                Intent intent = new Intent(EventDetailsActivity.this, AttendeesCheckedInActivity.class);
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

}
