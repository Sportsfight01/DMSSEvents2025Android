package com.digitalminds.dmssevent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingStatusResonse {

    @SerializedName("Status")
    @Expose
    private boolean Status;

    @SerializedName("Result")
    @Expose
    private GameStatusModel totalResult;


    public boolean getStatus() {
        return Status;
    }


    public void setStatus(boolean status) {
        Status = status;
    }

    public GameStatusModel getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(GameStatusModel totalResult) {
        this.totalResult = totalResult;
    }
}
