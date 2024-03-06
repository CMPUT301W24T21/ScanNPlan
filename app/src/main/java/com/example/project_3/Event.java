package com.example.project_3;

import android.location.Location;
import android.media.Image;

import com.google.type.DateTime;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
//
//public class Event {
//    private String name;
//    private LocalDateTime dateTime;
//    private String location;
//    private String details;
//    private Image poster;
//
//    private Boolean promo;
//    private Boolean reuse;
//    private byte[] qrCode; // Add QR code byte array field
//
//    public Event(String name, LocalDateTime dateTime, String location, String details/*, Image poster*/, boolean promo, boolean reuse) {
//        this.name = name;
//        this.dateTime = dateTime;
//        this.location = location;
//        this.details = details;
////        this.poster = poster;
//
//        this.promo = promo;
//        this.reuse = reuse;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public LocalDateTime getDate() {
//        return dateTime;
//    }
//
//    public void setDate(LocalDateTime dateTime) {
//        this.dateTime = dateTime;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
//
//    public String getDetails() {
//        return details;
//    }
//
//    public void setDetails(String details) {
//        this.details = details;
//    }
//
//    public Image getPoster() {
//        return poster;
//    }
//
//    public void setPoster(Image poster) {
//        this.poster = poster;
//    }
//    public Boolean getPromo() {
//        return promo;
//    }
//
//    public void setPromo(Boolean promo) {
//        this.promo = promo;
//    }
//
//    public Boolean getReuse() {
//        return reuse;
//    }
//
//    public void setReuse(Boolean reuse) {
//        this.reuse = reuse;
//    }
//    public byte[] getQrCode() {
//        return qrCode;
//    }
//
//    public void setQrCode(byte[] qrCode) {
//        this.qrCode = qrCode;
//    }
//}


public class Event {
    private String event;
    private Boolean promo;
    private Boolean reuse;
    private byte[] qrCode; // Add QR code byte array field

//    private LocalDateTime dateTime;
    private String Time;
    private String Date;
    private String location;
    private String details;




    Event(String event, boolean promo, boolean reuse, String Time, String Date,
          String location, String details){
        this.event = event;
        this.promo = promo;
        this.reuse = reuse;
        this.Date = Date;
        this.Time = Time;
        this.location = location;
        this.details = details;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getLocation() {return location;}

    public void setLocation(String location) {this.location = location;}

    public String getDetails() {return details;}

    public void setDetails(String details) {this.details = details;}

    public String getName() {
        return event;
    }

    public void setName(String event) {
        this.event = event;
    }

    public Boolean getPromo() {
        return promo;
    }

    public void setPromo(Boolean promo) {
        this.promo = promo;
    }

    public Boolean getReuse() {
        return reuse;
    }

    public void setReuse(Boolean reuse) {
        this.reuse = reuse;
    }
    public byte[] getQrCode() {return qrCode;}

    public void setQrCode(byte[] qrCode) {this.qrCode = qrCode;}
}