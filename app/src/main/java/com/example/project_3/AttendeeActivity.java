package com.example.project_3;

import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class AttendeeActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_homepage);
        String[] EventNames = {"concert1", "party2", "boardgames3"};
        String[] EventLocation = {"123 4th st", "567 8th avenue", "91011 12blv"};


        ArrayList<Event> dataList= new ArrayList<Event>();
        for (int i = 0; i < EventNames.length; i++) {
            boolean details = dataList.add(new Event(EventNames[i], LocalDateTime.of(2003, 10, 28, 3, 23), EventLocation[i], "DETAILS"));
        }


        EventArrayAdapter eventAdapter = new EventArrayAdapter(this, dataList);
        ListView eventList = findViewById(R.id.event_listView);
        eventList.setAdapter(eventAdapter);


    }

}
