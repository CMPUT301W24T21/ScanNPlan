package com.example.project_3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddEventDialogListener{

    private ArrayList<Event> dataList;
    private ListView eventList;
    private EventArrayAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String[] events = {};

        dataList = new ArrayList<>();
//        for (int i = 0; i < events.length; i++) {
//            dataList.add(new Event(events[i]));
//
//        }

        eventList = findViewById(R.id.event_list);
        eventAdapter = new EventArrayAdapter(this, dataList);
        eventList.setAdapter(eventAdapter);

        FloatingActionButton fab = findViewById(R.id.button_add_event);
        fab.setOnClickListener(v -> {
            new AddEventFragment().show(getSupportFragmentManager(), "Add Event");
        });

        // Initialize TextViews
//        totalEventReadTextView = findViewById(R.id.total_books_read);
//        totalBooksTextView = findViewById(R.id.total_books);
//        // Update TextViews initially
//        updateTotalBooksTextViews();
    }

    @Override
    public void addEvent(Event event) {
        dataList.add(event);
//        updateTotalBooksTextViews();
        eventAdapter.notifyDataSetChanged();
    }

    @Override
    public void editEvent(Event event) {
//        updateTotalBooksTextViews();
        eventAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteEvent(Event event){
        dataList.remove(event);
//        updateTotalBooksTextViews();
        eventAdapter.notifyDataSetChanged();
    }
    // OpenAI, 2024, ChatGPT, https://chat.openai.com/share/7891f6d5-e500-47db-92f8-9b6cc4765197
    // looked up how to update book numbers
//    private void updateTotalBooksTextViews() {
//        int totalBooksRead = 0;
//        for (Book book : dataList) {
//            if (book.getStatus().equalsIgnoreCase("Read")) {
//                totalBooksRead++;
//            }
//        }
//        int totalBooks = dataList.size();
//
//        totalBooksReadTextView.setText("Read: " + totalBooksRead + "/" + totalBooks);
//        totalBooksTextView.setText("Total: " + totalBooks);
//        totalBooksReadTextView.setTextSize(32);
//        totalBooksTextView.setTextSize(32);
//    }
}