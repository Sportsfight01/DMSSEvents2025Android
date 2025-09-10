package com.digitalminds.dmssevent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingDTOModel {

    @SerializedName("BookingID")
    @Expose
    private int BookingID;
    @SerializedName("Game")
    @Expose
    private String Game;
    @SerializedName("Resource")
    @Expose
    private int Resource;
    @SerializedName("Day")
    @Expose
    private int Day;
    @SerializedName("Slot")
    @Expose
    private int Slot;
    @SerializedName("Player1")
    @Expose
    private int Player1;
    @SerializedName("Player2")
    @Expose
    private int Player2;
    @SerializedName("Player3")
    @Expose
    private int Player3;
    @SerializedName("Player4")
    @Expose
    private int Player4;
    @SerializedName("isSelectedForBooking")
    @Expose
    private boolean isSelectedForBooking;
    @SerializedName("SelectionTime")
    @Expose
    private String SelectionTime;
    @SerializedName("BookingStatus")
    @Expose
    private boolean BookingStatus;
    @SerializedName("Player1Accepted")
    @Expose
    private int Player1Accepted;

    @SerializedName("Player2Accepted")
    @Expose
    private int Player2Accepted;

    @SerializedName("Player3Accepted")
    @Expose
    private int Player3Accepted;

    @SerializedName("Player4ccepted")
    @Expose
    private int Player4ccepted;

    @SerializedName("BookedDate")
    @Expose
    private String BookedDate;

    @SerializedName("BookedDay")
    @Expose
    private String BookedDay;
    @SerializedName("BookedDDMMM")
    @Expose
    private String BookedDDMMM;

    public int getBookingID() {
        return BookingID;
    }

    public void setBookingID(int bookingID) {
        BookingID = bookingID;
    }

    public String getGame() {
        return Game;
    }

    public void setGame(String game) {
        Game = game;
    }

    public int getResource() {
        return Resource;
    }

    public void setResource(int resource) {
        Resource = resource;
    }

    public int getDay() {
        return Day;
    }

    public void setDay(int day) {
        Day = day;
    }

    public int getSlot() {
        return Slot;
    }

    public void setSlot(int slot) {
        Slot = slot;
    }

    public int getPlayer1() {
        return Player1;
    }

    public void setPlayer1(int player1) {
        Player1 = player1;
    }

    public int getPlayer2() {
        return Player2;
    }

    public void setPlayer2(int player2) {
        Player2 = player2;
    }

    public int getPlayer3() {
        return Player3;
    }

    public void setPlayer3(int player3) {
        Player3 = player3;
    }

    public int getPlayer4() {
        return Player4;
    }

    public void setPlayer4(int player4) {
        Player4 = player4;
    }

    public boolean isSelectedForBooking() {
        return isSelectedForBooking;
    }

    public void setSelectedForBooking(boolean selectedForBooking) {
        isSelectedForBooking = selectedForBooking;
    }

    public String getSelectionTime() {
        return SelectionTime;
    }

    public void setSelectionTime(String selectionTime) {
        SelectionTime = selectionTime;
    }

    public boolean getBookingStatus() {
        return BookingStatus;
    }

    public void setBookingStatus(boolean bookingStatus) {
        BookingStatus = bookingStatus;
    }

    public int isPlayer1Accepted() {
        return Player1Accepted;
    }

    public void setPlayer1Accepted(int player1Accepted) {
        Player1Accepted = player1Accepted;
    }

    public int isPlayer2Accepted() {
        return Player2Accepted;
    }

    public void setPlayer2Accepted(int player2Accepted) {
        Player2Accepted = player2Accepted;
    }

    public int isPlayer3Accepted() {
        return Player3Accepted;
    }

    public void setPlayer3Accepted(int player3Accepted) {
        Player3Accepted = player3Accepted;
    }

    public int isPlayer4ccepted() {
        return Player4ccepted;
    }

    public void setPlayer4ccepted(int player4ccepted) {
        Player4ccepted = player4ccepted;
    }

    public String getBookedDate() {
        return BookedDate;
    }

    public void setBookedDate(String bookedDate) {
        BookedDate = bookedDate;
    }

    public String getBookedDay() {
        return BookedDay;
    }

    public void setBookedDay(String bookedDay) {
        BookedDay = bookedDay;
    }

    public String getBookedDDMMM() {
        return BookedDDMMM;
    }

    public void setBookedDDMMM(String bookedDDMMM) {
        BookedDDMMM = bookedDDMMM;
    }
}



