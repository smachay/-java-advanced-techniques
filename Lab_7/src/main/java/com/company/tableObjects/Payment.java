package com.company.tableObjects;

import java.sql.Date;
import java.time.LocalDate;

public class Payment {
    private int id;
    private Date date;
    private Double amount;
    private int personId;
    private int eventId;
    private int installmentNumber;


    public Payment(int id, Date date, Double amount, int personId, int eventId, int installmentNumber) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.personId = personId;
        this.eventId = eventId;
        this.installmentNumber = installmentNumber;
    }
    public Payment( Double amount, int personId, int eventId, int installmentNumber) {
        this.date = Date.valueOf(LocalDate.now());
        this.amount = amount;
        this.personId = personId;
        this.eventId = eventId;
        this.installmentNumber = installmentNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(int installmentNumber) {
        this.installmentNumber = installmentNumber;
    }
}
