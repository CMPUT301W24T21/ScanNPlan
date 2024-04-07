package com.example.project_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents an event with various details.
 */
public class Event implements Parcelable {
    private String name;
    private Boolean promo;
    private Boolean reuse;
    private String qrCode;
    private String qrPromoCode;
    private String time;
    private String date;
    private String location;
    private String details;
    private String image;
    private String link;
    private ArrayList<Announcement> announcements;
    private ArrayList<Map<String, Object>> announcementss;



    /**
     * Constructs an event with specified details.
     *
     * @param name     The name of the event.
     * @param date     The date of the event.
     * @param time     The time of the event.
     * @param location The location of the event.
     * @param details  Details of the event.
     * @param reuse    Whether the event can be reused.
     * @param image    The image associated with the event.
     * @param qrCode    The QR code associated with the event.
     * @param qrPromoCode    The Promo QR Code associated with the event.
     */
    public Event(String name, String date, String time, String location,
                 String details, boolean reuse, String image, String qrCode,
                 String qrPromoCode, String link, ArrayList<Map<String, Object>> announcementss) {
        this.name = name;
        this.reuse = reuse;
        this.date = date;
        this.time = time;
        this.location = location;
        this.details = details;
        this.image = image;
        this.qrCode = qrCode;
        this.qrPromoCode = qrPromoCode;
        this.link = link;
        this.announcementss = announcementss;
    }
    public Event(String name, String date, String time, String location,
                 String details, ArrayList<Announcement> announcements,  boolean reuse, String image, String qrCode,
                 String qrPromoCode, String link) {
        this.name = name;
        this.reuse = reuse;
        this.date = date;
        this.time = time;
        this.location = location;
        this.details = details;
        this.announcements = announcements;
        this.image = image;
        this.qrCode = qrCode;
        this.qrPromoCode = qrPromoCode;
        this.link = link;
    }
    public ArrayList<Map<String, Object>> getAnnouncementss() {
        return announcementss;
    }

    public void setAnnouncementss(ArrayList<Map<String, Object>> announcementss) {
        this.announcementss = announcementss;
    }
    //
    public Event() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
    public String getQrCode() {return this.qrCode;}
    /**
     * Sets the QR code associated with the event.
     *
     * @param qrCode The QR code to be associated with the event.
     */
    public void setQrCode(String qrCode) {this.qrCode = qrCode;}
    /**
     * Retrieves the QR code associated with the event.
     *
     * @return The QR code associated with the event.
     */

    public String getQrPromoCode() {return this.qrPromoCode;}

    /**
     * Sets the PROMO QR code associated with the event.
     *
     * @param qrPromoCode The PROMO QR code to be associated with the event.
     */
    public void setQrPromoCode(String qrPromoCode) {this.qrPromoCode = qrPromoCode;}

    public ArrayList<Announcement> getAnnouncements() {
        return this.announcements;
    }

