package com.example.project_3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_3.event_stuff.AddEventDialogListener;
import com.example.project_3.event_stuff.AddEventFragment;
import com.example.project_3.event_stuff.Event;
import com.example.project_3.event_stuff.EventArrayAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddEventDialogListener {

    private ArrayList<Event> dataList;
    private ListView eventList;
    private EventArrayAdapter eventAdapter;
    private TextView textEvents; // Reference to the TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataList = new ArrayList<>();

        eventList = findViewById(R.id.event_list);
        eventAdapter = new EventArrayAdapter(this, dataList);
        eventList.setAdapter(eventAdapter);

        FloatingActionButton fab = findViewById(R.id.button_add_event);
        fab.setOnClickListener(v -> {
            new AddEventFragment().show(getSupportFragmentManager(), "Add Event");
        });

        // Initialize the TextView
        textEvents = findViewById(R.id.text_no_events);

        // Check if dataList is empty, if yes, show the TextView
        checkEmptyDataList();
    }

    // Method to check if dataList is empty and show/hide the TextView accordingly
    private void checkEmptyDataList() {
        if (dataList.isEmpty()) {
            textEvents.setVisibility(View.VISIBLE);
        } else {
            textEvents.setVisibility(View.GONE);
        }
    }

    @Override
    public void addEvent(Event event) {
        dataList.add(event);
        eventAdapter.notifyDataSetChanged();
        checkEmptyDataList(); // Check again after adding an event
    }

    @Override
    public void editEvent(Event event) {
        eventAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteEvent(Event event){
        dataList.remove(event);
        eventAdapter.notifyDataSetChanged();
        checkEmptyDataList(); // Check again after deleting an event
    }

}
