package com.digitalminds.dmssevent.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sandeep.Kumar on 12-02-2018.
 */

public class GetAllNewsFeedModel {
    int Id,LikesCount,CommentsCount;
    String EmployeeName="",Title="",FBImageUrl="",CreatedOn="",UpdatedOn="",profileImage="",EmployeeId="",ProfileImageUrl="";
    boolean IsActive,IsFavourite;
     ArrayList<CommentsListModel> commentsListModelArrayList=new ArrayList<CommentsListModel>();

    public GetAllNewsFeedModel(JSONObject resultJson){
        if(resultJson!=null){
            try{
                Id=resultJson.isNull("Id") ? 0 :resultJson.getInt("Id");
                EmployeeId=resultJson.isNull("EmployeeId")?"":resultJson.getString("EmployeeId");
                CommentsCount=resultJson.isNull("CommentsCount")?0:resultJson.getInt("CommentsCount");
                LikesCount=resultJson.isNull("LikesCount")?0:resultJson.getInt("LikesCount");
                EmployeeName=resultJson.isNull("EmployeeName")?"":resultJson.getString("EmployeeName");
                Title=resultJson.isNull("Title")?"":resultJson.getString("Title");
                FBImageUrl=resultJson.isNull("FBImageUrl")?"":resultJson.getString("FBImageUrl");
                CreatedOn=resultJson.isNull("CreatedOn")?"":resultJson.getString("CreatedOn");
                UpdatedOn=resultJson.isNull("UpdatedOn")?"":resultJson.getString("UpdatedOn");
                ProfileImageUrl=resultJson.isNull("ProfileImageUrl")?"":resultJson.getString("ProfileImageUrl");
                IsActive=resultJson.isNull("IsActive")?false:resultJson.getBoolean("IsActive");
                IsFavourite=resultJson.isNull("IsFavourite")?false:resultJson.getBoolean("IsFavourite");
                if(!resultJson.isNull("CommentsList")){
                    JSONArray commentsListArray=resultJson.getJSONArray("CommentsList");
                    if(commentsListArray.length()>0){
                        for(int i=0;i<commentsListArray.length();i++){
                            CommentsListModel commentsListModel=new CommentsListModel(commentsListArray.getJSONObject(i));
                            commentsListModelArrayList.add(commentsListModel);
                        }
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        EmployeeId = employeeId;
    }

    public int getLikesCount() {
        return LikesCount;
    }

    public void setLikesCount(int likesCount) {
        LikesCount = likesCount;
    }

    public int getCommentsCount() {
        return CommentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        CommentsCount = commentsCount;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getFBImageUrl() {
        return FBImageUrl;
    }

    public void setFBImageUrl(String FBImageUrl) {
        this.FBImageUrl = FBImageUrl;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    public String getUpdatedOn() {
        return UpdatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        UpdatedOn = updatedOn;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public ArrayList<CommentsListModel> getCommentsListModelArrayList() {
        return commentsListModelArrayList;
    }

    public void setCommentsListModelArrayList(ArrayList<CommentsListModel> commentsListModelArrayList) {
        this.commentsListModelArrayList = commentsListModelArrayList;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isFavourite() {
        return IsFavourite;
    }

    public void setFavourite(boolean favourite) {
        IsFavourite = favourite;
    }

    public String getProfileImageUrl() {
        return ProfileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        ProfileImageUrl = profileImageUrl;
    }
}
