package com.digitalminds.dmssevent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingsResultModel {

    @SerializedName("BookedGamesCount")
    @Expose
    private int BookedGamesCount;


    @SerializedName("PendingGamesCount")
    @Expose
    private int PendingGamesCount;


    @SerializedName("BookedGamesMinutes")
    @Expose
    private int BookedGamesMinutes;


    @SerializedName("TotalMinutes")
    @Expose
    private int TotalMinutes;

    @SerializedName("ToDay")
    @Expose
    private int ToDay;


    @SerializedName("PendingBookings")
    @Expose
    private List<PendingBookimgsModel> PendingBookings = null;


    @SerializedName("MyBookings")
    @Expose
    private List<MyBookingsModel> MyBookings = null;

    @SerializedName("MyPastBookings")
    @Expose
    private List<MyBookingsModel> MyPendingBookings = null;

    public List<PendingBookimgsModel> getPendingBookings() {
        return PendingBookings;
    }

    public void setPendingBookings(List<PendingBookimgsModel> pendingBookings) {
        PendingBookings = pendingBookings;
    }

    public List<MyBookingsModel> getMyBookings() {
        return MyBookings;
    }

    public void setMyBookings(List<MyBookingsModel> myBookings) {
        MyBookings = myBookings;
    }

    public int getBookedGamesCount() {
        return BookedGamesCount;
    }

    public void setBookedGamesCount(int bookedGamesCount) {
        BookedGamesCount = bookedGamesCount;
    }

    public int getPendingGamesCount() {
        return PendingGamesCount;
    }

    public void setPendingGamesCount(int pendingGamesCount) {
        PendingGamesCount = pendingGamesCount;
    }

    public int getBookedGamesMinutes() {
        return BookedGamesMinutes;
    }

    public void setBookedGamesMinutes(int bookedGamesMinutes) {
        BookedGamesMinutes = bookedGamesMinutes;
    }

    public int getTotalMinutes() {
        return TotalMinutes;
    }

    public void setTotalMinutes(int totalMinutes) {
        TotalMinutes = totalMinutes;
    }

    public int getToDay() {
        return ToDay;
    }

    public void setToDay(int toDay) {
        ToDay = toDay;
    }

    public List<MyBookingsModel> getMyPendingBookings() {
        return MyPendingBookings;
    }

    public void setMyPendingBookings(List<MyBookingsModel> myPendingBookings) {
        MyPendingBookings = myPendingBookings;
    }
}