    public void setAnnouncements(ArrayList<Announcement> announcements) {
        this.announcements = announcements;
    }
    // Parcelable implementation
    protected Event(Parcel in) {
        name = in.readString();
        byte tmpPromo = in.readByte();
        promo = tmpPromo == 0 ? null : tmpPromo == 1;
        byte tmpReuse = in.readByte();
        reuse = tmpReuse == 0 ? null : tmpReuse == 1;
        qrCode = in.readString();
        qrPromoCode = in.readString();
        time = in.readString();
        date = in.readString();
        location = in.readString();
        details = in.readString();
        image = in.readString();
        link = in.readString();
        announcements = in.createTypedArrayList(Announcement.CREATOR);
        announcementss = new ArrayList<>();
        in.readList(announcementss, Map.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeByte((byte) (promo == null ? 0 : promo ? 1 : 2));
        dest.writeByte((byte) (reuse == null ? 0 : reuse ? 1 : 2));
        dest.writeString(qrCode);
        dest.writeString(qrPromoCode);
        dest.writeString(time);
        dest.writeString(date);
        dest.writeString(location);
        dest.writeString(details);
        dest.writeString(image);
        dest.writeString(link);
        dest.writeTypedList(announcements);
        dest.writeList(announcementss);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

}

//public class Event {
//    private String name;
//    private Boolean promo;
//    private Boolean reuse;
//    private String qrCode;
//    private String qrPromoCode;
//    private String time;
//    private String date;
//    private String location;
//    private String details;
//    private String image;
//    private String link;
//    private ArrayList<Announcement> announcements;
//    private ArrayList<Map<String, Object>> announcementss;
//
//
//
//    /**
//     * Constructs an event with specified details.
//     *
//     * @param name     The name of the event.
//     * @param date     The date of the event.
//     * @param time     The time of the event.
//     * @param location The location of the event.
//     * @param details  Details of the event.
//     * @param reuse    Whether the event can be reused.
//     * @param image    The image associated with the event.
//     * @param qrCode    The QR code associated with the event.
//     * @param qrPromoCode    The Promo QR Code associated with the event.
//     */
//    public Event(String name, String date, String time, String location,
//                 String details, boolean reuse, String image, String qrCode,
//                 String qrPromoCode, String link, ArrayList<Map<String, Object>> announcementss) {
//        this.name = name;
//        this.reuse = reuse;
//        this.date = date;
//        this.time = time;
//        this.location = location;
//        this.details = details;
//        this.image = image;
//        this.qrCode = qrCode;
//        this.qrPromoCode = qrPromoCode;
//        this.link = link;
//        this.announcementss = announcementss;
//    }
//    public Event(String name, String date, String time, String location,
//                 String details, ArrayList<Announcement> announcements,  boolean reuse, String image, String qrCode,
//                 String qrPromoCode, String link) {
//        this.name = name;
//        this.reuse = reuse;
//        this.date = date;
//        this.time = time;
//        this.location = location;
//        this.details = details;
//        this.announcements = announcements;
//        this.image = image;
//        this.qrCode = qrCode;
//        this.qrPromoCode = qrPromoCode;
//        this.link = link;
//    }
//    public ArrayList<Map<String, Object>> getAnnouncementss() {
//        return announcementss;
//    }
//
//    public void setAnnouncementss(ArrayList<Map<String, Object>> announcementss) {
//        this.announcementss = announcementss;
//    }
//    //
//    public Event() {
//    }
//
//    public String getLink() {
//        return link;
//    }
//
//    public void setLink(String link) {
//        this.link = link;
//    }
//
//    /**
//     * Constructs an event with specified name and date.
//     *
//     * @param name The name of the event.
//     * @param date The date of the event.
//     */
//    public Event(String name, String date) {
//        this.name = name;
//        this.date = date;
//    }
//
//    /**
//     * Constructs an event with specified details.
//     *
//     * @param name     The name of the event.
//     * @param date     The date of the event.
//     * @param time     The time of the event.
//     * @param location The location of the event.
//     * @param details  Details of the event.
//     */
//    public Event(String name, String date, String time,
//                 String location, String details) {
//        this.name = name;
//        this.date = date;
//        this.time = time;
//        this.location = location;
//        this.details = details;
//    }
//
//    /**
//     * Retrieves the image associated with the event.
//     *
//     * @return The image associated with the event.
//     */
//    public String getImage() {
//        return image;
//    }
//
//
//    /**
//     * Sets the image associated with the event.
//     *
//     * @param image The image to be associated with the event.
//     */
//    public void setImage(String image) {
//        this.image = image;
//    }
//
//
//    /**
//     * Retrieves the time of the event.
//     *
//     * @return The time of the event.
//     */
//    public String getTime() {
//        return this.time;
//    }
//
//
//    /**
//     * Sets the time of the event.
//     *
//     * @param time The time to be set for the event.
//     */
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    /**
//     * Retrieves the date of the event.
//     *
//     * @return The date of the event.
//     */
//    public String getDate() {
//        return this.date;
//    }
//
//
//    /**
//     * Sets the date of the event.
//     *
//     * @param date The date to be set for the event.
//     */
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    /**
//     * Retrieves the location of the event.
//     *
//     * @return The location of the event.
//     */
//    public String getLocation() {return location;}
//
//    /**
//     * Sets the location of the event.
//     *
//     * @param location The location to be set for the event.
//     */
//    public void setLocation(String location) {this.location = location;}
//
//    /**
//     * Retrieves the details of the event.
//     *
//     * @return The details of the event.
//     */
//    public String getDetails() {return details;}
//
//    /**
//     * Sets the details of the event.
//     *
//     * @param details The details to be set for the event.
//     */
//    public void setDetails(String details) {this.details = details;}
//
//    /**
//     * Retrieves the name of the event.
//     *
//     * @return The name of the event.
//     */
//    public String getName() {
//        return name;
//    }
//
//    /**
//     * Sets the name of the event.
//     *
//     * @param name The name to be set for the event.
//     */
//    public void setName(String name) {
//        this.name = name;
//    }
//
//
//    /**
//     * Retrieves whether the event can be reused.
//     *
//     * @return True if the event can be reused, false otherwise.
//     */
//    public Boolean getReuse() {
//        return this.reuse;
//    }
//
//    /**
//     * Sets whether the event can be reused.
//     *
//     * @param reuse Whether the event can be reused.
//     */
//    public void setReuse(Boolean reuse) {
//        this.reuse = reuse;
//    }
//
//    /**
//     * Retrieves the QR code associated with the event.
//     *
//     * @return The QR code associated with the event.
//     */
//    public String getQrCode() {return this.qrCode;}
//    /**
//     * Sets the QR code associated with the event.
//     *
//     * @param qrCode The QR code to be associated with the event.
//     */
//    public void setQrCode(String qrCode) {this.qrCode = qrCode;}
//    /**
//     * Retrieves the QR code associated with the event.
//     *
//     * @return The QR code associated with the event.
//     */
//
//    public String getQrPromoCode() {return this.qrPromoCode;}
//
//    /**
//     * Sets the PROMO QR code associated with the event.
//     *
//     * @param qrPromoCode The PROMO QR code to be associated with the event.
//     */
//    public void setQrPromoCode(String qrPromoCode) {this.qrPromoCode = qrPromoCode;}
//
//    public ArrayList<Announcement> getAnnouncements() {
//        return this.announcements;
//    }
//
//    public void setAnnouncements(ArrayList<Announcement> announcements) {
//        this.announcements = announcements;
//    }
//}