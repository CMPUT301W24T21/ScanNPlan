package com.example.project_3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class AdminEventDetailsFragment extends Fragment {
    private TextView appBar;
    private ImageView image;
    private TextView date;
    private TextView time;
    private TextView location;
    private TextView details;
    // initial pull request resubmission
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //create the view and set the layout
        View view = inflater.inflate(R.layout.admin_event_details, container, false);
        date = view.findViewById(R.id.event_date);
        time = view.findViewById(R.id.event_time);
        location = view.findViewById(R.id.event_location);
        details = view.findViewById(R.id.event_details);
        //Universal appbar and the back button as well
        TextView appBar = view.findViewById(R.id.appbar_title);
        //set appbar title to reflect the fragment
        appBar.setText("Event Details");
        MaterialButton back = view.findViewById(R.id.back_button);
        //if back is clicked pop the stack and go back to the activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        //Need to set up both of the delete functions on the poster and the event all together
        return view;
    }



}
