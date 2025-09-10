package com.digitalminds.dmssevent.models;

import org.json.JSONObject;

/**
 * Created by Sandeep.Kumar on 14-02-2018.
 */

public class TeamsListModel {
    int PlayerId;
    String Name="",Logo="",ColorCode="",MatchNo="",ScheduleDate="",ScheduleTime="",Player1="",Player2="",Player1Logo="",Player2Logo="",Team="",TeamNo="";
   public TeamsListModel(JSONObject resultJsonObject){
       if(resultJsonObject!=null){
           try {
               PlayerId=resultJsonObject.isNull("PlayerId")?0:resultJsonObject.getInt("PlayerId");
               Name=resultJsonObject.isNull("Name")?"":resultJsonObject.getString("Name");
               Logo=resultJsonObject.isNull("Logo")?"":resultJsonObject.getString("Logo");
               MatchNo=resultJsonObject.isNull("TeamNo")?"":resultJsonObject.getString("TeamNo");
               ScheduleDate=resultJsonObject.isNull("ScheduleDate")?"":resultJsonObject.getString("ScheduleDate");
               ScheduleTime=resultJsonObject.isNull("ScheduleTime")?"":resultJsonObject.getString("ScheduleTime");
               Player1=resultJsonObject.isNull("Player1")?"":resultJsonObject.getString("Player1");
               Player2=resultJsonObject.isNull("Player2")?"":resultJsonObject.getString("Player2");
               Player1Logo=resultJsonObject.isNull("Player1Logo")?"":resultJsonObject.getString("Player1Logo");
               Player2Logo=resultJsonObject.isNull("Player2Logo")?"":resultJsonObject.getString("Player2Logo");
               ColorCode=resultJsonObject.isNull("ColorCode")?"":resultJsonObject.getString("ColorCode");
               Team=resultJsonObject.isNull("Team")?"":resultJsonObject.getString("Team");
               TeamNo=resultJsonObject.isNull("TeamNo")?"":resultJsonObject.getString("TeamNo");


           }catch (Exception ex){
               ex.printStackTrace();
           }
       }
   }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getColorCode() {
        return ColorCode;
    }

    public void setColorCode(String colorCode) {
        ColorCode = colorCode;
    }

    public String getMatchNo() {
        return MatchNo;
    }

    public void setMatchNo(String matchNo) {
        MatchNo = matchNo;
    }

    public String getScheduleDate() {
        return ScheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        ScheduleDate = scheduleDate;
    }

    public String getScheduleTime() {
        return ScheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        ScheduleTime = scheduleTime;
    }

    public String getPlayer1() {
        return Player1;
    }

    public void setPlayer1(String player1) {
        Player1 = player1;
    }

    public String getPlayer2() {
        return Player2;
    }

    public void setPlayer2(String player2) {
        Player2 = player2;
    }

    public String getPlayer1Logo() {
        return Player1Logo;
    }

    public void setPlayer1Logo(String player1Logo) {
        Player1Logo = player1Logo;
    }

    public String getPlayer2Logo() {
        return Player2Logo;
    }

    public void setPlayer2Logo(String player2Logo) {
        Player2Logo = player2Logo;
    }

    public int getPlayerId() {
        return PlayerId;
    }

    public String getTeamNo() {
        return TeamNo;
    }

    public void setTeamNo(String teamNo) {
        TeamNo = teamNo;
    }

    public void setPlayerId(int playerId) {
        PlayerId = playerId;
    }

    public String getTeam() {
        return Team;
    }

    public void setTeam(String team) {
        Team = team;
    }
}
