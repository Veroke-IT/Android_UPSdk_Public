package com.example.android_up_sdk.HomeAuxiliaries.ModelClasses;

public class DModel_Collection {
    private int collectionId ;
    private String collectionImage;
    private String collectionName;

    public DModel_Collection(int collectionId, String collectionImage, String collectionName) {
        this.collectionId = collectionId;
        this.collectionImage = collectionImage;
        this.collectionName = collectionName;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public String getCollectionImage() {
        return collectionImage;
    }

    public void setCollectionImage(String collectionImage) {
        this.collectionImage = collectionImage;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
}

