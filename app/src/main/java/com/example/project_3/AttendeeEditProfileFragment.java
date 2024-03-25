package com.example.project_3;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
/**
 * A fragment for editing the attendee's profile, displaying name, contact info, and social links.
 */

public class AttendeeEditProfileFragment extends Fragment {
    private FirebaseFirestore db;
    private EditText nameTextView;
    private EditText contactInfoTextView;
    private EditText socialLinkTextView;
    private TextView appBarView;

    private User user;
    private String profileID;

    /**
     * Inflates the layout for the fragment and initializes the UI elements.
     * Retrieves and displays the user's profile information from Firestore.
     * @param inflater The layout inflater.
     * @param container The parent view group.
     * @param savedInstanceState The saved instance state bundle.
     * @return The inflated view for the fragment.
     */

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

        profileID = Settings.Secure.getString(this.getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);



        // Assuming user's email is passed as an argument to the fragment
        //String userEmail = getArguments().getString("email");
        MaterialButton back = view.findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean[] isProcessingClick = {true};
                getParentFragmentManager().popBackStack();
                getActivity().findViewById(R.id.REST_OF_PAGE).setVisibility(View.VISIBLE);
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isProcessingClick[0] = false;
                    }
                }, 500); // Adjust the delay time as needed

            }


        });

        MaterialButton saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update profile information in Firestore
                String newName = nameTextView.getText().toString();
                String newContactInfo = contactInfoTextView.getText().toString();
                String newSocialLink = socialLinkTextView.getText().toString();

                db.collection("Profiles").document(profileID)
                        .update("name", newName,
                                "contact_info", newContactInfo,
                                "social_link", newSocialLink)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Firestore", "Profile updated successfully");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Firestore", "Error updating profile", e);
                            }
                        });
            }
        });

        return view;
    }
}
