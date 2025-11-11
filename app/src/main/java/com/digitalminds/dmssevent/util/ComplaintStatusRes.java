package com.digitalminds.dmssevent.util;

import java.io.Serializable;

public class ComplaintStatusRes  {

    private String StatusName;
    private int StatusId;

    public String getStatusName() { return StatusName; }
    public int getStatusId() { return StatusId; }

    public void setStatusName(String StatusName) { this.StatusName = StatusName; }
    public void setStatusId(int statusId) { this.StatusId = statusId; }

    @Override
    public String toString() {
        return StatusName; // This is what will be shown in AutoCompleteTextView
    }

}
