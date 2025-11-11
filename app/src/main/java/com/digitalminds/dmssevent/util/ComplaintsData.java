package com.digitalminds.dmssevent.util;

import java.io.Serializable;

public class ComplaintsData implements Serializable {

    private String CategoryID;
    private String StatusId;
    private String Description;
    private String UserId;
    private String SubCategoryId;
    private String ReceiverId;
    private String ComplaintTypeId;
    private String CategoryName;
    private String SubCategoryName;
    private String ComplaintType;
    private String EmployeeName;
    private String EmailId;
    private String TicketId;
    private String CurrentStatus;
    private String LastUpdatedOn;

    public String getAreaId() {
        return AreaId;
    }

    public void setAreaId(String areaId) {
        AreaId = areaId;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    private String AreaId;
    private String AreaName;

    // Getters
    public String getCategoryID() { return CategoryID; }
    public String getStatusId() { return StatusId; }
    public String getDescription() { return Description; }
    public String getUserId() { return UserId; }
    public String getSubCategoryId() { return SubCategoryId; }
    public String getReceiverId() { return ReceiverId; }
    public String getComplaintTypeId() { return ComplaintTypeId; }
    public String getCategoryName() { return CategoryName; }
    public String getSubCategoryName() { return SubCategoryName; }
    public String getComplaintType() { return ComplaintType; }
    public String getEmployeeName() { return EmployeeName; }
    public String getEmailId() { return EmailId; }
    public String getTicketId() { return TicketId; }
    public String getCurrentStatus() { return CurrentStatus; }
    public String getLastUpdatedOn() { return LastUpdatedOn; }

    // Setters
    public void setCategoryID(String categoryID) { this.CategoryID = categoryID; }
    public void setStatusId(String statusId) { this.StatusId = statusId; }
    public void setDescription(String description) { this.Description = description; }
    public void setUserId(String userId) { this.UserId = userId; }
    public void setSubCategoryId(String subCategoryId) { this.SubCategoryId = subCategoryId; }
    public void setReceiverId(String receiverId) { this.ReceiverId = receiverId; }
    public void setComplaintTypeId(String complaintTypeId) { this.ComplaintTypeId = complaintTypeId; }
    public void setCategoryName(String categoryName) { this.CategoryName = categoryName; }
    public void setSubCategoryName(String subCategoryName) { this.SubCategoryName = subCategoryName; }
    public void setComplaintType(String complaintType) { this.ComplaintType = complaintType; }
    public void setEmployeeName(String EmployeeName) { this.EmployeeName = EmployeeName; }
    public void setEmailId(String EmailId) { this.EmailId = EmailId; }
    public void setTicketId(String TicketId) { this.TicketId = TicketId; }
    public void setCurrentStatus(String CurrentStatus) { this.CurrentStatus = CurrentStatus; }
    public void setLastUpdatedOn(String LastUpdatedOn) { this.LastUpdatedOn = LastUpdatedOn; }


}
