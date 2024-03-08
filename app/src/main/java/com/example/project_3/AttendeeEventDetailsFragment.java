package com.example.project_3;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

/**
 *Fragment for displaying details of a specific event for attendees.
 */

public class AttendeeEventDetailsFragment extends Fragment {
    private TextView appBar;
    private ListView listEvents;
    private Event selectedEvent;

    /**
     *Constructs a new instance of the fragment with the specified event.
     * @param selectedEvent The event to display details for.
     */

    public AttendeeEventDetailsFragment(Event selectedEvent) {
        this.selectedEvent = selectedEvent;

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
        View view = inflater.inflate(R.layout.attendee_registered_event_details, container, false);
        //Universal appbar and the back button as well
        TextView appBar = view.findViewById(R.id.appbar_title);
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
