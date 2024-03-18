package com.example.project_3;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * this is used to display a lsit of profiles which are obtained from the Firebase
 * Includes the appbar with the title and back button functionality
 * Admins are able to browse through user profiles and interact with them through a list view
 */
public class BrowseProfilesFragment extends Fragment {
    private TextView appBar;
    private ListView listProfiles;
    private FirebaseFirestore db;
    private CollectionReference profilesRef;
    private ArrayList<Profile> profilesNames;

    private ListProfileArrayAdapter profileArrayAdapter;
    /**
     * Default constructor for the browse profile fragment, initializes a new instance
     */
    public BrowseProfilesFragment() {
    }
    /**
     * called to inital creation of the fragment
     * @param savedInstanceState allows for the fragment to be reused
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    /**
     * This creates the  user interface view, inflates and initializes the list view to display profiles,
     * the app bars functionality is implemented here, connection to the firebase is established
     * to display profiles and Admins are able to interact with it and obtained detailed views of the users profile
     *
     * @param inflater used to inflate views in a fragment
     * @param container if non-null its the parent view that the UI is attached to,
     * @param savedInstanceState if non-null the fragment is reused from a previous state thats been saved
     *                           similar to above
     * @return Returns the view for the fragments UI
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //create the view and set the layout
        View view = inflater.inflate(R.layout.admin_profiles, container, false);
        listProfiles= view.findViewById(R.id.list_profiles_admin);
        TextView appBar = view.findViewById(R.id.appbar_title);
        //set appbar title to reflect the fragment
        appBar.setText("Browse Profiles");
        MaterialButton back = view.findViewById(R.id.back_button);
        //firebase setup and initializing the list
        db = FirebaseFirestore.getInstance();
        profilesRef = db.collection("Profiles");
        listProfiles = view.findViewById(R.id.list_profiles_admin);
        profilesNames = new ArrayList<>();
        profileArrayAdapter = new ListProfileArrayAdapter(view.getContext(), profilesNames);
        listProfiles.setAdapter(profileArrayAdapter);
        //updates UI with the profiles data
        profilesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (value != null) {
                    profilesNames.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        String name = doc.getString("name");
                        String social_link = doc.getString("social_link"); // Assuming you have a "date" field in your document
                        String contact_info = doc.getString("contact_info");
                        String profileType = doc.getString("profile_type");
                        String profile_picture = doc.getString("profile_picture");
                        if (profile_picture == null) {
                            profile_picture = "";
                        }
                        Profile profile;
                        profile = new Profile(profile_picture, name, contact_info, social_link, profileType);
                        profile.setProfileID(doc.getId());// Use the appropriate constructor
                        profilesNames.add(profile);
                    }
                    profileArrayAdapter.notifyDataSetChanged();

                }
            }
        });
        //if back is clicked pop the stack and go back to the activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
                getActivity().findViewById(R.id.admin_homepage_rest).setVisibility(View.VISIBLE);

            }
        });
        // takes us to the new view to see the profile detaails weve clicked
        listProfiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Profile selected_profile = (Profile) profilesNames.get(position);
                ProfileDetailsFragment fragment = new ProfileDetailsFragment(selected_profile);
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.admin_profiles_fragment_container, fragment).addToBackStack(null).commit();
                view.findViewById(R.id.rest_profiles_list).setVisibility(View.INVISIBLE);
            }
        });

        return view;

    }
}