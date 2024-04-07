package com.example.project_3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.checkerframework.checker.units.qual.A;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class NotificationActivity extends AppCompatActivity {
    ListView announcementsListView;
    AnnouncementArrayAdapter eventAdapter;
    ArrayList<Announcement> allAnnouncementList;

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

        allAnnouncementList = new ArrayList<Announcement>();

//        for (int i = 0; i < eventArray.size(); i++) {
//            Event currentEvent = eventArray.get(i);
//            allAnnouncementList.addAll(currentEvent.getAnnouncements());
//        }
        //*************
        //This is a test of the announcements
        java.util.Date date = new java.util.Date();
        allAnnouncementList.add(new Announcement(new Timestamp(date.getTime()), "Earliest Announcement content", "Event1"));

        allAnnouncementList.add(new Announcement(new Timestamp(date.getTime()+1000), "middle Announcement content", "Event2"));

        allAnnouncementList.add(new Announcement(new Timestamp(date.getTime()+1000), "Most recent Announcement content", "Event3"));
        //*******************

        allAnnouncementList.sort(new Comparator<Announcement>() {
            @Override
            public int compare(Announcement o1, Announcement o2) {
                if (o1.timestamp.compareTo(o2.timestamp) > 0) {
                    return -1;
                } else if (o1.timestamp.compareTo(o2.timestamp) < 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }
}