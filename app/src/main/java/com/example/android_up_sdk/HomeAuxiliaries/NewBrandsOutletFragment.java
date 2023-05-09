package com.example.android_up_sdk.HomeAuxiliaries;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android_up_sdk.BuildConfig;
import com.example.android_up_sdk.Dialogs.CustomBottomSheetDialog;
import com.example.android_up_sdk.Dialogs.OfferDetailAlertDialog;
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.ExpandableMerchintListAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModelMerchintList;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_OfferDetail;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_Recent_Outlet;
import com.example.android_up_sdk.HomeAuxiliaries.WebServices.NewBrands_WebHit_Get_getNewBrandOutlets;
import com.example.android_up_sdk.MainUPActivity;
import com.example.android_up_sdk.MerchantDetailFragment;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.example.android_up_sdk.Utils.CustomAlertConfirmationInterface;
import com.example.android_up_sdk.Utils.GPSTracker;
import com.example.android_up_sdk.Utils.IWebCallback;
import com.example.android_up_sdk.Utils.ProgressDialogue;

import java.util.ArrayList;

public class NewBrandsOutletFragment extends Fragment {
    private ExpandableListView lsvOutlets;
    private ArrayList<DModelMerchintList> lstParentOutlets;
    ArrayList<DModelMerchintList.Child> lstParentOutletsChild;
    private ExpandableMerchintListAdapter expandableMerchintListAdapter;
    private TextView txvNotFound;
    private View lsvFooterView;
    private int page, strCategoryId, listSizeOffset;
    private double lat, lng;
    private String strSortBy;
    private Dialog dialog;
    private DModel_OfferDetail dModel_offerDetail;
    private boolean shouldGetMoreOffers, isAlreadyFetchingOffers, shouldRequestWebHit, isThisFragmentVisible;
    private static final int PERMISSION_REQUEST_CODE = 1;

    ProgressDialogue progressDialogue;



