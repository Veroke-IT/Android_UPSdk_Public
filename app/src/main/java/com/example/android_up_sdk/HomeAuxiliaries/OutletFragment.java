package com.example.android_up_sdk.HomeAuxiliaries;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

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

import com.example.android_up_sdk.Dialogs.CustomBottomSheetDialog;
import com.example.android_up_sdk.Dialogs.OfferDetailAlertDialog;
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.ExpandableMerchintListAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.SearchOffersAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModelMerchintList;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_OfferDetail;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_Recent_Outlet;
import com.example.android_up_sdk.HomeAuxiliaries.WebServices.CategoryOffers_WebHit_Get_getOutlet;
import com.example.android_up_sdk.HomeAuxiliaries.WebServices.SearchOffers_WebHit_Get_getOffersSearchTags;
import com.example.android_up_sdk.MainUPActivity;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.example.android_up_sdk.Utils.CustomAlertConfirmationInterface;
import com.example.android_up_sdk.Utils.GPSTracker;
import com.example.android_up_sdk.Utils.IWebCallback;
import com.example.android_up_sdk.Utils.ProgressDialogue;

import java.util.ArrayList;

public class OutletFragment extends Fragment {
    private ExpandableListView lsvOutlets;
    private ListView lsvSearchOffers;
    private ArrayList<String> lstSearchOffers;
    private ArrayList<DModelMerchintList> lstCategoryOutlets;
    private ArrayList<DModelMerchintList.Child> lstCategoryOutletsChild;
    private SearchOffersAdapter searchOffersAdapter;
    private ExpandableMerchintListAdapter expandableMerchintListAdapter;
    private TextView   txvNotFound;
    private View lsvFooterView;
    private double lat, lng;
    private String strSortBy, strSortType, strCollectionId, strPlaylistId, strParentId, searchTags;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private int page, intCategoryId, intPopularCategoryId, interestId, listSizeOffset;
    private boolean  isThisFragmentVisible;
    private boolean shouldGetMoreOffers, isAlreadyFetchingOffers, shouldRequestWebHit;
    private DModel_OfferDetail dModel_offerDetail;

    private CategoryParentHubFragment categoryParentHubFragment;

    String strTobeSearch;
    ProgressDialogue progressDialogue;


    private static final String KEY_POSITION = "position";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_outlet, container, false);
        initialize();
        bindViews(view);

        if (isThisFragmentVisible) {
            updateInitialValues();
        }

