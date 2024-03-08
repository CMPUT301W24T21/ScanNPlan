package com.example.project_3;

import android.media.Image;

import com.google.type.DateTime;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

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

    public Event(String name, boolean promo, boolean reuse, String time, String date,
          String location, String details, String image) {
        this.name = name;
        this.promo = promo;
        this.reuse = reuse;
        this.date = date;
        this.time = time;
        this.location = location;
        this.details = details;
        this.image = image;
    }

    public Event(String name, String date, String time,
                String location, String details) {
            this.name = name;
            this.date = date;
            this.time = time;
            this.location = location;
            this.details = details;
        }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {return location;}

    public void setLocation(String location) {this.location = location;}

    public String getDetails() {return details;}

    public void setDetails(String details) {this.details = details;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPromo() {
        return this.promo;
    }

    public void setPromo(Boolean promo) {
        this.promo = promo;
    }

    public Boolean getReuse() {
        return this.reuse;
    }

    public void setReuse(Boolean reuse) {
        this.reuse = reuse;
    }
    public byte[] getQrCode() {return this.qrCode;}

    public void setQrCode(byte[] qrCode) {this.qrCode = qrCode;}
}