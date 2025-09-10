package com.digitalminds.dmssevent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingGamesListModel {
    boolean selected;
    @SerializedName("ID")
    @Expose
    private int ID;

    @SerializedName("ResourceName")
    @Expose
    private String ResourceName;

    @SerializedName("GameName")
    @Expose
    private String GameName;


    @SerializedName("ResourceIcon")
    @Expose
    private String ResourceIcon;

    @SerializedName("IsActive")
    @Expose
    private boolean IsActive;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getResourceName() {
        return ResourceName;
    }

    public void setResourceName(String resourceName) {
        ResourceName = resourceName;
    }

    public String getGameName() {
        return GameName;
    }

    public void setGameName(String gameName) {
        GameName = gameName;
    }

    public String getResourceIcon() {
        return ResourceIcon;
    }

    public void setResourceIcon(String resourceIcon) {
        ResourceIcon = resourceIcon;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
