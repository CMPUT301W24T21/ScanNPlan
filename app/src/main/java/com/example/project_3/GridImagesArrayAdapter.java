package com.example.project_3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

/**
 * !Once we are able to use images able to build out this file to show images
 */
public class GridImagesArrayAdapter extends ArrayAdapter {

    private ArrayList<Profile> images;
    private Context context;

    public GridImagesArrayAdapter(Context context, ArrayList<Profile> images){

        super(context,0, images);
        this.images = images;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent){
        View view;
        if (convertview == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.images_grid_content,parent, false);
        }
        else {
            view = convertview;
        }
        Profile profile = images.get(position);
        ImageView image = view.findViewById(R.id.imageView);
        if (profile.getProfile_picture()!= null) {
            image.setImageBitmap(profile.getProfile_picture());
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
