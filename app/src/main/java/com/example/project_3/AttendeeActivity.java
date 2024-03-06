package com.example.project_3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class AttendeeActivity extends AppCompatActivity {
    private Button openCameraButton;

    private Intent QRIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_homepage);
        this.openCameraButton = findViewById(R.id.openCameraButton);
        QRIntent = new Intent(this, QRScan.class);


        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(QRIntent);
            }
        });

        
    }



}
