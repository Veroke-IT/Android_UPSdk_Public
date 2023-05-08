package com.example.android_up_sdk.HomeAuxiliaries.WebServices;

import android.content.Context;
import android.util.Log;

import com.example.android_up_sdk.Utils.ApiMethod;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.example.android_up_sdk.Utils.IWebCallback;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;



public class NewOffers_WebHit_Get_getOffers {
    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseObject;
    public Context mContext;

    public void requestNewOffers(Context _context, final IWebCallback iWebCallback, int _page) {


        this.mContext = _context;
        String myUrl = AppConfig.getInstance().getBaseUrlApiMobile() + ApiMethod.GET.getAllOfferUseAgain;
        RequestParams params = new RequestParams();
        params.put("page", _page);


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

        private ArrayList<Datum> data;

        public ArrayList<Datum> getData() {
            return this.data;
        }

        public void setData(ArrayList<Datum> data) {
            this.data = data;
        }

        public class Datum {
            private String id;

            public String getId() {
                return this.id;
            }

            public void setId(String id) {
                this.id = id;
            }


            private String category_id;

            public String getCategory_id() {
                return category_id;
            }

            public void setCategory_id(String category_id) {
                this.category_id = category_id;
            }

            private String category_logo;

            public String getCategory_logo() {
                return category_logo;
            }

            public void setCategory_logo(String category_logo) {
                this.category_logo = category_logo;
            }

            private String logo;

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            private String outlet_id;

            public String getOutletId() {
                return this.outlet_id;
            }

            public void setOutletId(String outlet_id) {
                this.outlet_id = outlet_id;
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

            private String valid_for;

            public String getValidFor() {
                return this.valid_for;
            }

            public void setValidFor(String valid_for) {
                this.valid_for = valid_for;
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

            private String outlet_name;

            public String getOutlet_name() {
                return outlet_name;
            }

            public void setOutlet_name(String outlet_name) {
                this.outlet_name = outlet_name;
            }

            private String outlet_address;

            public String getOutlet_address() {
                return outlet_address;
            }

            public void setOutlet_address(String outlet_address) {
                this.outlet_address = outlet_address;
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

            private Double distance;

            public double getDistance() {
                return this.distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }



            @SerializedName("can_send_gift")
            private boolean canSendGift;

            public boolean isCanSendGift() {
                return canSendGift;
            }

            public void setCanSendGift(boolean canSendGift) {
                this.canSendGift = canSendGift;
            }

            @SerializedName("discount_type")
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

        }
    }
}
