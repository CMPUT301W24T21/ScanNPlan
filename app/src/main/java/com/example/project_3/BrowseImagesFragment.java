package com.example.project_3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class BrowseImagesFragment extends Fragment {
    private TextView appbar;
    private GridView listImages;
    public BrowseImagesFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //create the view and set the layout
        View view = inflater.inflate(R.layout.admin_images, container, false);
        listImages= view.findViewById(R.id.grid_images_admin);
        TextView appBar = view.findViewById(R.id.appbar_title);
        //set appbar title to reflect the fragment
        appBar.setText("Browse Images");
        MaterialButton back = view.findViewById(R.id.back_button);
        //if back is clicked pop the stack and go back to the activity
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
