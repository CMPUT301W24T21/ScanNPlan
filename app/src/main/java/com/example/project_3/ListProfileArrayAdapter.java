package com.example.project_3;

import android.content.Context;
import android.graphics.drawable.Icon;
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

/**
 * ArrayAdapter meant to display a list of profiles, generates the view for each profile
 * includes their image and their name in the list,
 */
public class ListProfileArrayAdapter extends ArrayAdapter{

    private ArrayList<Profile> profiles;
    private Context context;

    /**
     * constructor of the profile adapter
     *
     * @param context
     * @param profiles
     */
    public ListProfileArrayAdapter(Context context, ArrayList<Profile> profiles) {

        super(context,0, profiles);
        this.profiles = profiles;
        this.context = context;
    }

    /**
     * Provides the view for the Adapterview
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertview The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return returns view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent){
        View view;
        if (convertview == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.profiles_list_content,parent, false);
        }
        else {
            view = convertview;
        }

        Profile profile = profiles.get(position);
        ImageView image = view.findViewById(R.id.profiles_list_image);
        MaterialTextView username = view.findViewById(R.id.profiles_list_username);
        username.setText(profile.getName());
        assert profile != null;
        //delete button still needs implementation
        //MaterialButton deletes = view.findViewById(R.id.delete_button_profiles_list);
        //image.setImageIcon(event.getPoster());
        return view;
    }

}