    private static final String KEY_POSITION = "position";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_new_brands, container, false);
        initialize();
        bindViews(view);
        lsvOutlets.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (shouldGetMoreOffers && !isAlreadyFetchingOffers) {
                    if (expandableMerchintListAdapter != null) {
                        if ((lsvOutlets.getLastVisiblePosition() - listSizeOffset == (expandableMerchintListAdapter.getGroupCount() - 1))) {
                            page++;
                            lsvOutlets.addFooterView(lsvFooterView);
                            expandableMerchintListAdapter.notifyDataSetChanged();
                            lsvOutlets.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
                            requestNewBrandOutlets(false, strSortBy, lat, lng);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        //Following Both listener is for adjusting the list size, later this size is used for pagination
        lsvOutlets.setOnGroupExpandListener(groupPosition -> {
            if (lstParentOutlets != null &&
                    lstParentOutlets.size() > 0 &&
                    lstParentOutlets.get(groupPosition).getChild() != null &&
                    lstParentOutlets.get(groupPosition).getChild().size() > 0) {
                listSizeOffset = listSizeOffset + lstParentOutlets.get(groupPosition).getChild().size();
                AppConfig.getInstance().updateRecentViewed(new DModel_Recent_Outlet(lstParentOutlets.get(groupPosition).getId(),lstParentOutlets.get(groupPosition).getMerchantsLogoUrl(),
                        lstParentOutlets.get(groupPosition).getMerchantAddress(),lstParentOutlets.get(groupPosition).getMerchantName()));

            }
        });

        lsvOutlets.setOnGroupCollapseListener(groupPosition -> {
            if (lstParentOutlets != null &&
                    lstParentOutlets.size() > 0 &&
                    lstParentOutlets.get(groupPosition).getChild() != null &&
                    lstParentOutlets.get(groupPosition).getChild().size() > 0) {
                listSizeOffset = listSizeOffset - lstParentOutlets.get(groupPosition).getChild().size();
            }
        });


        if (isThisFragmentVisible) {
            updateInitialValues();
        }
        return view;
    }


    public static NewBrandsOutletFragment newInstance(int position, String _sortBy) {
        NewBrandsOutletFragment frag = new NewBrandsOutletFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        args.putString(AppConstt.BundleStrings.catSortBy, _sortBy);
        frag.setArguments(args);

        return (frag);
    }

    private void initialize() {
        page = 1;
        listSizeOffset = 0;
        strCategoryId = 0;
        shouldRequestWebHit = true;
        shouldGetMoreOffers = true;
        isAlreadyFetchingOffers = false;
        progressDialogue = new ProgressDialogue();
        lstParentOutlets = new ArrayList<>();
        dModel_offerDetail = new DModel_OfferDetail();
        strCategoryId = AppConfig.getInstance().mCategorySorting.getCategoryId();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            strSortBy = bundle.getString(AppConstt.BundleStrings.catSortBy);
        } else {
            strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_ALPHABETICALLY;
        }


    }

    private void bindViews(View frg) {
        txvNotFound = frg.findViewById(R.id.frg_new_brands_txv_nt_found);
        lsvOutlets = frg.findViewById(R.id.frg_new_brands_lsv);
        lsvFooterView = ((LayoutInflater) requireContext().getSystemService(requireContext().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.lsv_footer, null, false);

        lsvOutlets.setOnChildClickListener(
                (parent, v, groupPosition, childPosition, id) -> {
                    if (lstParentOutlets.get(groupPosition).getChild() != null && lstParentOutlets.get(groupPosition).getChild().size() > 0) {
                        int offerId = Integer.parseInt(lstParentOutlets.get(groupPosition).getChild().get(childPosition).getProductId());
                        String saveApprox = lstParentOutlets.get(groupPosition).getChild().get(childPosition).getApproxSavings();


                        String strAddress = lstParentOutlets.get(groupPosition).getMerchantName() + " - " + lstParentOutlets.get(groupPosition).getMerchantAddress();
                        dModel_offerDetail = new DModel_OfferDetail(offerId,
                                groupPosition,
                                childPosition,
                                "lstParentOutlets",
                                saveApprox,
                                lstParentOutlets.get(groupPosition).getChild().get(childPosition).getName(),
                                lstParentOutlets.get(groupPosition).getChild().get(childPosition).getDescription(),
                                lstParentOutlets.get(groupPosition).getChild().get(childPosition).getCategoryId(),
                                lstParentOutlets.get(groupPosition).getChild().get(childPosition).getOutletName(),
                                lstParentOutlets.get(groupPosition).getMerchantAddress(),
                                lstParentOutlets.get(groupPosition).getId(),
                                lstParentOutlets.get(groupPosition).getMerchantsLogoUrl(),
                                lstParentOutlets.get(groupPosition).getChild().get(childPosition).getDetailNExclusions(),
                                lstParentOutlets.get(groupPosition).getChild().get(childPosition).isFavorite(),
                                lstParentOutlets.get(groupPosition).getChild().get(childPosition).isCanRedeem(),
                                lstParentOutlets.get(groupPosition).getChild().get(childPosition).getExpiryDate(),
                                lstParentOutlets.get(groupPosition).getChild().get(childPosition).isCanSendGift(),
                                lstParentOutlets.get(groupPosition).getChild().get(childPosition).getDiscountType(),
                                lstParentOutlets.get(groupPosition).getChild().get(childPosition).getApproxSavingPercentage()
                        );
                        showOfferDetailAlertDialog(dModel_offerDetail);

                    }
                    return false;
                }
        );
    }

    public void updateInitialValues() {
        if (lstParentOutlets != null && lstParentOutlets.size() == 0) {

            performLocationSortClick(true);

        }
    }

    //region Dialogs

    private void showContextualAlertDialog(final CustomAlertConfirmationInterface _customDialogConfirmationListener) {
        RelativeLayout locationPermission= (RelativeLayout) this.getLayoutInflater().inflate(R.layout.location_permission_layout, null);

        CustomBottomSheetDialog cdd = new CustomBottomSheetDialog(requireActivity(),requireContext().getResources().getString(R.string.btn_allow),requireContext().getResources().getString(R.string.btn_not_now), true, _customDialogConfirmationListener,locationPermission);
        cdd.show();
        cdd.setCancelable(true);


    }

    private void showOfferDetailAlertDialog(DModel_OfferDetail dModel_offerDetail) {
        OfferDetailAlertDialog cdd = new OfferDetailAlertDialog(requireContext(), this, dModel_offerDetail, null);
        cdd.show();
        cdd.setCancelable(true);
    }

    //endregion

    //region permissions


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    private void performLocationSortClick(boolean _shouldShowLoader) {

        if (shouldRequestWebHit) {
            if (!isAlreadyFetchingOffers) {
                if (strSortBy.equalsIgnoreCase(AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION)) {
                    shouldGetMoreOffers = true;
                    listSizeOffset = 0;
                    page = 1;
                    if (!AppConfig.isLocationEnabled(requireContext()) || !AppConfig.getInstance().checkPermission(requireContext())) {
                        showContextualAlertDialog(new CustomAlertConfirmationInterface() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void callConfirmationDialogPositive() {
                                if (AppConfig.isLocationEnabled(requireContext())) {
                                    requestPermission();
                                } else {
                                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), AppConstt.IntentPreference.SOURCE_LOCATION_INTENT_CODE);
                                }
                            }

                            @Override
                            public void callConfirmationDialogNegative() {

                                Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentByTag(AppConstt.FrgTag.NewBrandsPagerFragment);
                                if (fragment instanceof NewBrandsPagerFragment) {
                                    ((NewBrandsPagerFragment) fragment).setCurrentPage(1);
                                }


                                lsvOutlets.setVisibility(View.GONE);
                                txvNotFound.setVisibility(View.VISIBLE);
                            }
                        });
                    } else {
                        GPSTracker.getInstance(requireContext()).getLocation();
                        lat = GPSTracker.getInstance(requireContext()).getLatitude();
                        lng = GPSTracker.getInstance(requireContext()).getLongitude();
                        if (lat != 0) {
                            requestNewBrandOutlets(_shouldShowLoader, strSortBy, lat, lng);
                        } else {
                            AppConfig.getInstance().showAlertDialog(requireActivity(),getString(R.string.gps_connection_heading), getString(R.string.gps_connection_message), null, null, false, true, null);
                        }
                    }
                } else {
                    GPSTracker.getInstance(requireContext()).getLocation();
                    lat = GPSTracker.getInstance(requireContext()).getLatitude();
                    lng = GPSTracker.getInstance(requireContext()).getLongitude();
                    shouldGetMoreOffers = true;
                    page = 1;
                    listSizeOffset = 0;
                    strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_ALPHABETICALLY;
                    requestNewBrandOutlets(_shouldShowLoader, strSortBy, lat, lng);
                }
            }
        } else {
            strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstt.IntentPreference.PACKAGE_LOCATION_INTENT_CODE == requestCode) {
            if (AppConfig.getInstance().checkPermission(requireContext())) {
                GPSTracker.getInstance(requireContext()).getLocation();
                lat = GPSTracker.getInstance(requireContext()).getLatitude();
                lng = GPSTracker.getInstance(requireContext()).getLongitude();
                // requestSetLocationPermission(1);
                if (lat != 0) {
                    strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
                    requestNewBrandOutlets(true, strSortBy, lat, lng);
                } else {
                    AppConfig.getInstance().showAlertDialog(requireActivity(),getString(R.string.gps_connection_heading), getString(R.string.gps_connection_message), null, null, false, true, null);
                }
            }
        } else if (AppConstt.IntentPreference.SOURCE_LOCATION_INTENT_CODE == requestCode) {

            if (AppConfig.isLocationEnabled(requireContext())) {
                if (!AppConfig.getInstance().checkPermission(requireContext())) {
                    requestPermission();
                } else {
                    GPSTracker.getInstance(requireContext()).getLocation();
                    lat = GPSTracker.getInstance(requireContext()).getLatitude();
                    lng = GPSTracker.getInstance(requireContext()).getLongitude();

                    if (lat != 0) {
                        requestNewBrandOutlets(true, strSortBy, lat, lng);
                    } else {
                        AppConfig.getInstance().showAlertDialog(requireActivity(),getString(R.string.gps_connection_heading), getString(R.string.gps_connection_message), null, null, false, true, null);
                    }
                }
            } else {
                Log.d("onActivityResult", "onActivityResult: " + requestCode);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                //was crashing here
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    GPSTracker.getInstance(requireContext()).getLocation();
                    lat = GPSTracker.getInstance(requireContext()).getLatitude();
                    lng = GPSTracker.getInstance(requireContext()).getLongitude();

                    if (lat != 0) {
                        strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
                        requestNewBrandOutlets(true, strSortBy, lat, lng);
                    } else {
                        AppConfig.getInstance().showAlertDialog(requireActivity(),getString(R.string.gps_connection_heading), getString(R.string.gps_connection_message), null, null, false, true, null);
                    }

                } else {
                    String permission = Manifest.permission.ACCESS_FINE_LOCATION;
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                            startActivityForResult(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:")), AppConstt.IntentPreference.PACKAGE_LOCATION_INTENT_CODE);

                    }
                    //                    performAlphabeticallySortClick(true);


                    Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentByTag(AppConstt.FrgTag.NewBrandsPagerFragment);
                    if (fragment instanceof NewBrandsPagerFragment) {
                        ((NewBrandsPagerFragment) fragment).setCurrentPage(1);
                    }


                    lsvOutlets.setVisibility(View.GONE);
                    txvNotFound.setVisibility(View.VISIBLE);
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    //endregion

    //region Api Calling


    private void requestNewBrandOutlets(final boolean _shouldShowLoader, String _sortBy, final double lat, double lng) {
        if (_shouldShowLoader) {
            progressDialogue.startIOSLoader(requireActivity());
        }
        isAlreadyFetchingOffers = true;
        NewBrands_WebHit_Get_getNewBrandOutlets newBrands_webhit_get_getNewBrandOutlets = new NewBrands_WebHit_Get_getNewBrandOutlets();
        newBrands_webhit_get_getNewBrandOutlets.requestBrandOutlets(getContext(), new IWebCallback() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDialogue.stopIOSLoader();
                isAlreadyFetchingOffers = false;
                if (isSuccess) {
                    updateBrandOutletsList();
                } else if (strMsg.equalsIgnoreCase(AppConstt.ServerStatus.DATABASE_NOT_FOUND + "")) {
                    shouldGetMoreOffers = false;
                    if (lstParentOutlets.size() == 0) {
                        lsvOutlets.setVisibility(View.GONE);
                        txvNotFound.setVisibility(View.VISIBLE);
                    } else {
                        lsvOutlets.removeFooterView(lsvFooterView);
                        expandableMerchintListAdapter.notifyDataSetChanged();
                    }
                } else {
                    AppConfig.getInstance().showAlertDialog(requireActivity(),null, strMsg, null, null, false, true, null);

                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDialogue.stopIOSLoader();
                isAlreadyFetchingOffers = false;
                lsvOutlets.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
                AppConfig.getInstance().showAlertDialog(requireActivity(),null, AppConfig.getInstance().getNetworkExceptionMessage(ex.getMessage()), null, null, false, true, null);
            }
        }, page, strCategoryId, _sortBy, lat, lng);
    }


    private void updateBrandOutletsList() {

        if (NewBrands_WebHit_Get_getNewBrandOutlets.responseObject != null &&
                NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData() != null &&
                NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().size() > 0) {

            if (NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().size() < 20) {
                shouldGetMoreOffers = false;
            }

            for (int i = 0; i < NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().size(); i++) {
                if (NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers() != null &&
                        NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().size() > 0) {
                    String Gfestival = "", Gspecial = "0";
                    lstParentOutletsChild = new ArrayList<>();
                    for (int j = 0; j < NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().size(); j++) {
                        if (NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getActive().equalsIgnoreCase("1")) {
                            String Cfestival;
                            String Cspecial = "";


                            if (NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getSpecial() != null
                                    && NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getSpecial().length() > 0) {
                                Cspecial = NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getSpecial();
                                if (Cspecial.equalsIgnoreCase("1")) {
                                    Gspecial = "1";
                                }
                                if (Cspecial.equalsIgnoreCase("1") &&
                                        NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getSpecialType() != null &&
                                        NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getSpecialType().length() > 0) {
                                    Cfestival = NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getSpecialType();
                                    if (Gfestival.length() == 0) {
                                        Gfestival = Cfestival;
                                    }
                                } else {
                                    Cfestival = "";
                                }
                            } else {
                                Cspecial = "0";
                                Cfestival = "";
                            }

                            String totalSaving = NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getApproxSaving();
                            float totalSavingsFloat = Float.parseFloat(totalSaving);
                            int totalSavingsRounded = (int) totalSavingsFloat;
                            String approxSavings, currencyUnit;

                                currencyUnit = getResources().getString(R.string.txv_qatar_riyal);


                            approxSavings = currencyUnit + " " + totalSavingsRounded;
                            String savedPercentage;
                            if (NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getPercentage_saving() != null) {
                                savedPercentage = String.valueOf(NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getPercentage_saving());
                            } else {
                                savedPercentage = "00.00";
                            }
                            int approxPerRounded = 0;
                            float approxPerFloat = Float.parseFloat(savedPercentage);
                            approxPerRounded = (int) approxPerFloat;
                            String savedPer =  "" + approxPerRounded;



                            String strImageUrl = "";
                            if (NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getImage() != null) {
                                strImageUrl = NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getImage();
                            }
                            boolean canRedeem = NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).isRedeeme();
                            lstParentOutletsChild.add(new DModelMerchintList.Child(
                                    NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getId(),
                                    strImageUrl,
                                    NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getTitle(),
                                    NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getDescription(),
                                    Cfestival,
                                    "",
                                    "",
                                    Cspecial,
                                    NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getValidFor(),
                                    approxSavings,
                                    NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getCategory_ids(),
                                    NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getOutlet_name(),
                                    false, canRedeem,
                                    NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getRenew(),
                                    NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getRenewDate(),
                                    NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getEndDatetime(),
                                    NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).isCanSendGift(),
                                    NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getOffers().get(j).getDiscount_type(),
                                    savedPer
                            ));
                        }
                    }
                    int distance;
                    if (NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getDistance() >= 0) {
                        distance = (int) NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getDistance();
                    } else {
                        distance = 0;
                    }

                    String strImageUrl = "";
                    if (NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getImage() != null) {
                        strImageUrl = NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getImage();
                    }

                    lstParentOutlets.add(new DModelMerchintList(
                            NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getId(),
                            NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getName(),
                            NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getAddress(),
                            NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getTimings(),
                            NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getDescription(),
                            distance,
                            true,
                            Gspecial,
                            Gfestival,
                            strImageUrl,
                            NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getLogo(),
                            NewBrands_WebHit_Get_getNewBrandOutlets.responseObject.getData().get(i).getPhone(),
                            lstParentOutletsChild
                    ));
                }

            }

            lsvOutlets.setVisibility(View.VISIBLE);
            txvNotFound.setVisibility(View.GONE);
            lsvOutlets.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);

            if (expandableMerchintListAdapter != null) {
                lsvOutlets.removeFooterView(lsvFooterView);
                expandableMerchintListAdapter.notifyDataSetChanged();
            } else {
                expandableMerchintListAdapter = new ExpandableMerchintListAdapter(getContext(), this, lstParentOutlets);
                lsvOutlets.setAdapter(expandableMerchintListAdapter);
            }

        } else {
            lsvOutlets.removeFooterView(lsvFooterView);
            if (page == 1) {
                lsvOutlets.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
            }
        }
    }

    public void navToMerchantDetailFragment(Bundle b) {
        if (getActivity() != null && isAdded()) {
//           Calling it from mainActivity:to resolve the crash occurs due to CleverTap lib version:4.0 .1
            ((MainUPActivity)getActivity()).navToMerchantDetailFragment(b);
        }

    }


    //endregion


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isThisFragmentVisible = isVisibleToUser;
            if (isResumed()) {
                updateInitialValues();
            }
        }
    }


}