package com.example.android_up_sdk.HomeAuxiliaries.WebServices;

import android.content.Context;

import com.example.android_up_sdk.Utils.ApiMethod;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.example.android_up_sdk.Utils.IWebCallback;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchOffers_WebHit_Get_getOffersSearchTags {
    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseObject;
    public Context mContext;

    public void requestSearchOffers(Context _context, final IWebCallback iWebCallback, int _page, String _searchKey, int _categoryId) {

        String myUrl = AppConfig.getInstance().getBaseUrlApiMobile() + ApiMethod.GET.getOffersSearchTags;
        this.mContext = _context;
        RequestParams params = new RequestParams();
        params.put("index", _page);
        params.put("search", _searchKey);
        if (_categoryId > 0) {
            params.put("category_id", _categoryId);
        }

        mClient.addHeader(ApiMethod.HEADER.Authorization,  AppConfig.getInstance().mUser.getAuthorizationToken());
        mClient.addHeader("app_id", "1");
        mClient.setMaxRetriesAndTimeout(AppConstt.LIMIT_API_RETRY, AppConstt.LIMIT_TIMEOUT_MILLIS);
        mClient.setTimeout(AppConstt.LIMIT_TIMEOUT_MILLIS);
        mClient.get(myUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;

                        try {
                            responseObject = null;
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

//                            case AppConstt.ServerStatus.UNAUTHORIZED:
//                                AppConfig.getInstance().navtoLogin();
//                                break;

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
        private Data data;
        private String message;
        private String status;

        public void setData(Data data) {
            this.data = data;
        }

        public Data getData() {
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

        public class DataItem {
            private String ouSearchTags;
            private String name;
            private String title;
            private String ofSearchTags;

            public void setOuSearchTags(String ouSearchTags) {
                this.ouSearchTags = ouSearchTags;
            }

            public String getOuSearchTags() {
                return ouSearchTags;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitle() {
                return title;
            }

            public void setOfSearchTags(String ofSearchTags) {
                this.ofSearchTags = ofSearchTags;
            }

            public String getOfSearchTags() {
                return ofSearchTags;
            }
        }

        public class Data {
            private String search;
            private List<DataItem> data;
            private List<String> tags;

            public String getSearch() {
                return search;
            }

            public void setSearch(String search) {
                this.search = search;
            }

            public void setData(List<DataItem> data) {
                this.data = data;
            }

            public List<DataItem> getData() {
                return data;
            }

            public void setTags(List<String> tags) {
                this.tags = tags;
            }

            public List<String> getTags() {
                return tags;
            }
        }
    }
}

