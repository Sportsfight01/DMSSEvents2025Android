package com.digitalminds.dmssevent.models;

import org.json.JSONObject;

public class SportsFightUserModel {
    String userName="";
    String email="";
    String mobile="";
    String profilePic=null;
    String CompanyName;
    String LocationName;
    String Latitude;
    String Longitude;
    int TotalGames;
    int TotalPoints;
    int WinPercentage;
    String ClubName;
    String gender;
    int userId;
    int winPercentage;
    boolean isEmailVerified;
    boolean isSelected=false;
    int totalPoints=0;
    public SportsFightUserModel(JSONObject jsonObject) {
        try {
            userId = jsonObject.isNull("userId")?0:jsonObject.getInt("userId");
            userName = jsonObject.isNull("userName")?"" : jsonObject.getString("userName");
            email = jsonObject.isNull("email")?"" : jsonObject.getString("email");
            mobile = jsonObject.isNull("mobile")?"" : jsonObject.getString("mobile");
            profilePic = jsonObject.isNull("profileImageUrl") ? "" : jsonObject.getString("profileImageUrl");
            gender = jsonObject.isNull("gender") ? "Male": jsonObject.getString("gender");
            winPercentage=jsonObject.isNull("WinPercentage") ? 0 : jsonObject.getInt("WinPercentage");
            totalPoints=jsonObject.isNull("TotalPoints") ? 0 : jsonObject.getInt("TotalPoints");
            isEmailVerified=jsonObject.isNull("IsEmailVerified") ? false : jsonObject.getBoolean("IsEmailVerified");
            CompanyName =jsonObject.isNull("CompanyName") ? "" : jsonObject.getString("CompanyName");
            LocationName= jsonObject.isNull("LocationName") ? "" : jsonObject.getString("LocationName");
            Latitude =jsonObject.isNull("Latitude") ? "" : jsonObject.getString("Latitude");
            Longitude= jsonObject.isNull("Longitude") ? "" : jsonObject.getString("Longitude");
            TotalGames=jsonObject.isNull("TotalGames") ? 0 : jsonObject.getInt("TotalGames");
            TotalPoints=jsonObject.isNull("TotalPoints") ? 0 : jsonObject.getInt("TotalPoints");
            WinPercentage=jsonObject.isNull("WinPercentage") ? 0 : jsonObject.getInt("WinPercentage");
            ClubName=jsonObject.isNull("ClubName") ? "" : jsonObject.getString("ClubName");

        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public int getTotalGames() {
        return TotalGames;
    }

    public void setTotalGames(int totalGames) {
        TotalGames = totalGames;
    }

    public int getTotalPoints() {
        return TotalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        TotalPoints = totalPoints;
    }

    public int getWinPercentage() {
        return WinPercentage;
    }

    public void setWinPercentage(int winPercentage) {
        WinPercentage = winPercentage;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getClubName() {
        return ClubName;
    }

    public void setClubName(String clubName) {
        ClubName = clubName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}



