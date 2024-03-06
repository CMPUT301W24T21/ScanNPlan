package com.example.project_3;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AttendeeActivity extends AppCompatActivity {
    private Button openCameraButton;

    private Intent QRIntent;

    private ListView eventList;
    private EventArrayAdapter eventAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_homepage);
        this.openCameraButton = findViewById(R.id.openCameraButton);
        QRIntent = new Intent(this, QRScan.class);


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
            boolean details = dataList.add(new Event(EventNames[i], LocalDateTime.of(2003, 10, 28, 3, 23), EventLocation[i], "DETAILS"));
        }


        eventAdapter = new EventArrayAdapter(this, dataList);
        eventList = findViewById(R.id.event_listView);
        eventList.setAdapter(eventAdapter);
        eventList.setOnItemClickListener(listSelector);

    }

    AdapterView.OnItemClickListener listSelector  = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //SEND INFO TO DIALOG FRAGMENT HERE
            Event selectedEvent = (Event) eventList.getItemAtPosition(position);
            Integer EventIndex = position;
            new EditCityFragment(selectedEvent).show(getSupportFragmentManager(), "Edit City");
        }
    };

}
