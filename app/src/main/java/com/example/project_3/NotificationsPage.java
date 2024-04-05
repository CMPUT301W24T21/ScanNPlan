package com.example.project_3;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import androidx.appcompat.app.AppCompatActivity;
//public class NotificationsPage extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.organizer_notifications_page);
//
//        Button backButton = findViewById(R.id.buttonBack);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }
//}
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationsPage extends AppCompatActivity {

    private ListView notificationsListView;
    private ArrayAdapter<String> adapter;
    private List<String> fieldNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_notifications_page);

        // Initialize ListView and adapter
        notificationsListView = findViewById(R.id.notifications_list);
        fieldNames = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fieldNames);
        notificationsListView.setAdapter(adapter);

        // Fetch field names from your document
        fetchFieldNames();

        Button backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fetchFieldNames() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notifsCollection = db.collection("Notifications");
        notifsCollection.document("notifs").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                fieldNames.clear();
                Map<String, Object> data = task.getResult().getData();
                if (data != null) {
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        fieldNames.add(entry.getKey());
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
