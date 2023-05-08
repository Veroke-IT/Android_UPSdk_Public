package com.example.android_up_sdk.HomeAuxiliaries;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android_up_sdk.HomeAuxiliaries.WebServices.MerchantPIN_WebHit_POST_redeemOffer;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.example.android_up_sdk.Utils.IWebCallback;
import com.example.android_up_sdk.Utils.PinEntry;
import com.example.android_up_sdk.Utils.ProgressDialogue;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;


public class MerchantPinFragment extends Fragment implements View.OnClickListener {

    TextView txvOfferName, txvMerchantName,txvApproxSaving;
    PinEntry pinEntry;
    CircularImageView imvMerchant;
    Button btnConfirmPurchase;
    String enteredLoginPin = "";
    LinearLayout llParentLayout;
    private String offerId, offerName, merchantName, merchantAddress, merchantId, merchantLogo, orderId, categoryId, approxsavings;
    private ProgressDialogue progressDialogue;
    RelativeLayout rlBackBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_merchant_pin, container, false);
        initiate();
        bindViews(view);

        return view;
    }

    private void initiate() {
        offerId = "";
        offerName = "";
        merchantName = "";
        merchantAddress = "";
        merchantId = "";
        merchantLogo = "";
        orderId = "";
        categoryId = "";
        approxsavings = "";


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            offerId = bundle.getString(AppConstt.BundleStrings.offerId);
            offerName = bundle.getString(AppConstt.BundleStrings.offerName);
            merchantName = bundle.getString(AppConstt.BundleStrings.merchantName);
            merchantAddress = bundle.getString(AppConstt.BundleStrings.merchantAddress);
            merchantId = bundle.getString(AppConstt.BundleStrings.merchantId);
            merchantLogo = bundle.getString(AppConstt.BundleStrings.merchantLogo);
            categoryId = bundle.getString(AppConstt.BundleStrings.categoryId);
            orderId = bundle.getString(AppConstt.BundleStrings.orderId);
            approxsavings = bundle.getString(AppConstt.BundleStrings.approxSavings);


        }

    }

    void bindViews(View v) {
        llParentLayout = v.findViewById(R.id.frg_merchant_pin_ll_parentlayout);
        rlBackBtn = v.findViewById(R.id.app_bar_rl_back);
        imvMerchant = v.findViewById(R.id.frg_merchant_pin_imvMerchant);
        txvOfferName = v.findViewById(R.id.txvOfferName);
        txvMerchantName = v.findViewById(R.id.txvMerchantName);
        txvApproxSaving = v.findViewById(R.id.txvApproxSaving);
        pinEntry = v.findViewById(R.id.pinEntry);
        btnConfirmPurchase = v.findViewById(R.id.btnConfirmPurchase);
        progressDialogue = new ProgressDialogue();
        btnConfirmPurchase.setClickable(false);
        txvOfferName.setText(offerName);
        txvMerchantName.setText(merchantName);
        String[] strArray = approxsavings.split(" ");
        if(strArray[1]!=null){
            txvApproxSaving.setText(getString(R.string.dlg_offer_detail_save_approx_text2) + " " + strArray[1]+" QAR!");
        }else {
            txvApproxSaving.setText(getString(R.string.dlg_offer_detail_save_approx_text2) + " " + approxsavings+"!");
        }

        if (merchantLogo != null && merchantLogo.length() > 0) {
            Picasso.get()
                    .load(AppConfig.getInstance().getBaseUrlImage() + merchantLogo)
                    .into(imvMerchant);
        } else {
            imvMerchant.setImageResource(R.drawable.rmv_place_holder);
        }

        llParentLayout.setOnClickListener(this);
        rlBackBtn.setOnClickListener(this);

        pinEntry.setOnPinEnteredListener(pin -> {
            if (pin.length() < 4) {
                enteredLoginPin = "";
                btnConfirmPurchase.setBackgroundTintList(requireContext().getResources().getColorStateList(R.color.thm_gift_divider));
                btnConfirmPurchase.setTextColor(getResources().getColor(R.color.thm_txt_hint_dark));
                btnConfirmPurchase.setOnClickListener(null);
                btnConfirmPurchase.setClickable(false);
            } else {
                enteredLoginPin = pin;
                AppConfig.getInstance().closeKeyboard(requireActivity());
                btnConfirmPurchase.setBackgroundTintList(requireContext().getResources().getColorStateList(R.color.thm_red));
                btnConfirmPurchase.setTextColor(getResources().getColor(R.color.white));
                btnConfirmPurchase.setOnClickListener(this);
                btnConfirmPurchase.setClickable(true);

            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnConfirmPurchase) {
            if (validatingRequired()) {
                requestRedeemOffer(Integer.parseInt(offerId), enteredLoginPin, Integer.parseInt(merchantId));
            }
        } else if (id == R.id.frg_merchant_pin_ll_parentlayout) {
            AppConfig.getInstance().closeKeyboard(requireActivity());
        } else if (id == R.id.app_bar_rl_back) {
            requireActivity().onBackPressed();
        }
    }


    void navToPurchaseSuccessFragment(Bundle b) {
        Fragment fr = new PurchaseSuccessFragment();
        fr.setArguments(b);
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.main_act_content_frame, fr, AppConstt.FrgTag.PurchaseSuccessFragment);
        ft.addToBackStack(AppConstt.FrgTag.PurchaseSuccessFragment);
        ft.hide(this);
        ft.commit();
    }

    private void requestRedeemOffer(int _offerId, String  _pin,int merchant_id) {
        progressDialogue.startIOSLoader(getActivity());
        MerchantPIN_WebHit_POST_redeemOffer merchantPIN_webhit_post_redeemOffer = new MerchantPIN_WebHit_POST_redeemOffer();
        merchantPIN_webhit_post_redeemOffer.requestRedeem(getContext(), new IWebCallback() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDialogue.stopIOSLoader();
                if (isSuccess) {
                    String purchaseSuccessPIN = MerchantPIN_WebHit_POST_redeemOffer.responseObject.getData().getId() + "";
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConstt.BundleStrings.offerName, offerName);
                    bundle.putString(AppConstt.BundleStrings.merchantName, merchantName);
                    bundle.putString(AppConstt.BundleStrings.merchantAddress, merchantAddress);
                    bundle.putString(AppConstt.BundleStrings.purchaseSuccessPIN, purchaseSuccessPIN);
                    bundle.putString(AppConstt.BundleStrings.offerId, offerId);
                    bundle.putString(AppConstt.BundleStrings.merchantId, merchantId);
                    bundle.putString(AppConstt.BundleStrings.merchantLogo, merchantLogo);
                    bundle.putString(AppConstt.BundleStrings.categoryId, categoryId);
                    bundle.putString(AppConstt.BundleStrings.orderId, orderId);
                    bundle.putString(AppConstt.BundleStrings.approxSavings, approxsavings);

                    navToPurchaseSuccessFragment(bundle);
                }
                else {
                    AppConfig.getInstance().showAlertDialog(requireActivity(),null, strMsg, null, null, false, true, null);
                }

            }

            @Override
            public void onWebException(Exception ex) {
                progressDialogue.stopIOSLoader();
                AppConfig.getInstance().showAlertDialog(requireActivity(),null, AppConfig.getInstance().getNetworkExceptionMessage(ex.getMessage()), null, null, false, true, null);
            }

        }, _offerId, _pin, merchant_id);
    }

    private boolean validatingRequired() {
        String message = "";

        //validate the content
        if (enteredLoginPin.equalsIgnoreCase(AppConfig.getInstance().mUser.getMasterMerchant())) {
            message = "";
        } else {
            if (enteredLoginPin.equalsIgnoreCase("")) {
                message = getResources().getString(R.string.MerchantErrorMessage);
                AppConfig.getInstance().showAlertDialog(requireActivity(),null, message, null, null, false, true, null);

            } else if (enteredLoginPin.length() < 4) {
                message = getResources().getString(R.string.MerchantErrorMessage);
                AppConfig.getInstance().showAlertDialog(requireActivity(),null, message, null, null, false, true,null);
            } else if (offerId.length() < 1) {
                message = getResources().getString(R.string.OfferErrorMessage);
                AppConfig.getInstance().showAlertDialog(requireActivity(),null, message, null, null, false, true,null);
            }
        }
        if (message.equalsIgnoreCase("") || message == null) {
            return true;
        } else {
            return false;
        }
    }
}