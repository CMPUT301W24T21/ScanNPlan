package com.example.project_3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EventArrayAdapter extends ArrayAdapter<Event> {
    public EventArrayAdapter(Context context, ArrayList<Event> events){
        super(context, 0, events);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View counterView, @NonNull ViewGroup parent){
        View view;
        if(counterView == null){
            view = LayoutInflater.from(super.getContext()).inflate(R.layout.content, parent, false);
        } else {
            view = counterView;
        } Event event = super.getItem(position);
        TextView eventName = view.findViewById(R.id.event_text);

        eventName.setText(event.getName());

        view.setOnClickListener(v->{
            openEditBookDialog(event);
        });
        return view;

    }
    private void openEditBookDialog(Event event){
        AddEventFragment editEventFragment = AddEventFragment.newInstance(event);
        editEventFragment.show(((AppCompatActivity) getContext()).getSupportFragmentManager(),
                "Edit this Event");
    }
}

