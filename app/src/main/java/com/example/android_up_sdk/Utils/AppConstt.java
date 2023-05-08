package com.example.android_up_sdk.Utils;

public interface AppConstt {

    int LIMIT_API_RETRY = 0;
    int LIMIT_TIMEOUT_MILLIS = 15000;
    String GSON_SPCL_CHR = "GSON_SPCL_CHR";
    String CHAR_SET = "bhjglyoqck829uf513z4i06xvtmrew7ewojd";
    String ARABIC = "ar";
    String ENGLISH = "en";
    String OUTLET_MENU = "Menu";
    String OUTLET_MENU_PDF = "Menu_PDF";
    String PURCHASE_RULES = "RulesOfPurchase";
    String RULES_OF_PURCHASE_URL = "https://urbanpoint.com/terms-of-service";


    interface ServerUrl {
        //Debug Build
        //AZURE URLS
        String DEB_BASE_URL_V2 = "http://ooredoo-sdk-internal.adminurban.com/";
        String DEB_URL_IMG = "https://urbanpoint-storage.azureedge.net/test/uploads_staging/uploads/";
        String DEB_URL_THUMBS = "https://urbanpoint-storage.azureedge.net/test/uploads_staging/uploads/";
        String DEB_URL_API_MOBILE_QATAR_V2 = DEB_BASE_URL_V2 + "api/mobile/";

        //Release Build
        //live Url
        String REl_BASE_URL = "http://ooredoo-sdk-internal.adminurban.com/";
        String REl_URL_IMG = "https://urbanpoint-storage.azureedge.net/test/uploads_staging/uploads/";
        String REl_URL_THUMBS = "https://urbanpoint-storage.azureedge.net/test/uploads_staging/uploads/";
        String REl_URL_API_MOBILE_QATAR_V2 = REl_BASE_URL + "api/mobile/";

//        String REl_BASE_URL = "https://adminurban.com/";
//        String REl_URL_IMG = "https://urbanpoint-storage.azureedge.net/uploads/";
//        String REl_URL_THUMBS = "https://urbanpoint-storage.azureedge.net/uploads/";
//        String REl_URL_API_MOBILE_QATAR_V2 = REl_BASE_URL + "up_qatar_v2/api/v2/mobile/";

    }

    interface ServerStatus {
        //Server RModel_SearchTags Status
        short OK = 200;
        short NO_CONTENT = 204;
        short DATABASE_NOT_FOUND = 404;
        short INTERNAL_SERVER_ERROR = 500;
        short NETWORK_ERROR = 0;
    }

    interface OfferRenewStatus{
        String Renewable = "1";
        String NonRenewable = "0";
    }

    interface DiscountTypeEnum{
        String standard = "0";
        String percentage = "1";
    }

    interface OutletMenuType {
        String Image = "image";
        String PDF = "pdf";
        String URL = "url";
    }

    class LocUpdate {
        public static final long MIN_TIME_BW_UPDATES = 1000 * 5;
        public static final float MIN_DISTANCE_BW_UPDATES = 5;
    }


    interface AppLanguage {
        String ARABIC = "ar";
        String ENGLISH = "en";

    }

    interface DeepLinkingUrl {
        String DeepLinkURL = "urban-point.app.link/";

    }
    interface DeepLinkingSates {
        String OFFER_DETAIL = "/offerid";
        String MERCHANT_DETAIL = "/merchantid";
    }

    class IntentPreference {
        public static final int SOURCE_LOCATION_INTENT_CODE = 94;
        public static final int PACKAGE_LOCATION_INTENT_CODE = 93;
    }

    interface NetworkMsg {
        String ERROR_NETWORK_EN = "No internet connection";
        String ERROR_NETWORK_AR = "لا يوجد اتصال بالإنترنت";
        String EXCEPTION_NETWORK_EN = "Oops! Something went wrong";
        String EXCEPTION_NETWORK_AR = "Oops! Something went wrong";
    }

    interface FrgTag {
        String HomeUpFragment = "HomeUpFragment";
        String SettingsUpFragment = "SettingsUpFragment";
        String CategoryHubFragment = "CategoryHubFragment";
        String PurchaseSuccessFragment = "PurchaseSuccessFragment";
        String SearchOffersFragment = "SearchOffersFragment";
        String MerchantPinFragment = "MerchantPinFragment";
        String MerchantDetailFragment = "MerchantDetailFragment";
        String CategoryParentHubFragment = "CategoryParentHubFragment";
        String WebViewFragment = "WebViewFragment";
        String MerchantMenuImagesFragment = "MerchantMenuImagesFragment";
        String NewBrandsPagerFragment = "NewBrandsPagerFragment";
        String NearbyOutletsPagerFragment = "NearbyOutletsPagerFragment";
        String HomeDetailFragment = "HomeDetailFragment";
        String OffersFragment = "OffersFragment";

    }

    class DEFAULT_VALUES {
        public static final String SORT_BY_ALPHABETICALLY = "alphabetically";
        public static final String SORT_BY_LOCATION = "location";
        public static final String Both = "2";
        public static final double DEFAULT_LAT = 0;
        public static final double DEFAULT_LNG = 0;

    }

    interface ViewPagerState {
        int CATEGORY_OUTLET = 0;
        int CATEGORY_PARENT = 1;

    }

    interface BundleStrings {

        String catSortBy = "catSortBy";
        String outletId = "outletId";
        String offerId = "offerId";
        String merchantImage = "merchantImage";
        String merchantLogo = "merchantLogo";
        String merchantName = "merchantName";
        String merchantAddress = "merchantAddress";
        String merchantTimmings = "merchantTimmings";
        String merchantDescription = "merchantDescription";
        String merchantPhone = "merchantPhone";
        String offerName = "offerName";
        String merchantId = "merchantId";
        String approxSavings = "approxSavings";
        String orderId = "orderId";
        String categoryId = "categoryId";
        String purchaseSuccessPIN = "purchaseSuccessPIN";
        String walletId = "walletId";
        String environment = "environment";
        String publicKey = "publicKey";

    }

    interface HeadersValue {
        String Authorization = "UP!and$";
    }

    interface Enviornment {
        String Internal = "U2FsdGVkX184xoruZhUU33T90KJrfjByBe1z1d4qn4Y=";
        String Prod = "U2FsdGVkX1+mPCbthcaKFK/RD5xrGs2PQIc6MF6GcsY=";
    }


    interface PublicKey {
        String Internal = "U2FsdGVkX1+O7DnTK8A2llIbswxDjNfDrFVDn885oRSzFNNm2ff50yDkRcufVGpc";
        String Prod = "U2FsdGVkX19MlY7evIzyH17oR6wpZx7pw5lsE8VShKkkVb1NAsPD7HIqL5VZ0Lr1";
    }


}
