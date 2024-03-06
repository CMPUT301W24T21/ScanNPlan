package com.example.project_3;

import android.media.Image;

public class Event {
    private String name;
    private String date;
    private String time;
    private String location;
    private String details;
    private Image poster;

    public Event(String name, String date, String time, String location, String details/*, Image poster*/) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.location = location;
        this.details = details;
        //this.poster = poster;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Image getPoster() {
        return poster;
    }

    public void setPoster(Image poster) {
        this.poster = poster;
    }
}
