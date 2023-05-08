package com.example.android_up_sdk.HomeAuxiliaries.WebServices;

import android.content.Context;
import android.util.Log;

import com.example.android_up_sdk.Utils.ApiMethod;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.example.android_up_sdk.Utils.GPSTracker;
import com.example.android_up_sdk.Utils.IWebCallback;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;


public class Home_WebHit_Post_HomeApiEssential {

    AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseObject = null;
    Context mContext;


    public void requestHomeApiEssential(Context _mContext, final IWebCallback iWebCallback, final String _deviceId) {

        this.mContext = _mContext;
        String myUrl = AppConfig.getInstance().getBaseUrlApiMobile() + ApiMethod.GET.getHomeApiEssentialNew;
        RequestParams requestParams = new RequestParams();

        if (AppConfig.getInstance().isLocationEnabled(_mContext) && AppConfig.getInstance().checkPermission(_mContext)) {
            double lat = GPSTracker.getInstance(_mContext).getLatitude();
            double lng = GPSTracker.getInstance(_mContext).getLongitude();

            requestParams.put("longitude", lng);
            requestParams.put("latitude", lat);
        }

        mClient.addHeader(ApiMethod.HEADER.Authorization, AppConfig.getInstance().mUser.getAuthorizationToken());
        mClient.addHeader("app_id", "1");
        mClient.setMaxRetriesAndTimeout(AppConstt.LIMIT_API_RETRY, AppConstt.LIMIT_TIMEOUT_MILLIS);
        mClient.setTimeout(AppConstt.LIMIT_TIMEOUT_MILLIS);
        mClient.get(mContext,myUrl,requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");
                            responseObject = gson.fromJson(strResponse, ResponseModel.class);

                            switch (statusCode) {
                                case AppConstt.ServerStatus.OK:
                                    if (Home_WebHit_Post_HomeApiEssential.responseObject.getData().getDefaults().getUber() != null &&
                                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getDefaults().getUber().equalsIgnoreCase("1")) {
                                        AppConfig.getInstance().mUser.setUberRequired(true);
                                    } else {
                                        AppConfig.getInstance().mUser.setUberRequired(false);
                                    }
                                    AppConfig.getInstance().mUser.setMasterMerchant(Home_WebHit_Post_HomeApiEssential.responseObject.getData().getSuper_access_pin());

                                    AppConfig.getInstance().saveUserData();
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
//
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
        private String status;

        private int statusCode;

        private String message;

        private Data data;

        private String error;

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public int getStatusCode() {
            return this.statusCode;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public Data getData() {
            return this.data;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getError() {
            return this.error;
        }



        public class Defaults {
            private String uber;



            public void setUber(String uber) {
                this.uber = uber;
            }

            public String getUber() {
                return this.uber;
            }


        }

        public class Linked_outlet_category {
            private String id;

            private String name;

            private String name_ar;

            private String description;

            private String image;

            private String image_v2;

            private String logo;


            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name){
                this.name = name;
            }
            public String getName(){
                return this.name;
            }
            public void setName_ar(String name_ar){
                this.name_ar = name_ar;
            }
            public String getName_ar(){
                return this.name_ar;
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
            public void setImage_v2(String image_v2){
                this.image_v2 = image_v2;
            }
            public String getImage_v2(){
                return this.image_v2;
            }
            public void setLogo(String logo){
                this.logo = logo;
            }
            public String getLogo(){
                return this.logo;
            }
        }

        public class Outlets_parents {
            private String name;

            private String id;

            public void setName(String name){
                this.name = name;
            }
            public String getName(){
                return this.name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        public class NearbyOutlets {
            private String name;

            private String image;

            private String id;

            private String isMultipleChild;

            private String offersCount;

            private String distance;

            @Expose
            private List<Linked_outlet_category> linked_outlet_category;

            @Expose
            private Outlets_parents outlets_parents;

            public void setName(String name){
                this.name = name;
            }
            public String getName(){
                return this.name;
            }
            public void setImage(String image){
                this.image = image;
            }
            public String getImage(){
                return this.image;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getIsMultipleChild() {
                return isMultipleChild;
            }

            public void setIsMultipleChild(String isMultipleChild) {
                this.isMultipleChild = isMultipleChild;
            }

            public String getOffersCount() {
                return offersCount;
            }

            public void setOffersCount(String offersCount) {
                this.offersCount = offersCount;
            }

            public void setDistance(String distance){
                this.distance = distance;
            }
            public String getDistance(){
                return this.distance;
            }
            public void setLinked_outlet_category(List<Linked_outlet_category> linked_outlet_category){
                this.linked_outlet_category = linked_outlet_category;
            }
            public List<Linked_outlet_category> getLinked_outlet_category(){
                return this.linked_outlet_category;
            }
            public void setOutlets_parents(Outlets_parents outlets_parents){
                this.outlets_parents = outlets_parents;
            }
            public Outlets_parents getOutlets_parents(){
                return this.outlets_parents;
            }
        }


        public class Data {
            private Defaults defaults;

            private String super_access_pin;

            private List<OfferUsedAgain> offerUsedAgain;

            private List<String> subscription_banner;

            private List<NearbyOutlets> nearbyOutlets;

            public void setDefaults(Defaults defaults) {
                this.defaults = defaults;
            }

            public Defaults getDefaults() {
                return this.defaults;
            }

            public void setSuper_access_pin(String super_access_pin) {
                this.super_access_pin = super_access_pin;
            }

            public String getSuper_access_pin() {
                return this.super_access_pin;
            }

            public void setOfferUsedAgain(List<OfferUsedAgain> offerUsedAgain) {
                this.offerUsedAgain = offerUsedAgain;
            }

            public List<OfferUsedAgain> getOfferUsedAgain() {
                return this.offerUsedAgain;
            }

            public void setSubscription_banner(List<String> subscription_banner) {
                this.subscription_banner = subscription_banner;
            }

            public List<String> getSubscription_banner() {
                return this.subscription_banner;
            }

            public void setNearbyOutlets(List<NearbyOutlets> nearbyOutlets) {
                this.nearbyOutlets = nearbyOutlets;
            }

            public List<NearbyOutlets> getNearbyOutlets() {
                return this.nearbyOutlets;
            }
        }

        public class OfferUsedAgain {

            @Expose
            private String active;
            @SerializedName("approx_saving")
            private String approxSaving;
            @SerializedName("created_at")
            private String createdAt;
            @Expose
            private String description;
            @SerializedName("end_datetime")
            private String endDatetime;
            @Expose
            private String id;
            @Expose
            private String image;
            @SerializedName("interest_tags")
            private Object interestTags;
            @SerializedName("order_id")
            private String orderId;
            @SerializedName("orders_count")
            private String ordersCount;
            @SerializedName("category_id")
            private String categoryId;
            @SerializedName("outlet_address")
            private String outletAddress;
            @SerializedName("outlet_id")
            private String outletId;
            @SerializedName("outlet_name")
            private String outletName;
            @SerializedName("outlet_logo")
            private String outletLogo;
            @SerializedName("category_logo")
            private String categoryLogo;
            @SerializedName("category_name")
            private String categoryName;
            @SerializedName("per_user")
            private String perUser;
            @Expose
            private String price;
            @Expose
            private String redeemed;
            @Expose
            private String isRedeeme;
            @Expose
            private String redemptions;
            @Expose
            private String renew;
            @SerializedName("rules_of_purchase")
            private String rulesOfPurchase;
            @SerializedName("SKU")
            private String sKU;
            @SerializedName("search_tags")
            private String searchTags;
            @Expose
            private String special;
            @SerializedName("special_price")
            private String specialPrice;
            @SerializedName("special_type")
            private String specialType;
            @SerializedName("start_datetime")
            private String startDatetime;
            @Expose
            private String title;
            @SerializedName("updated_at")
            private String updatedAt;
            @SerializedName("usage_allowance")
            private Object usageAllowance;
            @SerializedName("valid_for")
            private String validFor;
            @SerializedName("isfavourite")
            private String isfavourite;

            @SerializedName("can_send_gift")
            private boolean canSendGift;

            @SerializedName("discount_type")
            private String discountType;

            @SerializedName("percentage_saving")
            private String percentage_saving;

            public String getPercentage_saving() {
                return percentage_saving;
            }

            public void setPercentage_saving(String percentage_saving) {
                this.percentage_saving = percentage_saving;
            }

            public String getDiscountType() {
                return discountType;
            }

            public void setDiscountType(String discountType) {
                this.discountType = discountType;
            }

            public boolean isCanSendGift() {
                return canSendGift;
            }

            public void setCanSendGift(boolean canSendGift) {
                this.canSendGift = canSendGift;
            }

            public String getOutletLogo() {
                return outletLogo;
            }

            public void setOutletLogo(String outletLogo) {
                this.outletLogo = outletLogo;
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

            public String getActive() {
                return active;
            }

            public void setActive(String active) {
                this.active = active;
            }

            public String getApproxSaving() {
                return approxSaving;
            }

            public void setApproxSaving(String approxSaving) {
                this.approxSaving = approxSaving;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getEndDatetime() {
                return endDatetime;
            }

            public void setEndDatetime(String endDatetime) {
                this.endDatetime = endDatetime;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(String categoryId) {
                this.categoryId = categoryId;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public Object getInterestTags() {
                return interestTags;
            }

            public void setInterestTags(Object interestTags) {
                this.interestTags = interestTags;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getOrdersCount() {
                return ordersCount;
            }

            public void setOrdersCount(String ordersCount) {
                this.ordersCount = ordersCount;
            }

            public String getIsRedeeme() {
                return isRedeeme;
            }

            public void setIsRedeeme(String isRedeeme) {
                this.isRedeeme = isRedeeme;
            }

            public String getOutletAddress() {
                return outletAddress;
            }

            public void setOutletAddress(String outletAddress) {
                this.outletAddress = outletAddress;
            }

            public String getOutletId() {
                return outletId;
            }

            public void setOutletId(String outletId) {
                this.outletId = outletId;
            }

            public String getOutletName() {
                return outletName;
            }

            public void setOutletName(String outletName) {
                this.outletName = outletName;
            }

            public String getPerUser() {
                return perUser;
            }

            public void setPerUser(String perUser) {
                this.perUser = perUser;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getRedeemed() {
                return redeemed;
            }

            public void setRedeemed(String redeemed) {
                this.redeemed = redeemed;
            }

            public String getRedemptions() {
                return redemptions;
            }

            public void setRedemptions(String redemptions) {
                this.redemptions = redemptions;
            }

            public String getRenew() {
                return renew;
            }

            public void setRenew(String renew) {
                this.renew = renew;
            }

            public String getRulesOfPurchase() {
                return rulesOfPurchase;
            }

            public void setRulesOfPurchase(String rulesOfPurchase) {
                this.rulesOfPurchase = rulesOfPurchase;
            }

            public String getSKU() {
                return sKU;
            }

            public void setSKU(String sKU) {
                this.sKU = sKU;
            }

            public String getSearchTags() {
                return searchTags;
            }

            public void setSearchTags(String searchTags) {
                this.searchTags = searchTags;
            }

            public String getSpecial() {
                return special;
            }

            public void setSpecial(String special) {
                this.special = special;
            }

            public String getSpecialPrice() {
                return specialPrice;
            }

            public void setSpecialPrice(String specialPrice) {
                this.specialPrice = specialPrice;
            }

            public String getSpecialType() {
                return specialType;
            }

            public void setSpecialType(String specialType) {
                this.specialType = specialType;
            }

            public String getStartDatetime() {
                return startDatetime;
            }

            public void setStartDatetime(String startDatetime) {
                this.startDatetime = startDatetime;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public Object getUsageAllowance() {
                return usageAllowance;
            }

            public void setUsageAllowance(Object usageAllowance) {
                this.usageAllowance = usageAllowance;
            }

            public String getValidFor() {
                return validFor;
            }

            public void setValidFor(String validFor) {
                this.validFor = validFor;
            }

            public String getsKU() {
                return sKU;
            }

            public void setsKU(String sKU) {
                this.sKU = sKU;
            }

            public String getIsfavourite() {
                return isfavourite;
            }

            public void setIsfavourite(String isfavourite) {
                this.isfavourite = isfavourite;
            }
        }


    }


}
