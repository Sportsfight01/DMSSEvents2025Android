package com.digitalminds.dmssevent.models;

import org.json.JSONObject;

/**
 * Created by Sandeep.Kumar on 15-02-2018.
 */

public class GetTeamMembersModel {
    String Names="",TeamName="";
    public GetTeamMembersModel(JSONObject resultObject,String TeamName){
        if(resultObject!=null){
            try{
                Names=resultObject.isNull("Names")?"":resultObject.getString("Names");
            }catch (Exception ex){
                ex.printStackTrace();
            }
            this.TeamName=TeamName;
        }
    }

    public String getNames() {
        return Names;
    }

    public void setNames(String names) {
        Names = names;
    }

    public String getTeamName() {
        return TeamName;
    }

    public void setTeamName(String teamName) {
        TeamName = teamName;
    }
}
