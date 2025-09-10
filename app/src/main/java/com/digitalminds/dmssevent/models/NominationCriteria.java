package com.digitalminds.dmssevent.models;

/**
 * Created by Jaya.Krishna on 27-02-2018.
 */

public class NominationCriteria {
    String title, price, description;
    int eligibilityPoints;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEligibilityPoints() {
        return eligibilityPoints;
    }

    public void setEligibilityPoints(int eligibilityPoints) {
        this.eligibilityPoints = eligibilityPoints;
    }
}
