package com.company.tableObjects;

import java.sql.Date;

public class Event {
    private int id;
    private String name;
    private String place;
    private Date date;

    public Event(int id, String name, String place, Date date) {
        this.id = id;
        this.name = name;
        this.place = place;
        this.date = date;
    }
    public Event(String name, String place, Date date) {
        this.name = name;
        this.place = place;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
