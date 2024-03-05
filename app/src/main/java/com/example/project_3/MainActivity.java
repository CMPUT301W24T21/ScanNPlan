package com.example.project_3;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_3.R;
import com.example.project_3.event_stuff.AddEventDialogListener;
import com.example.project_3.event_stuff.AddEventFragment;
import com.example.project_3.event_stuff.Event;
import com.example.project_3.event_stuff.EventArrayAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AddEventDialogListener {

    private ArrayList<Event> dataList;
    private ListView eventList;
    private EventArrayAdapter eventAdapter;
    private TextView textEvents; // Reference to the TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataList = new ArrayList<>();

        eventList = findViewById(R.id.event_list);
        eventAdapter = new EventArrayAdapter(this, dataList);
        eventList.setAdapter(eventAdapter);

        FloatingActionButton fab = findViewById(R.id.button_add_event);
        fab.setOnClickListener(v -> {
            new AddEventFragment().show(getSupportFragmentManager(), "Add Event");
        });

//        FloatingActionButton fab_1 = findViewById(R.id.qr_button_event);
//        fab_1.setOnClickListener(v -> {
//            // Check if there's any event to generate QR code for
//            if (!dataList.isEmpty()) {
//                Event event = dataList.get(0); // For demonstration purposes, let's assume you want to generate QR code for the first event
//                Bitmap qrCodeBitmap = generateQRCode(event.getName()); // Generate QR code for event name
//
//                if (qrCodeBitmap != null) {
//                    // Show the QR code in a dialog
//                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                    ImageView imageView = new ImageView(MainActivity.this);
//                    imageView.setImageBitmap(qrCodeBitmap);
//                    builder.setView(imageView);
//                    builder.setPositiveButton("OK", (dialog, which) -> {
//                        dialog.dismiss(); // Dismiss the dialog when OK button is clicked
//                    });
//                    builder.create().show();
//                } else {
//                    // Show a message if QR code generation fails
//                    Toast.makeText(MainActivity.this, "Failed to generate QR code", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                // Show a message if there are no events
//                Toast.makeText(MainActivity.this, "No events to generate QR code for", Toast.LENGTH_SHORT).show();
//            }
//        });
        FloatingActionButton fab_1 = findViewById(R.id.qr_button_event);
        fab_1.setOnClickListener(v -> {
            // Check if there's any event to generate QR code for
            if (!dataList.isEmpty()) {
                Event event = dataList.get(0); // For demonstration purposes, let's assume you want to generate QR code for the first event

                // Generate regular QR code
                Bitmap regularQRCodeBitmap = generateQRCode(event.getName());

                // Generate promo QR code if promo checkbox is checked
                Bitmap promoQRCodeBitmap = null;
                if (event.isPromoEvent()) {
                    promoQRCodeBitmap = generatePromoQRCode(event.getName());
                }

                // Show the QR codes in a dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_qr_codes, null);
                ImageView regularQRCodeImageView = dialogView.findViewById(R.id.imageView_regular_qr_code);
                ImageView promoQRCodeImageView = dialogView.findViewById(R.id.imageView_promo_qr_code);

                // Set regular QR code image
                if (regularQRCodeBitmap != null) {
                    regularQRCodeImageView.setImageBitmap(regularQRCodeBitmap);
                }

                // Set promo QR code image if it's not null
                if (promoQRCodeBitmap != null) {
                    promoQRCodeImageView.setVisibility(View.VISIBLE);
                    promoQRCodeImageView.setImageBitmap(promoQRCodeBitmap);
                }

                // Set regular QR code image as the dialog's view
                builder.setView(dialogView);
                builder.setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss(); // Dismiss the dialog when OK button is clicked
                });
                builder.create().show();
            } else {
                // Show a message if there are no events
                Toast.makeText(MainActivity.this, "No events to generate QR code for", Toast.LENGTH_SHORT).show();
            }
        });


        // Initialize the TextView
        textEvents = findViewById(R.id.text_events);

        // Check if dataList is empty, if yes, show the TextView
        checkEmptyDataList();
    }

    // Method to check if dataList is empty and show/hide the TextView accordingly
    private void checkEmptyDataList() {
        if (dataList.isEmpty()) {
            textEvents.setVisibility(View.VISIBLE);
        } else {
            textEvents.setVisibility(View.GONE);
        }
    }

    @Override
    public void addEvent(Event event) {
        dataList.add(event);
        eventAdapter.notifyDataSetChanged();
        checkEmptyDataList(); // Check again after adding an event
    }

    @Override
    public void editEvent(Event event) {
        eventAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteEvent(Event event){
        dataList.remove(event);
        eventAdapter.notifyDataSetChanged();
        checkEmptyDataList(); // Check again after deleting an event
    }

    // Method to generate QR code
    private Bitmap generateQRCode(String data) {
        final int QR_CODE_SIZE = 512;
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE, hints);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap qrCodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    qrCodeBitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return qrCodeBitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
    private Bitmap generatePromoQRCode(String data) {
        final int QR_CODE_SIZE = 512;
        String promoData = "PROMO: " + data; // Prefixing the event name with "PROMO: "
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(promoData, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE, hints);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap qrCodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    qrCodeBitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return qrCodeBitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

}
