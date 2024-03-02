package com.example.project_3.event_stuff;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Event implements Serializable {
    private String name;
    private boolean promoEvent;
    private boolean reuseEvent;
    private Bitmap qrCode;



    public Event(String name, boolean promoEvent, boolean reuseEvent) {
        this.name = name;
        this.promoEvent = promoEvent;
        this.reuseEvent = reuseEvent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPromoEvent(boolean promoEvent) {
        this.promoEvent = promoEvent;
    }

    public boolean isPromoEvent() {
        return promoEvent;
    }

    public void setReuseEvent(boolean reuseEvent){this.reuseEvent = reuseEvent;}
    public boolean isReuseEvent() {
        return reuseEvent;
    }

    public Bitmap getQrCode() {
        return qrCode;
    }
    public void setQrCode(Bitmap qrCode) {
        this.qrCode = qrCode;
    }

}
