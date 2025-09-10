package com.digitalminds.dmssevent.models;

import com.google.gson.annotations.SerializedName;

public class LocationModel {
    @SerializedName("Status")
    private boolean Status;
    @SerializedName("Message")
    private String Message;
    @SerializedName("Result")
    private String Result;

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }
}
