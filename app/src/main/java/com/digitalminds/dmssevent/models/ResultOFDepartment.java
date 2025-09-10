package com.digitalminds.dmssevent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultOFDepartment {

    @SerializedName("Status")
    @Expose
    private String Status;

    @SerializedName("Result")
    @Expose
    private List<DepartmentSearchModel> totalResult;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<DepartmentSearchModel> getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(List<DepartmentSearchModel> totalResult) {
        this.totalResult = totalResult;
    }
}
