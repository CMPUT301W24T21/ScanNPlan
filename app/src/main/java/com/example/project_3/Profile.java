package com.example.project_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.util.Base64;

import java.util.List;

public class Profile {
    private String ProfileID;
    private Bitmap profile_picture;
    private String name;
    private String contact_info;
    private String social_link;
    private List<Event> events;
    private String profileType;

    public Profile(String image, String name, String contact_info, String social_link, String profileType){
        if (!image.isEmpty()) {
            byte[] decodedBytes = Base64.decode(image, Base64.DEFAULT);
            this.profile_picture = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }
        else{
            this.profile_picture= null;
        }
        this.name = name;
        this.contact_info = contact_info;
        this.social_link = social_link;
        this.profileType = profileType;
    }
    public Profile(String name, String contact_info, String social_link, String profileType){
        this.name = name;
        this.contact_info = contact_info;
        this.social_link = social_link;
        this.profileType = profileType;
    }

    public Bitmap getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(Bitmap profile_picture) {
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

    public String getProfileID() {
        return ProfileID;
    }

    public List<Event> getEvents() {
        return events;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }
}
