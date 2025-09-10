package com.digitalminds.dmssevent.models;

public class TrackStatus {

    private String date;
    private String serviceType;
    private String subject;
    private String description;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public TrackStatus(String date, String serviceType,String subject, String description ) {
        this.date = date;
        this.serviceType = serviceType;
        this.subject = subject;
        this.description = description;

    }
}
