package com.example.android_up_sdk.HomeAuxiliaries.WebServices;

import android.content.Context;

import com.example.android_up_sdk.Utils.ApiMethod;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.example.android_up_sdk.Utils.IWebCallback;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MerchantDetail_WebHit_Get_getMerchantDetail {
    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseObject;
    public Context mContext;

    public void requestOfferDetail(Context _context, final IWebCallback iWebCallback, int _outletId) {

        String myUrl = AppConfig.getInstance().getBaseUrlApiMobile() + ApiMethod.GET.getOutlets;
        this.mContext = _context;
        RequestParams params = new RequestParams();
        params.put("outlet_id", _outletId);

        mClient.addHeader(ApiMethod.HEADER.Authorization, AppConfig.getInstance().mUser.getAuthorizationToken());
        mClient.addHeader("app_id", "1");
        mClient.setMaxRetriesAndTimeout(AppConstt.LIMIT_API_RETRY, AppConstt.LIMIT_TIMEOUT_MILLIS);
        mClient.setTimeout(AppConstt.LIMIT_TIMEOUT_MILLIS);
        mClient.get(myUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");
                            responseObject = gson.fromJson(strResponse, ResponseModel.class);
                            switch (statusCode) {
                                case AppConstt.ServerStatus.OK:
                                    iWebCallback.onWebResult(true, responseObject.getMessage());
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

                            case AppConstt.ServerStatus.DATABASE_NOT_FOUND:
                                iWebCallback.onWebResult(false, AppConstt.ServerStatus.DATABASE_NOT_FOUND + "");
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
        @SerializedName("data")
        private List<DataItem> data;

        @SerializedName("message")
        private String message;

        @SerializedName("status")
        private String status;

        public void setData(List<DataItem> data) {
            this.data = data;
        }

        public List<DataItem> getData() {
            return data;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public class OffersItem {

            @SerializedName("special_type")
            private String specialType;

            @SerializedName("image")
            private String image;

            @SerializedName("rules_of_purchase")
            private String rulesOfPurchase;

            @SerializedName("start_datetime")
            private String startDatetime;

            @SerializedName("end_datetime")
            private String endDatetime;

            @SerializedName("redeemed")
            private String redeemed;

            @SerializedName("isRedeeme")
            private boolean isRedeeme;

            @SerializedName("can_send_gift")
            private boolean canSendGift;

            @SerializedName("active")
            private String active;

            @SerializedName("description")
            private String description;

            @SerializedName("created_at")
            private String createdAt;

            @SerializedName("search_tags")
            private String searchTags;

            @SerializedName("approx_saving")
            private String approxSaving;

            @SerializedName("title")
            private String title;

            @SerializedName("isfavourite")
            private String isfavourite;

            @SerializedName("special")
            private String special;

            @SerializedName("outlet_id")
            private String outletId;

            @SerializedName("updated_at")
            private String updatedAt;

            @SerializedName("special_price")
            private String specialPrice;

            @SerializedName("redemptions")
            private String redemptions;

            @SerializedName("price")
            private String price;

            @SerializedName("id")
            private String id;

            @SerializedName("renew")
            private String renew;


            @SerializedName("renewDate")
            private String renewDate;

            @SerializedName("SKU")
            private String sKU;

            @SerializedName("valid_for")
            private String validFor;

            @SerializedName("category_ids")
            private String categoryIds;
            @SerializedName("outlet_name")
            private String outletName;

            @SerializedName("per_user")
            private String perUser;

            @SerializedName("discount_type")
            private String discount_type;

            @SerializedName("percentage_saving")
            private String percentage_saving;

            public String getPercentage_saving() {
                return percentage_saving;
            }

            public void setPercentage_saving(String percentage_saving) {
                this.percentage_saving = percentage_saving;
            }

            public String getDiscount_type() {
                return discount_type;
            }

            public void setDiscount_type(String discount_type) {
                this.discount_type = discount_type;
            }

            public String getCategoryIds() {
                return categoryIds;
            }

            public void setCategoryIds(String categoryIds) {
                this.categoryIds = categoryIds;
            }

            public String getOutletName() {
                return outletName;
            }

            public void setOutletName(String outletName) {
                this.outletName = outletName;
            }

            public void setSpecialType(String specialType) {
                this.specialType = specialType;
            }

            public String getSpecialType() {
                return specialType;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getImage() {
                return image;
            }

            public void setRulesOfPurchase(String rulesOfPurchase) {
                this.rulesOfPurchase = rulesOfPurchase;
            }

            public String getRulesOfPurchase() {
                return rulesOfPurchase;
            }

            public void setStartDatetime(String startDatetime) {
                this.startDatetime = startDatetime;
            }

            public String getStartDatetime() {
                return startDatetime;
            }

            public void setEndDatetime(String endDatetime) {
                this.endDatetime = endDatetime;
            }

            public String getEndDatetime() {
                return endDatetime;
            }

            public void setRedeemed(String redeemed) {
                this.redeemed = redeemed;
            }

            public String getRedeemed() {
                return redeemed;
            }

            public boolean isRedeeme() {
                return isRedeeme;
            }

            public void setRedeeme(boolean redeeme) {
                isRedeeme = redeeme;
            }

            public void setActive(String active) {
                this.active = active;
            }

            public String getActive() {
                return active;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getDescription() {
                return description;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setSearchTags(String searchTags) {
                this.searchTags = searchTags;
            }

            public String getSearchTags() {
                return searchTags;
            }

            public void setApproxSaving(String approxSaving) {
                this.approxSaving = approxSaving;
            }

            public String getApproxSaving() {
                return approxSaving;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitle() {
                return title;
            }

            public void setIsfavourite(String isfavourite) {
                this.isfavourite = isfavourite;
            }

            public String getIsfavourite() {
                return isfavourite;
            }

            public void setSpecial(String special) {
                this.special = special;
            }

            public String getSpecial() {
                return special;
            }

            public void setOutletId(String outletId) {
                this.outletId = outletId;
            }

            public String getOutletId() {
                return outletId;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setSpecialPrice(String specialPrice) {
                this.specialPrice = specialPrice;
            }

            public String getSpecialPrice() {
                return specialPrice;
            }

            public void setRedemptions(String redemptions) {
                this.redemptions = redemptions;
            }

            public String getRedemptions() {
                return redemptions;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getPrice() {
                return price;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return id;
            }

            public void setRenew(String renew) {
                this.renew = renew;
            }

            public String getRenew() {
                return renew;
            }

            public void setSKU(String sKU) {
                this.sKU = sKU;
            }

            public String getSKU() {
                return sKU;
            }

            public void setValidFor(String validFor) {
                this.validFor = validFor;
            }

            public String getValidFor() {
                return validFor;
            }

            public void setPerUser(String perUser) {
                this.perUser = perUser;
            }

            public String getPerUser() {
                return perUser;
            }

            public String getRenewDate() {
                return renewDate;
            }

            public void setRenewDate(String renewDate) {
                this.renewDate = renewDate;
            }

            public boolean isCanSendGift() {
                return canSendGift;
            }

            public void setCanSendGift(boolean canSendGift) {
                this.canSendGift = canSendGift;
            }
        }

        public class DataItem {


            public class Outlet_images
            {
                private String id;

                private String file;

                private String outlet_id;

                private String orderBy;

                private String type;

                public void setId(String id){
                    this.id = id;
                }
                public String getId(){
                    return this.id;
                }
                public void setFile(String file){
                    this.file = file;
                }
                public String getFile(){
                    return this.file;
                }
                public void setOutlet_id(String outlet_id){
                    this.outlet_id = outlet_id;
                }
                public String getOutlet_id(){
                    return this.outlet_id;
                }
                public void setOrderBy(String orderBy){
                    this.orderBy = orderBy;
                }
                public String getOrderBy(){
                    return this.orderBy;
                }
                public void setType(String type){
                    this.type = type;
                }
                public String getType(){
                    return this.type;
                }
            }

            public class Outlet_menu {
                private String id;

                private String outlet_id;

                private String file;

                private String orderBy;

                private String type;

                public void setId(String id) {
                    this.id = id;
                }

                public String getId() {
                    return this.id;
                }

                public void setOutlet_id(String outlet_id) {
                    this.outlet_id = outlet_id;
                }

                public String getOutlet_id() {
                    return this.outlet_id;
                }

                public void setFile(String file) {
                    this.file = file;
                }

                public String getFile() {
                    return this.file;
                }

                public void setOrderBy(String orderBy) {
                    this.orderBy = orderBy;
                }

                public String getOrderBy() {
                    return this.orderBy;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getType() {
                    return this.type;
                }
            }

            @SerializedName("outlet_menu")
            private List<Outlet_menu> outlet_menu;

            public void setOutlet_menu(List<Outlet_menu> outlet_menu) {
                this.outlet_menu = outlet_menu;
            }

            public List<Outlet_menu> getOutlet_menu() {
                return this.outlet_menu;
            }

            @SerializedName("outlet_images")

            private List<Outlet_images> outlet_images;

            public void setOutlet_images(List<Outlet_images> outlet_images){
                this.outlet_images = outlet_images;
            }
            public List<Outlet_images> getOutlet_images(){
                return this.outlet_images;
            }

            @SerializedName("menu_card")
            private String menuCard;

            @SerializedName("distance")
            private int distance;

            @SerializedName("latitude")
            private String latitude;

            @SerializedName("phones")
            private Object phones;

            @SerializedName("description")
            private String description;

            @SerializedName("created_at")
            private String createdAt;

            @SerializedName("merchant_id")
            private String merchantId;

            @SerializedName("search_tags")
            private String searchTags;

            @SerializedName("type")
            private String type;

            @SerializedName("emails")
            private String emails;

            @SerializedName("pin")
            private String pin;

            @SerializedName("updated_at")
            private String updatedAt;

            @SerializedName("timings")
            private String timings;

            @SerializedName("logo")
            private String logo;
            @SerializedName("location_image")
            private String locationImage;

            @SerializedName("outletTiming")
            private String outletTiming;

            @SerializedName("id")
            private String id;

            @SerializedName("longitude")
            private String longitude;

            @SerializedName("offers")
            private List<OffersItem> offers;

            @SerializedName("image")
            private String image;

            @SerializedName("address")
            private String address;

            @SerializedName("active")
            private String active;

            @SerializedName("special")
            private String special;

            @SerializedName("phone")
            private String phone;

            @SerializedName("name")
            private String name;

            @SerializedName("neighborhood")
            private Object neighborhood;

            @SerializedName("SKU")
            private String sKU;


            public String getLocationImage() {
                return locationImage;
            }

            public void setLocationImage(String locationImage) {
                this.locationImage = locationImage;
            }

            public String getsKU() {
                return sKU;
            }

            public void setsKU(String sKU) {
                this.sKU = sKU;
            }

            public void setDistance(int distance) {
                this.distance = distance;
            }

            public int getDistance() {
                return distance;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setPhones(Object phones) {
                this.phones = phones;
            }

            public Object getPhones() {
                return phones;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getDescription() {
                return description;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setMerchantId(String merchantId) {
                this.merchantId = merchantId;
            }

            public String getMerchantId() {
                return merchantId;
            }

            public void setSearchTags(String searchTags) {
                this.searchTags = searchTags;
            }

            public String getSearchTags() {
                return searchTags;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getType() {
                return type;
            }

            public void setEmails(String emails) {
                this.emails = emails;
            }

            public String getEmails() {
                return emails;
            }

            public void setPin(String pin) {
                this.pin = pin;
            }

            public String getPin() {
                return pin;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setTimings(String timings) {
                this.timings = timings;
            }

            public String getTimings() {
                return timings;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getLogo() {
                return logo;
            }

            public void setOutletTiming(String outletTiming) {
                this.outletTiming = outletTiming;
            }

            public String getOutletTiming() {
                return outletTiming;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return id;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setOffers(List<OffersItem> offers) {
                this.offers = offers;
            }

            public List<OffersItem> getOffers() {
                return offers;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getImage() {
                return image;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getAddress() {
                return address;
            }

            public void setActive(String active) {
                this.active = active;
            }

            public String getActive() {
                return active;
            }

            public void setSpecial(String special) {
                this.special = special;
            }

            public String getSpecial() {
                return special;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getPhone() {
                return phone;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public void setNeighborhood(Object neighborhood) {
                this.neighborhood = neighborhood;
            }

            public Object getNeighborhood() {
                return neighborhood;
            }

            public void setSKU(String sKU) {
                this.sKU = sKU;
            }

            public String getSKU() {
                return sKU;
            }

            public String getMenuCard() {
                return menuCard;
            }

            public void setMenuCard(String menuCard) {
                this.menuCard = menuCard;
            }
        }

    }

}