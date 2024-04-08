package com.example.project_3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
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
 * fragment is meant to display a list of events, Admin is able to browse through the list and interact with each event
 * The events can be obtained from Firebase, each of the events basic details name, dae, time and location are shwon
 * Admins can select an event to view or edit its details
 */
public class BrowseEventsFragment extends Fragment {

    private TextView appBar;
    private ListView listEvents;
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private EditText addEventEditText;
    private ArrayList<Event> eventNamesList;
    private EventArrayAdapter eventNamesAdapter;
    /**
     * The default constructor for the class, initializes a new instance
     */
    public BrowseEventsFragment() {
    }

    /**
     * initial creation of the fragment
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * creates the view to see the list of all the events available, Admins are able to interact with the
     * events in the list, either deleting them or editing their details
     * once again appbar is intialized here as well, the onclick listeners are set up for the back and
     * delete buttons
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     *                  any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     *                  UI should be attached to.  The fragment should not add the view itself,
     *                  but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return returns view for the fragments UI
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //create the view and set the layout
        View view = inflater.inflate(R.layout.admin_events, container, false);
        listEvents = view.findViewById(R.id.list_events_admin);
        //Universal appbar and the back button as well
        TextView appBar = view.findViewById(R.id.appbar_title);
        //set appbar title to reflect the fragment
        appBar.setText("Browse Events");
        MaterialButton back = view.findViewById(R.id.back_button);
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");

        listEvents = view.findViewById(R.id.list_events_admin);
        eventNamesList = new ArrayList<>();
        eventNamesAdapter = new EventArrayAdapter(view.getContext(), eventNamesList);
        listEvents.setAdapter(eventNamesAdapter);
        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (value != null) {
                    eventNamesList.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        String eventName = doc.getId();
                        String date = doc.getString("Date");
                        String time = doc.getString("Time");
                        String location = doc.getString("Location");
                        String details = doc.getString("Details");
                        //creating an event object and adding it to the local list
                        Event event = new Event(eventName, date, time, location, details);
                        eventNamesList.add(event);
                    }
                    eventNamesAdapter.notifyDataSetChanged();
                }
            }
        });
        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Event selected_event = eventNamesList.get(position);
                AdminEventDetailsFragment fragment = new AdminEventDetailsFragment(selected_event, Boolean.FALSE);
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.admin_events_fragment_container, fragment).addToBackStack(null).commit();
                view.findViewById(R.id.rest_events_list).setVisibility(View.INVISIBLE);
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
        return view;
    }
}