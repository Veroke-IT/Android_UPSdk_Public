package com.example.android_up_sdk;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.duolingo.open.rtlviewpager.RtlViewPager;
import com.example.android_up_sdk.Dialogs.MultipleMobileNumberDialog;
import com.example.android_up_sdk.Dialogs.OfferDetailAlertDialog;
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.MerchantImagesAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.OutletOffersAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModelOutletOffers;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_OfferDetail;
import com.example.android_up_sdk.HomeAuxiliaries.WebServices.MerchantDetail_WebHit_Get_getMerchantDetail;
import com.example.android_up_sdk.HomeAuxiliaries.WebServices.MerchantDetail_WebHit_Get_getMerchantDetailOffers;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.example.android_up_sdk.Utils.CustomAlertConfirmationInterface;
import com.example.android_up_sdk.Utils.CustomAlertMultipleMobileInterface;
import com.example.android_up_sdk.Utils.ExpandableHeightGridView;
import com.example.android_up_sdk.Utils.IWebCallback;
import com.example.android_up_sdk.Utils.ProgressDialogue;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;



public class MerchantDetailFragment extends Fragment implements View.OnClickListener {


    private final int REQUEST_CODE_ASK_PERMISSIONS_CALL = 0;
    ProgressDialogue progressDialogue;

