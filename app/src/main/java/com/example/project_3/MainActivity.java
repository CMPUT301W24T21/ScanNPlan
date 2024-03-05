package com.example.project_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
     Intent AttendeeIntent = new Intent(this, AttendeeActivity.class);
    //Intent AdminIntent = new Intent(this, );
    //Intent OrganizerIntent = new Intent(this, );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This line starts the attendee activity
        startActivity(AttendeeIntent);
    }
}