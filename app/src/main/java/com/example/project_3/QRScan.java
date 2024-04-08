package com.example.project_3;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import android.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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
import java.util.HashMap;
import java.util.List;
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
    private GeoPoint geopoint;
    private Boolean isLocationUpdated = Boolean.FALSE;
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
        messageText.setText("Scan your Check-in or Promo QR Code");
        messageFormat = findViewById(R.id.textFormat);
        messageFormat.setText("Press the Scan button to continue!");
        if (!hasLocationPermissions()){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
            Toast.makeText(this, "It is recommended to turn on location permissions for check-in. You can choose to hide your geolocation from the organizer anytime in your Edit Profile page!", Toast.LENGTH_LONG).show();

        }

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
            }
            else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                messageText.setText(intentResult.getContents());
                messageFormat.setText(intentResult.getFormatName());
                db = FirebaseFirestore.getInstance();
                eventsRef = db.collection("Events");
                profilesRef = db.collection("Profiles");
                profileID = getIntent().getStringExtra("profileName");
                if (intentResult.getContents().startsWith("Events/")) {
                    // Handle event QR code scan
                    //toggleRestOfPageVisibility();
                    findViewById(R.id.REST_OF_PAGE).setVisibility(View.INVISIBLE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.scanner_fragment_container, new AttendeeNewEventDetailsFragment("/" + intentResult.getContents()))
                            .addToBackStack(null)
                            .commit();

//                    profileID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//                    profilesRef.document(profileID).update("events", FieldValue.arrayUnion(db.document(intentResult.getContents())));
//                    db.document(intentResult.getContents()).update("attendees", FieldValue.arrayUnion(db.document("Profiles/"+ profileID)));

                }
                else if (intentResult.getContents().startsWith("QrCodes/")) {
                    // Handle QR code scan for check-in
                    // Update Firestore with check-in information
                    db.document(intentResult.getContents()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {

                                    Log.d("DEBUG", "Profile path reference is:" + db.collection("Profiles").document(profileID).getPath());
                                    //Toast.makeText(getBaseContext(), db.collection("Profiles").document(profileID).getPath(), Toast.LENGTH_LONG).show();
                                    DocumentReference eventDoc = document.getDocumentReference("event");
                                    eventDoc.update("checked_in", FieldValue.arrayUnion(db.collection("Profiles").document(profileID)));
                                    getLocation(eventDoc);
                                    addEventCount(eventDoc);
                                    Toast.makeText(getBaseContext(), "Succesfully Checked-In!", Toast.LENGTH_SHORT).show();
                                    finish();

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

    }

    /**
     * Handles location permission request result.
     * @param requestCode The request code for permission request.
     * @param permissions The requested permissions.
     * @param grantResults The results of the permission request.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Checks if location permissions are granted.
     * @return True if location permissions are granted, false otherwise.
     */
    private boolean hasLocationPermissions() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Retrieves the current location and updates Firestore with the check-in location.
     * @param eventDoc The Firestore document reference for the event.
     */
    private void getLocation(DocumentReference eventDoc){
        if (!isLocationUpdated) {
            String location_context = Context.LOCATION_SERVICE;
            final LocationManager locationManager = (LocationManager) getBaseContext().getSystemService(location_context);
            List<String> providers = locationManager.getProviders(true);
            for (final String provider : providers) {
                if (getBaseContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    final LocationListener locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            geopoint = new GeoPoint(latitude, longitude);
                            eventDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Map<String, GeoPoint> checkInLocations = (Map<String, GeoPoint>) documentSnapshot.get("check_in_locations");
                                    if (checkInLocations == null) {
                                        checkInLocations = new HashMap<>();
                                    }
                                    checkInLocations.put(profileID, geopoint);
                                    eventDoc.update("check_in_locations", checkInLocations);
                                }
                            });
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                        }
                    };
                    locationManager.requestSingleUpdate(provider, locationListener, null);
                }
            }
        }
    }

    /**
     * Updates the event check-in count for the user in Firestore.
     * @param eventDoc The Firestore document reference for the event.
     */
    public void addEventCount(DocumentReference eventDoc){
        profilesRef.document(profileID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> checkedInEvents = (Map<String, Object>) document.get("checked_in_events");
                    Map<String, Object> updates = new HashMap<>();
                    if (checkedInEvents != null && checkedInEvents.containsKey(eventDoc.getId())) {
                        //if the user already checked in once, increment the count
                        updates.put("checked_in_events."+eventDoc.getId(), FieldValue.increment(1));
                        profilesRef.document(profileID).update(updates);
                    } else {
                        //if user has not checked_in, create new field and set the value to 1
                        updates.put("checked_in_events."+eventDoc.getId(), 1);
                        profilesRef.document(profileID).update(updates);
                    }
                }
            }
        });

    }

}