    private ImageView imvProduct;
    RelativeLayout btnCall, btnDirection, btnMenu;
    private TextView txvOfferTitle, txvMerchantName, txvMerchantWeekdaysTimings, txvMerchantWeekendsTimings, txvDescription, txvNotFound;
    private CircularImageView imvMerchant;
    private ArrayList<DModelOutletOffers> lstMerchantOffers;
    private OutletOffersAdapter merchantOffersAdapter;
    private ExpandableHeightGridView lsvMerchantOffers;
    private View lsvFooterView;
    private LinearLayout llMainCenter, llBottomCenter;
    private ScrollView scrollView;
    private String merchantPhone, directionLatitude, directionLongitude;
    private String merchantMenu;
    private String merchantMenuType;
    private int outletId, page;
    private boolean shouldGetMoreOffers, isAlreadyFetchingOffers;
    private String  offerName, merchantName, merchantImage, merchantLogo, merchantAddress, merchantDescription, merchantTimings;
    Bundle bundle;
    RelativeLayout rlBack;
    private DModel_OfferDetail dModel_offerDetail;
    private RtlViewPager rtlViewPager;
    private CirclePageIndicator circlePageIndicator;
    MerchantImagesAdapter merchantImagesAdapter;
    int currentPage, mIndicatorPosition;
    Timer timer;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_merchant_detail, container, false);
        initiate();
        bindViews(view);
        lsvMerchantOffers.setOnItemClickListener((parent, view1, position, id) -> {
            int offerId = Integer.parseInt(lstMerchantOffers.get(position).getId());
            String saveApprox = lstMerchantOffers.get(position).getApproxSavings();



            dModel_offerDetail = new DModel_OfferDetail(offerId,
                    position,
                    0,
                    "lstMerchantOffers",
                    saveApprox,
                    lstMerchantOffers.get(position).getOfferName(),
                    lstMerchantOffers.get(position).getOfferDescription(),
                    lstMerchantOffers.get(position).getCategoryId(),
                    lstMerchantOffers.get(position).getMerchantName(),
                    lstMerchantOffers.get(position).getMerchantAddress(),
                    lstMerchantOffers.get(position).getMerchantId(),
                    lstMerchantOffers.get(position).getMerchantLogo(),
                    lstMerchantOffers.get(position).getDetailNEclusion(),
                    lstMerchantOffers.get(position).isFav(),
                    lstMerchantOffers.get(position).isCanRedeem(),
                    lstMerchantOffers.get(position).getExpiryDate(),
                    lstMerchantOffers.get(position).isCanSendGift(),
                    lstMerchantOffers.get(position).getDiscountType(),
                    lstMerchantOffers.get(position).getApproxSavingPercentage()
            );
            showOfferDetailAlertDialog(dModel_offerDetail);

        });

        lsvMerchantOffers.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                View childView = scrollView.getChildAt(0);
                int diff = (childView.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));


                if (diff <= 340) {

                    if (shouldGetMoreOffers && !isAlreadyFetchingOffers) {
                        page++;
                        lsvMerchantOffers.addFooterView(lsvFooterView);
                        merchantOffersAdapter.notifyDataSetChanged();
                        requestMerchantDetailOffers(outletId, page);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        if (outletId > 0) {
            requestMerchantDetail(outletId);
        }
        return view;
    }

    private void initiate() {
        outletId = 0;
        page = 1;
        offerName = "";
        merchantName = "";
        merchantAddress = "";
        merchantDescription = "";
        merchantImage = "";
        merchantLogo = "";
        merchantPhone = "";
        directionLatitude = "";
        directionLongitude = "";
        shouldGetMoreOffers = true;
        isAlreadyFetchingOffers = false;
        lstMerchantOffers = new ArrayList<>();
        dModel_offerDetail = new DModel_OfferDetail();
        bundle = new Bundle();

        Bundle b = this.getArguments();
        if (b != null) {
            outletId = b.getInt(AppConstt.BundleStrings.outletId);
            offerName = b.getString(AppConstt.BundleStrings.offerName);
            merchantName = b.getString(AppConstt.BundleStrings.merchantName);
            merchantTimings = b.getString(AppConstt.BundleStrings.merchantTimmings);
            merchantDescription = b.getString(AppConstt.BundleStrings.merchantDescription);
            merchantImage = b.getString(AppConstt.BundleStrings.merchantImage);
            merchantLogo = b.getString(AppConstt.BundleStrings.merchantLogo);
            merchantPhone = b.getString(AppConstt.BundleStrings.merchantPhone);
        }
        progressDialogue = new ProgressDialogue();
    }

    private void bindViews(View v) {
        llMainCenter = v.findViewById(R.id.frg_merchant_detail_ll_main_cntnr);
        llBottomCenter = v.findViewById(R.id.frg_merchant_detail_ll_cntnr_bottom);
        rlBack = v.findViewById(R.id.frg_merchant_detail_rl_back);
        txvOfferTitle = v.findViewById(R.id.frg_merchant_detail_txv_merchant_title);
        imvProduct = v.findViewById(R.id.frg_merchant_detail_imv_product);
        imvMerchant = v.findViewById(R.id.frg_merchant_detail_imvMerchant);
        btnCall = v.findViewById(R.id.frg_merchant_detail_btn_call);
        btnDirection = v.findViewById(R.id.frg_merchant_detail_btn_direction);
        btnMenu = v.findViewById(R.id.frg_merchant_detail_btn_menu);
        txvMerchantName = v.findViewById(R.id.frg_merchant_detail_txv_merchant_name);
        txvMerchantWeekdaysTimings = v.findViewById(R.id.frg_merchant_detail_txv_weekdays_timing);
        txvMerchantWeekendsTimings = v.findViewById(R.id.frg_merchant_detail_txv_weekend_timing);
        txvDescription = v.findViewById(R.id.frg_merchant_detail_txv_description);
        txvNotFound = v.findViewById(R.id.frg_merchant_detail_txv_nt_found);
        lsvMerchantOffers = v.findViewById(R.id.frg_merchant_detial_lsv_offers);
        scrollView = v.findViewById(R.id.frg_merchant_detail_scrollview);
        lsvFooterView = ((LayoutInflater) requireContext().getSystemService(requireContext().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.lsv_footer, null, false);

        ViewGroup.LayoutParams params = imvProduct.getLayoutParams();
        int screenWidth = AppConfig.getScreenWidth();
        int height = (int) (screenWidth * 0.78);
        params.height = height;
        imvProduct.setLayoutParams(params);

        llMainCenter.setVisibility(View.GONE);


        rtlViewPager = v.findViewById(R.id.frg_merchant_detail_rtl_outlet_images);
        circlePageIndicator = v.findViewById(R.id.frg_merchant_detail_cpi_outlet_images);
        rtlViewPager.setOnTouchListener((v1, event) -> {
            if (timer != null) {
                timer.cancel();
            }
            return false;
        });

        if (offerName != null && offerName.length() > 0) {
            txvOfferTitle.setText(offerName);
        }
        if (merchantName != null && merchantName.length() > 0) {
            txvMerchantName.setText(merchantName);
        }
        if (merchantTimings != null && merchantTimings.length() > 0) {
            txvMerchantWeekendsTimings.setText(merchantTimings);
        }
        if (merchantDescription != null && merchantDescription.length() > 0) {
            txvDescription.setText(merchantDescription);
        }

        if (merchantImage != null &&
                merchantImage.length() > 0) {
            Picasso.get()
                    .load(AppConfig.getInstance().getBaseUrlImage() + merchantImage)
                    .error(R.drawable.icn_placeholder_banner)
                    .placeholder(R.drawable.icn_placeholder_banner)
                    .into(imvProduct);
        }

        if (merchantLogo != null &&
                merchantLogo.length() > 0) {
            Picasso.get()
                    .load(AppConfig.getInstance().getBaseUrlImage() + merchantLogo)
                    .error(R.drawable.rmv_place_holder)
                    .placeholder(R.drawable.rmv_place_holder)
                    .into(imvMerchant);
        } else {
            imvMerchant.setImageResource(R.drawable.rmv_place_holder);
        }


        rlBack.setOnClickListener(this);
        btnDirection.setOnClickListener(this);
        btnCall.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        imvMerchant.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.frg_merchant_detail_btn_menu) {
            if (merchantMenuType != null && !merchantMenuType.equalsIgnoreCase("")) {
                switch (merchantMenuType) {
                    case AppConstt.OutletMenuType.PDF:
                        ((MainUPActivity) requireActivity()).navToOutletMenuWebViewFragment(AppConfig.getInstance().getBaseUrlImage() + merchantMenu, AppConstt.OUTLET_MENU_PDF);
                        break;
                    case AppConstt.OutletMenuType.URL:
                        if (merchantMenu.endsWith(".pdf")) {
                            ((MainUPActivity) requireActivity()).navToOutletMenuWebViewFragment(merchantMenu, AppConstt.OUTLET_MENU_PDF);
                        } else {
                            ((MainUPActivity) requireActivity()).navToOutletMenuWebViewFragment(merchantMenu, AppConstt.OUTLET_MENU);
                        }
                        break;
                    case AppConstt.OutletMenuType.Image:
                        navToOutletMenuImagesFragment();
                        break;
                }
            }
        } else if (id == R.id.frg_merchant_detail_btn_direction) {
            callMapView();
        } else if (id == R.id.frg_merchant_detail_btn_call) {
            if (merchantPhone != null && !merchantPhone.equalsIgnoreCase("")) {
                showPhoneNumberDialog(merchantPhone);

            } else {
                AppConfig.getInstance().showAlertDialog(requireActivity(), null, getContext().getString(R.string.invalid__merchint_phone_number), getString(R.string.btn_okay), null, false, true, null);
            }
        }

        else if (id == R.id.frg_merchant_detail_rl_back) {
            requireFragmentManager().popBackStackImmediate();
        }
    }

    private void navToOutletMenuImagesFragment() {
        Fragment fr = new MerchantMenuImagesFragment();
        FragmentTransaction ft = requireFragmentManager().beginTransaction();
        ft.add(R.id.main_act_content_frame, fr, AppConstt.FrgTag.MerchantMenuImagesFragment);
        ft.addToBackStack(AppConstt.FrgTag.MerchantMenuImagesFragment);
        ft.hide(this);
        ft.commit();
    }

    //region Dialogs

    private void showPhoneNumberDialog(String phoneNumber) {
        String[] phoneNumbers = null;

        //Making Array by splitting String
        if (phoneNumber != null && phoneNumber.length() > 0 && phoneNumber.contains(";")) {
            phoneNumbers = phoneNumber.trim().split(";");
        } else if (phoneNumber != null && phoneNumber.length() > 0 && phoneNumber.contains(",")) {
            phoneNumbers = phoneNumber.trim().split(",");
        } else if (phoneNumber != null && phoneNumber.length() > 0) {
            phoneNumbers = new String[]{phoneNumber};
        }

        if (phoneNumbers != null) {
            ArrayList<String> lstPhoneNumbers = new ArrayList<>(Arrays.asList(phoneNumbers));
            MultipleMobileNumberDialog multipleMobileNumberDialog = new MultipleMobileNumberDialog(requireContext(), lstPhoneNumbers, new CustomAlertMultipleMobileInterface() {
                @Override
                public void callConfirmationDialogPositive(int pos) {
                    int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                    int hasCallPermission = ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CALL_PHONE);
                    if (currentApiVersion > 22 && hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                REQUEST_CODE_ASK_PERMISSIONS_CALL);
                    } else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + lstPhoneNumbers.get(pos)));
                        startActivity(callIntent);
                    }
                }

                @Override
                public void callConfirmationDialogNegative() {

                }
            });
            multipleMobileNumberDialog.setCancelable(true);
            multipleMobileNumberDialog.show();
        }
    }



    private void showOfferDetailAlertDialog(DModel_OfferDetail dModel_offerDetail) {
            OfferDetailAlertDialog cdd = new OfferDetailAlertDialog(requireContext(), this, dModel_offerDetail, null);
            cdd.show();
            cdd.setCancelable(true);

    }
    //endregion

    //region Api Callings

    private void requestMerchantDetail(int _outletId) {
        progressDialogue.startIOSLoader(requireActivity());
        isAlreadyFetchingOffers = true;
        MerchantDetail_WebHit_Get_getMerchantDetail MerchantDetail_WebHit_Get_getMerchantDetail = new MerchantDetail_WebHit_Get_getMerchantDetail();
        MerchantDetail_WebHit_Get_getMerchantDetail.requestOfferDetail(getContext(), new IWebCallback() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                isAlreadyFetchingOffers = false;

                if (isSuccess) {
                    updateMerchantDetail();
                } else if (strMsg.equalsIgnoreCase(AppConstt.ServerStatus.DATABASE_NOT_FOUND + "")) {
                    progressDialogue.stopIOSLoader();
                    shouldGetMoreOffers = false;
                    if (lstMerchantOffers.size() > 0) {
                        lsvMerchantOffers.removeFooterView(lsvFooterView);
                        merchantOffersAdapter.notifyDataSetChanged();
                    } else {
                        llMainCenter.setVisibility(View.GONE);
                        txvNotFound.setVisibility(View.VISIBLE);
                        AppConfig.getInstance().showAlertDialog(requireActivity(),null, getResources().getString(R.string.no_offers_found_in_merchant), getResources().getString(R.string.btn_done), null, false, false, new CustomAlertConfirmationInterface() {
                            @Override
                            public void callConfirmationDialogPositive() {
                                requireActivity().onBackPressed();
                                AppConfig.getInstance().removeRecentViewed(outletId);
                            }

                            @Override
                            public void callConfirmationDialogNegative() {

                            }
                        });


                    }

                } else {
                    progressDialogue.stopIOSLoader();
                    llMainCenter.setVisibility(View.GONE);
                    txvNotFound.setVisibility(View.VISIBLE);
                    AppConfig.getInstance().showAlertDialog(requireActivity(),null, strMsg, getResources().getString(R.string.btn_done), null, false, false, new CustomAlertConfirmationInterface() {
                        @Override
                        public void callConfirmationDialogPositive() {
                            requireActivity().onBackPressed();
                            AppConfig.getInstance().removeRecentViewed(outletId);
                        }

                        @Override
                        public void callConfirmationDialogNegative() {

                        }
                    });
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDialogue.stopIOSLoader();
                isAlreadyFetchingOffers = false;
                llMainCenter.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
                AppConfig.getInstance().showAlertDialog(requireActivity(),null, AppConfig.getInstance().getNetworkExceptionMessage(ex.getMessage()), null, null, false, true, null);
            }


        }, _outletId);
    }

    private void requestMerchantDetailOffers(int _outletId, int _page) {
        isAlreadyFetchingOffers = true;
        MerchantDetail_WebHit_Get_getMerchantDetailOffers MerchantDetail_WebHit_Get_getMerchantDetailOffers = new MerchantDetail_WebHit_Get_getMerchantDetailOffers();
        MerchantDetail_WebHit_Get_getMerchantDetailOffers.requestOfferDetail(getContext(), new IWebCallback() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                isAlreadyFetchingOffers = false;

                if (isSuccess) {
                    updateOffersValues();
                } else if (strMsg.equalsIgnoreCase(AppConstt.ServerStatus.DATABASE_NOT_FOUND + "")) {
                    progressDialogue.stopIOSLoader();
                    shouldGetMoreOffers = false;
                    if (lstMerchantOffers.size() > 0) {
                        lsvMerchantOffers.removeFooterView(lsvFooterView);
                        merchantOffersAdapter.notifyDataSetChanged();
                    } else {
                        llMainCenter.setVisibility(View.GONE);
                        txvNotFound.setVisibility(View.VISIBLE);
                    }

                } else {
                    llMainCenter.setVisibility(View.GONE);
                    txvNotFound.setVisibility(View.VISIBLE);
                    AppConfig.getInstance().showAlertDialog(requireActivity(),null, strMsg, null, null, false, true, null);

                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDialogue.stopIOSLoader();
                isAlreadyFetchingOffers = false;
                llMainCenter.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
                AppConfig.getInstance().showAlertDialog(requireActivity(),null, AppConfig.getInstance().getNetworkExceptionMessage(ex.getMessage()), null, null, false, true, null);
            }

        }, _outletId, _page);
    }

    private void updateMerchantDetail() {
        if (MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData() != null &&
                MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().size() > 0) {
            llMainCenter.setVisibility(View.VISIBLE);
            txvNotFound.setVisibility(View.GONE);

            // This one is for pagination
            if (MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().size() <= 20) {
                shouldGetMoreOffers = false;
            }

            String[] strArray = MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getName().split("-");
            SpannableString spannedMerchantName = new SpannableString(MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getName());
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            spannedMerchantName.setSpan(boldSpan, 0, strArray[0].length(), Spanned.SPAN_COMPOSING);

            txvOfferTitle.setText(spannedMerchantName);
            offerName=spannedMerchantName.toString();
            String weekDaytime=MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getTimings();
            String weekDaytimeLineBreak=weekDaytime.replace(",","\n");
            txvMerchantName.setText(MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getAddress());
            txvMerchantWeekdaysTimings.setText(weekDaytimeLineBreak);
            txvMerchantWeekendsTimings.setText(MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getTimings());
            txvDescription.setText(MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getDescription());

            if (MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getImage() != null &&
                    MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getImage().length() > 0) {
                Picasso.get()
                        .load(AppConfig.getInstance().getBaseUrlImage() + MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getImage())
                        .into(imvProduct);
            }

            if (MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getLogo() != null &&
                    MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getLogo().length() > 0) {
                Picasso.get()
                        .load(AppConfig.getInstance().getBaseUrlImage() + MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getLogo())
                        .into(imvMerchant);
            } else {
                imvMerchant.setImageResource(R.drawable.rmv_place_holder);
            }

            if (MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOutlet_images() != null &&
                    MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOutlet_images().size() > 0) {
                merchantImagesAdapter = new MerchantImagesAdapter(getActivity(), MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOutlet_images());
                rtlViewPager.setAdapter(merchantImagesAdapter);
                rtlViewPager.setVisibility(View.VISIBLE);
                if (MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOutlet_images().size() > 1) {
                    circlePageIndicator.setVisibility(View.VISIBLE);
                    if (timer != null) {
                        timer.cancel();
                        timer.purge();
                    }
                    currentPage = 0;
                    mIndicatorPosition = 0;
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new timerTask(), 00, 5000);
                } else {
                    circlePageIndicator.setVisibility(View.GONE);
                }
                rtlViewPager.setOffscreenPageLimit(1);
                circlePageIndicator.setViewPager(rtlViewPager);
                imvProduct.setVisibility(View.GONE);
            } else {
                imvProduct.setVisibility(View.VISIBLE);
                rtlViewPager.setVisibility(View.GONE);
                circlePageIndicator.setVisibility(View.GONE);
            }

            txvOfferTitle.setVisibility(View.VISIBLE);
            txvMerchantName.setVisibility(View.VISIBLE);
            txvDescription.setVisibility(View.VISIBLE);
            imvMerchant.setVisibility(View.VISIBLE);
            //imvProduct.setVisibility(View.VISIBLE);

            if (MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getLatitude() != null) {
                directionLatitude = MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getLatitude();
            }

            if (MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getLongitude() != null) {
                directionLongitude = MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getLongitude();
            }



            if (MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getPhone() != null) {
                merchantPhone = MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getPhone();
            }

            if (MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOutlet_menu() != null &&
                    MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOutlet_menu().size() > 0) {
                btnMenu.setVisibility(View.VISIBLE);
                merchantMenuType = MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOutlet_menu().get(0).getType();
                merchantMenu = MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOutlet_menu().get(0).getFile();
            } else {
                btnMenu.setVisibility(View.GONE);
            }





            if (MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData() != null &&
                    MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers() != null &&
                    MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().size() > 0) {
                for (int i = 0; i < MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().size(); i++) {

                    String strImageUrl = "";
                    if (MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getImage() != null) {
                        strImageUrl = MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getImage();
                    }


                    String totalSaving = MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getApproxSaving();
                    float totalSavingsFloat = Float.parseFloat(totalSaving);
                    int totalSavingsRounded = (int) totalSavingsFloat;
                    String approxSavings, currencyUnit;

                    currencyUnit = getResources().getString(R.string.txv_qatar_riyal);

                    approxSavings = currencyUnit + " " + totalSavingsRounded;

                    String savedPercentage;
                    if (MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getPercentage_saving() != null) {
                        savedPercentage = String.valueOf(MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getPercentage_saving());
                    } else {
                        savedPercentage = "00.00";
                    }
                    int approxPerRounded = 0;
                    float approxPerFloat = Float.parseFloat(savedPercentage);
                    approxPerRounded = (int) approxPerFloat;
                    String savedPer =  "" + approxPerRounded;

                    boolean canRedeem = MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).isRedeeme();

                    String special, specialType;
                    if (MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getSpecial() != null &&
                            MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getSpecial().length() > 0) {
                        special = MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getSpecial();
                    } else {
                        special = "0";
                    }
                    if (MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getSpecialType() != null &&
                            MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getSpecialType().length() > 0) {
                        specialType = MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getSpecialType();
                    } else {
                        specialType = "";
                    }
                    lstMerchantOffers.add(new DModelOutletOffers(
                            MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getId(),
                            strImageUrl,
                            MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getTitle(),
                            MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getDescription(),
                            MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getOutletName(),
                            MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getAddress(),
                            MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getId(),
                            MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getLogo(),
                            MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getCategoryIds(),
                            "",
                            0,
                            special,
                            specialType,
                            MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getValidFor(),
                            approxSavings,
                            false,
                            false,
                            canRedeem,
                            MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getRenew(),
                            MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getRenewDate(),
                            MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getEndDatetime(),
                            MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).isCanSendGift(),
                            MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getDiscount_type(),
                            savedPer
                    ));
                }
                lsvMerchantOffers.setFocusable(false);
                lsvMerchantOffers.setExpanded(true);

                if (merchantOffersAdapter != null) {
                    lsvMerchantOffers.removeFooterView(lsvFooterView);
                    merchantOffersAdapter.notifyDataSetChanged();
                } else {
                    merchantOffersAdapter = new OutletOffersAdapter(getActivity(), this, lstMerchantOffers);
                    lsvMerchantOffers.setAdapter(merchantOffersAdapter);
                }
            }
            llBottomCenter.setVisibility(View.VISIBLE);
            progressDialogue.stopIOSLoader();
        } else {
            progressDialogue.stopIOSLoader();
            llMainCenter.setVisibility(View.GONE);
            txvNotFound.setVisibility(View.VISIBLE);
            AppConfig.getInstance().showAlertDialog(requireActivity(),null, getResources().getString(R.string.no_offers_found_in_merchant), getResources().getString(R.string.btn_done), null, false, false, new CustomAlertConfirmationInterface() {
                @Override
                public void callConfirmationDialogPositive() {
                    AppConfig.getInstance().removeRecentViewed(outletId);
                    requireActivity().onBackPressed();
                }

                @Override
                public void callConfirmationDialogNegative() {

                }
            });


        }
    }

    private void updateOffersValues() {
        if (MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData() != null &&
                MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().size() > 0) {

            llMainCenter.setVisibility(View.VISIBLE);
            txvNotFound.setVisibility(View.GONE);

            // This one is for pagination
            if (MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().size() <= 20) {
                shouldGetMoreOffers = false;
            }

            txvOfferTitle.setVisibility(View.VISIBLE);
            txvMerchantName.setVisibility(View.VISIBLE);
            txvDescription.setVisibility(View.VISIBLE);
            imvMerchant.setVisibility(View.VISIBLE);
            // imvProduct.setVisibility(View.VISIBLE);

            if (MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(0).getLatitude() != null) {
                directionLatitude = MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(0).getLatitude();
            }

            if (MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(0).getLongitude() != null) {
                directionLongitude = MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(0).getLongitude();
            }


            if (MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData() != null &&
                    MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().size() > 0) {
                for (int i = 0; i < MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().size(); i++) {

                    String strImageUrl = "";
                    if (MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getImage() != null) {
                        strImageUrl = MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getImage();
                    }

                    String totalSaving = MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getApproxSaving();
                    float totalSavingsFloat = Float.parseFloat(totalSaving);
                    int totalSavingsRounded = (int) totalSavingsFloat;
                    String approxSavings, currencyUnit;

                    currencyUnit = getResources().getString(R.string.txv_qatar_riyal);

                    approxSavings = currencyUnit + " " + totalSavingsRounded;
                    String savedPercentage;
                    if (MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getPercentage_saving() != null) {
                        savedPercentage = String.valueOf(MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getPercentage_saving());
                    } else {
                        savedPercentage = "00.00";
                    }
                    int approxPerRounded = 0;
                    float approxPerFloat = Float.parseFloat(savedPercentage);
                    approxPerRounded = (int) approxPerFloat;
                    String savedPer =  "" + approxPerRounded;
                    boolean canRedeem = MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).isRedeeme();

                    String special, specialType;
                    if (MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getSpecial() != null &&
                            MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getSpecial().length() > 0) {
                        special = MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getSpecial();
                    } else {
                        special = "0";
                    }
                    if (MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getSpecialType() != null &&
                            MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getSpecialType().length() > 0) {
                        specialType = MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getSpecialType();
                    } else {
                        specialType = "";
                    }

                    lstMerchantOffers.add(new DModelOutletOffers(
                            MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getId(),
                            strImageUrl,
                            MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getTitle(),
                            MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getDescription(),
                            MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getOutlet_name(),
                            MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getOutlet_address(),
                            MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getOutletId(),
                            MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getOutlet_logo(),
                            MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getCategory_id(),
                            "",
                            0,
                            special,
                            specialType,
                            MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getValidFor(),
                            approxSavings,
                            false,
                            false,
                            canRedeem,
                            MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getRenew(),
                            MerchantDetail_WebHit_Get_getMerchantDetailOffers.responseObject.getData().get(i).getRenewDate(),
                            MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getEndDatetime(),
                            MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).isCanSendGift(),
                            MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOffers().get(i).getDiscount_type(),
                            savedPer
                    ));
                }
                lsvMerchantOffers.setFocusable(false);
                lsvMerchantOffers.setExpanded(true);

                if (merchantOffersAdapter != null) {
                    lsvMerchantOffers.removeFooterView(lsvFooterView);
                    merchantOffersAdapter.notifyDataSetChanged();
                } else {
                    merchantOffersAdapter = new OutletOffersAdapter(getActivity(), this, lstMerchantOffers);
                    lsvMerchantOffers.setAdapter(merchantOffersAdapter);
                }
            }
            llBottomCenter.setVisibility(View.VISIBLE);
            progressDialogue.stopIOSLoader();
        } else {
            progressDialogue.stopIOSLoader();
            llMainCenter.setVisibility(View.GONE);
            txvNotFound.setVisibility(View.VISIBLE);
        }
    }

    //endregion

    //region  Maps
    private void callMapView() {
        if (!directionLatitude.equals("")) {
            //mLatitude = Double.parseDouble(mEventDetailsModel.getmLatitude());
            try {
                String url = "http://maps.google.com/maps?f=d&daddr=" + directionLatitude
                        + "," + directionLongitude + "&dirflg=d";
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                intent.setClassName("com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity");
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?f=d&daddr=" + directionLatitude
                                + "," + directionLongitude));
                startActivity(intent);
            }
        }
    }



    //endregion



    public class timerTask extends TimerTask {

        @Override
        public void run() {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            if (rtlViewPager.getCurrentItem() == MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOutlet_images().size() - 1) {
                                currentPage = 0;
                                mIndicatorPosition = 0;
                                //updateIndicatorViews(mIndicatorPosition);
                            }
                            rtlViewPager.setCurrentItem(currentPage, true);
                            currentPage = currentPage + 1;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }
    }



}