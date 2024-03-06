package com.example.project_3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EventArrayAdapter extends ArrayAdapter {
    private ArrayList<Event> events;
    private Context context;
    public EventArrayAdapter(Context context, ArrayList<Event> events) {

        super(context,0, events);
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.attendee_event_list_content, parent, false);
        } else {
            view = convertView;
        }
        Event event = events.get(position);
//        Event event = (Event) getItem(position);
        TextView eventName = view.findViewById(R.id.event_name);
//        TextView eventLocation = view.findViewById(R.id.event_location);
        assert event != null;
        eventName.setText(event.getName());
//        eventName.setText(event.getLocation());
        return view;

    }
}