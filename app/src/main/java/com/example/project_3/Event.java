package com.example.project_3;

import android.graphics.drawable.Icon;
import android.media.Image;

import java.time.LocalDateTime;

public class Event {
    private String name;
    private LocalDateTime dateTime;
    private String location;
    private String details;
    private Image poster;

    public Event(String name, LocalDateTime dateTime, String location, String details/*, Image poster*/) {
        this.name = name;
        this.dateTime = dateTime;
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

    public LocalDateTime getDate() {
        return dateTime;
    }

    public void setDate(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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

    public Icon getPoster() {
        return poster;
    }

    public void setPoster(Image poster) {
        this.poster = poster;
    }
}
