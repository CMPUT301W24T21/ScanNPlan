package com.example.project_3;

import java.io.Serializable;
import java.sql.Timestamp;
import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.sql.Timestamp;

public class Announcement implements Parcelable {
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

    // Parcelable implementation
    protected Announcement(Parcel in) {
        timestamp = (Timestamp) in.readSerializable();
        content = in.readString();
        eventName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(timestamp);
        dest.writeString(content);
        dest.writeString(eventName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Announcement> CREATOR = new Parcelable.Creator<Announcement>() {
        @Override
        public Announcement createFromParcel(Parcel in) {
            return new Announcement(in);
        }

        @Override
        public Announcement[] newArray(int size) {
            return new Announcement[size];
        }
    };
}

//public class Announcement implements Serializable {
//    Timestamp timestamp;
//    String content;
//    String eventName;
//
//    public Announcement(Timestamp timestamp, String content, String eventName) {
//        this.timestamp = timestamp;
//        this.content = content;
//        this.eventName = eventName;
//    }
//
//    public Timestamp getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(Timestamp timestamp) {
//        this.timestamp = timestamp;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public String getEventName() {
//        return eventName;
//    }
//
//    public void setEventName(String eventName) {
//        this.eventName = eventName;
//    }
//}
