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


public class BrowseEventsFragment extends Fragment {

    private TextView appBar;
    private ListView listEvents;
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private EditText addEventEditText;
    private ArrayList<Event> eventNamesList;
    private ListEventArrayAdapter eventNamesAdapter;

    public BrowseEventsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        eventNamesAdapter = new ListEventArrayAdapter(view.getContext(), eventNamesList);
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
                        String date = doc.getString("date");
                        String time = doc.getString("time");
                        String location = doc.getString("location");
                        String details = doc.getString("details");
                        // Assuming you have a "date" field in your document
                        Event event = new Event(eventName, date, time, location, details); // Use the appropriate constructor

                        eventNamesList.add(event);
                    }
                    eventNamesAdapter.notifyDataSetChanged();

                }
            }
        });
        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Event selected_event = (Event) eventNamesList.get(position);
                AdminEventDetailsFragment fragment = new AdminEventDetailsFragment(selected_event);
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