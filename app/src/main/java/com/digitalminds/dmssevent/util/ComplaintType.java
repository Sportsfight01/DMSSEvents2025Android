package com.digitalminds.dmssevent.util;

public class ComplaintType {
    int ComplaintTypeId;
    String Name;


    public ComplaintType(String name, int id) {
        this.ComplaintTypeId = id;
        this.Name = name;
    }

    @Override
    public String toString() {
        return Name; // This is what will be shown in AutoCompleteTextView
    }

    public int getComplaintTypeId() {
        return ComplaintTypeId;
    }
}