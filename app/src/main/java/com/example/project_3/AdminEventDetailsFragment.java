package com.example.project_3;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminEventDetailsFragment extends Fragment {

    private Event event;
    private FirebaseFirestore db;
    private CollectionReference eventsref;
    private Boolean isImage;

    public AdminEventDetailsFragment(Event event, Boolean isImage) {
        this.event = event;
        this.isImage = isImage;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //create the view and set the layout
        View view = inflater.inflate(R.layout.admin_event_details, container, false);
        //Universal appbar and the back button as well
        TextView appBar = view.findViewById(R.id.appbar_title);
        //set appbar title to reflect the fragment
        appBar.setText(event.getName());
        db = FirebaseFirestore.getInstance();
        eventsref = db.collection("Events");
        TextView date = view.findViewById(R.id.event_date);
        date.setText(event.getDate());
        TextView time = view.findViewById(R.id.event_time);
        time.setText(event.getTime());
        TextView location = view.findViewById(R.id.event_location);
        location.setText(event.getLocation());
        TextView details = view.findViewById(R.id.event_details);
        details.setText(event.getDetails());
        MaterialButton delete = view.findViewById(R.id.delete_event_button);
        //delete the event image
        MaterialButton deleteImage = view.findViewById(R.id.delete_event_image_button);
        ImageView image = view.findViewById(R.id.event_poster);
        eventsref.document(event.getName()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String baseString = document.getString("Image");
                    if (baseString != null && !baseString.isEmpty()) {
                        byte[] decodedBytes = Base64.decode(baseString, Base64.DEFAULT);
                        image.setImageBitmap(BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));
                    }
                }
            }});
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //test idk if this will actually work
                // or we can just set the image to be null
                eventsref.document(event.getName()).update("Image", FieldValue.delete());
                getParentFragmentManager().popBackStack();
                // okay this will heavily depend on how we demo this
                if (!isImage) {
                    getActivity().findViewById(R.id.rest_events_list).setVisibility(View.VISIBLE);
                }
                else{
                    getActivity().findViewById(R.id.rest_events_images_list).setVisibility(View.VISIBLE);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsref.document(event.getName()).delete();
                getParentFragmentManager().popBackStack();
                if (!isImage) {
                    getActivity().findViewById(R.id.rest_events_list).setVisibility(View.VISIBLE);
                }
                else{
                    getActivity().findViewById(R.id.rest_events_images_list).setVisibility(View.VISIBLE);
                }
            }
        });
        MaterialButton back = view.findViewById(R.id.back_button);
        //if back is clicked pop the stack and go back to the activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
                if (!isImage) {
                    getActivity().findViewById(R.id.rest_events_list).setVisibility(View.VISIBLE);
                }
                else{
                    getActivity().findViewById(R.id.rest_events_images_list).setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
