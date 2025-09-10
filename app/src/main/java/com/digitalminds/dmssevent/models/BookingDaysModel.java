package com.digitalminds.dmssevent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingDaysModel {
    boolean selected;
    @SerializedName("DayNo")
    @Expose
    private int DayNo;

    @SerializedName("DayName")
    @Expose
    private String DayName;


    @SerializedName("dateString")
    @Expose
    private String dateString;

    @SerializedName("MMString")
    @Expose
    private String MMString;

    @SerializedName("ddString")
    @Expose
    private String ddString;

    public int getDayNo() {
        return DayNo;
    }

    public void setDayNo(int dayNo) {
        DayNo = dayNo;
    }

    public String getDayName() {
        return DayName;
    }

    public void setDayName(String dayName) {
        DayName = dayName;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getMMString() {
        return MMString;
    }

    public void setMMString(String MMString) {
        this.MMString = MMString;
    }

    public String getDdString() {
        return ddString;
    }

    public void setDdString(String ddString) {
        this.ddString = ddString;
    }
}
