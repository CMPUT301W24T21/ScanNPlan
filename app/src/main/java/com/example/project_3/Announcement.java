package com.example.project_3;

import java.sql.Timestamp;

public class Announcement {
    Timestamp timestamp;
    String content;
    String eventName;

    public Announcement(Timestamp timestamp, String content, String eventName) {
        this.timestamp = timestamp;
        this.content = content;
        this.eventName = eventName;
    }
}
