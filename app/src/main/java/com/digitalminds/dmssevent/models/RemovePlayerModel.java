package com.digitalminds.dmssevent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemovePlayerModel {


    @SerializedName("Status")
    @Expose
    private boolean Status;

    @SerializedName("Result")
    @Expose
    private String totalResult;

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public String getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(String totalResult) {
        this.totalResult = totalResult;
    }
}
