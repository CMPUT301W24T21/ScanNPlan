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

public class ListProfileArrayAdapter extends ArrayAdapter{

    private ArrayList<Profile> profiles;
    private Context context;
    public ListProfileArrayAdapter(Context context, ArrayList<Profile> profiles) {

        super(context,0, profiles);
        this.profiles = profiles;
        this.context = context;
    }

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
        MaterialButton back = view.findViewById(R.id.delete_button_profiles_list);
        //image.setImageIcon(event.getPoster());
        return view;
    }

}