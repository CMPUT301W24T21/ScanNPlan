package com.example.project_3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
//source: https://github.com/osmdroid/osmdroid/wiki/How-to-use-the-osmdroid-library-(Java) for locations permission checking
/**
 * Activity for scanning QR codes and barcodes using the ZXing library.
 */
public class QRScan extends AppCompatActivity implements View.OnClickListener {
    Button scanBtn;
    TextView messageText, messageFormat;
    private FirebaseFirestore db;
    private CollectionReference profilesRef;
    private CollectionReference eventsRef;
    private String profileID;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    /**
     * Initializes the activity, sets up the layout, and initializes UI elements.
     * @param savedInstanceState The saved instance state bundle.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_event);

        // referencing and initializing
        // the button and textviews
        scanBtn = findViewById(R.id.scanBtn);
        messageText = findViewById(R.id.textContent);
        messageFormat = findViewById(R.id.textFormat);

        // adding listener to the button
        scanBtn.setOnClickListener(this);


    }


    /**
     * Handles the click event for the scan button, initiating the scan process.
     * @param v The view that was clicked (scan button).
     */

    @Override
    public void onClick(View v) {
        // we need to create the object
        // of IntentIntegrator class
        // which is the class of QR library
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }


    /**
     * Handles the result of the scan, displaying the scanned content and format.
     * @param requestCode The request code for the scan activity.
     * @param resultCode The result code of the scan activity.
     * @param data The intent data containing the scan result.
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                messageText.setText(intentResult.getContents());
                messageFormat.setText(intentResult.getFormatName());
                db = FirebaseFirestore.getInstance();
                eventsRef = db.collection("Events");
                profilesRef = db.collection("Profiles");
                profileID = getIntent().getStringExtra("profileName");
                if (intentResult.getContents().startsWith("Events/")) {
                    //toggleRestOfPageVisibility();
                    findViewById(R.id.REST_OF_PAGE).setVisibility(View.INVISIBLE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.scanner_fragment_container, new AttendeeNewEventDetailsFragment("/" + intentResult.getContents()))
                            .addToBackStack(null)
                            .commit();

//                    profileID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//                    profilesRef.document(profileID).update("events", FieldValue.arrayUnion(db.document(intentResult.getContents())));
//                    db.document(intentResult.getContents()).update("attendees", FieldValue.arrayUnion(db.document("Profiles/"+ profileID)));

                } else if (intentResult.getContents().startsWith("QrCodes/")) {
                    db.document(intentResult.getContents()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {

                                    Log.d("DEBUG", "Profile path reference is:" + db.collection("Profiles").document(profileID).getPath());
                                    //Toast.makeText(getBaseContext(), db.collection("Profiles").document(profileID).getPath(), Toast.LENGTH_LONG).show();
                                    document.getDocumentReference("event").update("checked_in", FieldValue.arrayUnion(db.collection("Profiles").document(profileID)));
                                    FusedLocationProviderClient fusedlocation = LocationServices.getFusedLocationProviderClient(getBaseContext());
                                    if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        // checks if permissions need to be requested from user again
                                        String[] permissions = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
                                        requestPermissionsIfNecessary(permissions);
                                        return;
                                    }
                                    //get last location and add it to location on geopoint
                                    fusedlocation.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            profilesRef.document(profileID).update("location", new GeoPoint(location.getLatitude(), location.getLongitude()));
                                        }
                                    });

                                } else {
                                    Log.d("DEBUG", "No such document");
                                }
                            } else {
                                Log.d("DEBUG", "get failed with ", task.getException());
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Invalid QR code!", Toast.LENGTH_SHORT).show();

                }
            }   
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        // Finish the current activity to go back to the previous one
        //finish();
    }
    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}