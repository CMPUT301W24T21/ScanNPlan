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


/**
 * This is a custom ArrayAdapter used to populate a GridView with event images.
 */
public class GridEventImagesArrayAdapter extends ArrayAdapter {
    private ArrayList<Event> images;
    private Context context;

    /**
     * Constructor for GridEventImagesArrayAdapter.
     *
     * @param context The context in which the adapter is being used.
     * @param images  The list of Event objects representing event images.
     */
    public GridEventImagesArrayAdapter(Context context, ArrayList<Event> images){

        super(context,0, images);
        this.images = images;
        this.context = context;
    }


    /**
     * Get a View that displays the data at the specified position in the data set.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertview The old view to reuse, if possible.
     * @param parent      The parent ViewGroup that this view will eventually be attached to.
     * @return The View corresponding to the data at the specified position.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent){
        View view;
        if (convertview == null){
            // Inflate a new view if convertView is null
            view = LayoutInflater.from(getContext()).inflate(R.layout.event_images_grid_content,parent, false);
        }
        else {
            // Reuse convertView if available
            view = convertview;
        }
        Event event = images.get(position);
        ImageView image = view.findViewById(R.id.Event_imageView);
        if (event.getImage()!= null) {
            // Set the image if available
            //bitmap appears to be incomaptabile
            //image.setImageBitmap(event.setImage());
        }
        else{
            // Clear the image if not available
            image.setImageDrawable(null);
        }
        assert image != null;
        //delete button still needs implementation
//        MaterialButton back = view.findViewById(R.id.delete_button_events_list);
        //image.setImageIcon(event.getPoster());
        return view;
    }
}
