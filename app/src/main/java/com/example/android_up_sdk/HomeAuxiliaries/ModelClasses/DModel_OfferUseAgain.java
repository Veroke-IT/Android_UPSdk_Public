package com.example.android_up_sdk.HomeAuxiliaries.ModelClasses;

public class DModel_OfferUseAgain {

    private String offerId;
    private String offerImage;
    private String categoryId;
    private String categoryName;
    private String categoryLogo;
    private String MerchantName;
    private String MerchantAddress;
    private String MerchantId;
    private String MerchantLogo;
    private String approxSaving;
    private String offerName;
    private String offerFreeDescription;
    private String personsDetail;
    private boolean isFavourite;
    private boolean canRedeem;
    private String expiryDate;
    private boolean canSendGift;
    private String discountType;
    private String approxSavingPercentage;

    public DModel_OfferUseAgain(String offerId, String offerImage, String categoryId, String categoryName, String categoryLogo, String merchantName, String merchantAddress, String merchantId, String merchantLogo, String approxSaving, String offerName, String offerFreeDescription, String personsDetail, boolean isFavourite, boolean canRedeem, String expiryDate, boolean canSendGift,String discountType,String approxSavingPercentage) {
        this.offerId = offerId;
        this.offerImage = offerImage;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryLogo = categoryLogo;
        MerchantName = merchantName;
        MerchantAddress = merchantAddress;
        MerchantId = merchantId;
        MerchantLogo = merchantLogo;
        this.approxSaving = approxSaving;
        this.offerName = offerName;
        this.offerFreeDescription = offerFreeDescription;
        this.personsDetail = personsDetail;
        this.isFavourite = isFavourite;
        this.canRedeem = canRedeem;
        this.expiryDate = expiryDate;
        this.canSendGift = canSendGift;
        this.discountType = discountType;
        this.approxSavingPercentage = approxSavingPercentage;
    }

    public String getOfferImage() {
        return offerImage;
    }

    public void setOfferImage(String offerImage) {
        this.offerImage = offerImage;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryLogo() {
        return categoryLogo;
    }

    public void setCategoryLogo(String categoryLogo) {
        this.categoryLogo = categoryLogo;
    }

    public String getMerchantName() {
        return MerchantName;
    }

    public void setMerchantName(String merchantName) {
        MerchantName = merchantName;
    }

    public String getMerchantAddress() {
        return MerchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        MerchantAddress = merchantAddress;
    }

    public String getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(String merchantId) {
        MerchantId = merchantId;
    }

    public String getMerchantLogo() {
        return MerchantLogo;
    }

    public void setMerchantLogo(String merchantLogo) {
        MerchantLogo = merchantLogo;
    }

    public String getApproxSaving() {
        return approxSaving;
    }

    public void setApproxSaving(String approxSaving) {
        this.approxSaving = approxSaving;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferFreeDescription() {
        return offerFreeDescription;
    }

    public void setOfferFreeDescription(String offerFreeDescription) {
        this.offerFreeDescription = offerFreeDescription;
    }

    public String getPersonsDetail() {
        return personsDetail;
    }

    public void setPersonsDetail(String personsDetail) {
        this.personsDetail = personsDetail;
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
}
