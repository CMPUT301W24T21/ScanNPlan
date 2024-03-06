package com.example.project_3.attendee_stuff;

//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.project_3.R; // Assuming R is the correct resource file for your project
//
//public class AttendeesListActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.attendees_list);
//
//        // Using intent to get the event name which is passed from MainActivity
//        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra("eventName")) {
//            String eventName = intent.getStringExtra("eventName");
//            //TextView eventNameTextView = findViewById(R.id.text_event_name);
//            //eventNameTextView.setText(eventName);
//        }
//
//        // Implementing the back button using on click listener
//        Button backButton = findViewById(R.id.back_button); // Assuming you have a Button with id button_back in your layout
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Navigate back to the previous activity
//                finish();
//            }
//        });
//    }
//}
