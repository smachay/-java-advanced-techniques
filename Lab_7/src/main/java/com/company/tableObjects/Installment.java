package com.company.tableObjects;

import java.sql.Date;

public class Installment {
    private int id;
    private int eventId;
    private int number;
    private Date deadline;
    private Double amount;

    public Installment(int id, int eventId, int number, Date deadline, Double amount) {
        this.id = id;
        this.eventId = eventId;
        this.number = number;
        this.deadline = deadline;
        this.amount = amount;
    }
    public Installment( int eventId, int number, Date deadline, Double amount) {
        this.eventId = eventId;
        this.number = number;
        this.deadline = deadline;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
