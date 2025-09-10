package com.digitalminds.dmssevent.models;

import org.json.JSONObject;

/**
 * Created by Sandeep.Kumar on 14-02-2018.
 */

public class ScheduleListGamesModel {
    int Id;
    String TeamA="",TeamB="",ScheduleTime="",ColorCode="",Game="",GameType="",MatchNo="",Player1="",Player2="",ScheduleDate=""
            ,Points="",WinningPlayer="",WinningComments="",MatchType="",Player1Logo="",Player2Logo="";

    public ScheduleListGamesModel(JSONObject resultJson){
        if(resultJson!=null){
            try {
                Id=resultJson.isNull("Id")?0:resultJson.getInt("Id");
                TeamA=resultJson.isNull("TeamA")?"":resultJson.getString("TeamA");
                TeamB=resultJson.isNull("TeamB")?"":resultJson.getString("TeamB");
                ColorCode=resultJson.isNull("ColorCode")?"":resultJson.getString("ColorCode");
                ScheduleTime=resultJson.isNull("ScheduleTime")?"":resultJson.getString("ScheduleTime");
                Game = resultJson.isNull("Game") ? "" : resultJson.getString("Game");
                GameType = resultJson.isNull("GameType") ? "" : resultJson.getString("GameType");
                MatchNo = resultJson.isNull("MatchNo") ? "" : resultJson.getString("MatchNo");
                Player1 = resultJson.isNull("Player1") ? "" : resultJson.getString("Player1");
                Player2 = resultJson.isNull("Player2") ? "" : resultJson.getString("Player2");
                ScheduleDate = resultJson.isNull("ScheduleDate") ? "" : resultJson.getString("ScheduleDate");
                Points = resultJson.isNull("Points") ? "" : resultJson.getString("Points");
                WinningPlayer = resultJson.isNull("WinningPlayer") ? "" : resultJson.getString("WinningPlayer");
                WinningComments = resultJson.isNull("WinningComments") ? "" : resultJson.getString("WinningComments");
                MatchType = resultJson.isNull("MatchType") ? "" : resultJson.getString("MatchType");
                Player1Logo = resultJson.isNull("Player1Logo") ? "" : resultJson.getString("Player1Logo");
                Player2Logo = resultJson.isNull("Player2Logo") ? "" : resultJson.getString("Player2Logo");
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

    public String getTeamA() {
        return TeamA;
    }

    public void setTeamA(String teamA) {
        TeamA = teamA;
    }

    public String getTeamB() {
        return TeamB;
    }

    public void setTeamB(String teamB) {
        TeamB = teamB;
    }

    public String getScheduleTime() {
        return ScheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        ScheduleTime = scheduleTime;
    }

    public String getColorCode() {
        return ColorCode;
    }

    public void setColorCode(String colorCode) {
        ColorCode = colorCode;
    }

    public String getGame() {
        return Game;
    }

    public void setGame(String game) {
        Game = game;
    }

    public String getGameType() {
        return GameType;
    }

    public void setGameType(String gameType) {
        GameType = gameType;
    }

    public String getMatchNo() {
        return MatchNo;
    }

    public void setMatchNo(String matchNo) {
        MatchNo = matchNo;
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

    public String getScheduleDate() {
        return ScheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        ScheduleDate = scheduleDate;
    }

    public String getPoints() {
        return Points;
    }

    public void setPoints(String points) {
        Points = points;
    }

    public String getWinningPlayer() {
        return WinningPlayer;
    }

    public void setWinningPlayer(String winningPlayer) {
        WinningPlayer = winningPlayer;
    }

    public String getWinningComments() {
        return WinningComments;
    }

    public void setWinningComments(String winningComments) {
        WinningComments = winningComments;
    }

    public String getMatchType() {
        return MatchType;
    }

    public void setMatchType(String matchType) {
        MatchType = matchType;
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
}
