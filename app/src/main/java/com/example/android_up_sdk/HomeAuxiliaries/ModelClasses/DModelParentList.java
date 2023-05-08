package com.example.android_up_sdk.HomeAuxiliaries.ModelClasses;


import java.util.List;

public class DModelParentList {


    public String strParentId;
    public String strParentName;
    public String strOutletId;
    public String strOutletName;
    public String strOutletImage;
    public String strOutletLogo;
    public String strOutletAddress;
    public String strOutletTimmings;
    public String strOutletDescription;
    public String strOutletPhone;
    public String strFestival;
    public String strSpecial;
    public int intCategoryId;
    public int intPopulrCategoryId;
    public int interestId;
    public String strSortBy;
    public int strDistance;
    public boolean isOutletExpendible;
    public boolean isDistanceRequired;
    public List<Child> child;

    public DModelParentList(String strParentId, String strParentName, String strOutletId, String strOutletName, String strOutletImage, String strOutletLogo, String strOutletAddress, String strOutletTimmings, String strOutletDescription, String strOutletPhone, String strFestival, String strSpecial, int intCategoryId, int intPopulrCategoryId, int interestId, String strSortBy, int strDistance, boolean isOutletExpendible, boolean isDistanceRequired, List<Child> child) {
        this.strParentId = strParentId;
        this.strParentName = strParentName;
        this.strOutletId = strOutletId;
        this.strOutletName = strOutletName;
        this.strOutletImage = strOutletImage;
        this.strOutletLogo = strOutletLogo;
        this.strOutletAddress = strOutletAddress;
        this.strOutletTimmings = strOutletTimmings;
        this.strOutletDescription = strOutletDescription;
        this.strOutletPhone = strOutletPhone;
        this.strFestival = strFestival;
        this.strSpecial = strSpecial;
        this.intCategoryId = intCategoryId;
        this.intPopulrCategoryId = intPopulrCategoryId;
        this.interestId = interestId;
        this.strSortBy = strSortBy;
        this.strDistance = strDistance;
        this.isOutletExpendible = isOutletExpendible;
        this.isDistanceRequired = isDistanceRequired;
        this.child = child;
    }


    public static class Child {
        private String strOfferId;
        private String strOfferName;
        private String strCategoryId;
        private String strOutletName;
        private String Description;
        private String strOfferImage;
        private String strOfferFestival;
        private String strOfferSpecial;
        private String detailNExclusion;
        private String approxSavings;
        private boolean isFavorite;
        private boolean canRedeem;
        String renew;
        String renewDate;
        private String expiryDate;
        private boolean canSendGift;
        private String discountType;
        private String approxSavingPercentage;

