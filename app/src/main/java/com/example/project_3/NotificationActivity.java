package com.example.project_3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {
    ListView announcementsListView;
    EventArrayAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_activity_notification);
        announcementsListView = findViewById(R.id.announcementsListView);
        Bundle extras = getIntent().getExtras();
        ArrayList<Event> eventArray = (ArrayList<Event>) extras.get("eventArray");
        Log.d("DEBUG", "onCreate: " + eventArray.toString() );
        // Todo:
        // - Create notificationAdapter Class
        // - Link it to the listview
        // - go through each event, go through each notification for each event, add it to an arrray of notifications
        eventAdapter = new EventArrayAdapter(this, eventArray);
        announcementsListView.setAdapter(eventAdapter);


        for (int i = 0; i < eventArray.size(); i++) {

        }


    }
}