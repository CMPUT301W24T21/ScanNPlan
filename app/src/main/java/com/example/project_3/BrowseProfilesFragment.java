package com.example.project_3;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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


public class BrowseProfilesFragment extends Fragment {
    private TextView appBar;
    private ListView listProfiles;
    private FirebaseFirestore db;
    private CollectionReference profilesRef;
    private ArrayList<Profile> profilesNames;

    private ListProfileArrayAdapter profileArrayAdapter;
    public BrowseProfilesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //create the view and set the layout
        View view = inflater.inflate(R.layout.admin_profiles, container, false);
        listProfiles= view.findViewById(R.id.list_profiles_admin);
        TextView appBar = view.findViewById(R.id.appbar_title);
        //set appbar title to reflect the fragment
        appBar.setText("Browse Profiles");
        MaterialButton back = view.findViewById(R.id.back_button);
        db = FirebaseFirestore.getInstance();
        profilesRef = db.collection("Profiles");

        listProfiles = view.findViewById(R.id.list_profiles_admin);
        profilesNames = new ArrayList<>();
        profileArrayAdapter = new ListProfileArrayAdapter(view.getContext(), profilesNames);
        listProfiles.setAdapter(profileArrayAdapter);
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
                        Profile profile = new Profile(name, contact_info, social_link); // Use the appropriate constructor
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
        listProfiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProfileDetailsFragment fragment = new ProfileDetailsFragment();
                //open the fragment and add it to the stack on the container
                getChildFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, fragment).addToBackStack(null).commit();
            }
        });
        return view;

    }
}