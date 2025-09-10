package com.digitalminds.dmssevent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TotalResultBookMyGameModel {
    @SerializedName("GamesList")
    @Expose
    private List<BookingGamesListModel> GamesList = null;


    @SerializedName("days")
    @Expose
    private List<BookingDaysModel> days = null;


    @SerializedName("bookingList")
    @Expose
    private List<BookMyGameResultModel> bookingList = null;


    @SerializedName("Today")
    @Expose
    private int Today ;

    public List<BookingGamesListModel> getGamesList() {
        return GamesList;
    }

    public void setGamesList(List<BookingGamesListModel> gamesList) {
        GamesList = gamesList;
    }

    public List<BookingDaysModel> getDays() {
        return days;
    }

    public void setDays(List<BookingDaysModel> days) {
        this.days = days;
    }

    public List<BookMyGameResultModel> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<BookMyGameResultModel> bookingList) {
        this.bookingList = bookingList;
    }

    public int getToday() {
        return Today;
    }

    public void setToday(int today) {
        Today = today;
    }
}
