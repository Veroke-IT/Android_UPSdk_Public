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

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class NewBrands_WebHit_Get_getAllNewBrands {
    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseObject;
    public Context mContext;

    public void requestNewBrands(Context _context, final IWebCallback iWebCallback, int _page, int _categoryId, String _sortBy, double _lat,
                                 double _lng) {

        this.mContext = _context;
        String myUrl = AppConfig.getInstance().getBaseUrlApiMobile() + ApiMethod.GET.getAllNewBrands;
        RequestParams params = new RequestParams();
        params.put("page", _page);
        if (_categoryId > 0) {
            params.put("category_id", _categoryId);
        }

        if (_lat > 0 && _sortBy.equalsIgnoreCase(AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION)) {
            params.put("latitude", _lat);
            params.put("longitude", _lng);
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
        private List<Data> data;
        private String message;
        private String status;

        public void setData(List<Data> data) {
            this.data = data;
        }

        public List<Data> getData() {
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



        public class Data {
            private String updatedAt;
            private String distance;
            private String name;
            private String createdAt;
            private String logo;
            private String id;
            private int total_outlets;
            private List<Outlet> outlets;


            public int getTotal_outlets() {
                return total_outlets;
            }

            public void setTotal_outlets(int total_outlets) {
                this.total_outlets = total_outlets;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getDistance() {
                return distance;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getLogo() {
                return logo;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return id;
            }

            public void setOutlets(List<Outlet> outlet) {
                this.outlets = outlet;
            }

            public List<Outlet> getOutlets() {
                return outlets;
            }
        }

        public class Outlet {
            private List<OffersItem> offers;
            private String image;
            private String address;
            private String latitude;
            private String parentsId;
            private String phones;
            private String description;
            private String active;
            private String createdAt;
            private String merchantId;
            private String searchTags;
            private String type;
            private String emails;
            private String special;
            private String pin;
            private String updatedAt;
            private String phone;
            private String name;
            private String timings;
            private String logo;
            private String id;
            private String neighborhood;
            private String sKU;
            private String longitude;
            private int category_id;

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

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setParentsId(String parentsId) {
                this.parentsId = parentsId;
            }

            public String getParentsId() {
                return parentsId;
            }

            public void setPhones(String phones) {
                this.phones = phones;
            }

            public String getPhones() {
                return phones;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getDescription() {
                return description;
            }

            public void setActive(String active) {
                this.active = active;
            }

            public String getActive() {
                return active;
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

            public void setSpecial(String special) {
                this.special = special;
            }

            public String getSpecial() {
                return special;
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

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return id;
            }

            public void setNeighborhood(String neighborhood) {
                this.neighborhood = neighborhood;
            }

            public String getNeighborhood() {
                return neighborhood;
            }

            public void setSKU(String sKU) {
                this.sKU = sKU;
            }

            public String getSKU() {
                return sKU;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getLongitude() {
                return longitude;
            }

            public int getCategory_id() {
                return category_id;
            }

            public void setCategory_id(int category_id) {
                this.category_id = category_id;
            }
        }

        public class OffersItem {
            private String rulesOfPurchase;
            private String interestTags;
            private String startDatetime;
            private String endDatetime;
            private String redeemed;
            private boolean isRedeeme;
            private String description;
            private String createdAt;
            private String searchTags;
            private String title;
            private String updatedAt;
            private String price;
            private String id;
            private String valid_for;
            private String special_type;
            private String image;
            private String active;
            private String approx_saving;
            private String discount_type;
            private String special;
            private String ordersCount;
            private String outletId;
            private String specialPrice;
            private String redemptions;
            private String renew;
            private String sKU;
            private String perUser;
            private String category_ids;
            private String outlet_name;
            private String renewDate;
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

            public void setRulesOfPurchase(String rulesOfPurchase) {
                this.rulesOfPurchase = rulesOfPurchase;
            }

            public String getRulesOfPurchase() {
                return rulesOfPurchase;
            }

            public void setInterestTags(String interestTags) {
                this.interestTags = interestTags;
            }

            public String getInterestTags() {
                return interestTags;
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

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitle() {
                return title;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public String getUpdatedAt() {
                return updatedAt;
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

            public void setValidFor(String validFor) {
                this.valid_for = validFor;
            }

            public String getValidFor() {
                return valid_for;
            }

            public void setSpecialType(String specialType) {
                this.special_type = specialType;
            }

            public String getSpecialType() {
                return special_type;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getImage() {
                return image;
            }

            public void setActive(String active) {
                this.active = active;
            }

            public String getActive() {
                return active;
            }

            public void setApproxSaving(String approxSaving) {
                this.approx_saving = approxSaving;
            }

            public String getApproxSaving() {
                return approx_saving;
            }

            public void setSpecial(String special) {
                this.special = special;
            }

            public String getSpecial() {
                return special;
            }

            public void setOrdersCount(String ordersCount) {
                this.ordersCount = ordersCount;
            }

            public String getOrdersCount() {
                return ordersCount;
            }

            public void setOutletId(String outletId) {
                this.outletId = outletId;
            }

            public String getOutletId() {
                return outletId;
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

            public void setRenew(String renew) {
                this.renew = renew;
            }

            public String getRenew() {
                return renew;
            }

            public String getRenewDate() {
                return this.renewDate;
            }

            public void setRenewDate(String renewDate) {
                this.renewDate = renewDate;
            }

            public void setSKU(String sKU) {
                this.sKU = sKU;
            }

            public String getSKU() {
                return sKU;
            }

            public void setPerUser(String perUser) {
                this.perUser = perUser;
            }

            public String getPerUser() {
                return perUser;
            }

            private String isfavourite;

            public String getIsfavourite() {
                return this.isfavourite;
            }

            public void setIsfavourite(String isfavourite) {
                this.isfavourite = isfavourite;
            }

            public String getCategory_ids() {
                return category_ids;
            }

            public void setCategory_ids(String category_ids) {
                this.category_ids = category_ids;
            }

            public String getOutlet_name() {
                return outlet_name;
            }

            public void setOutlet_name(String outlet_name) {
                this.outlet_name = outlet_name;
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

    }


}

