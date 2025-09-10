package com.digitalminds.dmssevent.models;

import org.json.JSONObject;

/**
 * Created by Sandeep.Kumar on 12-02-2018.
 */

public class CommentsListModel {
    int Id, NewsId;
    String Comment = "",EmployeeName="",EmpId="";

    public CommentsListModel(JSONObject reJsonObject) {
        if (reJsonObject != null) {
            try {
                Id=reJsonObject.isNull("Id")?0:reJsonObject.getInt("Id");
                NewsId=reJsonObject.isNull("NewsId")?0:reJsonObject.getInt("NewsId");
                EmpId=reJsonObject.isNull("EmpId")?"":reJsonObject.getString("EmpId");
                Comment=reJsonObject.isNull("Comment")?"":reJsonObject.getString("Comment");
                EmployeeName=reJsonObject.isNull("EmployeeName")?"":reJsonObject.getString("EmployeeName");
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

    public int getNewsId() {
        return NewsId;
    }

    public void setNewsId(int newsId) {
        NewsId = newsId;
    }

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String empId) {
        EmpId = empId;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }
}
