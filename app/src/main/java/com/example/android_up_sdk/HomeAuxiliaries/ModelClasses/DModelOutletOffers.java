package com.example.android_up_sdk.HomeAuxiliaries.ModelClasses;

public class DModelOutletOffers {

    String id;
    String image;
    String offerName;
    String offerDescription;
    String merchantName;
    String merchantAddress;
    String merchantId;
    String merchantLogo;
    String categoryId;
    String categoryLogo;
    double merchantDistance;
    String special;
    String festival;
    String detailNEclusion;
    String approxSavings;
    boolean isDistanceRequired;
    boolean isFav;
    boolean canRedeem;
    String renew;
    String renewDate;
    private String expiryDate;
    private boolean canSendGift;
    private String discountType;
    private String approxSavingPercentage;

    public DModelOutletOffers(String id, String image, String offerName, String offerDescription, String merchantName, String merchantAddress, String merchantId, String merchantLogo, String categoryId, String categoryLogo, double merchantDistance, String special, String festival, String detailNEclusion, String approxSavings, boolean isDistanceRequired, boolean isFav, boolean canRedeem, String renew, String renewDate, String expiryDate, boolean canSendGift,String discountType,String approxSavingPercentage) {
        this.id = id;
        this.image = image;
        this.offerName = offerName;
        this.offerDescription = offerDescription;
        this.merchantName = merchantName;
        this.merchantAddress = merchantAddress;
        this.merchantId = merchantId;
        this.merchantLogo = merchantLogo;
        this.categoryId = categoryId;
        this.categoryLogo = categoryLogo;
        this.merchantDistance = merchantDistance;
        this.special = special;
        this.festival = festival;
        this.detailNEclusion = detailNEclusion;
        this.approxSavings = approxSavings;
        this.isDistanceRequired = isDistanceRequired;
        this.isFav = isFav;
        this.canRedeem = canRedeem;
        this.renew = renew;
        this.renewDate = renewDate;
        this.expiryDate = expiryDate;
        this.canSendGift = canSendGift;
        this.discountType = discountType;
        this.approxSavingPercentage = approxSavingPercentage;
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

    public String getRenewDate() {
        return renewDate;
    }

    public void setRenewDate(String renewDate) {
        this.renewDate = renewDate;
    }

    public String getRenew() {
        return renew;
    }

    public void setRenew(String renew) {
        this.renew = renew;
    }

    public String getCategoryLogo() {
        return categoryLogo;
    }

    public void setCategoryLogo(String categoryLogo) {
        this.categoryLogo = categoryLogo;
    }

    public String getApproxSavings() {
        return approxSavings;
    }

    public void setApproxSavings(String approxSavings) {
        this.approxSavings = approxSavings;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public boolean isDistanceRequired() {
        return isDistanceRequired;
    }

    public void setDistanceRequired(boolean distanceRequired) {
        isDistanceRequired = distanceRequired;
    }

    public void setMerchantDistance(double merchantDistance) {
        this.merchantDistance = merchantDistance;
    }

    public double getMerchantDistance() {
        return merchantDistance;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getSpecial() {
        return special;
    }

    public String getMerchantLogo() {
        return merchantLogo;
    }

    public void setMerchantLogo(String merchantLogo) {
        this.merchantLogo = merchantLogo;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String getDetailNEclusion() {
        return detailNEclusion;
    }

    public void setDetailNEclusion(String detailNEclusion) {
        this.detailNEclusion = detailNEclusion;
    }

    public boolean isCanRedeem() {
        return canRedeem;
    }

    public void setCanRedeem(boolean canRedeem) {
        this.canRedeem = canRedeem;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public String getFestival() {
        return festival;
    }

    public void setFestival(String festival) {
        this.festival = festival;
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
