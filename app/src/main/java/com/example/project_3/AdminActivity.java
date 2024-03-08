package com.example.project_3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;


import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.google.android.material.button.MaterialButton;

/**
 * AdminActivity acts as the dashboard/ homepage for any admin tasks like browsing and managing,
 * Images, profiles and events. each accessed by pressing their respective buttons. Also a logout
 * to take you back to the main activity 'login'
 */
public class AdminActivity extends AppCompatActivity {
    //buttons for the admin homepage
    private MaterialButton manage_profiles;
    private MaterialButton manage_images;
    private MaterialButton manage_events;
    private  MaterialButton logout;
    //onCreate function for the activity
    //params: savedInstanceState: Bundle
    //returns: void

    /**
     *  Intializes AdminActivity setting up the layout and button links for navigation
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_homepage);
        this.manage_events = findViewById(R.id.manage_events);
        this.manage_images = findViewById(R.id.manage_images);
        this.manage_profiles = findViewById(R.id.manage_profiles);
        this.logout = findViewById(R.id.Logout_button_admin);

        //manage profiles button takes you to the browse profiles window
        manage_profiles.setOnClickListener(new View.OnClickListener() {
            @Override
            //onClick method setting up a click listener for the manage profiles button
            //param: a View
            //returns: void
            public void onClick(View v) {
                BrowseProfilesFragment fragment = new BrowseProfilesFragment();
                //open the fragment and add it to the stack on the container
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, fragment).addToBackStack(null).commit();
                findViewById(R.id.admin_homepage_rest).setVisibility(View.INVISIBLE);
            }
        });
        manage_events.setOnClickListener(new View.OnClickListener() {
            @Override
            //onClick method setting up a click listener for the manage profiles button
            //param: a View
            //returns: void
            public void onClick(View v) {
                BrowseEventsFragment fragment = new BrowseEventsFragment();
                //open the fragment and add it to the stack on the container
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, fragment).addToBackStack(null).commit();
                findViewById(R.id.admin_homepage_rest).setVisibility(View.INVISIBLE);
            }
        });
        manage_images.setOnClickListener(new View.OnClickListener() {
            @Override
            //onClick method setting up a click listener for the manage profiles button
            //param: a View
            //returns: void
            public void onClick(View v) {
                BrowseImagesFragment fragment = new BrowseImagesFragment();
                //open the fragment and add it to the stack on the container
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, fragment).addToBackStack(null).commit();
                findViewById(R.id.admin_homepage_rest).setVisibility(View.INVISIBLE);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

}