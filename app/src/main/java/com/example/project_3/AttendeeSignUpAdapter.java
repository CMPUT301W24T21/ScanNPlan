package com.example.project_3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class AttendeeSignUpAdapter extends ArrayAdapter<String> {

    private List<String> attendees;
    private Context context;

    public AttendeeSignUpAdapter(Context context, List<String> attendees) {
        super(context, R.layout.attendees_signed_up, attendees);
        this.context = context;
        this.attendees = attendees;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.attendees_signed_up, null);
        }

        TextView textViewAttendeeName = view.findViewById(R.id.text_attendee_name);
        textViewAttendeeName.setText(attendees.get(position));

        return view;
    }
}
