package com.example.android_up_sdk.HomeAuxiliaries.WebServices;

import android.content.Context;

import com.example.android_up_sdk.Utils.ApiMethod;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.example.android_up_sdk.Utils.IWebCallback;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchOffers_WebHit_Get_TrendingSearchTag {
    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseObject = null;
    public Context mContext;

    public void requestTrendingSearchTags(Context _context, final IWebCallback iWebCallback) {
        String myUrl = AppConfig.getInstance().getBaseUrlApiMobile() + ApiMethod.GET.getTrendingSearchTag;
        this.mContext = _context;

        mClient.addHeader(ApiMethod.HEADER.Authorization, AppConfig.getInstance().mUser.getAuthorizationToken());
        mClient.addHeader("app_id", "1");
        mClient.setMaxRetriesAndTimeout(AppConstt.LIMIT_API_RETRY, AppConstt.LIMIT_TIMEOUT_MILLIS);
        mClient.setTimeout(AppConstt.LIMIT_TIMEOUT_MILLIS);
        mClient.get(myUrl, new AsyncHttpResponseHandler() {
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
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                switch (statusCode) {
                    case AppConstt.ServerStatus.NETWORK_ERROR:
                        iWebCallback.onWebResult(false, AppConfig.getInstance().getNetworkErrorMessage());
                        break;


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

        });

    }

    public class ResponseModel {

        public class Data {
            private int id;

            private String text;

            private String status;

            private String created_at;

            private String updated_at;

            public void setId(int id) {
                this.id = id;
            }

            public int getId() {
                return this.id;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getText() {
                return this.text;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getStatus() {
                return this.status;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getCreated_at() {
                return this.created_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            public String getUpdated_at() {
                return this.updated_at;
            }
        }

        private String status;

        private String message;

        private List<Data> data;

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }

        public void setData(List<Data> data) {
            this.data = data;
        }

        public List<Data> getData() {
            return this.data;
        }

    }

}
