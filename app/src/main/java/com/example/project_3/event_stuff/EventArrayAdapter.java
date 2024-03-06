package com.example.project_3.event_stuff;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.project_3.R;
//
//import java.util.ArrayList;
//
//public class EventArrayAdapter extends ArrayAdapter<Event> {
//    public EventArrayAdapter(Context context, ArrayList<Event> events){
//        super(context, 0, events);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
//        View view = convertView;
//        if(view == null){
//            view = LayoutInflater.from(getContext()).inflate(R.layout.content, parent, false);
//        }
//
//        Event event = getItem(position);
//
//        TextView eventNameTextView = view.findViewById(R.id.event_text);
//        ImageView qrCodeImageView = view.findViewById(R.id.qr_code_image_view);
//
//        eventNameTextView.setText(event.getName());
//        qrCodeImageView.setImageBitmap(event.getQrCode());
//
//        view.setOnClickListener(v->{
//            openEditEventDialog(event);
//        });
//
//        return view;
//    }
//
//    private void openEditEventDialog(Event event){
//        AddEventFragment editEventFragment = AddEventFragment.newInstance(event);
//        editEventFragment.show(((AppCompatActivity) getContext()).getSupportFragmentManager(),
//                "Edit this Event");
//    }
//}
////
////import com.example.project_3.R;
////import android.content.Context;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.widget.ArrayAdapter;
////import android.widget.TextView;
////
////import androidx.annotation.NonNull;
////import androidx.annotation.Nullable;
////
////import java.util.ArrayList;
////
////public class EventArrayAdapter extends ArrayAdapter<Event> {
////    private ArrayList<Event> events;
////    private Context context;
////
////    public EventArrayAdapter(Context context, ArrayList<Event> events){
////        super(context,0, events);
////        this.events = events;
////        this.context = context;
////    }
////
////
////    @NonNull
////    @Override
////    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//////        return super.getView(position, convertView, parent);
////        View view = convertView;
////
////        if(view == null){
////            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
////        }
////
////        Event event = events.get(position);
////
////        TextView eventName = view.findViewById(R.id.event_text);
////
////        eventName.setText(event.getName());
////
////        return view;
////    }
////}



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project_3.R;

import java.util.ArrayList;

public class EventArrayAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private Context context;

    public EventArrayAdapter(Context context, ArrayList<Event> events){
        super(context,0, events);
        this.events = events;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }

        Event event = events.get(position);

        TextView eventName = view.findViewById(R.id.event_text);
//        TextView provinceName = view.findViewById(R.id.province_text);

        eventName.setText(event.getName());
//        provinceName.setText(city.getProvinceName());

        return view;
    }
}
