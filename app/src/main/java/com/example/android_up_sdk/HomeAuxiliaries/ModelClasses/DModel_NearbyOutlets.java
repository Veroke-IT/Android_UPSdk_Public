package com.example.android_up_sdk.HomeAuxiliaries.ModelClasses;

public class DModel_NearbyOutlets {

    String id;
    String name;
    String Outlet_image;
    boolean isMultipleChild;
    String offersCounts;
    String distance;
    String category_id;
    String category_name;
    String category_name_ar;
    String category_image;
    String category_image_v2;
    String category_logo;
    String parent_outlet_id;
    String parent_outlet_name;

    public DModel_NearbyOutlets(String id, String name, boolean isMultipleChild, String offersCounts, String distance, String category_id, String category_name, String category_name_ar, String category_image, String category_image_v2, String category_logo,String Outlet_image, String parent_outlet_id,String parent_outlet_name) {
        this.id = id;
        this.name = name;
        this.isMultipleChild = isMultipleChild;
        this.offersCounts = offersCounts;
        this.distance = distance;
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_name_ar = category_name_ar;
        this.category_image = category_image;
        this.category_image_v2 = category_image_v2;
        this.category_logo = category_logo;
        this.Outlet_image = Outlet_image;
        this.parent_outlet_id = parent_outlet_id;
        this.parent_outlet_name = parent_outlet_name;
    }

    public String getParent_outlet_id() {
        return parent_outlet_id;
    }

    public void setParent_outlet_id(String parent_outlet_id) {
        this.parent_outlet_id = parent_outlet_id;
    }

    public String getParent_outlet_name() {
        return parent_outlet_name;
    }

    public void setParent_outlet_name(String parent_outlet_name) {
        this.parent_outlet_name = parent_outlet_name;
    }

    public String getOutlet_image() {
        return Outlet_image;
    }

    public void setOutlet_image(String outlet_image) {
        Outlet_image = outlet_image;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_name_ar() {
        return category_name_ar;
    }

    public void setCategory_name_ar(String category_name_ar) {
        this.category_name_ar = category_name_ar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getIsMultipleChild() {
        return isMultipleChild;
    }

    public void setIsMultipleChild(boolean isMultipleChild) {
        this.isMultipleChild = isMultipleChild;
    }

    public String getOffersCounts() {
        return offersCounts;
    }

    public void setOffersCounts(String offersCounts) {
        this.offersCounts = offersCounts;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public String getCategory_image_v2() {
        return category_image_v2;
    }

    public void setCategory_image_v2(String category_image_v2) {
        this.category_image_v2 = category_image_v2;
    }

    public String getCategory_logo() {
        return category_logo;
    }

    public void setCategory_logo(String category_logo) {
        this.category_logo = category_logo;
    }
}



