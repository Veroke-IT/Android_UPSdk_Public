package com.example.android_up_sdk.HomeAuxiliaries.ModelClasses;


import java.util.List;

public class DModelMerchintList {
    private String id;
    private String merchantName;
    private String merchantAddress;
    private String merchantTimmings;
    private String merchantDescription;
    private int merchantDistance;
    private boolean isDistanceRequired;
    private String festival;
    private String merchantsImageUrl;
    private String merchantsLogoUrl;
    private String merchantsPhone;
    private String strSpecial;
    private List<Child> child;



    public DModelMerchintList(String id, String merchantName, String merchantAddress, String merchantTimmings,
                              String merchantDescription, int merchantDistance, boolean isDistanceRequired, String special, String festival,
                              String merchantsImageUrl, String merchantsLogoUrl, String merchantsPhone, List<Child> child) {
        this.id = id;
        this.merchantName = merchantName;
        this.merchantAddress = merchantAddress;
        this.merchantTimmings = merchantTimmings;
        this.merchantDescription = merchantDescription;
        this.isDistanceRequired = isDistanceRequired;
        this.merchantDistance = merchantDistance;
        this.festival = festival;
        this.merchantsImageUrl = merchantsImageUrl;
        this.merchantsLogoUrl = merchantsLogoUrl;
        this.merchantsPhone = merchantsPhone;
        this.child = child;
        this.strSpecial = special;
    }

    public static class Child {
        private String ProductId;
        private String ImgUrl;
        private String Name;
        private String Description;
        private String Festival;
        private String Status;
        private String Thumbnail;
        private String special;
        private String detailNExclusions;
        private String approxSavings;
        private String categoryId;
        private String outletName;
        private boolean isFavorite;
        private boolean canRedeem;
        String renew;
        String renewDate;
        private String expiryDate;
        private boolean canSendGift;
        private String discountType;
        private String approxSavingPercentage;


        public Child(String productId, String imgUrl, String name, String description, String festival, String status, String thumbnail, String special, String detailNExclusions, String approxSavings, String categoryId, String outletName, boolean isFavorite, boolean canRedeem, String renew, String renewDate, String expiryDate, boolean canSendGift,String discountType,String approxSavingPercentage) {
            ProductId = productId;
            ImgUrl = imgUrl;
            Name = name;
            Description = description;
            Festival = festival;
            Status = status;
            Thumbnail = thumbnail;
            this.special = special;
            this.detailNExclusions = detailNExclusions;
            this.approxSavings = approxSavings;
            this.categoryId = categoryId;
            this.outletName = outletName;
            this.isFavorite = isFavorite;
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

        public boolean isCanSendGift() {
            return canSendGift;
        }

        public void setCanSendGift(boolean canSendGift) {
            this.canSendGift = canSendGift;
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

        public String getProductId() {
            return ProductId;
        }

        public void setProductId(String productId) {
            ProductId = productId;
        }

        public String getImgUrl() {
            return ImgUrl;
        }

        public void setImgUrl(String imgUrl) {
            ImgUrl = imgUrl;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public boolean isCanRedeem() {
            return canRedeem;
        }

        public void setCanRedeem(boolean canRedeem) {
            this.canRedeem = canRedeem;
        }

        public void setSpecial(String special) {
            this.special = special;
        }

        public String getSpecial() {
            return special;
        }

        public String getFestival() {
            return Festival;
        }

        public void setFestival(String festival) {
            Festival = festival;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getThumbnail() {
            return Thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            Thumbnail = thumbnail;
        }

        public String getDetailNExclusions() {
            return detailNExclusions;
        }

        public void setDetailNExclusions(String detailNExclusions) {
            this.detailNExclusions = detailNExclusions;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getOutletName() {
            return outletName;
        }

        public void setOutletName(String outletName) {
            this.outletName = outletName;
        }

        public String getApproxSavings() {
            return approxSavings;
        }

        public void setApproxSavings(String approxSavings) {
            this.approxSavings = approxSavings;
        }

        public boolean isFavorite() {
            return isFavorite;
        }

        public void setFavorite(boolean favorite) {
            isFavorite = favorite;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }
    }

    public List<Child> getChild() {
        return child;
    }

    public void setChild(List<Child> child) {
        this.child = child;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public void setStrSpecial(String strSpecial) {
        this.strSpecial = strSpecial;
    }

    public String getStrSpecial() {
        return strSpecial;
    }

    public String getMerchantTimmings() {
        return merchantTimmings;
    }

    public void setMerchantTimmings(String merchantTimmings) {
        this.merchantTimmings = merchantTimmings;
    }

    public String getMerchantDescription() {
        return merchantDescription;
    }

    public void setMerchantDescription(String merchantDescription) {
        this.merchantDescription = merchantDescription;
    }

    public boolean isDistanceRequired() {
        return isDistanceRequired;
    }

    public void setDistanceRequired(boolean distanceRequired) {
        isDistanceRequired = distanceRequired;
    }

    public int getMerchantDistance() {
        return merchantDistance;
    }

    public void setMerchantDistance(int merchantDistance) {
        this.merchantDistance = merchantDistance;
    }

    public String getFestival() {
        return festival;
    }

    public void setFestival(String festival) {
        this.festival = festival;
    }

    public String getMerchantsImageUrl() {
        return merchantsImageUrl;
    }

    public void setMerchantsImageUrl(String merchantsImageUrl) {
        this.merchantsImageUrl = merchantsImageUrl;
    }

    public String getMerchantsLogoUrl() {
        return merchantsLogoUrl;
    }

    public void setMerchantsLogoUrl(String merchantsLogoUrl) {
        this.merchantsLogoUrl = merchantsLogoUrl;
    }

    public String getMerchantsPhone() {
        return merchantsPhone;
    }

    public void setMerchantsPhone(String merchantsPhone) {
        this.merchantsPhone = merchantsPhone;
    }


}
