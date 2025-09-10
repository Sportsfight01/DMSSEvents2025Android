package com.digitalminds.dmssevent.models;

import org.json.JSONObject;

/**
 * Created by Sandeep.Kumar on 15-02-2018.
 */

public class GetAllGamesModel {
    int Id;
    String Name="",GameType="",IconURL="";
    boolean IsActive;
    public GetAllGamesModel(JSONObject resuJsonObject){
        if(resuJsonObject!=null){
            try {
                Id=resuJsonObject.isNull("Id")?0:resuJsonObject.getInt("Id");
                Name=resuJsonObject.isNull("Name")?"":resuJsonObject.getString("Name");
                GameType=resuJsonObject.isNull("GameType")?"":resuJsonObject.getString("GameType");
                IconURL=resuJsonObject.isNull("IconURL")?"":resuJsonObject.getString("IconURL");
                IsActive=resuJsonObject.isNull("IsActive")?false:resuJsonObject.getBoolean("IsActive");


            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public String getGameType() {
        return GameType;
    }

    public void setGameType(String gameType) {
        GameType = gameType;
    }

    public String getIconURL() {
        return IconURL;
    }

    public void setIconURL(String iconURL) {
        IconURL = iconURL;
    }
}
