package com.example.project_3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class User {
    private String name;
    private List<Event> events;
    private Random userID;
    private Profile UserProfile;

    public User() {
        userID = new Random();
        this.name = name;
        this.events = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

}
