package com.example.project_3;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * this fragment displays the details of a selected persons profile
 * this includes the persons name, socials, and contact info
 * also the ability to delete the persons profile from the firebase
 */
public class ProfileDetailsFragment extends Fragment {
    private Profile profile;
    private FirebaseFirestore db;
    private CollectionReference profilesref;
    /**
     *Constructs a new instance of the fragment with the selected profile
     * @param profile the profile with the details displayed
     */
    public ProfileDetailsFragment(Profile profile) {
        this.profile = profile;

    }
    /**
     *initial creation of the fragment
     *
     * @param savedInstanceState (if the fragment iis reused this is what allows us to do it)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    /**
     * called when fragment is attached to the context
     * everything that needs to be know for the host activity is initialized here
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    /**
     *creates the view to view all the specific details of a users profile, name, social and contacts
     * once again appbar is intialized here as well, the onclick listeners are set up for the back and
     * delete buttons
     *
     *@param inflater used to inflate views in a fragment
     * @param container if non-null its the parent view that the UI is attached to,
     * @param savedInstanceState if non-null the fragment is reused from a previous state thats been saved
     *                           similar to above
     * @return Returns the view for the fragments UI
     */
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
        MaterialButton back = view.findViewById(R.id.back_button);
        MaterialButton deletes = view.findViewById(R.id.delete_profile_button);
        db = FirebaseFirestore.getInstance();
        profilesref = db.collection("Profiles");
        ImageView pfp = view.findViewById(R.id.profile_image);
        if (profile.getProfile_picture() != null){
            pfp.setImageBitmap(profile.getProfile_picture());
        }
        else{
            pfp.setImageDrawable(null);
        }

        //goes through the collection and identifies the document with a matching ID and deletes it
        deletes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilesref.document(profile.getProfileID()).delete();
                getParentFragmentManager().popBackStack();
                getActivity().findViewById(R.id.rest_profiles_list).setVisibility(View.VISIBLE);
            }
        });
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
