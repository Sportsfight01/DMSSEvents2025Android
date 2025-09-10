package com.digitalminds.dmssevent.models;

import org.json.JSONObject;

/**
 * Created by Sandeep.Kumar on 05-03-2018.
 */

public class NominationListModel {
    String EmployeeId="",EmployeeName="",AwardName="",DeptName="";

    public NominationListModel(JSONObject jsonObject) {
        if (jsonObject != null) {
            try {
                EmployeeId=jsonObject.isNull("EmployeeId")?"":jsonObject.getString("EmployeeId");
                EmployeeName=jsonObject.isNull("EmployeeName")?"":jsonObject.getString("EmployeeName");
                AwardName=jsonObject.isNull("AwardName")?"":jsonObject.getString("AwardName");
                DeptName=jsonObject.isNull("DeptName")?"":jsonObject.getString("DeptName");
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public String getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        EmployeeId = employeeId;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getAwardName() {
        return AwardName;
    }

    public void setAwardName(String awardName) {
        AwardName = awardName;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }
}
