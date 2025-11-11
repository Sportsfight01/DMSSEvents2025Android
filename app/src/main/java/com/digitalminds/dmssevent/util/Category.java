package com.digitalminds.dmssevent.util;

public class Category{
    int CategoryId;
    int AreaId;
    String Name;

    public Category(int AreaId, int categoryId, String name) {
        this.CategoryId = categoryId;
        this.AreaId = AreaId;
        this.Name = name;
    }

    @Override
    public String toString() {
        return Name;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public int getAreaId() {
        return AreaId;
    }
}