        public Child(String strOfferId, String strOfferName, String strCategoryId, String strOutletName, String description, String strOfferImage, String strOfferFestival, String strOfferSpecial, String detailNExclusion, String approxSavings, boolean isFavorite, boolean canRedeem, String renew, String renewDate, String expiryDate, boolean canSendGift,String discountType,String approxSavingPercentage) {
            this.strOfferId = strOfferId;
            this.strOfferName = strOfferName;
            this.strCategoryId = strCategoryId;
            this.strOutletName = strOutletName;
            Description = description;
            this.strOfferImage = strOfferImage;
            this.strOfferFestival = strOfferFestival;
            this.strOfferSpecial = strOfferSpecial;
            this.detailNExclusion = detailNExclusion;
            this.approxSavings = approxSavings;
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

        public String getStrCategoryId() {
            return strCategoryId;
        }

        public void setStrCategoryId(String strCategoryId) {
            this.strCategoryId = strCategoryId;
        }

        public String getStrOutletName() {
            return strOutletName;
        }

        public void setStrOutletName(String strOutletName) {
            this.strOutletName = strOutletName;
        }

        public String getStrOfferId() {
            return strOfferId;
        }

        public void setStrOfferId(String strOfferId) {
            this.strOfferId = strOfferId;
        }

        public String getStrOfferName() {
            return strOfferName;
        }

        public void setStrOfferName(String strOfferName) {
            this.strOfferName = strOfferName;
        }

        public String getStrOfferImage() {
            return strOfferImage;
        }

        public void setStrOfferImage(String strOfferImage) {
            this.strOfferImage = strOfferImage;
        }

        public String getStrOfferFestival() {
            return strOfferFestival;
        }

        public void setStrOfferFestival(String strOfferFestival) {
            this.strOfferFestival = strOfferFestival;
        }

        public String getStrOfferSpecial() {
            return strOfferSpecial;
        }

        public void setStrOfferSpecial(String strOfferSpecial) {
            this.strOfferSpecial = strOfferSpecial;
        }

        public String getDetailNExclusion() {
            return detailNExclusion;
        }

        public void setDetailNExclusion(String detailNExclusion) {
            this.detailNExclusion = detailNExclusion;
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

    public String getStrParentId() {
        return strParentId;
    }

    public void setStrParentId(String strParentId) {
        this.strParentId = strParentId;
    }

    public String getStrParentName() {
        return strParentName;
    }

    public void setStrParentName(String strParentName) {
        this.strParentName = strParentName;
    }

    public String getStrOutletId() {
        return strOutletId;
    }

    public void setStrOutletId(String strOutletId) {
        this.strOutletId = strOutletId;
    }

    public String getStrOutletName() {
        return strOutletName;
    }

    public void setStrOutletName(String strOutletName) {
        this.strOutletName = strOutletName;
    }

    public String getStrOutletImage() {
        return strOutletImage;
    }

    public void setStrOutletImage(String strOutletImage) {
        this.strOutletImage = strOutletImage;
    }

    public String getStrOutletLogo() {
        return strOutletLogo;
    }

    public void setStrOutletLogo(String strOutletLogo) {
        this.strOutletLogo = strOutletLogo;
    }

    public String getStrOutletAddress() {
        return strOutletAddress;
    }

    public void setStrOutletAddress(String strOutletAddress) {
        this.strOutletAddress = strOutletAddress;
    }

    public String getStrOutletTimmings() {
        return strOutletTimmings;
    }

    public void setStrOutletTimmings(String strOutletTimmings) {
        this.strOutletTimmings = strOutletTimmings;
    }

    public String getStrOutletDescription() {
        return strOutletDescription;
    }

    public void setStrOutletDescription(String strOutletDescription) {
        this.strOutletDescription = strOutletDescription;
    }

    public String getStrOutletPhone() {
        return strOutletPhone;
    }

    public void setStrOutletPhone(String strOutletPhone) {
        this.strOutletPhone = strOutletPhone;
    }

    public String getStrFestival() {
        return strFestival;
    }

    public void setStrFestival(String strFestival) {
        this.strFestival = strFestival;
    }

    public String getStrSpecial() {
        return strSpecial;
    }

    public void setStrSpecial(String strSpecial) {
        this.strSpecial = strSpecial;
    }

    public int getIntCategoryId() {
        return intCategoryId;
    }

    public void setIntCategoryId(int intCategoryId) {
        this.intCategoryId = intCategoryId;
    }

    public int getIntPopulrCategoryId() {
        return intPopulrCategoryId;
    }

    public void setIntPopulrCategoryId(int intPopulrCategoryId) {
        this.intPopulrCategoryId = intPopulrCategoryId;
    }

    public String getStrSortBy() {
        return strSortBy;
    }

    public void setStrSortBy(String strSortBy) {
        this.strSortBy = strSortBy;
    }

    public int getStrDistance() {
        return strDistance;
    }

    public void setStrDistance(int strDistance) {
        this.strDistance = strDistance;
    }

    public boolean isOutletExpendible() {
        return isOutletExpendible;
    }

    public void setOutletExpendible(boolean outletExpendible) {
        isOutletExpendible = outletExpendible;
    }

    public boolean isDistanceRequired() {
        return isDistanceRequired;
    }

    public void setDistanceRequired(boolean distanceRequired) {
        isDistanceRequired = distanceRequired;
    }

    public int getInterestId() {
        return interestId;
    }

    public void setInterestId(int interestId) {
        this.interestId = interestId;
    }

    public List<Child> getChild() {
        return child;
    }

    public void setChild(List<Child> child) {
        this.child = child;
    }
}