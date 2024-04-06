package com.example.project_3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GridEventImagesArrayAdapter extends ArrayAdapter {
    private ArrayList<Event> images;
    private Context context;

    public GridEventImagesArrayAdapter(Context context, ArrayList<Event> images){

        super(context,0, images);
        this.images = images;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent){
        View view;
        if (convertview == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.event_images_grid_content,parent, false);
        }
        else {
            view = convertview;
        }
        Event event = images.get(position);
        ImageView image = view.findViewById(R.id.Event_imageView);
        if (event.getImage()!= null) {
            //bitmap appears to be incomaptabile
            //image.setImageBitmap(event.setImage());
        }
        else{
            image.setImageDrawable(null);
        }
        assert image != null;
        //delete button still needs implementation
//        MaterialButton back = view.findViewById(R.id.delete_button_events_list);
        //image.setImageIcon(event.getPoster());
        return view;
    }
}
