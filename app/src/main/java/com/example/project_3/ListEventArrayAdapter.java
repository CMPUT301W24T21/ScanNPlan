package com.example.project_3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ListEventArrayAdapter extends ArrayAdapter{

    private ArrayList<Event> events;
    private Context context;
    public ListEventArrayAdapter(Context context, ArrayList<Event> events) {

        super(context,0, events);
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent){
        View view;
        if (convertview == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.event_list_content,parent, false);
        }
        else {
            view = convertview;
        }

        Event event = events.get(position);
        ImageView image = view.findViewById(R.id.events_list_image);
        //image.setImageIcon(event.ge);
        //don't have an image yet to use as placeholder
        MaterialTextView eventName = view.findViewById(R.id.events_list_title);
        //sets our text to be of the event
        eventName.setText(event.getName());
        assert event != null;
        //delete button still needs implementation
        MaterialButton back = view.findViewById(R.id.delete_button_events_list);
        //image.setImageIcon(event.getPoster());
        return view;
    }

}
