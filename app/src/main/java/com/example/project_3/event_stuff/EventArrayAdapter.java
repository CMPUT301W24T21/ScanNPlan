package com.example.project_3.event_stuff;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_3.R;

import java.util.ArrayList;

public class EventArrayAdapter extends ArrayAdapter<Event> {
    private Context mContext;

    public EventArrayAdapter(Context context, ArrayList<Event> events){
        super(context, 0, events);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.content, parent, false);
        }

        Event event = getItem(position);

        TextView eventNameTextView = view.findViewById(R.id.event_text);
        ImageView qrCodeImageView = view.findViewById(R.id.qr_code_image_view);

        eventNameTextView.setText(event.getName());
        qrCodeImageView.setImageBitmap(event.getQrCode());

        view.setOnClickListener(v->{
            openEditEventDialog(event);
        });

        return view;
    }

    private void openEditEventDialog(Event event){
        AddEventFragment editEventFragment = AddEventFragment.newInstance(event);
        editEventFragment.show(((AppCompatActivity) getContext()).getSupportFragmentManager(),
                "Edit this Event");
    }
}
