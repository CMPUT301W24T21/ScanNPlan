package com.example.project_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.util.Base64;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user profile with various details.
 */

public class Profile {
    private String ProfileID;
    private Bitmap profile_picture;
    private String name;
    private String contact_info;
    private String social_link;
    private List<Event> events;
    private String profileType;

    /**
     * Constructs a profile with specified details including a profile picture.
     *
     * @param image        The base64-encoded string representing the profile picture.
     * @param name         The name of the user.
     * @param contact_info The contact information of the user.
     * @param social_link  The social media link of the user.
     * @param profileType  The type of profile (e.g., attendee, organizer).
     */
    public Profile(String image, String name, String contact_info, String social_link, String profileType){
        if (image != null && !image.isEmpty()) {
            byte[] decodedBytes = Base64.decode(image, Base64.DEFAULT);
            this.profile_picture = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }
        else{
            this.profile_picture = null;
        }
        this.name = name;
        this.contact_info = contact_info;
        this.social_link = social_link;
        this.profileType = profileType;
    }

    /**
     * Constructs a profile with specified details excluding a profile picture.
     *
     * @param name         The name of the user.
     * @param contact_info The contact information of the user.
     * @param social_link  The social media link of the user.
     * @param profileType  The type of profile (e.g., attendee, organizer).
     */
    public Profile(String name, String contact_info, String social_link, String profileType){
        this.name = name;
        this.contact_info = contact_info;
        this.social_link = social_link;
        this.profileType = profileType;
    }

    /**
     * Constructs a profile with specified details, including associated events.
     *
     * @param name         The name of the user.
     * @param contact_info The contact information of the user.
     * @param social_link  The social media link of the user.
     * @param profileType  The type of profile (e.g., attendee, organizer).
     * @param EventRefs    The list of document references to events associated with the user.
     */
    public Profile(String name, String contact_info, String social_link, String profileType, ArrayList<DocumentReference> EventRefs){
        this.name = name;
        this.contact_info = contact_info;
        this.social_link = social_link;
        this.profileType = profileType;
    }

    /**
     * Retrieves the profile picture of the user.
     *
     * @return The profile picture.
     */
    public Bitmap getProfile_picture() {
        return profile_picture;
    }

    /**
     * Sets the profile picture of the user.
     *
     * @param profile_picture The profile picture to be set.
     */
    public void setProfile_picture(Bitmap profile_picture) {
        this.profile_picture = profile_picture;
    }

    /**
     * Retrieves the name of the user.
     *
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name The name to be set for the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the contact information of the user.
     *
     * @return The contact information of the user.
     */
    public String getContact_info() {
        return contact_info;
    }

    /**
     * Sets the contact information of the user.
     *
     * @param contact_info The contact information to be set for the user.
     */
    public void setContact_info(String contact_info) {
        this.contact_info = contact_info;
    }

    /**
     * Retrieves the social media link of the user.
     *
     * @return The social media link of the user.
     */
    public String getSocial_link() {
        return social_link;
    }

    /**
     * Sets the social media link of the user.
     *
     * @param social_link The social media link to be set for the user.
     */
    public void setSocial_link(String social_link) {
        this.social_link = social_link;
    }

    /**
     * Adds an event to the list of events associated with the user.
     *
     * @param event The event to be added.
     */
    public void addEvent(Event event) {

    }

    /**
     * Sets the profile ID of the user.
     *
     * @param profileID The profile ID to be set.
     */
    public void setProfileID(String profileID) {
        ProfileID = profileID;
    }

    /**
     * Retrieves the profile ID of the user.
     *
     * @return The profile ID of the user.
     */
    public String getProfileID() {
        return ProfileID;
    }

    /**
     * Retrieves the list of events associated with the user.
     *
     * @return The list of events associated with the user.
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * Retrieves the type of profile.
     *
     * @return The type of profile (e.g., attendee, organizer).
     */
    public String getProfileType() {
        return profileType;
    }

    /**
     * Sets the type of profile.
     *
     * @param profileType The type of profile to be set (e.g., attendee, organizer).
     */
    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }
}
