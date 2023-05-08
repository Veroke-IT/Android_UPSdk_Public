package com.example.android_up_sdk.HomeAuxiliaries.ModelClasses;

public class DModel_Categories {

    private int categoryID ;
    private String categoryName ;
    private String categoryTitle ;
    private String categoryImage ;
    private String categoryLogo ;

    public DModel_Categories(int categoryID, String categoryName, String categoryTitle, String categoryImage, String categoryLogo) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.categoryTitle = categoryTitle;
        this.categoryImage = categoryImage;
        this.categoryLogo = categoryLogo;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryLogo() {
        return categoryLogo;
    }

    public void setCategoryLogo(String categoryLogo) {
        this.categoryLogo = categoryLogo;
    }
}
