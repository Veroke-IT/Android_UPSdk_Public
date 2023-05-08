package com.example.android_up_sdk.Utils;

public interface IWebCallback {

    void onWebResult(boolean isSuccess, String strMsg);

    void onWebException(Exception ex);
}
