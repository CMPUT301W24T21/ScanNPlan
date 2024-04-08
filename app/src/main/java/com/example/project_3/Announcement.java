package com.example.project_3;

import java.io.Serializable;
import java.sql.Timestamp;
import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Announcement contains information about the timestamp, content, and associated event name.
 */

public class Announcement implements Serializable {
    Timestamp timestamp;
    String content;
    String eventName;

    /**
     * Constructor for Announcement.
     *
     * @param timestamp The timestamp of the announcement.
     * @param content   The content of the announcement.
     * @param eventName The name of the event associated with the announcement.
     */
    public Announcement(Timestamp timestamp, String content, String eventName) {
        this.timestamp = timestamp;
        this.content = content;
        this.eventName = eventName;
    }

    /**
     * Get the timestamp of the announcement.
     *
     * @return The timestamp.
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Set the timestamp of the announcement.
     *
     * @param timestamp The timestamp to set.
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Get the content of the announcement.
     *
     * @return The content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the content of the announcement.
     *
     * @param content The content to set.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get the name of the event associated with the announcement.
     *
     * @return The event name.
     */

    public String getEventName() {
        return eventName;
    }


    /**
     * Set the name of the event associated with the announcement.
     *
     * @param eventName The event name to set.
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}

