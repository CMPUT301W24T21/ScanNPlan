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

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
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
public class BrowseEventImagesFragment extends Fragment {

    // we want to pull images separately from both profiles and images, we are gonna do profiles first just to get them on the board
    // i assume this will basically be doubling the database lines that we pull from our profiles and events collections
    private TextView appbar;
    private GridView GridImages;
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private ArrayList<Event> eventsImages;
    private EventArrayAdapter imagesAdapter;
    /**
     * Default constructor, initializes new instance of the fragment
     */
    public BrowseEventImagesFragment(){
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
        View view = inflater.inflate(R.layout.admin_event_images, container, false);
        GridImages= view.findViewById(R.id.grid_Event_images_admin);
        TextView appBar = view.findViewById(R.id.appbar_title);
        //set appbar title to reflect the fragment
        appBar.setText("Event Images");
        MaterialButton back = view.findViewById(R.id.back_button);
        //if back is clicked pop the stack and go back to the activity
        //firebase setup and filling the gridview
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");

        GridImages = view.findViewById(R.id.grid_Event_images_admin);
        eventsImages = new ArrayList<>();
        imagesAdapter = new EventArrayAdapter(view.getContext(), eventsImages);
        GridImages.setAdapter(imagesAdapter);
        //updates the UI with the image data

        //for profile images
        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (value != null) {
                    eventsImages.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        String eventName = doc.getId();
                        String date = doc.getString("Date");
                        String time = doc.getString("Time");
                        String location = doc.getString("Location");
                        String details = doc.getString("Details");
                        //creating an event object and adding it to the local list
                        Event event = new Event(eventName, date, time, location, details);
                        eventsImages.add(event);
                    }
                    imagesAdapter.notifyDataSetChanged();
                }
            }
        });
        //takes us to the event so we can delete the image
        GridImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Event selected_event = (Event) eventsImages.get(position);
                AdminEventDetailsFragment fragment = new AdminEventDetailsFragment(selected_event, Boolean.TRUE);
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.admin_events_images_fragment_container, fragment).addToBackStack(null).commit();
                view.findViewById(R.id.rest_events_images_list).setVisibility(View.INVISIBLE);
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

