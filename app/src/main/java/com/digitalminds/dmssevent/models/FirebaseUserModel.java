package com.digitalminds.dmssevent.models;

import java.io.Serializable;
import java.util.ArrayList;

public class FirebaseUserModel {

    public String empId;
    public String emailId;
    public String empName;
    public ArrayList<DateModel> dates;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public ArrayList<DateModel> getDates() {
        return dates;
    }

    public void setDates(ArrayList<DateModel> dates) {
        this.dates = dates;
    }
    public static class DateModel implements Serializable {
        String latitude;
        String longitutde;
        String time;

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitutde() {
            return longitutde;
        }

        public void setLongitutde(String longitutde) {
            this.longitutde = longitutde;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}

