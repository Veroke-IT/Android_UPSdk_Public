package com.example.android_up_sdk.HomeAuxiliaries.ModelClasses;

public class DModel_OfferDetail {
    private int offerId;
    private int groupPosition;
    private int childPosition;
    private String lstName;
    private String strSaveApprox;
    private String strOfferName;
    private String strOfferDescription;
    private String strCategoryIds;
    private String strMerchantName;
    private String strmerchantAddress;
    private String strMerchntId;
    private String strMerchntlogo;
    private String strDetailNExclusions;
    private boolean isFavourite;
    private boolean canRedeem;
    private String expiryDate;
    private boolean canSendGift;
    private String discountType;
    private String approxSavingPercentage;

    public DModel_OfferDetail(int offerId, int groupPosition, int childPosition, String lstName, String strSaveApprox, String strOfferName, String strOfferDescription, String strCategoryIds, String strMerchantName, String strmerchantAddress, String strMerchntId, String strMerchntlogo, String strDetailNExclusions, boolean isFavourite, boolean canRedeem, String expiryDate, boolean canSendGift,String discountType,String approxSavingPercentage) {
        this.offerId = offerId;
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;
        this.lstName = lstName;
        this.strSaveApprox = strSaveApprox;
        this.strOfferName = strOfferName;
        this.strOfferDescription = strOfferDescription;
        this.strCategoryIds = strCategoryIds;
        this.strMerchantName = strMerchantName;
        this.strmerchantAddress = strmerchantAddress;
        this.strMerchntId = strMerchntId;
        this.strMerchntlogo = strMerchntlogo;
        this.strDetailNExclusions = strDetailNExclusions;
        this.isFavourite = isFavourite;
        this.canRedeem = canRedeem;
        this.expiryDate = expiryDate;
        this.canSendGift = canSendGift;
        this.discountType = discountType;
        this.approxSavingPercentage = approxSavingPercentage;
    }

    public DModel_OfferDetail() {
        this.offerId = 0;
        this.groupPosition = 0;
        this.childPosition = 0;
        this.lstName = "";
        this.strSaveApprox = "";
        this.strOfferName = "";
        this.strOfferDescription = "";
        this.strCategoryIds = "";
        this.strMerchantName = "";
        this.strmerchantAddress = "";
        this.strMerchntlogo = "";
        this.strMerchntId = "";
        this.strDetailNExclusions = "";
        this.isFavourite = false;
        this.canRedeem = true;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getApproxSavingPercentage() {
        return approxSavingPercentage;
    }

    public void setApproxSavingPercentage(String approxSavingPercentage) {
        this.approxSavingPercentage = approxSavingPercentage;
    }

    public int getGroupPosition() {
        return groupPosition;
    }

    public void setGroupPosition(int groupPosition) {
        this.groupPosition = groupPosition;
    }

    public int getChildPosition() {
        return childPosition;
    }

    public void setChildPosition(int childPosition) {
        this.childPosition = childPosition;
    }

    public String getLstName() {
        return lstName;
    }

    public void setLstName(String lstName) {
        this.lstName = lstName;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public String getStrSaveApprox() {
        return strSaveApprox;
    }

    public void setStrSaveApprox(String strSaveApprox) {
        this.strSaveApprox = strSaveApprox;
    }

    public String getStrOfferName() {
        return strOfferName;
    }

    public void setStrOfferName(String strOfferName) {
        this.strOfferName = strOfferName;
    }

    public String getStrOfferDescription() {
        return strOfferDescription;
    }

    public void setStrOfferDescription(String strOfferDescription) {
        this.strOfferDescription = strOfferDescription;
    }

    public String getStrCategoryIds() {
        return strCategoryIds;
    }

    public void setStrCategoryIds(String strCategoryIds) {
        this.strCategoryIds = strCategoryIds;
    }

    public String getStrMerchantName() {
        return strMerchantName;
    }

    public void setStrMerchantName(String strMerchantName) {
        this.strMerchantName = strMerchantName;
    }

    public String getStrmerchantAddress() {
        return strmerchantAddress;
    }

    public void setStrmerchantAddress(String strmerchantAddress) {
        this.strmerchantAddress = strmerchantAddress;
    }

    public String getStrMerchntId() {
        return strMerchntId;
    }

    public void setStrMerchntId(String strMerchntId) {
        this.strMerchntId = strMerchntId;
    }

    public String getStrMerchntlogo() {
        return strMerchntlogo;
    }

    public void setStrMerchntlogo(String strMerchntlogo) {
        this.strMerchntlogo = strMerchntlogo;
    }

    public String getStrDetailNExclusions() {
        return strDetailNExclusions;
    }

    public void setStrDetailNExclusions(String strDetailNExclusions) {
        this.strDetailNExclusions = strDetailNExclusions;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public boolean isCanRedeem() {
        return canRedeem;
    }

    public void setCanRedeem(boolean canRedeem) {
        this.canRedeem = canRedeem;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }


    public boolean isCanSendGift() {
        return canSendGift;
    }

    public void setCanSendGift(boolean canSendGift) {
        this.canSendGift = canSendGift;
    }
}
