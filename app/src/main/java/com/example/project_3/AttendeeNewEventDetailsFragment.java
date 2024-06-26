package com.example.project_3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * AttendeeNewEventDetailsFragment displays the details of a new event for attendees.
 * It allows attendees to register for events and provides information about the event.
 */

public class AttendeeNewEventDetailsFragment extends Fragment {
    private TextView appBar;
    private ListView listEvents;
    private Event selectedEvent;
    private String docPath;
    private String profileID;
    private int max_attendees = Integer.MAX_VALUE;
    private int current_attendees = 0;
    private Boolean isScanner;
    private FirebaseFirestore db;


    /**
     * Constructs a new instance of the fragment with the specified event.
     *
     * @param docPath   A firebase document path for the event to be registered for.
     * @param isScanner A boolean indicating whether the fragment is launched from the scanner.
     */
    public AttendeeNewEventDetailsFragment(String docPath, Boolean isScanner) {
        this.docPath = docPath;
        this.isScanner = isScanner;
    }

    /**
     * Called to do initial creation of the fragment.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    /**
     * Called when a fragment is first attached to its context.
     *
     * @param context The context to attach to.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    /**
     * Inflates the layout for the fragment and initializes the UI with the event details.
     * @param inflater The layout inflater.
     * @param container The parent view group.
     * @param savedInstanceState The saved instance state bundle.
     * @return The inflated view for the fragment.
     */

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //create the view and set the layout
        View view = inflater.inflate(R.layout.attendee_new_event_details, container, false);
        Context context = view.getContext();
        //Universal appbar and the back button as well
        TextView appBar = view.findViewById(R.id.appbar_title);
        disableAllTextInputEditText(view);

        db = FirebaseFirestore.getInstance();

        Log.d("DEBUG", "docPath: " + docPath);
        db.document(docPath).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("DEBUG", "!!!!!Doc Found");
                        Map<String, Object> eventDetails = document.getData();
                        //Log.d("DEBUG", "DocumentSnapshot data: " + document.getData());
                        ArrayList<Map<String, Object>> announcements = new ArrayList<>();
                        selectedEvent = new Event(document.getId().toString(),
                                (String) eventDetails.get("Date"),
                                (String) eventDetails.get("Time"),
                                (String) eventDetails.get("Location"),
                                (String) eventDetails.get("Details"),
                                (boolean) eventDetails.get("Reuse"),
                                (String) eventDetails.get("Image"),
                                null,
                                null,
                                null, announcements);

                        //set appbar title to reflect the fragment
                        appBar.setText(selectedEvent.getName());

                        //Sets up the other fields
                        TextView date = view.findViewById(R.id.event_date);
                        date.setText(selectedEvent.getDate());
                        TextView time = view.findViewById(R.id.event_time);
                        time.setText(selectedEvent.getTime());
                        TextView location = view.findViewById(R.id.event_location);
                        location.setText(selectedEvent.getLocation());
                        TextView details = view.findViewById(R.id.event_details);
                        details.setText(selectedEvent.getDetails());

                        String base64Image = document.getString("Image");
                        if (base64Image != null && !base64Image.isEmpty()) {
                            // Decode base64 string to bitmap
                            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            if (decodedByte != null) {
                                // Set bitmap to ImageView
                                ImageView poster = view.findViewById(R.id.event_poster);
                                poster.setImageBitmap(decodedByte);
                            } else {
                                Log.e("Image Decoding", "Failed to decode base64 string into bitmap.");
                            }
                        }

                    } else {
                        Toast.makeText(getActivity(), "This Promo QR code is either invalid or expired.", Toast.LENGTH_SHORT).show();
                        final boolean[] isProcessingClick = {true};
                        getParentFragmentManager().popBackStack();
                        getActivity().findViewById(R.id.REST_OF_PAGE).setVisibility(View.VISIBLE);
                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isProcessingClick[0] = false;
                            }
                        }, 500); // Adjust the delay time as needed

                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to retrieve Event Details.", Toast.LENGTH_SHORT).show();
                    final boolean[] isProcessingClick = {true};
                    getParentFragmentManager().popBackStack();
                    getActivity().findViewById(R.id.REST_OF_PAGE).setVisibility(View.VISIBLE);
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isProcessingClick[0] = false;
                        }
                    }, 500); // Adjust the delay time as needed

                }

            }
        });
        db.document(docPath).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Long number = task.getResult().getLong("max_attendees");
                if (number != null){
                    max_attendees = number.intValue();
                }
                List<DocumentReference> signups = (List<DocumentReference>) task.getResult().get("attendees");
                if (signups != null){
                    current_attendees = signups.size();
                }
            }
        });



        MaterialButton registerButton = view.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_attendees + 1 <= max_attendees) {
                    FirebaseMessaging.getInstance().subscribeToTopic(String.valueOf(docPath.hashCode()))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = "Subscribed";
                                    if (!task.isSuccessful()) {
                                        msg = "Subscribe failed";
                                    }
                                    Log.d("SUBSCRIBED", msg);

                                    Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                }
                            });

                    profileID = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                    db = FirebaseFirestore.getInstance();
                    db.collection("Profiles").document(profileID).update("events", FieldValue.arrayUnion(db.document(docPath)));
                    db.document(docPath).update("attendees", FieldValue.arrayUnion(db.document("Profiles/" + profileID)));
//                getParentFragmentManager().popBackStack();
//                getActivity().findViewById(R.id.REST_OF_PAGE).setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Event registered successfully!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "Event is full!", Toast.LENGTH_SHORT).show();

                }
                Intent intent = new Intent(getActivity(), AttendeeActivity.class);
                startActivity(intent);
            }
        });

        MaterialButton back = view.findViewById(R.id.back_button);
        //if back is clicked pop the stack and go back to the activity
        /**
         * Handles the back button click event, popping the fragment from the stack sending you back.
         */


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean[] isProcessingClick = {true};
                if (!isScanner){
                    Intent intent = new Intent(getActivity(), AttendeeBrowseEventsActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getActivity(), AttendeeActivity.class);
                    startActivity(intent);
                }
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isProcessingClick[0] = false;
                    }
                }, 500); // Adjust the delay time as needed\

            }


        });
        return view;
    }

    /**
     * used to initialize the page.
     * @param view The inflated view for the fragment.
     * @param savedInstanceState The saved instance state bundle.
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void disableAllTextInputEditText(View view) {
        if (view instanceof TextInputEditText) {
            ((TextInputEditText) view).setEnabled(false);
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                disableAllTextInputEditText(viewGroup.getChildAt(i));
            }
        }
    }
}