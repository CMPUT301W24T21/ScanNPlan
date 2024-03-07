package com.example.project_3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class ProfileDetailsFragment extends Fragment {
    private Profile profile;
    private Boolean no_args;
    public ProfileDetailsFragment(){
        no_args = Boolean.TRUE;
    }
    public ProfileDetailsFragment(Profile profile){
        this.profile = profile;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //create the view and set the layout
        View view = inflater.inflate(R.layout.profile_details, container, false);
        //Universal appbar and the back button as well
        TextView appBar = view.findViewById(R.id.appbar_title);
        appBar.setText("Profile Details");
        MaterialButton back = view.findViewById(R.id.back_button);
        TextView name = view.findViewById(R.id.profile_name_editText);
        TextView social = view.findViewById(R.id.homepage_editText);
        TextView contact_info = view.findViewById(R.id.contact_info_editText);
        if (!no_args){
            name.setText(profile.getName());
            social.setText(profile.getSocialLink());
            social.setText(profile.getContactInfo());

        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });



        return view;
    }
}
