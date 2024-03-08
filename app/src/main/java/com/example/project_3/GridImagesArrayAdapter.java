package com.example.project_3;

import android.content.Context;
import android.media.Image;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * !Once we are able to use images able to build out this file to show images
 */
public class GridImagesArrayAdapter extends ArrayAdapter {
    // images will be dealt with after halfway checkpoint
    public GridImagesArrayAdapter(@NonNull Context context, ArrayList<Profile> profiles){
        super(context, 0, profiles);
        //stand in info, to fix and finish up alter
    }
}
