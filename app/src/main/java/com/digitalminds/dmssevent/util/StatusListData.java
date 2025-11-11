package com.digitalminds.dmssevent.util;


public class StatusListData{
    int StatusId;
    String Status;

    public StatusListData(String name, int id) {
        this.StatusId = id;
        this.Status = name;
    }

    @Override
    public String toString() {
        return Status;
    }

    public int getCategoryId() {
        return StatusId;
    }

}