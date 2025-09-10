package com.digitalminds.dmssevent.models;

/**
 * Created by Sandeep.Kumar on 30-01-2018.
 */

public class NewsModel {
    String[] names={""};

    public NewsModel(String[] names) {
        this.names = names;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }
}
