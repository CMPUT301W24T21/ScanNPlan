package com.example.project_3;


/**
 * Represents a user with a profile.
 */
public class User {
    private Profile userProfile;

    /**
     * Constructs a new User with the given profile.
     *
     * @param profile The profile of the user.
     */
    public User(Profile profile) {
        this.userProfile = profile;
    };

    /**
     * Gets the user's profile.
     *
     * @return The user's profile.
     */
    public Profile getUserProfile() {
        return userProfile;
    }
}
