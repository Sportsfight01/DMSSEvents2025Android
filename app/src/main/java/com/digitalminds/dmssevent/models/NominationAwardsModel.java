package com.digitalminds.dmssevent.models;

import org.json.JSONObject;

/**
 * Created by Sandeep.Kumar on 01-03-2018.
 */

public class NominationAwardsModel {
    int Id;
    String AwardName="",Description="",Criteria="",Reward="",IconURL="";
    String AwardType="";

    public NominationAwardsModel(JSONObject resultJson){
        if(resultJson!=null){
            try {
                Id = resultJson.isNull("Id") ? 0 : resultJson.getInt("Id");
                AwardName = resultJson.isNull("AwardName") ? "" : resultJson.getString("AwardName");
                Description = resultJson.isNull("Description") ? "" : resultJson.getString("Description");
                Criteria = resultJson.isNull("Criteria") ? "" : resultJson.getString("Criteria");
                Reward = resultJson.isNull("Reward") ? "" : resultJson.getString("Reward");
                IconURL = resultJson.isNull("IconURL") ? "" : resultJson.getString("IconURL");
                AwardType = resultJson.isNull("AwardType") ? "" : resultJson.getString("AwardType");
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

    public String getAwardName() {
        return AwardName;
    }

    public void setAwardName(String awardName) {
        AwardName = awardName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCriteria() {
        return Criteria;
    }

    public void setCriteria(String criteria) {
        Criteria = criteria;
    }

    public String getReward() {
        return Reward;
    }

    public void setReward(String reward) {
        Reward = reward;
    }

    public String getIconURL() {
        return IconURL;
    }

    public void setIconURL(String iconURL) {
        IconURL = iconURL;
    }

    public String getAwardType() {
        return AwardType;
    }

    public void setAwardType(String awardType) {
        AwardType = awardType;
    }
}
