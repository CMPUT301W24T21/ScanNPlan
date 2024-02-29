package com.example.project_3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ListProfileArrayAdapter extends ArrayAdapter<Profile> {

    public ListProfileArrayAdapter(@NonNull Context context, ArrayList<Profile> profiles) {
        super(context, 0, profiles);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent){
        View view;
        if (convertview == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.profiles_list_content, parent, false);
        }
        else{
            view = convertview;
        }
        Profile profile = getItem(position);
        ImageView image = view.findViewById(R.id.profiles_list_image);
        MaterialTextView username = view.findViewById(R.id.profiles_list_username);
        MaterialButton back = view.findViewById(R.id.back_button_profiles);
        assert profile != null;
        image.setImageIcon(profile.getProfilePicture());
        username.setText(profile.getName());
        return view;
    }
}