//        if (Build.VERSION.SDK_INT < 23) {
//            requestSetLocationPermission(1);
//        }

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
                            requestCategoryOutlets(page, intCategoryId, strSortBy, strSortType, lat, lng, false, false, searchTags);
                        }
                    }

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        lsvSearchOffers.setOnItemClickListener((parent, view1, position, id) -> {
            if (lstSearchOffers != null && lstSearchOffers.size() > 0) {
                searchTags = lstSearchOffers.get(position);
                page = 1;
                listSizeOffset = 0;
                lstCategoryOutlets.clear();
                shouldGetMoreOffers = true;
                requestCategoryOutlets(page, intCategoryId, strSortBy, strSortType, lat, lng, true, true, searchTags);

            }
            AppConfig.getInstance().closeKeyboard(requireActivity());
            lsvSearchOffers.setVisibility(View.GONE);
        });

        //Following Both listener is for adjusting the list size, later this size is used for pagination
        lsvOutlets.setOnGroupExpandListener(groupPosition -> {
            if (lstCategoryOutlets != null &&
                    lstCategoryOutlets.size() > 0 &&
                    lstCategoryOutlets.get(groupPosition).getChild() != null &&
                    lstCategoryOutlets.get(groupPosition).getChild().size() > 0) {

                listSizeOffset = listSizeOffset + lstCategoryOutlets.get(groupPosition).getChild().size();

                AppConfig.getInstance().updateRecentViewed(new DModel_Recent_Outlet(lstCategoryOutlets.get(groupPosition).getId(),lstCategoryOutlets.get(groupPosition).getMerchantsLogoUrl(),
                        lstCategoryOutlets.get(groupPosition).getMerchantAddress(),lstCategoryOutlets.get(groupPosition).getMerchantName()));

            }

        });

        lsvOutlets.setOnGroupCollapseListener(groupPosition -> {
            if (lstCategoryOutlets != null &&
                    lstCategoryOutlets.size() > 0 &&
                    lstCategoryOutlets.get(groupPosition).getChild() != null &&
                    lstCategoryOutlets.get(groupPosition).getChild().size() > 0) {
                listSizeOffset = listSizeOffset - lstCategoryOutlets.get(groupPosition).getChild().size();
            }

        });
        return view;
    }

    public static OutletFragment newInstance(int position, String _sortBy) {
        OutletFragment frag = new OutletFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        args.putString(AppConstt.BundleStrings.catSortBy, _sortBy);
        frag.setArguments(args);

        return (frag);
    }

    private void initialize() {
        page = 1;
        listSizeOffset = 0;
        searchTags = "";
        lat = AppConstt.DEFAULT_VALUES.DEFAULT_LAT;
        lng = AppConstt.DEFAULT_VALUES.DEFAULT_LNG;
        shouldRequestWebHit = true;
        shouldGetMoreOffers = true;
        isAlreadyFetchingOffers = false;
        progressDialogue = new ProgressDialogue();
        lstSearchOffers = new ArrayList<>();
        lstCategoryOutlets = new ArrayList<>();
        lstCategoryOutletsChild = new ArrayList<>();
        dModel_offerDetail = new DModel_OfferDetail();

        intCategoryId = AppConfig.getInstance().mCategorySorting.getCategoryId();
        intPopularCategoryId = AppConfig.getInstance().mCategorySorting.getPopularCategoryId();
        interestId = AppConfig.getInstance().mCategorySorting.getInterestId();
        strCollectionId = AppConfig.getInstance().mCategorySorting.getCollectionId();
        strPlaylistId = AppConfig.getInstance().mCategorySorting.getPlaylistId();
        strParentId = AppConfig.getInstance().mCategorySorting.getParentId();
        strSortType = AppConfig.getInstance().mCategorySorting.getGenderSelectedSortType();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            strSortBy = bundle.getString(AppConstt.BundleStrings.catSortBy);
        } else {
            strSortBy = AppConfig.getInstance().mCategorySorting.getSortBy();
        }




    }
    private void bindViews(View frg) {
        txvNotFound = frg.findViewById(R.id.frg_category_outlets_txv_nt_found);
        lsvOutlets = frg.findViewById(R.id.frg_category_outlets_lsv);
        lsvSearchOffers = frg.findViewById(R.id.frg_category_outlets_lsv_autocomplte);

        lsvFooterView = ((LayoutInflater) requireContext().getSystemService(requireContext().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.lsv_footer, null, false);


        lsvOutlets.setOnChildClickListener(
                (parent, v, groupPosition, childPosition, id) -> {
                    if (lstCategoryOutlets.get(groupPosition).getChild() != null && lstCategoryOutlets.get(groupPosition).getChild().size() > 0) {
                        int offerId = Integer.parseInt(lstCategoryOutlets.get(groupPosition).getChild().get(childPosition).getProductId());
                        String saveApprox = lstCategoryOutlets.get(groupPosition).getChild().get(childPosition).getApproxSavings();


                        String strAddress = lstCategoryOutlets.get(groupPosition).getMerchantName() + " - " + lstCategoryOutlets.get(groupPosition).getMerchantAddress();
                        dModel_offerDetail = new DModel_OfferDetail(offerId,
                                groupPosition,
                                childPosition,
                                "lstCategoryOutlets",
                                saveApprox,
                                lstCategoryOutlets.get(groupPosition).getChild().get(childPosition).getName(),
                                lstCategoryOutlets.get(groupPosition).getChild().get(childPosition).getDescription(),
                                lstCategoryOutlets.get(groupPosition).getChild().get(childPosition).getCategoryId(),
                                lstCategoryOutlets.get(groupPosition).getChild().get(childPosition).getOutletName(),
                                lstCategoryOutlets.get(groupPosition).getMerchantAddress(),
                                lstCategoryOutlets.get(groupPosition).getId(),
                                lstCategoryOutlets.get(groupPosition).getMerchantsLogoUrl(),
                                lstCategoryOutlets.get(groupPosition).getChild().get(childPosition).getDetailNExclusions(),
                                lstCategoryOutlets.get(groupPosition).getChild().get(childPosition).isFavorite(),
                                lstCategoryOutlets.get(groupPosition).getChild().get(childPosition).isCanRedeem(),
                                lstCategoryOutlets.get(groupPosition).getChild().get(childPosition).getExpiryDate(),
                                lstCategoryOutlets.get(groupPosition).getChild().get(childPosition).isCanSendGift(),
                                lstCategoryOutlets.get(groupPosition).getChild().get(childPosition).getDiscountType(),
                                lstCategoryOutlets.get(groupPosition).getChild().get(childPosition).getApproxSavingPercentage()
                        );
                        showOfferDetailAlertDialog(dModel_offerDetail);

                    }

                    return false;
                }
        );
    }

    public void updateInitialValues() {
        if (lstCategoryOutlets != null && lstCategoryOutlets.size() == 0) {
            performLocationSortClick();
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

    //region Permissions

    private void performLocationSortClick() {

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
//                                requestSetLocationPermission(0);
                                Fragment fragment3 = requireActivity().getSupportFragmentManager().findFragmentByTag(AppConstt.FrgTag.HomeDetailFragment);
                                if (fragment3 instanceof HomeDetailFragment){
                                    ((HomeDetailFragment) fragment3).setCurrentPage(1);
                                }
                                else {
                                    Fragment fragment2 = requireActivity().getSupportFragmentManager().findFragmentByTag(AppConstt.FrgTag.CategoryHubFragment);
                                    if (fragment2 instanceof CategoryHubFragment) {
                                        ((CategoryHubFragment) fragment2).setCurrentPage(1);
                                    }
                                    else {
                                        Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentByTag(AppConstt.FrgTag.CategoryParentHubFragment);
                                        if (fragment instanceof CategoryParentHubFragment) {
                                            ((CategoryParentHubFragment) fragment).setCurrentPage(1);
                                        }

                                    }
                                }

                                lsvOutlets.setVisibility(View.GONE);
                                lsvSearchOffers.setVisibility(View.GONE);
                                txvNotFound.setVisibility(View.VISIBLE);
                            }
                        });
                    } else {
                        GPSTracker.getInstance(requireContext()).getLocation();
                        lat = GPSTracker.getInstance(requireContext()).getLatitude();
                        lng = GPSTracker.getInstance(requireContext()).getLongitude();
                        if (lat != 0) {
                            requestCategoryOutlets(page, intCategoryId, strSortBy, strSortType, lat, lng, true, true, searchTags);
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
                    requestCategoryOutlets(page, intCategoryId, strSortBy, strSortType, lat, lng, true, true, searchTags);
                }
            }
        }
        else {
            strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                //was crashing here
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    GPSTracker.getInstance(getContext()).getLocation();
                    lat = GPSTracker.getInstance(getContext()).getLatitude();
                    lng = GPSTracker.getInstance(getContext()).getLongitude();
//                    requestSetLocationPermission(1);

                    if (lat != 0) {
                        strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
                        requestCategoryOutlets(page, intCategoryId, strSortBy, strSortType, lat, lng, true, true, searchTags);
                    } else {
                        AppConfig.getInstance().showAlertDialog(requireActivity(),getString(R.string.gps_connection_heading), getString(R.string.gps_connection_message), null, null, false, true, null);
                    }

                } else {
                    String permission = Manifest.permission.ACCESS_FINE_LOCATION;
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);

                    Fragment fragment2 = requireActivity().getSupportFragmentManager().findFragmentByTag(AppConstt.FrgTag.CategoryHubFragment);
                    if (fragment2 instanceof CategoryHubFragment) {
                        ((CategoryHubFragment) fragment2).setCurrentPage(1);
                    } else {
                        Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentByTag(AppConstt.FrgTag.CategoryParentHubFragment);
                        if (fragment instanceof CategoryParentHubFragment) {
                            ((CategoryParentHubFragment) fragment).setCurrentPage(1);
                        }

                    }

                    lsvOutlets.setVisibility(View.GONE);
                    lsvSearchOffers.setVisibility(View.GONE);
                    txvNotFound.setVisibility(View.VISIBLE);
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

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
//                requestSetLocationPermission(1);
                if (lat != 0) {
                    strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
                    requestCategoryOutlets(page, intCategoryId, strSortBy, strSortType, lat, lng, true, true, searchTags);
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
//                    requestSetLocationPermission(1);
                    if (lat != 0) {
                        requestCategoryOutlets(page, intCategoryId, strSortBy, strSortType, lat, lng, true, true, searchTags);
                    } else {
                        AppConfig.getInstance().showAlertDialog(requireActivity(),getString(R.string.gps_connection_heading), getString(R.string.gps_connection_message), null, null, false, true, null);
                    }
                }
            } else {
                Log.d("onActivityResult", "onActivityResult: " + requestCode);
            }

        }
    }
    //endregion


    //region Api Callings

    private void requestCategoryOutlets(int _page, final int _categoryId, String _sortBy, String _genderType, final double lat, double lng, final boolean _shouldClearLst, final boolean _shouldShowLoader, final String _strSearchTags) {
        if (_shouldShowLoader) {
            progressDialogue.startIOSLoader(getActivity());
        }
        isAlreadyFetchingOffers = true;
        CategoryOffers_WebHit_Get_getOutlet CategoryOffers_WebHit_Get_getOutlet = new CategoryOffers_WebHit_Get_getOutlet();
        CategoryOffers_WebHit_Get_getOutlet.requestCategoryOffers(getContext(), new IWebCallback() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDialogue.stopIOSLoader();
                isAlreadyFetchingOffers = false;

                if (isSuccess) {
                    if (_shouldClearLst) {
                        lstCategoryOutlets.clear();
                        expandableMerchintListAdapter = null;
                    }
                    updateCategoryOutletList(lat);
                } else if (strMsg.equalsIgnoreCase(AppConstt.ServerStatus.DATABASE_NOT_FOUND + "")) {
                    shouldGetMoreOffers = false;
                    if (lstCategoryOutlets.size() == 0) {
                        lsvOutlets.setVisibility(View.GONE);
                        lsvSearchOffers.setVisibility(View.GONE);
                        txvNotFound.setVisibility(View.VISIBLE);
                    } else {
                        lsvOutlets.removeFooterView(lsvFooterView);
                        shouldGetMoreOffers = false;
                        expandableMerchintListAdapter.notifyDataSetChanged();
                    }
                } else {
                    lsvOutlets.setVisibility(View.GONE);
                    lsvSearchOffers.setVisibility(View.GONE);
                    txvNotFound.setVisibility(View.VISIBLE);
                    AppConfig.getInstance().showAlertDialog(requireActivity(),null, strMsg, null, null, false, true, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDialogue.stopIOSLoader();
                isAlreadyFetchingOffers = false;
                shouldGetMoreOffers = true;
                lsvOutlets.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
                AppConfig.getInstance().showAlertDialog(requireActivity(),null, AppConfig.getInstance().getNetworkExceptionMessage(ex.getMessage()), null, null, false, true, null);

            }
        }, _page, _categoryId, intPopularCategoryId, strCollectionId, strPlaylistId, _sortBy, _genderType, lat, lng, false, _strSearchTags, strParentId, interestId);

    }

    private void updateCategoryOutletList(double _lat) {
        boolean isDistanceRequired;
        isDistanceRequired = _lat > 0 && strSortBy.equalsIgnoreCase(AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION);
        

        if (CategoryOffers_WebHit_Get_getOutlet.responseObject != null &&
                CategoryOffers_WebHit_Get_getOutlet.responseObject.getData() != null &&
                CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().size() > 0) {

            if (CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().size() < 20) {
                shouldGetMoreOffers = false;
            }

            for (int i = 0; i < CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().size(); i++) {
                if (CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers() != null &&
                        CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().size() > 0) {
                    String Gfestival = "", Gspecial = "0";
                    lstCategoryOutletsChild = new ArrayList<>();
                    for (int j = 0; j < CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().size(); j++) {
                        if (CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getActive().equalsIgnoreCase("1")) {
                            String Cfestival;
                            String Cspecial = "";


                            if (CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecial() != null
                                    && CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecial().length() > 0) {
                                Cspecial = CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecial();
                                if (Cspecial.equalsIgnoreCase("1")) {
                                    Gspecial = "1";
                                }
                                if (Cspecial.equalsIgnoreCase("1") &&
                                        CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecialType() != null &&
                                        CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecialType().length() > 0) {
                                    Cfestival = CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecialType();
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

                            String totalSaving = CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getApproxSaving();
                            float totalSavingsFloat = Float.parseFloat(totalSaving);
                            int totalSavingsRounded = (int) totalSavingsFloat;
                            String approxSavings, currencyUnit;

                            currencyUnit = getResources().getString(R.string.txv_qatar_riyal);


                            approxSavings = currencyUnit + " " + totalSavingsRounded;
                            String savedPercentage;
                            if (CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getPercentage_saving() != null) {
                                savedPercentage = String.valueOf(CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getPercentage_saving());
                            } else {
                                savedPercentage = "00.00";
                            }
                            int approxPerRounded = 0;
                            float approxPerFloat = Float.parseFloat(savedPercentage);
                            approxPerRounded = (int) approxPerFloat;
                            String savedPer =  "" + approxPerRounded;



                            String strImageUrl = "";
                            if (CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getImage() != null) {
                                strImageUrl = CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getImage();
                            }
                            lstCategoryOutletsChild.add(new DModelMerchintList.Child(
                                    CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getId(),
                                    strImageUrl,
                                    CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getTitle(),
                                    CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getDescription(),
                                    Cfestival,
                                    "",
                                    "",
                                    Cspecial,
                                    CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getValidFor(),
                                    approxSavings,
                                    CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getCategory_ids(),
                                    CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getOutlet_name(),
                                    false, true,
                                    CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getRenew(),
                                    CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getRenewDate(),
                                    CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getEndDatetime(),
                                    CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).isCanSendGift(),
                                    CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getDiscount_type(),
                                    savedPer
                            ));
                        }
                    }
                    int distance;
                    if (CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getDistance() >= 0) {
                        distance = (int) CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getDistance();
                    } else {
                        distance = 0;
                    }

                    String strImageUrl = "";
                    if (CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getImage() != null) {
                        strImageUrl = CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getImage();
                    }

                    lstCategoryOutlets.add(new DModelMerchintList(
                            CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getId(),
                            CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getName(),
                            CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getAddress(),
                            CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getTimings(),
                            CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getDescription(),
                            distance,
                            isDistanceRequired,
                            Gspecial,
                            Gfestival,
                            strImageUrl,
                            CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getLogo(),
                            CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getPhone(),
                            lstCategoryOutletsChild
                    ));

                }


            }

            lsvOutlets.setVisibility(View.VISIBLE);
            lsvSearchOffers.setVisibility(View.GONE);
            txvNotFound.setVisibility(View.GONE);
            lsvOutlets.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
            if (!(lstCategoryOutlets != null && lstCategoryOutlets.size() > 0)) {
                lsvOutlets.setVisibility(View.GONE);
                lsvSearchOffers.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
            } else if (expandableMerchintListAdapter != null) {
                lsvOutlets.removeFooterView(lsvFooterView);
                expandableMerchintListAdapter.notifyDataSetChanged();
            } else {
                expandableMerchintListAdapter = new ExpandableMerchintListAdapter(getContext(), this, lstCategoryOutlets);
                lsvOutlets.setAdapter(expandableMerchintListAdapter);
            }

        } else {
            if (lstCategoryOutlets.size()>0) {
                lsvOutlets.removeFooterView(lsvFooterView);
                shouldGetMoreOffers = false;
                expandableMerchintListAdapter.notifyDataSetChanged();
            }else {
                lsvOutlets.setVisibility(View.GONE);
                lsvSearchOffers.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
            }

        }
    }

    private void requestNewOffersAutoComplete(String _searchKey) {
        lstSearchOffers.clear();
        searchOffersAdapter = null;
        isAlreadyFetchingOffers = true;
        SearchOffers_WebHit_Get_getOffersSearchTags searchOffers_webhit_get_getOffersSearchTags = new SearchOffers_WebHit_Get_getOffersSearchTags();
        searchOffers_webhit_get_getOffersSearchTags.requestSearchOffers(getContext(), new IWebCallback() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                if (categoryParentHubFragment != null)
                    categoryParentHubFragment.updateSearchBar();
                isAlreadyFetchingOffers = false;
                if (isSuccess) {
                    updateListAutoComplete();
                } else if ((strMsg.equalsIgnoreCase(AppConstt.ServerStatus.DATABASE_NOT_FOUND + "")) ||
                        (strMsg.equalsIgnoreCase(AppConstt.ServerStatus.INTERNAL_SERVER_ERROR + ""))) {
                    shouldGetMoreOffers = false;
                    lsvOutlets.setVisibility(View.GONE);
                    lsvSearchOffers.setVisibility(View.GONE);
                    txvNotFound.setVisibility(View.VISIBLE);
                } else {
                    lsvOutlets.setVisibility(View.GONE);
                    lsvSearchOffers.setVisibility(View.GONE);
                    txvNotFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                if (categoryParentHubFragment != null)
                    categoryParentHubFragment.updateSearchBar();

                lsvSearchOffers.setVisibility(View.GONE);
                isAlreadyFetchingOffers = false;
                lsvOutlets.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
            }
        }, 1, _searchKey, intCategoryId);
    }


    private void updateListAutoComplete() {
        if (SearchOffers_WebHit_Get_getOffersSearchTags.responseObject != null &&
                SearchOffers_WebHit_Get_getOffersSearchTags.responseObject.getData() != null &&
                SearchOffers_WebHit_Get_getOffersSearchTags.responseObject.getData().getTags().size() > 0 &&
                strTobeSearch.length() > 0) {
            if (strTobeSearch.equalsIgnoreCase(SearchOffers_WebHit_Get_getOffersSearchTags.responseObject.getData().getSearch())) {
                lstSearchOffers.clear();
                for (int i = 0; i < SearchOffers_WebHit_Get_getOffersSearchTags.responseObject.getData().getTags().size(); i++) {
                    lstSearchOffers.add(SearchOffers_WebHit_Get_getOffersSearchTags.responseObject.getData().getTags().get(i));
                }

                lsvOutlets.setVisibility(View.GONE);
                lsvSearchOffers.setVisibility(View.VISIBLE);
                txvNotFound.setVisibility(View.GONE);
                if (searchOffersAdapter != null) {
                    searchOffersAdapter.notifyDataSetChanged();
                } else {
                    searchOffersAdapter = new SearchOffersAdapter(getContext(), lstSearchOffers);
                    lsvSearchOffers.setAdapter(searchOffersAdapter);
                }
            }
        } else {
            lsvOutlets.setVisibility(View.GONE);
            lsvSearchOffers.setVisibility(View.GONE);
            txvNotFound.setVisibility(View.VISIBLE);
        }
    }

    public void updateOutletLst() {
        lsvOutlets.setVisibility(View.VISIBLE);
        lsvSearchOffers.setVisibility(View.GONE);
        searchTags = "";
        AppConfig.getInstance().closeKeyboard(requireActivity());

    }


    //endregion



    public void validateSearch(Fragment _frg, String _strSearchKey) {
        if (_frg instanceof CategoryParentHubFragment) {
            categoryParentHubFragment = (CategoryParentHubFragment) _frg;
        }
        strTobeSearch = _strSearchKey;
        requestNewOffersAutoComplete(strTobeSearch);
    }

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

    public void navToMerchantDetailFragment(Bundle b) {
        if (getActivity() != null && isAdded()) {
//           Calling it from mainActivity:to resolve the crash occurs due to CleverTap lib version:4.0 .1
            ((MainUPActivity)getActivity()).navToMerchantDetailFragment(b);
        }

    }
}