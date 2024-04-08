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
 * This is a custom ArrayAdapter used to populate a GridView with profile images.

 */
public class GridProfileImagesArrayAdapter extends ArrayAdapter {

    private ArrayList<Profile> images;
    private Context context;

    /**
     * Constructor for GridProfileImagesArrayAdapter.
     *
     * @param context The context in which the adapter is being used.
     * @param images  The list of Profile objects representing profile images.
     */
    public GridProfileImagesArrayAdapter(Context context, ArrayList<Profile> images){

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
            view = LayoutInflater.from(getContext()).inflate(R.layout.profile_images_grid_content,parent, false);
        }
        else {
            // Reuse convertView if available
            view = convertview;
        }
        Profile profile = images.get(position);
        ImageView image = view.findViewById(R.id.Profile_imageView);
        if (profile.getProfile_picture()!= null) {
            // Set the profile picture if available
            image.setImageBitmap(profile.getProfile_picture());
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