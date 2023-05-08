package com.example.android_up_sdk.HomeAuxiliaries.ModelClasses;

public class DModel_PopupCat {

    int catId;
    String catTitle;
    String catImage;

    public DModel_PopupCat(int catId, String catTitle, String catImage) {
        this.catId = catId;
        this.catTitle = catTitle;
        this.catImage = catImage;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getCatTitle() {
        return catTitle;
    }

    public void setCatTitle(String catTitle) {
        this.catTitle = catTitle;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }
}
