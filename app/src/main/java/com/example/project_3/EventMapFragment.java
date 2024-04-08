package com.example.project_3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
//Source: https://github.com/osmdroid/osmdroid/wiki/How-to-use-the-osmdroid-library-(Java)
/**
 * This fragment displays a map with event locations and attendee check-ins.
 *
 */

public class EventMapFragment extends Fragment  {
    private MapView map;
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private CollectionReference profilesRef;
    private String event;

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    /**
     * Constructor to set the event for this fragment.
     * @param event The event ID to display on the map.
     */
    public EventMapFragment(String event){
        this.event = event;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        Context context = getContext();

        // Load configuration for OSMdroid
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));

        Toast.makeText(context, "Please be patient...", Toast.LENGTH_SHORT).show();
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");
        profilesRef = db.collection("Profiles");

        // Initialize map view
        map = view.findViewById(R.id.map);
        TextView appbar = view.findViewById(R.id.appbar_title);
        appbar.setText("Map");
        Button back = view.findViewById(R.id.back_button);

        // Set onClickListener for back button to navigate back and show event details
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
                getActivity().findViewById(R.id.rest_event_details).setVisibility(View.VISIBLE);
            }
        });
        map = (MapView) view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);

        // Request necessary permissions
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
        requestPermissionsIfNecessary(permissions);

        // Get map controller
        IMapController mapController = map.getController();

        // Retrieve event details from Firestore
        eventsRef.document(event).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.get("checked_in") == null) {
                            // If no attendees have checked in, show a message and navigate back
                            Toast.makeText(getActivity(), "No attendees have checked in yet!", Toast.LENGTH_LONG).show();
                            back.callOnClick();
                        } else {

                            if (document.get("check_in_locations") == null){
                                mapController.setCenter(new org.osmdroid.util.GeoPoint(0.0, 0.0));
                                back.callOnClick();
                                Toast.makeText(getContext(), "Some users may have decided to not share their location", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Map<String, GeoPoint> checkedIn = (Map<String, GeoPoint>) document.get("check_in_locations");
                                //Source: https://stackoverflow.com/questions/46898/how-do-i-efficiently-iterate-over-each-entry-in-a-java-map
                                //Was not sure how to iterate a Map object but this one helped.
                                for (Map.Entry<String, GeoPoint> entry : checkedIn.entrySet()) {
                                    String profileId = entry.getKey();
                                    GeoPoint point = entry.getValue();
                                    profilesRef.document(profileId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnap) {
                                            if (documentSnap.exists()) {
                                                Boolean locationEnabled = documentSnap.getBoolean("locationEnabled");
                                                if (locationEnabled != null && locationEnabled) {
                                                    Marker marker = new Marker(map);
                                                    org.osmdroid.util.GeoPoint osm_point = new org.osmdroid.util.GeoPoint(point.getLatitude(), point.getLongitude());
                                                    marker.setPosition(osm_point);
                                                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                                    map.getOverlays().add(marker);
                                                    mapController.setCenter(osm_point);
                                                } else {
                                                    mapController.setCenter(new org.osmdroid.util.GeoPoint(0.0, 0.0));
                                                    Toast.makeText(getContext(), "Some users may not want to share their location!", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                System.out.println("No such document!");
                                            }
                                    }
                                });
                            }


                        }
                        }
                    }
                }
            }});
        // Refresh the map and set zoom level
        map.invalidate();
        mapController.setZoom(12);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this.getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Request permissions if necessary.
     * @param permissions Array of permissions to request.
     */
    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this.getContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this.getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}