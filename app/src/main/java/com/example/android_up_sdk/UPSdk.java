package com.example.android_up_sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.android_up_sdk.Utils.AppConstt;


public class UPSdk {

    String mWalletId;
    String mEnvironment;
    String mPublicKey;
    Context mContext;

    public UPSdk(Context context,String wallet_id,String environment,String public_key) {

        mContext=context;
        mWalletId=wallet_id;
        mEnvironment=environment;
        mPublicKey=public_key;


    }

    public void start() {

        try {

            if (mContext==null ||mPublicKey==null || mEnvironment==null || mWalletId==null){
                throw new IllegalStateException("Values cannot be null");
            }
            else {
                Intent intent=new Intent(mContext,MainUPActivity.class);
                intent.putExtra(AppConstt.BundleStrings.walletId,mWalletId);
                intent.putExtra(AppConstt.BundleStrings.environment,mEnvironment);
                intent.putExtra(AppConstt.BundleStrings.publicKey,mPublicKey);
                mContext.startActivity(intent);
            }
            Log.d("SDK","Not Initialzed");

        } catch (Throwable t) {
            Log.d("SDK","Not Initialzed");

        }

    }

}
