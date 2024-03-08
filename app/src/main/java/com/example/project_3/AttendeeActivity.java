package com.example.project_3;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;


public class AttendeeActivity extends AppCompatActivity {
    private Button openCameraButton;

    private Intent QRIntent;

    private ListView eventList;
    private EventArrayAdapter eventAdapter;

    private ExtendedFloatingActionButton editProfileButton;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_homepage);
        this.openCameraButton = findViewById(R.id.openCameraButton);
        QRIntent = new Intent(this, QRScan.class);

        editProfileButton = findViewById(R.id.EditProfile);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRestOfPageVisibility();
                replaceFragment(new AttendeeEditProfileFragment());
            }
        });


        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(QRIntent);
            }
        });
        
        
        String[] EventNames = {"concert1", "party2", "boardgames3"};
        String[] EventLocation = {"123 4th st", "567 8th avenue", "91011 12blv"};


        ArrayList<Event> dataList= new ArrayList<Event>();
        for (int i = 0; i < EventNames.length; i++) {
            boolean details = dataList.add(new Event(EventNames[i], "DATE123", "TIME456", EventLocation[i], "DETAILS789"));
        }


        eventAdapter = new EventArrayAdapter(this, dataList);
        eventList = findViewById(R.id.event_listView);
        eventList.setAdapter(eventAdapter);
        eventList.setOnItemClickListener(listSelector);

    }

    private void toggleRestOfPageVisibility() {
        View restOfPage = findViewById(R.id.REST_OF_PAGE);
        if (restOfPage.getVisibility() == View.VISIBLE) {
            restOfPage.setVisibility(View.INVISIBLE);
        } else {
            restOfPage.setVisibility(View.VISIBLE);
        }
    }

    // Added replaceFragment method
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.attendee_fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    AdapterView.OnItemClickListener listSelector  = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Event selectedEvent = (Event) eventList.getItemAtPosition(position);
            Integer EventIndex = position;

            findViewById(R.id.REST_OF_PAGE).setVisibility(View.INVISIBLE);

            AttendeeEventDetailsFragment fragment = new AttendeeEventDetailsFragment(selectedEvent);
            getSupportFragmentManager().beginTransaction()
            .add(R.id.attendee_fragment_container, fragment, null).addToBackStack("test").commit();
        }
    };

}
