package com.digitalminds.dmssevent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PendingBookimgsModel {

    @SerializedName("bookingDTO")
    @Expose
    private BookingDTOModel bookingDTO = null;

    @SerializedName("resourceForBooking")
    @Expose
    private ResourceForBookingModel resourceForBooking = null;


    @SerializedName("bookedSlot")
    @Expose
    private String bookedSlot;

    @SerializedName("PlayersDetails")
    @Expose
    private List<SelectedPlayersResultModel> PlayersDetails = null;


    public BookingDTOModel getBookingDTO() {
        return bookingDTO;
    }

    public void setBookingDTO(BookingDTOModel bookingDTO) {
        this.bookingDTO = bookingDTO;
    }

    public ResourceForBookingModel getResourceForBooking() {
        return resourceForBooking;
    }

    public void setResourceForBooking(ResourceForBookingModel resourceForBooking) {
        this.resourceForBooking = resourceForBooking;
    }

    public String getBookedSlot() {
        return bookedSlot;
    }

    public void setBookedSlot(String bookedSlot) {
        this.bookedSlot = bookedSlot;
    }

    public List<SelectedPlayersResultModel> getPlayersDetails() {
        return PlayersDetails;
    }

    public void setPlayersDetails(List<SelectedPlayersResultModel> playersDetails) {
        PlayersDetails = playersDetails;
    }
}


