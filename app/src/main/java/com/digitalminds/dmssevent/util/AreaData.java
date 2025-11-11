package com.digitalminds.dmssevent.util;

public class AreaData{
    int AreaId;
    String AreaName;

    public AreaData(String AreaName, int AreaId) {
        this.AreaId = AreaId;
        this.AreaName = AreaName;
    }

    @Override
    public String toString() {
        return AreaName;
    }

    public int getCategoryId() {
        return AreaId;
    }

}
