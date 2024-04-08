package com.example.project_3;

import java.io.Serializable;
import java.sql.Timestamp;
import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.sql.Timestamp;

public class Announcement implements Serializable {
    Timestamp timestamp;
    String content;
    String eventName;

    public Announcement(Timestamp timestamp, String content, String eventName) {
        this.timestamp = timestamp;
        this.content = content;
        this.eventName = eventName;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
