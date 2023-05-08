package com.example.android_up_sdk.Utils;

public interface IAdapterCallback {
    int EVENT_A = 1;
    int EVENT_B = 2;
    int EVENT_C = 3;

    void onAdapterEventFired(int eventId, int position);


}
