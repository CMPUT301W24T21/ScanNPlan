package com.example.project_3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class DownloadPoster extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 1;

    private TextView textTargetUri;
    private ImageView targetImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_poster_activity);

        textTargetUri = findViewById(R.id.targeturi);
        targetImage = findViewById(R.id.targetimage);
        Button buttonLoad = findViewById(R.id.load_image);
        Button backButton = findViewById(R.id.back_button);
        Button buttonSave = findViewById(R.id.button_ok);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the activity and return to the previous one
            }
        });

        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open gallery to select an image
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send back the selected image URI to the previous activity
                if (textTargetUri.getText() != null && !textTargetUri.getText().toString().isEmpty()) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("imageUri", textTargetUri.getText().toString());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(DownloadPoster.this, "No image selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                textTargetUri.setText(selectedImageUri.toString());
                loadImage(selectedImageUri);
            }
        } else {
            Toast.makeText(this, "Image selection canceled", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImage(Uri imageUri) {
        try {
            Glide.with(this)
                    .load(imageUri)
                    .apply(new RequestOptions().placeholder(R.drawable.baseline_image_24))
                    .into(targetImage);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
