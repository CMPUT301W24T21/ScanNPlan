package com.example.project_3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;

import org.checkerframework.checker.units.qual.A;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Activity responsible for displaying notifications to the user.
 */
public class NotificationActivity extends AppCompatActivity {
    ListView announcementsListView;
    AnnouncementArrayAdapter announcementAdapter;
    ArrayList<Announcement> allAnnouncementList;

    /**
     * Initializes the activity, sets up the layout, and populates the list view with announcements.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_activity_notification);
        announcementsListView = findViewById(R.id.announcementsListView);
        Bundle extras = getIntent().getExtras();
        ArrayList<Event> eventArray = (ArrayList<Event>) extras.get("eventArray");
        Log.d("DEBUG", "onCreate: " + eventArray.toString() );

        MaterialButton back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView appBarText = findViewById(R.id.appbar_title);
        appBarText.setText("Announcements");

        // Todo:
        //  - Create notificationAdapter Class (DONE)
        //  - Link it to the listview (DONE)
        //  - go through each event, go through each notification for each event, add it to an arrray of notifications (DONE)

        allAnnouncementList = new ArrayList<Announcement>();

        for (int i = 0; i < eventArray.size(); i++) {
            Event currentEvent = eventArray.get(i);
            allAnnouncementList.addAll(currentEvent.getAnnouncements());
        }
        //*************
        //This is a test of the announcements
//        java.util.Date date = new java.util.Date();
//        allAnnouncementList.add(new Announcement(new Timestamp(date.getTime()+1000), "middle Announcement content", "Event2"));
//        allAnnouncementList.add(new Announcement(new Timestamp(date.getTime()+3000), "Most recent Announcement content", "Event4"));
//        allAnnouncementList.add(new Announcement(new Timestamp(date.getTime()+2000), "second Most recent Announcement content", "Event3"));
//        allAnnouncementList.add(new Announcement(new Timestamp(date.getTime()), "Earliest Announcement content", "Event1"));
        //*******************

        allAnnouncementList.sort(new Comparator<Announcement>() {
            @Override
            public int compare(Announcement o1, Announcement o2) {
                return Integer.compare(o2.timestamp.compareTo(o1.timestamp), 0);
            }
        });
        announcementAdapter = new AnnouncementArrayAdapter(this, allAnnouncementList);
        announcementsListView.setAdapter(announcementAdapter);
    }
}