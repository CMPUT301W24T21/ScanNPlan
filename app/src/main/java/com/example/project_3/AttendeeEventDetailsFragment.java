package com.example.project_3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class AttendeeEventDetailsFragment extends Fragment {
    private TextView appBar;
    private ListView listEvents;
    private Event selectedEvent;

    public AttendeeEventDetailsFragment(Event selectedEvent) {
        this.selectedEvent = selectedEvent;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        return view;
    }
}
