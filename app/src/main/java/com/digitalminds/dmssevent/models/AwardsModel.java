package com.digitalminds.dmssevent.models;

import android.net.Uri;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sandeep.kumar on 05-04-2017.
 */
public class AwardsModel {
int Id;
    String AwardName="",Description="",Criteria="",Rewards="",ColorCode="",awardImage = "";
    boolean IsActive;
    int position;

    ArrayList<EmployeeAwardModel> employeeAwardModelArrayList=new ArrayList<EmployeeAwardModel>();

    public AwardsModel(JSONObject resultJson){
    try{
        Id=resultJson.isNull("Id")?0:resultJson.getInt("Id");
        AwardName=resultJson.isNull("AwardName")?"":resultJson.getString("AwardName");
        Description=resultJson.isNull("Description")?"":resultJson.getString("Description");
        Criteria=resultJson.isNull("Criteria")?"":resultJson.getString("Criteria");
        Rewards=resultJson.isNull("Rewards")?"":resultJson.getString("Rewards");
        ColorCode=resultJson.isNull("ColorCode")?"":resultJson.getString("ColorCode");
        IsActive=resultJson.isNull("IsActive")?false:resultJson.getBoolean("IsActive");
        if (!resultJson.isNull("Employee")) {
            JSONArray employeeJsonArray=resultJson.getJSONArray("Employee");
            for (int i = 0; i < employeeJsonArray.length(); i++) {
                EmployeeAwardModel employeeAwardModel = new EmployeeAwardModel(employeeJsonArray.getJSONObject(i));
                employeeAwardModelArrayList.add(employeeAwardModel);
            }
            saveAwardImageUrl();
        }
    }catch (Exception ex){
        ex.printStackTrace();
    }
    }

    private void saveAwardImageUrl() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://dmssevents-606bf.appspot.com");
        try{
            if( employeeAwardModelArrayList.get(0).getEmpIdCard().equalsIgnoreCase("DM003")){
                final String imageName = "Milestone2017_awards/" + employeeAwardModelArrayList.get(0).getEmpIdCard() + "_" +employeeAwardModelArrayList.get(0).DisplayName  + ".jpg";
                StorageReference childRef = storageRef.child(imageName);
                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        awardImage = uri.toString();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        awardImage = "https://firebasestorage.googleapis.com/v0/b/dmssevents-606bf.appspot.com/o/Milestone2017_awards%2Fmilestone.jpg?alt=media&token=3dab9be0-dbd7-438f-a5ac-e14a53e457c6";
                    }
                });
            }else{
                final String imageName = "Milestone2017_awards/" + employeeAwardModelArrayList.get(0).getEmpIdCard() + "_" + AwardName + ".jpg";
                StorageReference childRef = storageRef.child(imageName);
                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        awardImage = uri.toString();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        awardImage = "https://firebasestorage.googleapis.com/v0/b/dmssevents-606bf.appspot.com/o/Milestone2017_awards%2Fmilestone.jpg?alt=media&token=3dab9be0-dbd7-438f-a5ac-e14a53e457c6";
                    }
                });
            }


        }catch (Exception ex){
            ex.fillInStackTrace();
        }



    }

    public String getAwardImage() {
        return awardImage;
    }

    public void setAwardImage(String awardImage) {
        this.awardImage = awardImage;
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

    public String getRewards() {
        return Rewards;
    }

    public void setRewards(String rewards) {
        Rewards = rewards;
    }

    public String getColorCode() {
        return ColorCode;
    }

    public void setColorCode(String colorCode) {
        ColorCode = colorCode;
    }


    public boolean isActive() {
        return IsActive;
    }

    public void setIsActive(boolean isActive) {
        IsActive = isActive;
    }

    public ArrayList<EmployeeAwardModel> getEmployeeAwardModelArrayList() {
        return employeeAwardModelArrayList;
    }

    public void setEmployeeAwardModelArrayList(ArrayList<EmployeeAwardModel> employeeAwardModelArrayList) {
        this.employeeAwardModelArrayList = employeeAwardModelArrayList;
    }
}

