package com.example.project_3.event_stuff;

import android.view.View;
import android.widget.AdapterView;

public interface AddEventDialogListener {
    void addEvent(Event event);
    void editEvent(Event event);
    void deleteEvent(Event editEvent);

    //void onItemClick(AdapterView<?> parent, View view, int position, long id);
}
