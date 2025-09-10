package com.digitalminds.dmssevent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalBookingGamesResonse {

    @SerializedName("Status")
    @Expose
    private String Status;

    @SerializedName("Result")
    @Expose
    private BookingsResultModel totalResult;


    public String getStatus() {
        return Status;
    }


    public void setStatus(String status) {
        Status = status;
    }

    public BookingsResultModel getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(BookingsResultModel totalResult) {
        this.totalResult = totalResult;
    }
}
