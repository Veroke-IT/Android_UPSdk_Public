package com.example.android_up_sdk.HomeAuxiliaries;

import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android_up_sdk.MainUPActivity;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;



public class PurchaseSuccessFragment extends Fragment implements View.OnClickListener {
    TextView txvOfferName, txvMerchantName, txvPurchasePin,txvApproxSaving;
    Button btnDone;
    CircularImageView imvMerchant;
    String strOfferName, strMerchantName, strMerchantAddress, strConfirmationPIN, strDate, strOfferId,
            strMerchantPIN, strMerchantId, merchantLogo, strOrderId,strCategoryId,strApproxSavings;
    RelativeLayout rlBackBtn;


@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_purchase_success, container, false);
        initiate();

        bindViews(view);
        return view;
    }

    private void initiate() {
        strOfferName = "";
        strMerchantName = "";
        strMerchantAddress = "";
        strConfirmationPIN = "";
        strDate = "";
        strOfferId = "";
        strMerchantPIN = "";
        strMerchantId = "";
        merchantLogo = "";
        strCategoryId = "";
        strOrderId = "";
        strApproxSavings = "";
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            strOfferName = bundle.getString(AppConstt.BundleStrings.offerName);
            strMerchantName = bundle.getString(AppConstt.BundleStrings.merchantName);
            strMerchantAddress = bundle.getString(AppConstt.BundleStrings.merchantAddress);
            strConfirmationPIN = bundle.getString(AppConstt.BundleStrings.purchaseSuccessPIN);
            strOfferId = bundle.getString(AppConstt.BundleStrings.offerId);

            strMerchantId = bundle.getString(AppConstt.BundleStrings.merchantId);
            merchantLogo = bundle.getString(AppConstt.BundleStrings.merchantLogo);
            strCategoryId = bundle.getString(AppConstt.BundleStrings.categoryId);
            strOrderId = bundle.getString(AppConstt.BundleStrings.orderId);
            strApproxSavings = bundle.getString(AppConstt.BundleStrings.approxSavings);

        }
    }

    void bindViews(View v) {
        txvOfferName = v.findViewById(R.id.txvOfferName);
        txvMerchantName = v.findViewById(R.id.txvMerchantName);
        txvApproxSaving = v.findViewById(R.id.txvApproxSaving);
        txvPurchasePin = v.findViewById(R.id.frg_purchase_sucess_txv_confirmation_code);
        imvMerchant = v.findViewById(R.id.frg_purchase_success_imvMerchant);
        rlBackBtn = v.findViewById(R.id.app_bar_rl_back);
        btnDone = v.findViewById(R.id.btnDone);


        txvOfferName.setText(strOfferName);
        txvMerchantName.setText(strMerchantName);
        String[] strArray = strApproxSavings.split(" ");
        if(strArray[1]!=null){
            txvApproxSaving.setText(getString(R.string.dlg_offer_detail_save_approx_text2) + " " + strArray[1]+" QAR!");
        }else {
            txvApproxSaving.setText(getString(R.string.dlg_offer_detail_save_approx_text2) + " " + strApproxSavings+"!");
        }
        txvPurchasePin.setText(strConfirmationPIN);

        if (merchantLogo != null && merchantLogo.length() > 0) {
            Picasso.get()
                    .load(AppConfig.getInstance().getBaseUrlImage() + merchantLogo)
                    .error(R.drawable.rmv_place_holder)
                    .placeholder(R.drawable.rmv_place_holder)
                    .into(imvMerchant);
        } else {
            imvMerchant.setImageResource(R.drawable.rmv_place_holder);
        }
        btnDone.setOnClickListener(this);
        rlBackBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnDone || id == R.id.app_bar_rl_back) {
            ((MainUPActivity) requireActivity()).navToHomeFragment();
        }
    }


}