package com.example.project_3;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_3.event_stuff.Event;
import com.example.project_3.event_stuff.EventArrayAdapter;
import com.example.project_3.EventDetailsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private EditText addEventEditText;

    //    private EditText addProvinceEditText;
    private ListView eventList;
    private ArrayList<Event> eventDataList;
    private EventArrayAdapter eventArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        eventDataList = new ArrayList<>();
        eventList = findViewById(R.id.event_list);
        eventArrayAdapter = new EventArrayAdapter(this, eventDataList);
        eventList.setAdapter(eventArrayAdapter);

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event selectedEvent = eventArrayAdapter.getItem(position);
                if (selectedEvent != null) {
                    Intent intent = new Intent(MainActivity.this, EventDetailsActivity.class);
                    intent.putExtra("eventName", selectedEvent.getName());
                    startActivity(intent);
                }
            }
        });

        FloatingActionButton fabAddEvent = findViewById(R.id.fab_add_event);
        fabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEventDialog();
            }
        });

        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    eventDataList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        String event = doc.getId();
                        boolean promo = false; // Default value, modify as needed
                        boolean reuse = false; // Default value, modify as needed
//                        String province = doc.getString("Province");
                        Log.d("Firestore", String.format("Event(%s, %s, %s) fetched", event, promo, reuse));
                        eventDataList.add(new Event(event, promo, reuse));
//                        Log.d("Firestore", String.format("City(%s, %s) fetched", city, province));
//                        eventDataList.add(new City(city, province));
                    }
                    eventArrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void addNewEvent(Event event) {
        eventDataList.add(event);
        eventArrayAdapter.notifyDataSetChanged();

        byte[] qrCodeByteArray = generateQRCode(event.getName());

        // Prepare data to be stored in Firebase
        HashMap<String, Object> data = new HashMap<>();
        data.put("promo", event.getPromo());
        data.put("reuse", event.getReuse());
        data.put("qrCode", Blob.fromBytes(qrCodeByteArray)); // Store QR code as byte array

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
    private byte[] generateQRCode(String text) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }




    private void showAddEventDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_add_event, null);

        final EditText addEventEditText = view.findViewById(R.id.add_event_editText);
        final CheckBox promoCheck = view.findViewById(R.id.promoChecker);
        final CheckBox reuseCheck = view.findViewById(R.id.reuseChecker);

        Button buttonPoster = view.findViewById(R.id.buttonPoster);
        Button buttonLink = view.findViewById(R.id.buttonLink);
        promoCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                buttonPoster.setVisibility(View.VISIBLE);
                buttonLink.setVisibility(View.VISIBLE);
            } else {
                buttonPoster.setVisibility(View.GONE);
                buttonLink.setVisibility(View.GONE);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setTitle("Add Event")
                .setPositiveButton("OK", (dialog, which) -> {
                    String eventName = addEventEditText.getText().toString();
                    boolean promo_check = promoCheck.isChecked();
                    boolean reuse_check = reuseCheck.isChecked();

                    // Process your event data here...
                    if (!eventName.isEmpty()) {
                        Event newEvent = new Event(eventName, promo_check, reuse_check);
                        // Set checkbox states in newEvent or handle as needed
                        newEvent.setPromo(promo_check);
                        newEvent.setReuse(reuse_check);
                        addNewEvent(newEvent);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }



    // Your existing code...
}

