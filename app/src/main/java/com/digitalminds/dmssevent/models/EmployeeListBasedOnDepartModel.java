package com.digitalminds.dmssevent.models;

import org.json.JSONObject;

/**
 * Created by Sandeep.Kumar on 01-03-2018.
 */

public class EmployeeListBasedOnDepartModel {
    int Id;
    String EmailID = "", EmpGuid = "", EmpID = "", FirstName = "", LastName = "", CommonName = "",
            DisplayName = "", OfficePhone = "", Mobile = "", ManagerGuid = "", JobTittle = "", BuisnessUnit = "", Department = "", UserRole = "", ProfilePhoto = "", ProfileImageURL = "",LeaderTitle = "";
    boolean Disabled, IsPasswordSet;

    public EmployeeListBasedOnDepartModel(JSONObject resJsonObject) {
        if (resJsonObject != null) {
            try {
                Id = resJsonObject.isNull("Id") ? 0 : resJsonObject.getInt("Id");
                EmailID = resJsonObject.isNull("EmailID") ? "" : resJsonObject.getString("EmailID");
                EmpGuid = resJsonObject.isNull("EmpGuid") ? "" : resJsonObject.getString("EmpGuid");
                EmpID = resJsonObject.isNull("EmpID") ? "" : resJsonObject.getString("EmpID");
                FirstName = resJsonObject.isNull("FirstName") ? "" : resJsonObject.getString("FirstName");
                LastName = resJsonObject.isNull("LastName") ? "" : resJsonObject.getString("LastName");
                CommonName = resJsonObject.isNull("CommonName") ? "" : resJsonObject.getString("CommonName");
                DisplayName = resJsonObject.isNull("DisplayName") ? "" : resJsonObject.getString("DisplayName");
                OfficePhone = resJsonObject.isNull("OfficePhone") ? "" : resJsonObject.getString("OfficePhone");
                Mobile = resJsonObject.isNull("Mobile") ? "" : resJsonObject.getString("Mobile");
                ManagerGuid = resJsonObject.isNull("ManagerGuid") ? "" : resJsonObject.getString("ManagerGuid");
                JobTittle = resJsonObject.isNull("JobTittle") ? "" : resJsonObject.getString("JobTittle");
                BuisnessUnit = resJsonObject.isNull("BuisnessUnit") ? "" : resJsonObject.getString("BuisnessUnit");
                Department = resJsonObject.isNull("Department") ? "" : resJsonObject.getString("Department");
                UserRole = resJsonObject.isNull("UserRole") ? "" : resJsonObject.getString("UserRole");
                ProfilePhoto = resJsonObject.isNull("ProfilePhoto") ? "" : resJsonObject.getString("ProfilePhoto");
                ProfileImageURL = resJsonObject.isNull("ProfileImageURL") ? "" : resJsonObject.getString("ProfileImageURL");
                LeaderTitle = resJsonObject.isNull("LeaderTitle") ? "" : resJsonObject.getString("LeaderTitle");
                Disabled = resJsonObject.isNull("Disabled") ? false : resJsonObject.getBoolean("Disabled");
                IsPasswordSet = resJsonObject.isNull("IsPasswordSet") ? false : resJsonObject.getBoolean("IsPasswordSet");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    public EmployeeListBasedOnDepartModel(String name) {
        this.Id=-1;
        this.DisplayName=name;

    }
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getEmpGuid() {
        return EmpGuid;
    }

    public void setEmpGuid(String empGuid) {
        EmpGuid = empGuid;
    }

    public String getEmpID() {
        return EmpID;
    }

    public void setEmpID(String empID) {
        EmpID = empID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getCommonName() {
        return CommonName;
    }

    public void setCommonName(String commonName) {
        CommonName = commonName;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getOfficePhone() {
        return OfficePhone;
    }

    public void setOfficePhone(String officePhone) {
        OfficePhone = officePhone;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getManagerGuid() {
        return ManagerGuid;
    }

    public void setManagerGuid(String managerGuid) {
        ManagerGuid = managerGuid;
    }

    public String getJobTittle() {
        return JobTittle;
    }

    public void setJobTittle(String jobTittle) {
        JobTittle = jobTittle;
    }

    public String getBuisnessUnit() {
        return BuisnessUnit;
    }

    public void setBuisnessUnit(String buisnessUnit) {
        BuisnessUnit = buisnessUnit;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getUserRole() {
        return UserRole;
    }

    public void setUserRole(String userRole) {
        UserRole = userRole;
    }

    public String getProfilePhoto() {
        return ProfilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        ProfilePhoto = profilePhoto;
    }

    public String getProfileImageURL() {
        return ProfileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        ProfileImageURL = profileImageURL;
    }

    public boolean isDisabled() {
        return Disabled;
    }

    public void setDisabled(boolean disabled) {
        Disabled = disabled;
    }

    public boolean isPasswordSet() {
        return IsPasswordSet;
    }

    public void setPasswordSet(boolean passwordSet) {
        IsPasswordSet = passwordSet;
    }

    public String getLeaderTitle() {
        return LeaderTitle;
    }

    public void setLeaderTitle(String leaderTitle) {
        LeaderTitle = leaderTitle;
    }
}
