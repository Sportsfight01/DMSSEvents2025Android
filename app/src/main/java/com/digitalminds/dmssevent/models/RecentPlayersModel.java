package com.digitalminds.dmssevent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecentPlayersModel {


    @SerializedName("Status")
    @Expose
    private String Status;

    @SerializedName("Result")
    @Expose
    private List<SelectedPlayersResultModel> totalResult;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<SelectedPlayersResultModel> getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(List<SelectedPlayersResultModel> totalResult) {
        this.totalResult = totalResult;
    }
}
