package com.example.project_3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * this fragment is used to display a grid of images which are either user profile
 * or event posters the universal app bar title is included along with the back button
 * TODO: implement delete functionality once we have images.
 */
public class BrowseProfileImagesFragment extends Fragment {

    // we want to pull images seperatly from both profiles and images, we are gonna do profiles frist just to get them on the board
    // i assume this will basically be doubling the database lines that we pull from our profiles and events collections
    private TextView appbar;
    private GridView GridImages;
    private FirebaseFirestore db;
    private CollectionReference profilesRef;
    private ArrayList<Profile> profilesImages;
    private ProfileArrayAdapter imagesAdapter;
    private String profileID;
    /**
     * Default constructor, initializes new instance of the fragment
     */
    public BrowseProfileImagesFragment(){
    }
    /**
     * initial creation of the fragment
     *
     * @param savedInstanceState
     * (if the fragment iis reused this is what allows us to do it)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    /**
     * this creates the  user interface view, inflates and initializes the grid view to display images,
     * the app bars functionality is implemented here
     * @param inflater
     * used to inflate views in a fragment
     * @param container
     * if non-null its the parent view that the UI is attached to,
     * @param savedInstanceState
     * if non-null the fragment is reused from a previous state thats been saved
     *                           similar to above
     * @return
     * Returns the view for the fragments UI
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //create the view and set the layout
        View view = inflater.inflate(R.layout.admin_profile_images, container, false);
        GridImages= view.findViewById(R.id.grid_Profile_images_admin);
        TextView appBar = view.findViewById(R.id.appbar_title);
        //set appbar title to reflect the fragment
        appBar.setText("Browse Profile Images");
        MaterialButton back = view.findViewById(R.id.back_button);
        //if back is clicked pop the stack and go back to the activity
        //firebase setup and filling the gridview
        db = FirebaseFirestore.getInstance();
        profilesRef = db.collection("Profiles");
        GridImages = view.findViewById(R.id.grid_Profile_images_admin);
        profilesImages = new ArrayList<>();
        imagesAdapter = new ProfileArrayAdapter(view.getContext(), profilesImages);
        GridImages.setAdapter(imagesAdapter);
        //updates the UI with the image data


        //for profile images
        profilesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (value != null){
                    profilesImages.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        String name = doc.getString("name");
                        String social_link = doc.getString("social_link"); // Assuming you have a "date" field in your document
                        String contact_info = doc.getString("contact_info");
                        String profileType = doc.getString("profile_type");
                        String profile_picture = doc.getString("profile_image");
                        if (profile_picture == null) {
                            profile_picture = "";
                        }
                        Profile profile;
                        profile = new Profile(profile_picture, name, contact_info, social_link, profileType);
                        profile.setProfileID(doc.getId());// Use the appropriate constructor
                        profilesImages.add(profile);
                    }
                    imagesAdapter.notifyDataSetChanged();

                }
            }
        });
        //takes us to the profile so we can delete the image
        GridImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Profile selected_profile = (Profile) profilesImages.get(position);
                ProfileDetailsFragment fragment = new ProfileDetailsFragment(selected_profile, Boolean.TRUE);
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.admin_profiles_images_fragment_container, fragment).addToBackStack(null).commit();
                view.findViewById(R.id.rest_profiles_images_list).setVisibility(View.INVISIBLE);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
                getActivity().findViewById(R.id.admin_homepage_rest).setVisibility(View.VISIBLE);
            }
        });
        return view;
        // still need to add the delete functionality for this page

    }
}

