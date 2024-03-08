package com.example.project_3;

import android.graphics.drawable.Icon;

import java.util.List;

public class Profile {
    private String ProfileID;
    private Icon profile_picture;
    private String name;
    private String contact_info;
    private String social_link;
    private List<Event> events;

    public Profile(Icon image, String name, String contact_info, String social_link){
        this.profile_picture = image;
        this.name = name;
        this.contact_info = contact_info;
        this.social_link = social_link;
    }
    public Profile(String name, String contact_info, String social_link){
        this.name = name;
        this.contact_info = contact_info;
        this.social_link = social_link;
    }

    public Icon getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(Icon profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact_info() {
        return contact_info;
    }

    public void setContact_info(String contact_info) {
        this.contact_info = contact_info;
    }

    public String getSocial_link() {
        return social_link;
    }

    public void setSocial_link(String social_link) {
        this.social_link = social_link;
    }
    public void addEvent(Event event) {

    }

    public void setProfileID(String profileID) {
        ProfileID = profileID;
    }
}
