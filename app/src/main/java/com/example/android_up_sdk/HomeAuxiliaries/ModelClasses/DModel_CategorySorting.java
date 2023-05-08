package com.example.android_up_sdk.HomeAuxiliaries.ModelClasses;

import com.example.android_up_sdk.Utils.AppConstt;

public class DModel_CategorySorting {

    public String genderSelectedSortType; // sorting of Offers in beauty n health w.r.t gender specification
    public String sortBy;
    public int categoryId;
    public String categoryName;
    public int popularCategoryId;
    public String parentId;
    public String collectionId;
    public String playlistId;
    public int interestId;



    public DModel_CategorySorting() {
        this.genderSelectedSortType = AppConstt.DEFAULT_VALUES.Both;
        this.sortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
        this.categoryId = 0;
        this.categoryName = "";
        this.popularCategoryId = 0;
        this.collectionId = "";
        this.playlistId = "";
        this.interestId = 0;

    }

    public DModel_CategorySorting(String genderSelectedSortType, String sortBy, int categoryId, String categoryName, int popularCategoryId, String parentId, String collectionId, String playlistId, int interestId) {
        this.genderSelectedSortType = genderSelectedSortType;
        this.sortBy = sortBy;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.popularCategoryId = popularCategoryId;
        this.parentId = parentId;
        this.collectionId = collectionId;
        this.playlistId = playlistId;
        this.interestId = interestId;
    }

    public String getGenderSelectedSortType() {
        return genderSelectedSortType;
    }

    public void setGenderSelectedSortType(String genderSelectedSortType) {
        this.genderSelectedSortType = genderSelectedSortType;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getPopularCategoryId() {
        return popularCategoryId;
    }

    public void setPopularCategoryId(int popularCategoryId) {
        this.popularCategoryId = popularCategoryId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public int getInterestId() {
        return interestId;
    }

    public void setInterestId(int interestId) {
        this.interestId = interestId;
    }
}
