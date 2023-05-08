package com.example.android_up_sdk.HomeAuxiliaries.WebServices;

import android.content.Context;
import android.util.Log;

import com.example.android_up_sdk.Utils.ApiMethod;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.example.android_up_sdk.Utils.IWebCallback;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Home_WebHit_Post_HomeApiExtra {
    AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseObject = null;
    Context mContext;


    public void requestHomeApiExtra(Context _mContext, final IWebCallback iWebCallback) {

        this.mContext = _mContext;
        String myUrl = AppConfig.getInstance().getBaseUrlApiMobile() + ApiMethod.GET.getCacheHomeApiExtra;

        mClient.addHeader(ApiMethod.HEADER.Authorization, AppConfig.getInstance().mUser.getAuthorizationToken());
        mClient.addHeader("app_id", "1");
        mClient.setMaxRetriesAndTimeout(AppConstt.LIMIT_API_RETRY, AppConstt.LIMIT_TIMEOUT_MILLIS);
        mClient.setTimeout(AppConstt.LIMIT_TIMEOUT_MILLIS);
        mClient.get(myUrl, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");
                            responseObject = gson.fromJson(strResponse, ResponseModel.class);

                            switch (statusCode) {
                                case AppConstt.ServerStatus.OK:
                                    iWebCallback.onWebResult(true, "");
                                    break;

                                case AppConstt.ServerStatus.NO_CONTENT:
                                    iWebCallback.onWebResult(true, responseObject.getMessage());
                                    break;

                                default:
                                    iWebCallback.onWebResult(false, responseObject.getMessage());
                                    break;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            iWebCallback.onWebException(ex);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                            error) {
                        switch (statusCode) {
                            case AppConstt.ServerStatus.NETWORK_ERROR:
                                iWebCallback.onWebResult(false, AppConfig.getInstance().getNetworkErrorMessage());
                                break;


                            default:
                                try {
                                    Gson gson = new Gson();
                                    String strResponse = new String(responseBody, "UTF-8");
                                    responseObject = gson.fromJson(strResponse, ResponseModel.class);
                                    iWebCallback.onWebResult(false, responseObject.getMessage());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    iWebCallback.onWebException(e);
                                }
                                break;
                        }
                    }
                }
        );
    }


    public class ResponseModel {

        @Expose
        private Data data;
        @Expose
        private String message;
        @Expose
        private String status;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public class Data {

            private List<Category> categories;
            @Expose
            private List<NewBrand> newBrands;
            @Expose
            private List<PopularCategory> popularCategories;
            @Expose
            private List<Featured> featured;


            public List<NewBrand> getNewBrands() {
                return newBrands;
            }

            public void setNewBrands(List<NewBrand> newBrands) {
                this.newBrands = newBrands;
            }

            public List<PopularCategory> getPopularCategories() {
                return popularCategories;
            }

            public void setPopularCategories(List<PopularCategory> popularCategories) {
                this.popularCategories = popularCategories;
            }


            public void setFeatured(List<Featured> featured) {
                this.featured = featured;
            }

            public List<Featured> getFeatured() {
                return this.featured;
            }

            public List<Category> getCategories() {
                return categories;
            }

            public void setCategories(List<Category> categories) {
                this.categories = categories;
            }
        }



        public class NewBrand {

            @SerializedName("category_id")
            private int categoryId;
            @SerializedName("category_image")
            private String categoryImage;
            @SerializedName("category_logo")
            private String categoryLogo;
            @SerializedName("category_name")
            private String categoryName;
            @SerializedName("created_at")
            private String createdAt;
            @SerializedName("outlet_id")
            private String outletId;
            @SerializedName("outlet_image")
            private String outletImage;
            @SerializedName("outlet_logo")
            private String outletLogo;
            @SerializedName("outlet_type")
            private String outletType;
            @SerializedName("parent_outlet_id")
            private String parentOutletId;
            @SerializedName("parent_outlet_name")
            private String parentOutletName;
            @SerializedName("parents_id")
            private String parentsId;
            @SerializedName("isMultipleChild")
            private boolean isMultipleChild;

            public boolean isMultipleChild() {
                return isMultipleChild;
            }

            public void setMultipleChild(boolean multipleChild) {
                isMultipleChild = multipleChild;
            }

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
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

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getOutletId() {
                return outletId;
            }

            public void setOutletId(String outletId) {
                this.outletId = outletId;
            }

            public String getOutletImage() {
                return outletImage;
            }

            public void setOutletImage(String outletImage) {
                this.outletImage = outletImage;
            }

            public String getOutletLogo() {
                return outletLogo;
            }

            public void setOutletLogo(String outletLogo) {
                this.outletLogo = outletLogo;
            }

            public String getOutletType() {
                return outletType;
            }

            public void setOutletType(String outletType) {
                this.outletType = outletType;
            }

            public String getParentOutletId() {
                return parentOutletId;
            }

            public void setParentOutletId(String parentOutletId) {
                this.parentOutletId = parentOutletId;
            }

            public String getParentOutletName() {
                return parentOutletName;
            }

            public void setParentOutletName(String parentOutletName) {
                this.parentOutletName = parentOutletName;
            }

            public String getParentsId() {
                return parentsId;
            }

            public void setParentsId(String parentsId) {
                this.parentsId = parentsId;
            }

        }

        public class PopularCategory {
            @Expose
            private int id;
            @Expose
            private String name;
            @Expose
            private String name_ar;
            @Expose
            private String description;
            @Expose
            private String image;
            @Expose
            private String status;
            @SerializedName("created_at")
            private String createdAt;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getImage() {
                return image;
            }

            public String getName_ar() {
                return name_ar;
            }

            public void setName_ar(String name_ar) {
                this.name_ar = name_ar;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }
        }

        public class Featured {

            public class Outlets_parents
            {
                private String name;

                private String checkMultipleChild;

                public void setName(String name){
                    this.name = name;
                }
                public String getName(){
                    return this.name;
                }
                public void setCheckMultipleChild(String checkMultipleChild){
                    this.checkMultipleChild = checkMultipleChild;
                }
                public String getCheckMultipleChild(){
                    return this.checkMultipleChild;
                }
            }

            public class Linked_outlet_category
            {
                private int id;

                private String name;

                private String description;

                private String image;

                private String logo;

                public void setId(int id){
                    this.id = id;
                }
                public int getId(){
                    return this.id;
                }
                public void setName(String name){
                    this.name = name;
                }
                public String getName(){
                    return this.name;
                }
                public void setDescription(String description){
                    this.description = description;
                }
                public String getDescription(){
                    return this.description;
                }
                public void setImage(String image){
                    this.image = image;
                }
                public String getImage(){
                    return this.image;
                }
                public void setLogo(String logo){
                    this.logo = logo;
                }
                public String getLogo(){
                    return this.logo;
                }
            }

            private String name;

            private int id;

            private int parents_id;

            private String outlet_logo;

            private String outlet_image;

            private Outlets_parents outlets_parents;

            private List<Linked_outlet_category> linked_outlet_category;

            public void setName(String name){
                this.name = name;
            }
            public String getName(){
                return this.name;
            }
            public void setId(int id){
                this.id = id;
            }
            public int getId(){
                return this.id;
            }
            public void setParents_id(int parents_id){
                this.parents_id = parents_id;
            }
            public int getParents_id(){
                return this.parents_id;
            }
            public void setOutlet_logo(String outlet_logo){
                this.outlet_logo = outlet_logo;
            }
            public String getOutlet_logo(){
                return this.outlet_logo;
            }
            public void setOutlet_image(String outlet_image){
                this.outlet_image = outlet_image;
            }
            public String getOutlet_image(){
                return this.outlet_image;
            }
            public void setOutlets_parents(Outlets_parents outlets_parents){
                this.outlets_parents = outlets_parents;
            }
            public Outlets_parents getOutlets_parents(){
                return this.outlets_parents;
            }
            public void setLinked_outlet_category(List<Linked_outlet_category> linked_outlet_category){
                this.linked_outlet_category = linked_outlet_category;
            }
            public List<Linked_outlet_category> getLinked_outlet_category(){
                return this.linked_outlet_category;
            }
        }

        public class Category {

            @Expose
            private int id;
            @Expose
            private String image;
            @Expose
            private String name;
            @Expose
            private String name_ar;
            @Expose
            private String logo;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getName_ar() {
                return name_ar;
            }

            public void setName_ar(String name_ar) {
                this.name_ar = name_ar;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }
        }


    }
}
