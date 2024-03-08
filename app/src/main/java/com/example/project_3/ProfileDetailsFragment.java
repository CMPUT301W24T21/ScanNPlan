package com.example.project_3;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileDetailsFragment extends Fragment {
    private Profile profile;
    private FirebaseFirestore db;
    private CollectionReference profilesRef;

    public ProfileDetailsFragment(Profile profile) {
        this.profile = profile;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //create the view and set the layout
        View view = inflater.inflate(R.layout.profile_details, container, false);
        //Universal appbar and the back button as well
        TextView appBar = view.findViewById(R.id.appbar_title);
        //set appbar title to reflect the fragment
        appBar.setText(profile.getName());

        TextView name = view.findViewById(R.id.profile_name_editText);
        name.setText(profile.getName());
        TextView social_link = view.findViewById(R.id.homepage_editText);
        social_link.setText(profile.getSocial_link());
        TextView contact_info = view.findViewById(R.id.contact_info_editText);
        contact_info.setText(profile.getContact_info());
        MaterialButton delete = view.findViewById(R.id.delete_profile_button);
        db = FirebaseFirestore.getInstance();
        profilesRef = db.collection("Profiles");

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilesRef.document(profile.getProfileID()).delete();
                Log.e("Successful deletion", "Successfully deleted document");
            }
        });
        MaterialButton back = view.findViewById(R.id.back_button);
        //if back is clicked pop the stack and go back to the activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
                getActivity().findViewById(R.id.rest_profiles_list).setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
