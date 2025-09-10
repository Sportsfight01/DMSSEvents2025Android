package com.digitalminds.dmssevent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultModelConfirmation {

    @SerializedName("GameBookingStatus")
    @Expose
    private String GameBookingStatus;
    @SerializedName("Message")
    @Expose
    private String Message;

    public String getGameBookingStatus() {
        return GameBookingStatus;
    }

    public void setGameBookingStatus(String gameBookingStatus) {
        GameBookingStatus = gameBookingStatus;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
