package com.example.android_up_sdk.HomeAuxiliaries.ModelClasses;

public class DModel_NewBrands {

    String parent_outlet_name;
    String outlet_id;
    String parents_id;
    String outlet_image;
    String outlet_logo;
    String outlet_type;
    boolean isMultipleChild;
    int category_id;
    String category_name;
    String category_image;
    String category_logo;

    public DModel_NewBrands(String parent_outlet_name, String outlet_id, String parents_id, String outlet_image, String outlet_logo, String outlet_type, boolean isMultipleChild, int category_id, String category_name, String category_image, String category_logo) {
        this.parent_outlet_name = parent_outlet_name;
        this.outlet_id = outlet_id;
        this.parents_id = parents_id;
        this.outlet_image = outlet_image;
        this.outlet_logo = outlet_logo;
        this.outlet_type = outlet_type;
        this.isMultipleChild = isMultipleChild;
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_image = category_image;
        this.category_logo = category_logo;
    }

    public String getParent_outlet_name() {
        return parent_outlet_name;
    }

    public void setParent_outlet_name(String parent_outlet_name) {
        this.parent_outlet_name = parent_outlet_name;
    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(String outlet_id) {
        this.outlet_id = outlet_id;
    }

    public String getParents_id() {
        return parents_id;
    }

    public void setParents_id(String parents_id) {
        this.parents_id = parents_id;
    }

    public String getOutlet_image() {
        return outlet_image;
    }

    public void setOutlet_image(String outlet_image) {
        this.outlet_image = outlet_image;
    }

    public String getOutlet_logo() {
        return outlet_logo;
    }

    public void setOutlet_logo(String outlet_logo) {
        this.outlet_logo = outlet_logo;
    }

    public String getOutlet_type() {
        return outlet_type;
    }

    public void setOutlet_type(String outlet_type) {
        this.outlet_type = outlet_type;
    }

    public boolean isMultipleChild() {
        return isMultipleChild;
    }

    public void setMultipleChild(boolean multipleChild) {
        isMultipleChild = multipleChild;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public String getCategory_logo() {
        return category_logo;
    }

    public void setCategory_logo(String category_logo) {
        this.category_logo = category_logo;
    }
}
