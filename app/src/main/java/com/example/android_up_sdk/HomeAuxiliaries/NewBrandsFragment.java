package com.example.android_up_sdk.HomeAuxiliaries;

import android.Manifest;
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
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.ExpandableParentOutletAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModelParentList;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_OfferDetail;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_Recent_Outlet;
import com.example.android_up_sdk.HomeAuxiliaries.WebServices.CategoryOffers_WebHit_Get_getOutletsParents;
import com.example.android_up_sdk.HomeAuxiliaries.WebServices.NewBrands_WebHit_Get_getAllNewBrands;
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


public class NewBrandsFragment extends Fragment {

    private ExpandableListView lsvOutlets;
    private ArrayList<DModelParentList> lstParentOutlets;
    ArrayList<DModelParentList.Child> lstParentOutletsChild;
    private ExpandableParentOutletAdapter expandableParentOutletAdapter;
    private TextView txvNotFound;
    private View lsvFooterView;
    private int page, strCategoryId, interestId, listSizeOffset;
    private double lat, lng;
    private String strSortBy;
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
                    if (expandableParentOutletAdapter != null) {
                        if ((lsvOutlets.getLastVisiblePosition() - listSizeOffset == (expandableParentOutletAdapter.getGroupCount() - 1))) {
                            page++;
                            lsvOutlets.addFooterView(lsvFooterView);
                            expandableParentOutletAdapter.notifyDataSetChanged();
                            lsvOutlets.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
                            requestNewBrands(false, strSortBy, lat, lng);
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
                AppConfig.getInstance().updateRecentViewed(new DModel_Recent_Outlet(lstParentOutlets.get(groupPosition).getStrOutletId(),lstParentOutlets.get(groupPosition).getStrOutletLogo(),
                        lstParentOutlets.get(groupPosition).getStrOutletAddress(),lstParentOutlets.get(groupPosition).getStrOutletName()));

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

    public static NewBrandsFragment newInstance(int position, String _sortBy) {
        NewBrandsFragment frag = new NewBrandsFragment();
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
        interestId = AppConfig.getInstance().mCategorySorting.getInterestId();

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
                        int offerId = Integer.parseInt(lstParentOutlets.get(groupPosition).getChild().get(childPosition).getStrOfferId());
                        String saveApprox = lstParentOutlets.get(groupPosition).getChild().get(childPosition).getApproxSavings();


                        String strAddress = lstParentOutlets.get(groupPosition).getStrOutletName() + " - " + lstParentOutlets.get(groupPosition).getStrOutletAddress();
                        dModel_offerDetail = new DModel_OfferDetail(offerId,
                                groupPosition,
                                childPosition,
                                "lstParentOutlets",
                                saveApprox,
                                lstParentOutlets.get(groupPosition).getChild().get(childPosition).getStrOfferName(),
                                lstParentOutlets.get(groupPosition).getChild().get(childPosition).getDescription(),
                                lstParentOutlets.get(groupPosition).getChild().get(childPosition).getStrCategoryId(),
                                lstParentOutlets.get(groupPosition).getChild().get(childPosition).getStrOutletName(),
                                lstParentOutlets.get(groupPosition).getStrOutletAddress(),
                                lstParentOutlets.get(groupPosition).getStrOutletId(),
                                lstParentOutlets.get(groupPosition).getStrOutletLogo(),
                                lstParentOutlets.get(groupPosition).getChild().get(childPosition).getDetailNExclusion(),
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

    //region permissions


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

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
                            requestNewBrands(true, strSortBy, lat, lng);
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
                    requestNewBrands(true, strSortBy, lat, lng);
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
                    requestNewBrands(true, strSortBy, lat, lng);
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
                        requestNewBrands(true, strSortBy, lat, lng);
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
                        requestNewBrands(true, strSortBy, lat, lng);
                    } else {
                        AppConfig.getInstance().showAlertDialog(requireActivity(),getString(R.string.gps_connection_heading), getString(R.string.gps_connection_message), null, null, false, true, null);
                    }

                } else {
                    String permission = Manifest.permission.ACCESS_FINE_LOCATION;
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                            startActivityForResult(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" )), AppConstt.IntentPreference.PACKAGE_LOCATION_INTENT_CODE);

                    }


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

    //region Navigation


    public void navToCategoryHubFragment(Bundle bundle) {
        ((MainUPActivity) requireActivity()).navToCategoryHubFragment(bundle);
    }

    public void navToMerchantDetailFragment(Bundle bundle) {
        if (getActivity() != null && isAdded()) {
            Fragment fr = new MerchantDetailFragment();
            fr.setArguments(bundle);
            FragmentManager fm = requireActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.main_act_content_frame, fr, AppConstt.FrgTag.MerchantDetailFragment);
            ft.addToBackStack(AppConstt.FrgTag.MerchantDetailFragment);
//            ft.hide(this);
            ft.commit();
        }
    }

    //endregion

    //region Api Calling


    private void requestNewBrands(final boolean _shouldShowLoader, String _sortBy, final double lat, double lng) {
        if (_shouldShowLoader) {
            progressDialogue.startIOSLoader(requireActivity());
        }
        isAlreadyFetchingOffers = true;
        NewBrands_WebHit_Get_getAllNewBrands newBrands_webhit_get_getAllNewBrands = new NewBrands_WebHit_Get_getAllNewBrands();
        newBrands_webhit_get_getAllNewBrands.requestNewBrands(getContext(), new IWebCallback() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDialogue.stopIOSLoader();
                isAlreadyFetchingOffers = false;
                if (isSuccess) {
                    updateParentOutletsList();
                } else if (strMsg.equalsIgnoreCase(AppConstt.ServerStatus.DATABASE_NOT_FOUND + "")) {
                    shouldGetMoreOffers = false;
                    if (lstParentOutlets.size() == 0) {
                        lsvOutlets.setVisibility(View.GONE);
                        txvNotFound.setVisibility(View.VISIBLE);
                    } else {
                        lsvOutlets.removeFooterView(lsvFooterView);
                        expandableParentOutletAdapter.notifyDataSetChanged();
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

    private void updateParentOutletsList() {

        if (NewBrands_WebHit_Get_getAllNewBrands.responseObject != null &&
                NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData() != null &&
                NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().size() > 0) {

            if (NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().size() < 20) {
                shouldGetMoreOffers = false;
            }


            for (int i = 0; i < NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().size(); i++) {
                String strParentId = NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getId();
                String strParentName = NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getName();
                String strOutletID = "";
                String strOutletName = "";
                String strOutletImage = "";
                String strOutletLogo = NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getLogo();
                String strOutletAddress = "";
                String strOutletTimmings = "";
                String strOutletDescription = "";
                String strOutletPhone = "";
                String strFestival = "";
                String strSpecial = "0";
                String Gfestival = "";
                boolean isOutletExpedible = false;

                int distance;
                if (NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getDistance()!=null){
                    if (NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getDistance().length() > 0) {
                        String strDistance = NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getDistance();
                        double roundedDistance = Double.parseDouble(strDistance);
                        distance = (int) roundedDistance;
                    } else {
                        distance = 0;
                    }
                }else {
                    distance = 0;
                }


                lstParentOutletsChild = new ArrayList<>();
                if (NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets() != null) {
                    strOutletID = NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getId();
                    strOutletName = NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getName();
                    strOutletAddress = NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getAddress();
                    strOutletTimmings = NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getTimings();
                    strOutletDescription = NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getDescription();
                    //strCategoryId = NewBrands_Webhit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().getCategory_id();
                    if (NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getImage() != null) {
                        strOutletImage = NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getImage();
                    }
                    if (NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getLogo() != null) {
                        strOutletLogo = NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getLogo();
                    }
                    if (NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getPhone() != null) {
                        strOutletPhone = NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getPhone();
                    }
                    if (NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getTotal_outlets()==1){

                        if (NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers() != null &&
                            NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().size() > 0) {
                        isOutletExpedible = true;
                        for (int j = 0; j < NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().size(); j++) {
                            String Cfestival;
                            String special = "";
                            if (NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getSpecial() != null
                                    && NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getSpecial().length() > 0) {
                                special = NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getSpecial();
                            } else {
                                if (Gfestival.length() == 0) {
                                    special = "0";
                                }
                            }
                            if (NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getSpecialType() != null &&
                                    NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getSpecialType().length() > 0) {
                                Cfestival = NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getSpecialType();
                                if (Gfestival.length() == 0) {
                                    Gfestival = NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getSpecialType();
                                }
                            } else {
                                Cfestival = "";
                            }




                            String totalSaving;
                            if (NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getApproxSaving() != null) {
                                totalSaving = NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getApproxSaving();
                            } else {
                                totalSaving = "0";
                            }

                            float totalSavingsFloat = Float.parseFloat(totalSaving);
                            int totalSavingsRounded = (int) totalSavingsFloat;
                            String approxSavings, currencyUnit;

                                currencyUnit = getResources().getString(R.string.txv_qatar_riyal);


                            approxSavings = currencyUnit + " " + totalSavingsRounded;
                            String savedPercentage;
                            if (NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getPercentage_saving() != null) {
                                savedPercentage = String.valueOf(NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getPercentage_saving());
                            } else {
                                savedPercentage = "00.00";
                            }
                            int approxPerRounded = 0;
                            float approxPerFloat = Float.parseFloat(savedPercentage);
                            approxPerRounded = (int) approxPerFloat;
                            String savedPer =  "" + approxPerRounded;



                            lstParentOutletsChild.add(new DModelParentList.Child(
                                    NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getId(),
                                    NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getTitle(),
                                    NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getCategory_ids(),
                                    NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getOutlet_name(),
                                    NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getDescription(),
                                    NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getImage(),
                                    Cfestival,
                                    special,
                                    NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getValidFor(),
                                    approxSavings,
                                    false, true,
                                    NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getRenew(),
                                    NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getRenewDate(),
                                    NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getEndDatetime(),
                                    NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).isCanSendGift(),
                                    NewBrands_WebHit_Get_getAllNewBrands.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getDiscount_type(),
                                    savedPer
                            ));//Todo FIX NEWBRANDS FRAGMENT FOR MODEL;
                        }
                    }
                        else {
                        isOutletExpedible = false;
                    }
                    }
                    else {
                        isOutletExpedible = false;
                    }
                }
                lstParentOutlets.add(new DModelParentList(
                        strParentId,
                        strParentName,
                        strOutletID,
                        strOutletName,
                        strOutletImage,
                        strOutletLogo,
                        strOutletAddress,
                        strOutletTimmings,
                        strOutletDescription,
                        strOutletPhone,
                        strFestival,
                        strSpecial,
                        strCategoryId,
                        0,
                        interestId,
                        AppConstt.DEFAULT_VALUES.SORT_BY_ALPHABETICALLY,
                        distance,
                        isOutletExpedible,
                        false,
                        lstParentOutletsChild
                ));
            }


            lsvOutlets.setVisibility(View.VISIBLE);
            txvNotFound.setVisibility(View.GONE);
            lsvOutlets.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);

            if (expandableParentOutletAdapter != null) {
                lsvOutlets.removeFooterView(lsvFooterView);
                expandableParentOutletAdapter.notifyDataSetChanged();
            } else {
                expandableParentOutletAdapter = new ExpandableParentOutletAdapter(getContext(), this, lstParentOutlets);
                lsvOutlets.setAdapter(expandableParentOutletAdapter);
            }

        } else {
            lsvOutlets.removeFooterView(lsvFooterView);
            if (page == 1) {
                lsvOutlets.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
            }
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