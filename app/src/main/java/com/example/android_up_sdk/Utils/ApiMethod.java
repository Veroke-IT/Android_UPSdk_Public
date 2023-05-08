package com.example.android_up_sdk.Utils;

public interface ApiMethod {

    interface GET {
        String getHomeApiEssentialNew = "getHomeApiEssentialNew";
        String getCacheHomeApiExtra = "getcacheHomeApiExtra";
        String getTrendingSearchTag = "getTrendingSearchTag";
        String getOutlets = "getOutlets";
        String getOutletsParents = "getOutletsParents";
        String getOffersSearchTags = "getOffersSearchTags";
        String getAllOfferUseAgain = "getAllOfferUseAgain";
        String getOffers = "getOffers";
        String getAllNewBrands = "getAllNewBrands";
        String getBrandsOutlets = "getBrandsOutlets";
        String getOutletsNew = "getOutletsNew";
        String homeApiDetails = "homeApiDetails";

    }

    interface POST {
        String redeemOffer = "redeemOffer";
        String getAuth = "api/auth/generateWalletAuthV1";
    }


    interface HEADER {
        String Access_Token = "Access-Token";
        String Authorization = "Authorization";
        String app_id = "app_id ";
    }
}
