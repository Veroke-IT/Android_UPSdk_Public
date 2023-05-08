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

public class NearbyOutlets_WebHit_Get_getOutletsNew {
    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseObject;
    public Context mContext;

    public void requestNearbyOutlets(Context _context, final IWebCallback iWebCallback, int _page, String _sortBy, double _lat,
                                     double _lng) {

        this.mContext = _context;
        String myUrl = AppConfig.getInstance().getBaseUrlApiMobile() + ApiMethod.GET.getOutletsNew;
        RequestParams params = new RequestParams();
        params.put("page", _page);
        params.put("sort_order", "ASC");



        if (_lat > 0 && _sortBy.equalsIgnoreCase(AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION)) {
            params.put("latitude", _lat);
            params.put("longitude", _lng);
        }else {
            params.put("sort_by", "name");
        }

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
        private String status;

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        private String message;

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private List<Datum> data;

        public List<Datum> getData() {
            return this.data;
        }

        public void setData(List<Datum> data) {
            this.data = data;
        }

        public class Offer {
            private String id;

            public String getId() {
                return this.id;
            }

            public void setId(String id) {
                this.id = id;
            }

            private String valid_for;

            public String getValidFor() {
                return valid_for;
            }

            public void setValidFor(String valid_for) {
                this.valid_for = valid_for;
            }

            private String outlet_id;

            public String getOutletId() {
                return this.outlet_id;
            }

            public void setOutletId(String outlet_id) {
                this.outlet_id = outlet_id;
            }

            private String category_ids;

            public String getCategory_ids() {
                return category_ids;
            }

            public void setCategory_ids(String category_ids) {
                this.category_ids = category_ids;
            }

            private String outlet_name;

            public String getOutlet_name() {
                return outlet_name;
            }

            public void setOutlet_name(String outlet_name) {
                this.outlet_name = outlet_name;
            }

            private String title;

            public String getTitle() {
                return this.title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            private String image;

            public String getImage() {
                return this.image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            private String SKU;

            public String getSKU() {
                return this.SKU;
            }

            public void setSKU(String SKU) {
                this.SKU = SKU;
            }

            private String search_tags;

            public String getSearchTags() {
                return this.search_tags;
            }

            public void setSearchTags(String search_tags) {
                this.search_tags = search_tags;
            }

            private String price;

            public String getPrice() {
                return this.price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            private String special_price;

            public String getSpecialPrice() {
                return this.special_price;
            }

            public void setSpecialPrice(String special_price) {
                this.special_price = special_price;
            }

            private String approx_saving;

            public String getApproxSaving() {
                return this.approx_saving;
            }

            public void setApproxSaving(String approx_saving) {
                this.approx_saving = approx_saving;
            }

            private String discount_type;

            public String getDiscount_type() {
                return discount_type;
            }

            public void setDiscount_type(String discount_type) {
                this.discount_type = discount_type;
            }

            private String percentage_saving;

            public String getPercentage_saving() {
                return percentage_saving;
            }

            public void setPercentage_saving(String percentage_saving) {
                this.percentage_saving = percentage_saving;
            }

            private String start_datetime;

            public String getStartDatetime() {
                return this.start_datetime;
            }

            public void setStartDatetime(String start_datetime) {
                this.start_datetime = start_datetime;
            }

            private String end_datetime;

            public String getEndDatetime() {
                return this.end_datetime;
            }

            public void setEndDatetime(String end_datetime) {
                this.end_datetime = end_datetime;
            }

            private String description;

            public String getDescription() {
                return this.description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            private String special;

            public String getSpecial() {
                return this.special;
            }

            public void setSpecial(String special) {
                this.special = special;
            }

            private String special_type;

            public String getSpecialType() {
                return this.special_type;
            }

            public void setSpecialType(String special_type) {
                this.special_type = special_type;
            }

            private String renew;

            public String getRenew() {
                return this.renew;
            }

            public void setRenew(String renew) {
                this.renew = renew;
            }

            private String renewDate;

            public String getRenewDate() {
                return this.renewDate;
            }

            public void setRenewDate(String renewDate) {
                this.renewDate = renewDate;
            }

            private String redemptions;

            public String getRedemptions() {
                return this.redemptions;
            }

            public void setRedemptions(String redemptions) {
                this.redemptions = redemptions;
            }

            private String redeemed;

            private boolean isRedeeme;

            public boolean getIsRedeeme() {
                return isRedeeme;
            }

            public void setIsRedeeme(boolean isRedeeme) {
                this.isRedeeme = isRedeeme;
            }

            public String getRedeemed() {
                return this.redeemed;
            }

            public void setRedeemed(String redeemed) {
                this.redeemed = redeemed;
            }

            private String per_user;

            public String getPerUser() {
                return this.per_user;
            }

            public void setPerUser(String per_user) {
                this.per_user = per_user;
            }

            private String active;

            public String getActive() {
                return this.active;
            }

            public void setActive(String active) {
                this.active = active;
            }

            private String created_at;

            public String getCreatedAt() {
                return this.created_at;
            }

            public void setCreatedAt(String created_at) {
                this.created_at = created_at;
            }

            private String updated_at;

            public String getUpdatedAt() {
                return this.updated_at;
            }

            public void setUpdatedAt(String updated_at) {
                this.updated_at = updated_at;
            }

            private boolean isfavourite;

            public boolean getIsfavourite() {
                return this.isfavourite;
            }

            public void setIsfavourite(boolean isfavourite) {
                this.isfavourite = isfavourite;
            }

            @SerializedName("can_send_gift")
            private boolean canSendGift;

            public boolean isCanSendGift() {
                return canSendGift;
            }

            public void setCanSendGift(boolean canSendGift) {
                this.canSendGift = canSendGift;
            }
        }

        public class Datum {
            private String id;

            public String getId() {
                return this.id;
            }

            public void setId(String id) {
                this.id = id;
            }

            private String merchant_id;

            public String getMerchantId() {
                return this.merchant_id;
            }

            public void setMerchantId(String merchant_id) {
                this.merchant_id = merchant_id;
            }

            private String name;

            public String getName() {
                return this.name;
            }

            public void setName(String name) {
                this.name = name;
            }

            private String phone;

            public String getPhone() {
                return this.phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            private String phones;

            public String getPhones() {
                return this.phones;
            }

            public void setPhones(String phones) {
                this.phones = phones;
            }

            private String pin;

            public String getPin() {
                return this.pin;
            }

            public void setPin(String pin) {
                this.pin = pin;
            }

            private String search_tags;

            public String getSearchTags() {
                return this.search_tags;
            }

            public void setSearchTags(String search_tags) {
                this.search_tags = search_tags;
            }

            private String logo;

            public String getLogo() {
                return this.logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            private String image;

            public String getImage() {
                return this.image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            private String address;

            public String getAddress() {
                return this.address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            private String latitude;

            public String getLatitude() {
                return this.latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            private String longitude;

            public String getLongitude() {
                return this.longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            private String neighborhood;

            public String getNeighborhood() {
                return this.neighborhood;
            }

            public void setNeighborhood(String neighborhood) {
                this.neighborhood = neighborhood;
            }

            private String timings;

            public String getTimings() {
                return this.timings;
            }

            public void setTimings(String timings) {
                this.timings = timings;
            }

            private String description;

            public String getDescription() {
                return this.description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            private String type;

            public String getType() {
                return this.type;
            }

            public void setType(String type) {
                this.type = type;
            }

            private String special;

            public String getSpecial() {
                return this.special;
            }

            public void setSpecial(String special) {
                this.special = special;
            }

            private String active;

            public String getActive() {
                return this.active;
            }

            public void setActive(String active) {
                this.active = active;
            }

            private String created_at;

            public String getCreatedAt() {
                return this.created_at;
            }

            public void setCreatedAt(String created_at) {
                this.created_at = created_at;
            }

            private String updated_at;

            public String getUpdatedAt() {
                return this.updated_at;
            }

            public void setUpdatedAt(String updated_at) {
                this.updated_at = updated_at;
            }

            private double distance;

            public double getDistance() {
                return this.distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            private String outletTiming;

            public String getOutletTiming() {
                return this.outletTiming;
            }

            public void setOutletTiming(String outletTiming) {
                this.outletTiming = outletTiming;
            }

            private List<Offer> offers;

            public List<Offer> getOffers() {
                return this.offers;
            }

            public void setOffers(List<Offer> offers) {
                this.offers = offers;
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


        }
    }
}
