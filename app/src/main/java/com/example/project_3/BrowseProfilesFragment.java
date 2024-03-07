package com.example.project_3;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;


public class BrowseProfilesFragment extends Fragment {
    private TextView appBar;
    private ListView listProfiles;
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