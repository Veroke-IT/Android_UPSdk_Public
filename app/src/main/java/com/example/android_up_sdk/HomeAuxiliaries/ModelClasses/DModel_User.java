package com.example.android_up_sdk.HomeAuxiliaries.ModelClasses;

public class DModel_User {

    public String authorizationToken;
    public boolean isNeverAskActive;
    public boolean isUberRequired;
    public String masterMerchant;
    public String upAccess;

    public DModel_User() {
        this.authorizationToken = "";
        this.isNeverAskActive = false;
        this.isUberRequired = false;
        this.masterMerchant = "";
        this.upAccess = "";
    }

    public String getUpAccess() {
        return upAccess;
    }

    public void setUpAccess(String upAccess) {
        this.upAccess = upAccess;
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }

    public boolean isNeverAskActive() {
        return isNeverAskActive;
    }

    public void setNeverAskActive(boolean neverAskActive) {
        isNeverAskActive = neverAskActive;
    }

    public boolean isUberRequired() {
        return isUberRequired;
    }

    public void setUberRequired(boolean uberRequired) {
        isUberRequired = uberRequired;
    }

    public String getMasterMerchant() {
        return masterMerchant;
    }

    public void setMasterMerchant(String masterMerchant) {
        this.masterMerchant = masterMerchant;
    }
}
