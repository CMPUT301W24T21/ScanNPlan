package com.example.project_3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AttendeeEditProfileFragment extends Fragment {
    private FirebaseFirestore db;
    private EditText nameTextView;
    private EditText contactInfoTextView;
    private EditText socialLinkTextView;
    private TextView appBarView;

    private User user;
    private String profileID;

    private Bitmap bitmapImage;

    private ImageView profile_image;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_settings, container, false);

        db = FirebaseFirestore.getInstance();
        appBarView = view.findViewById(R.id.appbar_title);
        appBarView.setText("Profile Settings");
        nameTextView = view.findViewById(R.id.profile_name_editText);
        socialLinkTextView = view.findViewById(R.id.homepage_editText);
        contactInfoTextView = view.findViewById(R.id.contact_info_editText);
        profile_image = view.findViewById(R.id.profile_image);

        profileID = Settings.Secure.getString(this.getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        profileID = "Test2";

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

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        MaterialButton saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = nameTextView.getText().toString();
                String newContactInfo = contactInfoTextView.getText().toString();
                String newSocialLink = socialLinkTextView.getText().toString();
                String base64Image = encodeImage(bitmapImage);

                db.collection("Profiles").document(profileID)
                        .update("name", newName,
                                "contact_info", newContactInfo,
                                "social_link", newSocialLink,
                                "profile_image", base64Image)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Firestore", "Profile updated successfully");
                                Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Firestore", "Error updating profile", e);
                                Toast.makeText(getActivity(), "Profile did not update", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        db.collection("Profiles").document(profileID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String profileName = documentSnapshot.getString("name");
                    String contactInfo = documentSnapshot.getString("contact_info");
                    String socialLink = documentSnapshot.getString("social_link");
                    String base64Image = documentSnapshot.getString("profile_image");

                    nameTextView.setText(profileName);
                    contactInfoTextView.setText(contactInfo);
                    socialLinkTextView.setText(socialLink);

                    Bitmap decodedImage = decodeImage(base64Image);
                    if (decodedImage != null) {
                        profile_image.setImageBitmap(decodedImage);
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Error fetching profile data", e);
            }
        });

        MaterialButton deletePhotoButton = view.findViewById(R.id.delete_photo_button);
        deletePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // removes the profile image into another random string that will be changed to be
                //deterministically changed
                db.collection("Profiles").document(profileID)
                        .update("profile_image", FieldValue.delete())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Firestore", "Profile image deleted successfully");
                                //  clear the ImageView
                                profile_image.setImageDrawable(null);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Firestore", "Error deleting profile image", e);
                                Toast.makeText(getActivity(), "Failed to delete profile image", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                profile_image.setImageBitmap(bitmapImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


    private Bitmap decodeImage(String base64Image) {
        try {
            byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } catch (Exception e) {
            Log.e("DecodeImage", "Error decoding image", e);
            return null;
        }
    }


}
