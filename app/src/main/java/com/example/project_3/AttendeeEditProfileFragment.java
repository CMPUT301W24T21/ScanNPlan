package com.example.project_3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AttendeeEditProfileFragment extends Fragment {
    private FirebaseFirestore db;
    private EditText nameTextView;
    private EditText contactInfoTextView;
    private EditText socialLinkTextView;
    private TextView appBarView;

    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_settings, container, false);

        db = FirebaseFirestore.getInstance();
        appBarView = view.findViewById(R.id.appbar_title);
        appBarView.setText("Profile Settings");
        nameTextView = view.findViewById(R.id.profile_name_editText);
        socialLinkTextView= view.findViewById(R.id.homepage_editText);
        contactInfoTextView = view.findViewById(R.id.contact_info_editText);

        // Assuming user's email is passed as an argument to the fragment
        //String userEmail = getArguments().getString("email");
        MaterialButton back = view.findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
                getActivity().findViewById(R.id.REST_OF_PAGE).setVisibility(View.VISIBLE);
            }


        });

        db.collection("Profiles").document("Test's Profile").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    final int firestore = Log.e("Firestore", error.toString());
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    String contactInfo = documentSnapshot.getString("contact_info");
                    String socialLink = documentSnapshot.getString("social_link");
                    String profileType = documentSnapshot.getString("profile_type");
                    user = new User(new Profile(name, contactInfo, socialLink, profileType));

                    nameTextView.setText(name);
                    contactInfoTextView.setText(contactInfo);
                    socialLinkTextView.setText(socialLink);
                }
            }
        });

        return view;
    }
}
