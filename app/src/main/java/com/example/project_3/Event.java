package com.example.project_3;

import java.io.Serializable;

public class Event implements Serializable {
    private String name;
    private boolean promoEvent;



    public Event(String name, boolean promoEvent) {
        this.name = name;
        this.promoEvent = promoEvent;

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
}
