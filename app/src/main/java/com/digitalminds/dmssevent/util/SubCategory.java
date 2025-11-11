package com.digitalminds.dmssevent.util;

public class SubCategory {
    int CategoryId;
    int SubCategoryId;
    String Name;

    public SubCategory(int categoryId, int subCategoryId, String name) {
        this.CategoryId = categoryId;
        this.SubCategoryId = subCategoryId;
        this.Name = name;
    }

    @Override
    public String toString() {
        return Name;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public int getSubCategoryId() {
        return SubCategoryId;
    }
}