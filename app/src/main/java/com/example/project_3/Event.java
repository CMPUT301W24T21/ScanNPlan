package com.example.project_3;

import android.media.Image;

import com.google.type.DateTime;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Represents an event with various details.
 */
public class Event {
    private String name;
    private Boolean promo;
    private Boolean reuse;
    private byte[] qrCode; // Add QR code byte array field
    private String time;
    private String date;
    private String location;
    private String details;
    private String image;

    /**
     * Constructs an event with specified details.
     *
     * @param name     The name of the event.
     * @param date     The date of the event.
     * @param time     The time of the event.
     * @param location The location of the event.
     * @param details  Details of the event.
     * @param promo    Whether the event is promotional.
     * @param reuse    Whether the event can be reused.
     * @param image    The image associated with the event.
     */
    public Event(String name, String date, String time, String location,
                 String details, boolean promo, boolean reuse, String image) {
        this.name = name;
        this.promo = promo;
        this.reuse = reuse;
        this.date = date;
        this.time = time;
        this.location = location;
        this.details = details;
        this.image = image;
    }

    /**
     * Constructs an event with specified name and date.
     *
     * @param name The name of the event.
     * @param date The date of the event.
     */
    public Event(String name, String date) {
        this.name = name;
        this.date = date;
    }

    /**
     * Constructs an event with specified details.
     *
     * @param name     The name of the event.
     * @param date     The date of the event.
     * @param time     The time of the event.
     * @param location The location of the event.
     * @param details  Details of the event.
     */
    public Event(String name, String date, String time,
                String location, String details) {
            this.name = name;
            this.date = date;
            this.time = time;
            this.location = location;
            this.details = details;
        }

    /**
     * Retrieves the image associated with the event.
     *
     * @return The image associated with the event.
     */
    public String getImage() {
        return image;
    }


    /**
     * Sets the image associated with the event.
     *
     * @param image The image to be associated with the event.
     */
    public void setImage(String image) {
        this.image = image;
    }


    /**
     * Retrieves the time of the event.
     *
     * @return The time of the event.
     */
    public String getTime() {
        return this.time;
    }


    /**
     * Sets the time of the event.
     *
     * @param time The time to be set for the event.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Retrieves the date of the event.
     *
     * @return The date of the event.
     */
    public String getDate() {
        return this.date;
    }


    /**
     * Sets the date of the event.
     *
     * @param date The date to be set for the event.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Retrieves the location of the event.
     *
     * @return The location of the event.
     */
    public String getLocation() {return location;}

    /**
     * Sets the location of the event.
     *
     * @param location The location to be set for the event.
     */
    public void setLocation(String location) {this.location = location;}

    /**
     * Retrieves the details of the event.
     *
     * @return The details of the event.
     */
    public String getDetails() {return details;}

    /**
     * Sets the details of the event.
     *
     * @param details The details to be set for the event.
     */
    public void setDetails(String details) {this.details = details;}

    /**
     * Retrieves the name of the event.
     *
     * @return The name of the event.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the event.
     *
     * @param name The name to be set for the event.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves whether the event is promotional.
     *
     * @return True if the event is promotional, false otherwise.
     */
    public Boolean getPromo() {
        return this.promo;
    }

    /**
     * Sets whether the event is promotional.
     *
     * @param promo Whether the event is promotional.
     */
    public void setPromo(Boolean promo) {
        this.promo = promo;
    }

    /**
     * Retrieves whether the event can be reused.
     *
     * @return True if the event can be reused, false otherwise.
     */
    public Boolean getReuse() {
        return this.reuse;
    }

    /**
     * Sets whether the event can be reused.
     *
     * @param reuse Whether the event can be reused.
     */
    public void setReuse(Boolean reuse) {
        this.reuse = reuse;
    }

    /**
     * Retrieves the QR code associated with the event.
     *
     * @return The QR code associated with the event.
     */
    public byte[] getQrCode() {return this.qrCode;}

    /**
     * Sets the QR code associated with the event.
     *
     * @param qrCode The QR code to be associated with the event.
     */
    public void setQrCode(byte[] qrCode) {this.qrCode = qrCode;}
}