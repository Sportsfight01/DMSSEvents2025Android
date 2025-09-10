package com.digitalminds.dmssevent.models;

        import org.json.JSONObject;

/**
 * Created by Sandeep.Kumar on 01-03-2018.
 */

public class DepartmentListModel {
    int Id;
    String DeptName = "";

    public DepartmentListModel(JSONObject jsonObject) {
        if (jsonObject != null) {
            try {
                Id = jsonObject.isNull("Id") ? 0 : jsonObject.getInt("Id");
                DeptName = jsonObject.isNull("DeptName") ? "" : jsonObject.getString("DeptName");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    public DepartmentListModel(String name) {
      this.Id=-1;
        this.DeptName=name;
        }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }
}
