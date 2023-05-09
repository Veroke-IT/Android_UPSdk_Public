package com.example.android_up_sdk.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_OfferDetail;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_Recent_Outlet;
import com.example.android_up_sdk.MainUPActivity;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.example.android_up_sdk.Utils.CustomAlertConfirmationInterface;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class OfferDetailAlertDialog extends BottomSheetDialog implements View.OnClickListener, BottomSheetDialog.OnShowListener {

    DModel_OfferDetail dModel_offerDetail;
    CustomAlertConfirmationInterface customAlertConfirmationInterface;
    Context mContext;
    MainUPActivity mainActivity;



    public OfferDetailAlertDialog(@NonNull Context context, Fragment _frg, DModel_OfferDetail _dModel_offerDetail, final CustomAlertConfirmationInterface _customDialogConfirmationListener) {
        super(context, R.style.SubMenuBottomSheetDialogTheme);

        this.dModel_offerDetail = _dModel_offerDetail;
        this.mContext = context;

        if (context instanceof MainUPActivity) {
            mainActivity = (MainUPActivity) context;
        }

        this.customAlertConfirmationInterface = _customDialogConfirmationListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lay_offer_dialog);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        bindViews();

    }

    private void bindViews() {
        TextView txvMerchantName = findViewById(R.id.dlg_offer_detail_alrt_txv_merchant_name);
        TextView txvMerchantNameOnly = findViewById(R.id.dlg_offer_detail_alrt_txv_merchant_name_only);

        TextView txvSaveApprox = findViewById(R.id.dlg_offer_detail_alrt_txv_save_aprrox);
        TextView txvFreeOffer = findViewById(R.id.dlg_offer_detail_alrt_txv_free_offer);
        TextView txvFreeReason = findViewById(R.id.dlg_offer_detail_alrt_txv_free_reason);

        TextView txvValidFor = findViewById(R.id.dlg_offer_detail_alrt_txv_valid_for);
        TextView txvExpiryDate = findViewById(R.id.dlg_offer_detail_alrt_txv_expiry_date);

        TextView txvTermsOfUse = findViewById(R.id.dlg_offer_detail_alrt_txv_termsofuse);



        LinearLayout llDetainNExclusion = findViewById(R.id.dlg_offer_detail_alrt_ll_detail_exclusion);
        Button btnUseOffer = findViewById(R.id.dlg_offer_detail_alrt_btn_use_offer);
        Button btnUsedOffer = findViewById(R.id.dlg_offer_detail_alrt_btn_used_offer);

        LinearLayout llDescriptionContainer = findViewById(R.id.dlg_offer_detail_alrt_ll_desc_container);

        btnUseOffer.setOnClickListener(this);
        txvTermsOfUse.setOnClickListener(this);

        Log.d("Check","Can "+dModel_offerDetail.isCanRedeem());

        if (dModel_offerDetail.isCanRedeem()) {
            btnUsedOffer.setVisibility(View.GONE);
            btnUseOffer.setVisibility(View.VISIBLE);
        }
        else  {
            btnUsedOffer.setVisibility(View.VISIBLE);
            btnUseOffer.setVisibility(View.GONE);

        }




        String[] strArray = dModel_offerDetail.getStrMerchantName().split("-");
        SpannableString spannedMerchantName = new SpannableString(dModel_offerDetail.getStrMerchantName());
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannedMerchantName.setSpan(boldSpan, 0, strArray[0].length(), Spanned.SPAN_COMPOSING);

        txvMerchantName.setText(spannedMerchantName);
        txvMerchantNameOnly.setText(strArray[0]);

        String[] strArrayApprox = dModel_offerDetail.getStrSaveApprox().split(" ");
        if(strArrayApprox[1]!=null){
            txvSaveApprox.setText(getContext().getString(R.string.dlg_offer_detail_save_approx) + " " + strArrayApprox[1]+" QAR");
        }else {
            txvSaveApprox.setText(getContext().getString(R.string.dlg_offer_detail_save_approx) + " " + dModel_offerDetail.getStrSaveApprox());
        }




        txvFreeOffer.setText(dModel_offerDetail.getStrOfferName());
        txvFreeReason.setText(dModel_offerDetail.getStrOfferDescription());

        if (dModel_offerDetail.getExpiryDate() != null && !dModel_offerDetail.getExpiryDate().equalsIgnoreCase("")) {
            SimpleDateFormat format = new SimpleDateFormat();
            format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

            try {
                Date expiryDate = format.parse(dModel_offerDetail.getExpiryDate());
                String strExpiryDate = mContext.getResources().getString(R.string.dlg_offer_expires_on) + format2.format(expiryDate);
                txvExpiryDate.setText(strExpiryDate);

            }
            catch (ParseException e) {
                txvExpiryDate.setText(dModel_offerDetail.getExpiryDate());
                e.printStackTrace();
            }

            txvExpiryDate.setVisibility(View.VISIBLE);
        } else {
            txvExpiryDate.setVisibility(View.GONE);
        }





        if (dModel_offerDetail.getStrDetailNExclusions() != null && dModel_offerDetail.getStrDetailNExclusions().length() > 0) {
            llDetainNExclusion.setVisibility(View.VISIBLE);
            txvValidFor.setText(dModel_offerDetail.getStrDetailNExclusions());

        }


        AppConfig.getInstance().updateRecentViewed(new DModel_Recent_Outlet(dModel_offerDetail.getStrMerchntId(),dModel_offerDetail.getStrMerchntlogo(),
                dModel_offerDetail.getStrmerchantAddress(),dModel_offerDetail.getStrMerchantName()));

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.dlg_offer_detail_alrt_btn_use_offer) {
            if (customAlertConfirmationInterface != null)
                customAlertConfirmationInterface.callConfirmationDialogPositive();

            if (mainActivity != null) {

                mainActivity.redeemOffers(dModel_offerDetail.getOfferId() + "",
                        dModel_offerDetail.getStrOfferName(),
                        dModel_offerDetail.getStrMerchantName(),
                        dModel_offerDetail.getStrmerchantAddress(),
                        dModel_offerDetail.getStrMerchntId(),
                        dModel_offerDetail.getStrCategoryIds(),
                        dModel_offerDetail.getStrMerchntlogo(),
                        dModel_offerDetail.getStrSaveApprox());
            }

            this.dismiss();
        } else if (id == R.id.dlg_offer_detail_alrt_txv_termsofuse) {
            if (mainActivity != null) {
                mainActivity.navToRulesOfPurchaseFragment();
            }
            this.dismiss();
        }
    }


    @Override
    public void onShow(DialogInterface dialog) {
        BottomSheetDialog sheetDialog = (BottomSheetDialog) dialog;


        FrameLayout bottomSheet =  sheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

        // Right here!
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet)
                    .setState(BottomSheetBehavior.STATE_EXPANDED);
            BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(true);

        }


    }
}

