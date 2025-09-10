package com.digitalminds.dmssevent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfirmBookingResponse {

    @SerializedName("Status")
    @Expose
    private boolean Status;

    @SerializedName("Result")
    @Expose
    private ResultModelConfirmation totalResult;


    public boolean getStatus() {
        return Status;
    }


    public void setStatus(boolean status) {
        Status = status;
    }

    public ResultModelConfirmation getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(ResultModelConfirmation totalResult) {
        this.totalResult = totalResult;
    }
}
