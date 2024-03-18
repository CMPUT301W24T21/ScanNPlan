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

/**
 * Custom ArrayAdapter for displaying Event objects in a ListView.
 */
public class EventArrayAdapter extends ArrayAdapter {
    private ArrayList<Event> events;
    private Context context;

    /**
     * Constructs an EventArrayAdapter.
     *
     * @param context The context in which the adapter is being used.
     * @param events  The list of events to be displayed.
     */
    public EventArrayAdapter(Context context, ArrayList<Event> events) {

        super(context,0, events);
        this.events = events;
        this.context = context;
    }

    /**
     * Returns a view for the ListView based on the position.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse.
     * @param parent      The parent that this view will eventually be attached to.
     * @return The View corresponding to the data at the specified position.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            // Inflate a new view if convertView is null
            view = LayoutInflater.from(getContext()).inflate(R.layout.attendee_event_list_content, parent, false);
        } else {
            view = convertView;
        }

        // Get the Event object at the current position
        Event event = events.get(position);
//        Event event = (Event) getItem(position);

        // Binding data to TextViews in the view
        TextView eventName = view.findViewById(R.id.event_name);
        TextView eventLocation = view.findViewById(R.id.event_location);
        assert event != null;

        // Setting text for event name and location
        eventName.setText(event.getName());
        eventLocation.setText(event.getLocation());
        return view;

    }
}