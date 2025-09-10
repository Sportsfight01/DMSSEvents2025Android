package com.digitalminds.dmssevent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookMyGameResultModel {
    boolean selected;
    boolean timeSlotBookedSuccess;
    boolean timeSlotPending;
    boolean timeSlotAvailable;
    boolean timeSlotOver;
    @SerializedName("bookingDTO")
    @Expose
    private BookingDTOModel bookingDTO = null;


    @SerializedName("resourceForBooking")
    @Expose
    private ResourceForBookingModel resourceForBooking = null;


    @SerializedName("bookedSlot")
    @Expose
    private String bookedSlot;


    @SerializedName("Slot")
    @Expose
    private String Slot;

    @SerializedName("SlotStatus")
    @Expose
    private int SlotStatus;
    @SerializedName("SlotStatusReason")
    @Expose
    private String SlotStatusReason;


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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getSlot() {
        return Slot;
    }

    public void setSlot(String slot) {
        Slot = slot;
    }

    public int getSlotStatus() {
        return SlotStatus;
    }

    public void setSlotStatus(int slotStatus) {
        SlotStatus = slotStatus;
    }

    public boolean isTimeSlotBookedSuccess() {
        return timeSlotBookedSuccess;
    }

    public void setTimeSlotBookedSuccess(boolean timeSlotBookedSuccess) {
        this.timeSlotBookedSuccess = timeSlotBookedSuccess;
    }

    public boolean isTimeSlotPending() {
        return timeSlotPending;
    }

    public void setTimeSlotPending(boolean timeSlotPending) {
        this.timeSlotPending = timeSlotPending;
    }

    public boolean isTimeSlotAvailable() {
        return timeSlotAvailable;
    }

    public void setTimeSlotAvailable(boolean timeSlotAvailable) {
        this.timeSlotAvailable = timeSlotAvailable;
    }

    public boolean isTimeSlotOver() {
        return timeSlotOver;
    }

    public void setTimeSlotOver(boolean timeSlotOver) {
        this.timeSlotOver = timeSlotOver;
    }

    public String getSlotStatusReason() {
        return SlotStatusReason;
    }

    public void setSlotStatusReason(String slotStatusReason) {
        SlotStatusReason = slotStatusReason;
    }
}
