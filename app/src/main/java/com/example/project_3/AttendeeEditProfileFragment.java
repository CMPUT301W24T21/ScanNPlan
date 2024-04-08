package com.example.project_3;

import static com.example.project_3.ImageGen.generatePlaceholderImage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.materialswitch.MaterialSwitch;

import com.google.android.material.switchmaterial.SwitchMaterial;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Fragment for editing attendee profile settings.
 */

public class AttendeeEditProfileFragment extends Fragment {
    private FirebaseFirestore db;
    private EditText nameTextView;
    private EditText contactInfoTextView;
    private EditText socialLinkTextView;
    private TextView appBarView;

    private Uri selectedImageUri;

    private String profileID;

    private Bitmap bitmapImage;

    private ImageView profile_image;
    private Boolean LocationEnabled;

    private static final int PICK_IMAGE_REQUEST = 1;

    public AttendeeEditProfileFragment(String profileID) {
        this.profileID = profileID;
    }
    /**
     * Sets up the fragment's UI and initializes the Firebase Firestore instance.
     */

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

        // Set the profile image to a placeholder image
        String placeholderBase64Image = generatePlaceholderImage("Deleted Image");
        Bitmap placeholderBitmap = decodeImage(placeholderBase64Image);
        profile_image.setImageBitmap(placeholderBitmap);

        MaterialButton back = view.findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
                getParentFragmentManager().popBackStack();

                // Start AttendeeActivity
                Intent intent = new Intent(getActivity(), AttendeeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        SwitchMaterial geolocation = view.findViewById(R.id.geolocation_toggle);
        geolocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LocationEnabled = isChecked;
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

                db.collection("Profiles").document(profileID)
                        .update("name", newName,
                                "contact_info", newContactInfo,
                                "social_link", newSocialLink,
                                "locationEnabled", LocationEnabled)
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
                    Boolean location_status = documentSnapshot.getBoolean("locationEnabled");
                    if (location_status == null) {
                        location_status = false;
                        db.collection("Profiles").document(profileID).update("locationEnabled", Boolean.FALSE);
                    }
                    nameTextView.setText(profileName);
                    geolocation.setChecked(Boolean.TRUE.equals(location_status));
                    contactInfoTextView.setText(contactInfo);
                    socialLinkTextView.setText(socialLink);

                    Bitmap decodedImage = decodeImage(base64Image);
                    if (decodedImage != null) {
                        profile_image.setImageBitmap(decodedImage);
                    } else {
                        // Generate placeholder image using profile name
                        String placeholderBase64Image = generatePlaceholderImage(profileName);
                        Bitmap placeholderBitmap = decodeImage(placeholderBase64Image);
                        profile_image.setImageBitmap(placeholderBitmap);

                        // Save placeholder image to Firebase if profile image is empty
                        if (base64Image == null || base64Image.isEmpty()) {
                            db.collection("Profiles").document(profileID)
                                    .update("profile_image", placeholderBase64Image)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Firestore", "Placeholder image saved successfully");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("Firestore", "Error saving placeholder image", e);
                                        }
                                    });
                        }
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
                db.collection("Profiles").document(profileID)
                        .update("profile_image", "")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Firestore", "Profile image deleted successfully");
                                profile_image.setImageBitmap(placeholderBitmap);
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


    /**
     * Handles the result of the image selection from the gallery.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                    profile_image.setImageBitmap(bitmapImage);
                    String base64Image = encodeImage(bitmapImage);

                    db.collection("Profiles").document(profileID)
                            .update("profile_image", base64Image)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Firestore", "Profile image updated successfully");
                                    Toast.makeText(getActivity(), "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Firestore", "Error updating profile image", e);
                                    Toast.makeText(getActivity(), "Failed to update profile image", Toast.LENGTH_SHORT).show();
                                }
                            });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("Image Loading", "Selected image URI is null");
            }
        }
    }

    /**
     * Encodes a Bitmap image to a Base64 string.
     * @param bitmap The Bitmap image to encode.
     * @return The Base64 string representation of the image.
     */
    private String encodeImage(Bitmap bitmap) {
        if (bitmap == null) {
            return "";
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    /**
     * Decodes a Base64 string to a Bitmap image.
     * @param base64Image The Base64 string to decode.
     * @return The decoded Bitmap image.
     */

    private Bitmap decodeImage(String base64Image) {
        if (base64Image == null || base64Image.isEmpty()) {
            return null;
        }
        try {
            byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } catch (Exception e) {
            Log.e("DecodeImage", "Error decoding image", e);
            return null;
        }
    }


}
