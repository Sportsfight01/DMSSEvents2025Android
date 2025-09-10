package com.digitalminds.dmssevent.models;

public class GamesListModel {

    String gameName;
    boolean selected;

    public GamesListModel(String gameName, boolean selected) {
        this.gameName = gameName;
        this.selected = selected;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
