package com.example.project_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private Intent AttendeeIntent;
    private Intent AdminIntent;
    //Intent AdminIntent = new Intent(this, );
    private Intent OrganizerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AttendeeIntent = new Intent(this, AttendeeActivity.class);
        AdminIntent = new Intent(this, AdminActivity.class);
        //OrganizerIntent = new Intent(this, );


        //This line starts the attendee activity
        startActivity(AttendeeIntent);
    }
}