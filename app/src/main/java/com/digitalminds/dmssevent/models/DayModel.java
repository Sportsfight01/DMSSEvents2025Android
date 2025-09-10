package com.digitalminds.dmssevent.models;

public class DayModel {

    String dayName;
    String date;
    boolean selected;

    public DayModel(String dayName, String date,boolean selected) {
        this.dayName = dayName;
        this.date = date;
        this.selected = selected;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
