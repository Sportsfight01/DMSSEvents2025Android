package com.digitalminds.dmssevent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectedPlayersResultModel {
    boolean selected=false;
    boolean selectedFirst=false;
    boolean firstTime;
    boolean deleteList;
    boolean addButton;
    boolean hideAddButton;
    @SerializedName("Id")
    @Expose
    private int Id;

    @SerializedName("DisplayName")
    @Expose
    private String DisplayName;

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

    @SerializedName("EmailID")
    @Expose
    private String EmailID;

    @SerializedName("Status")
    @Expose
    private int Status;

    public int getId() {
        return Id;
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

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelectedFirst() {
        return selectedFirst;
    }

    public void setSelectedFirst(boolean selectedFirst) {
        this.selectedFirst = selectedFirst;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public boolean isDeleteList() {
        return deleteList;
    }

    public void setDeleteList(boolean deleteList) {
        this.deleteList = deleteList;
    }

    public boolean isAddButton() {
        return addButton;
    }

    public void setAddButton(boolean addButton) {
        this.addButton = addButton;
    }

    public boolean isHideAddButton() {
        return hideAddButton;
    }

    public void setHideAddButton(boolean hideAddButton) {
        this.hideAddButton = hideAddButton;
    }
}
