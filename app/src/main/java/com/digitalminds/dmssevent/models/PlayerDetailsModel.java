package com.digitalminds.dmssevent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayerDetailsModel {

    @SerializedName("Id")
    @Expose
    private int Id;

    @SerializedName("Status")
    @Expose
    private int Status;

    @SerializedName("DisplayName")
    @Expose
    private String DisplayName;

    @SerializedName("EmailID")
    @Expose
    private String EmailID;

    @SerializedName("EmpID")
    @Expose
    private String EmpID;

    @SerializedName("DeptName")
    @Expose
    private String DeptName;

    @SerializedName("RoleName")
    @Expose
    private String RoleName;

    @SerializedName("Mobile")
    @Expose
    private String Mobile;

    @SerializedName("ProfilePhoto")
    @Expose
    private String ProfilePhoto;

    public int getId() {
        return Id;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getEmpID() {
        return EmpID;
    }

    public void setEmpID(String empID) {
        EmpID = empID;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getProfilePhoto() {
        return ProfilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        ProfilePhoto = profilePhoto;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }
}
