package com.example.project_3;

import android.media.Image;

public class Profile {
    private Image profile_picture;
    private String name;
    private String contact_info;
    private String social_link;

    public Profile(Image image, String name, String contact_info, String social_link){
        this.profile_picture = image;
        this.name = name;
        this.contact_info = contact_info;
        this.social_link = social_link;
    }

    public void setProfilePicture(Image profile_picture) {
        this.profile_picture = profile_picture;
    }

    public void setContactInfo(String contact_info) {
        this.contact_info = contact_info;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSocialLink(String social_link) {
        this.social_link = social_link;
    }

    public Image getProfilePicture(){
        return this.profile_picture;
    }
    public String getName(){
        return this.name;
    }
    public String getContactInfo(){
        return this.contact_info;
    }
    public String getSocialLink(){
        return this.social_link;

    }

}
