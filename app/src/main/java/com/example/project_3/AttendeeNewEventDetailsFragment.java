package com.example.project_3;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class AttendeeNewEventDetailsFragment extends Fragment {
    private TextView appBar;
    private ListView listEvents;
    private Event selectedEvent;
    private String docPath;
    private String profileID;
    private FirebaseFirestore db;


    /**
     *Constructs a new instance of the fragment with the specified event.
     * @param docPath A firebase document path for the event to be registered for.
     */

    public AttendeeNewEventDetailsFragment(String docPath) {
        this.docPath = docPath;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        //Universal appbar and the back button as well
        TextView appBar = view.findViewById(R.id.appbar_title);

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

                        selectedEvent = new Event(document.getId().toString(),
                                (String) eventDetails.get("Date"),
                                (String) eventDetails.get("Time"),
                                (String) eventDetails.get("Location"),
                                (String) eventDetails.get("Details"),
                                (boolean) eventDetails.get("Reuse"),
                                (String) eventDetails.get("Image"),
                                null,
                                null,
                                null);

                        //set appbar title to reflect the fragment
                        appBar.setText(selectedEvent.getName());

                        //Sets up the other fields
                        ImageView poster = view.findViewById(R.id.event_poster);

                        TextView date = view.findViewById(R.id.event_date);
                        date.setText(selectedEvent.getDate());
                        TextView time = view.findViewById(R.id.event_time);
                        time.setText(selectedEvent.getTime());
                        TextView location = view.findViewById(R.id.event_location);
                        location.setText(selectedEvent.getLocation());
                        TextView details = view.findViewById(R.id.event_details);
                        details.setText(selectedEvent.getDetails());

                    } else {
                        Log.d("DEBUG", "No such document");
                    }
                } else {
                    Log.d("DEBUG", "get failed with ", task.getException());
                }

            }
        });




        MaterialButton registerButton = view.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileID = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                db = FirebaseFirestore.getInstance();
                db.collection("Profiles").document(profileID).update("events", FieldValue.arrayUnion(db.document(docPath)));
                db.document(docPath).update("attendees", FieldValue.arrayUnion(db.document("Profiles/"+ profileID)));

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
                getParentFragmentManager().popBackStack();
                getActivity().findViewById(R.id.REST_OF_PAGE).setVisibility(View.VISIBLE);
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isProcessingClick[0] = false;
                    }
                }, 500); // Adjust the delay time as needed

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
}
