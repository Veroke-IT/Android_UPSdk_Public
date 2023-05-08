package com.example.android_up_sdk.HomeAuxiliaries.ModelClasses;

public class DModel_Recent_Outlet {

    String id;
    String merchantsLogoUrl;
    String merchantName;
    String offerName;

    public DModel_Recent_Outlet(String id, String merchantsLogoUrl, String merchantName, String offerName) {
        this.id = id;
        this.merchantsLogoUrl = merchantsLogoUrl;
        this.merchantName = merchantName;
        this.offerName = offerName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantsLogoUrl() {
        return merchantsLogoUrl;
    }

    public void setMerchantsLogoUrl(String merchantsLogoUrl) {
        this.merchantsLogoUrl = merchantsLogoUrl;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }
}
