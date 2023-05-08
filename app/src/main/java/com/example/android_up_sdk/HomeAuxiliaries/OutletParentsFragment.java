package com.example.android_up_sdk.HomeAuxiliaries;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android_up_sdk.Dialogs.CustomAlertDialog;
import com.example.android_up_sdk.Dialogs.OfferDetailAlertDialog;
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.ExpandableParentOutletAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.SearchOffersAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModelParentList;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_OfferDetail;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_Recent_Outlet;
import com.example.android_up_sdk.HomeAuxiliaries.WebServices.CategoryOffers_WebHit_Get_getOutletsParents;
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


public class OutletParentsFragment extends Fragment {

    private ExpandableListView lsvOutlets;
    private ListView lsvSearchOffers;
    private ArrayList<String> lstSearchOffers;
    private ArrayList<DModelParentList> lstParentOutlets;
    ArrayList<DModelParentList.Child> lstParentOutletsChild;
    private SearchOffersAdapter searchOffersAdapter;
    private ExpandableParentOutletAdapter expandableParentOutletAdapter;
    private TextView  txvNotFound;
    private View lsvFooterView;
    private double lat, lng;
    private String strSortBy, strSortType, strCollectionId, strPlaylistId, strTobeSearch, searchTags;
    private int page, categoryId, popularCategoryId, interestId, listSizeOffset;
    private boolean  isThisFragmentVisible;
    private boolean shouldGetMoreOffers, isAlreadyFetchingOffers, shouldRequestWebHit;
    private DModel_OfferDetail dModel_offerDetail;
    private CategoryParentHubFragment categoryParentHubFragment;
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
                            requestParentOutlets(page, categoryId, strSortBy, strSortType, lat, lng, false, false, searchTags);
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
                lstParentOutlets.clear();
                shouldGetMoreOffers = true;
                requestParentOutlets(page, categoryId, strSortBy, strSortType, lat, lng, true, true, searchTags);
            }
            AppConfig.getInstance().closeKeyboard(requireActivity());
            lsvSearchOffers.setVisibility(View.GONE);
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

        return view;
    }

    private void initialize() {
        page = 1;
        listSizeOffset = 0;

        searchTags = "";
        strTobeSearch = "";
        GPSTracker.getInstance(requireContext()).getLocation();
        lat = GPSTracker.getInstance(requireContext()).getLatitude();
        lng = GPSTracker.getInstance(requireContext()).getLongitude();
        shouldRequestWebHit = true;
        shouldGetMoreOffers = true;
        isAlreadyFetchingOffers = false;
        progressDialogue = new ProgressDialogue();
        lstParentOutlets = new ArrayList<>();
        lstSearchOffers = new ArrayList<>();
        dModel_offerDetail = new DModel_OfferDetail();


        categoryId = AppConfig.getInstance().mCategorySorting.getCategoryId();
        popularCategoryId = AppConfig.getInstance().mCategorySorting.getPopularCategoryId();
        interestId = AppConfig.getInstance().mCategorySorting.getInterestId();
        strCollectionId = AppConfig.getInstance().mCategorySorting.getCollectionId();
        strPlaylistId = AppConfig.getInstance().mCategorySorting.getPlaylistId();
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

                    if (lstParentOutlets.get(groupPosition).getChild() != null && lstParentOutlets.get(groupPosition).getChild().size() > 0) {
                        int offerId = Integer.parseInt(lstParentOutlets.get(groupPosition).getChild().get(childPosition).getStrOfferId());
                        String saveApprox = lstParentOutlets.get(groupPosition).getChild().get(childPosition).getApproxSavings();

                        dModel_offerDetail = new DModel_OfferDetail(offerId,
                                groupPosition,
                                childPosition,
                                "lstParentOutlets",
                                saveApprox,
                                lstParentOutlets.get(groupPosition).getChild().get(childPosition).getStrOfferName(),
                                lstParentOutlets.get(groupPosition).getChild().get(childPosition).getDescription(),
                                lstParentOutlets.get(groupPosition).getChild().get(childPosition).getStrCategoryId(),
                                lstParentOutlets.get(groupPosition).getStrOutletName(),
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


    public static OutletParentsFragment newInstance(int position, String _sortBy) {
        OutletParentsFragment frag = new OutletParentsFragment();
        Bundle args = new Bundle();
        args.putString(AppConstt.BundleStrings.catSortBy, _sortBy);
        args.putInt(KEY_POSITION, position);
        frag.setArguments(args);

        return (frag);
    }

    public void updateInitialValues() {
        if (lstParentOutlets != null && lstParentOutlets.size() == 0) {

            performAlphabeticallySortClick();

        }
    }
    private void performAlphabeticallySortClick() {
        listSizeOffset = 0;
        if (shouldRequestWebHit) {
            if (!isAlreadyFetchingOffers) {
                shouldGetMoreOffers = true;
                page = 1;
                strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_ALPHABETICALLY;
                requestParentOutlets(page, categoryId, strSortBy, strSortType, lat, lng, true, true, searchTags);
            }
        } else {
            strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_ALPHABETICALLY;
//            genderUpdateAlert();

        }
    }

    private void requestParentOutlets(final int _page, final int _categoryId, String
            _sortBy, String _genderType, final double lat, double lng, final boolean _shouldClearLst,
                                      final boolean _shouldShowLoader, final String _strSearchTags) {
        if (_shouldShowLoader) {
            progressDialogue.startIOSLoader(requireActivity());
        }
        isAlreadyFetchingOffers = true;
        CategoryOffers_WebHit_Get_getOutletsParents CategoryOffers_WebHit_Get_getOutletsParents = new CategoryOffers_WebHit_Get_getOutletsParents();
        CategoryOffers_WebHit_Get_getOutletsParents.requestCategoryOffers(getContext(), new IWebCallback() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDialogue.stopIOSLoader();
                isAlreadyFetchingOffers = false;

                if (isSuccess) {
                    if (_shouldClearLst) {
                        lstParentOutlets.clear();
                        expandableParentOutletAdapter = null;
                    }

                    updateParentOutletsList(lat);
                } else if (strMsg.equalsIgnoreCase(AppConstt.ServerStatus.DATABASE_NOT_FOUND + "")) {
                    shouldGetMoreOffers = false;
                    if (lstParentOutlets.size() == 0) {
                        lsvOutlets.setVisibility(View.GONE);
                        lsvSearchOffers.setVisibility(View.GONE);
                        txvNotFound.setVisibility(View.VISIBLE);
                    } else {
                        lsvOutlets.removeFooterView(lsvFooterView);
                        shouldGetMoreOffers = false;
                        expandableParentOutletAdapter.notifyDataSetChanged();
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
                lsvOutlets.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);

                AppConfig.getInstance().showAlertDialog(requireActivity(),null, AppConfig.getInstance().getNetworkExceptionMessage(ex.getMessage()), null, null, false, true, null);

            }
        }, _page, _categoryId, popularCategoryId, strCollectionId, strPlaylistId, _sortBy, _genderType, lat, lng, false, _strSearchTags, interestId);

    }

    private void requestNewOffersAutoComplete(int _page, String _searchKey) {
        lstSearchOffers.clear();
        searchOffersAdapter = null;
        isAlreadyFetchingOffers = true;
        SearchOffers_WebHit_Get_getOffersSearchTags SearchOffers_WebHit_Get_getOffersSearchTags = new SearchOffers_WebHit_Get_getOffersSearchTags();
        SearchOffers_WebHit_Get_getOffersSearchTags.requestSearchOffers(getContext(), new IWebCallback() {
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
//                    showAlertDialog(null, strMsg, null, null, false, true, null);
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
//                showAlertDialog(null, AppConfig.getInstance().getNetworkExceptionMessage(ex.getMessage()), null, null, false, true, null);
            }
        }, _page, _searchKey, categoryId);
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

    private void updateParentOutletsList(double _lat) {
        boolean isDistanceRequired;
        if (_lat > 0 && strSortBy.equalsIgnoreCase(AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION)) {
            isDistanceRequired = true;
        } else {
            isDistanceRequired = false;
        }
        
        if (CategoryOffers_WebHit_Get_getOutletsParents.responseObject != null &&
                CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData() != null &&
                CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().size() > 0) {

            if (CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().size() <= 20) {
                shouldGetMoreOffers = false;
            }

            for (int i = 0; i < CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().size(); i++) {

                String strParentId = CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getId();
                String strParentName = CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getName();
                String strOutletID = "";
                String strOutletName = "";
                String strOutletImage = "";
                String strOutletLogo = CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getLogo();
                String strOutletAddress = "";
                String strOutletTimmings = "";
                String strOutletDescription = "";
                String strOutletPhone = "";
                String strFestival = "";
                String Gspecial = "0";
                String Gfestival = "";
                boolean isOutletExpedible = false;

                int distance;
                if (CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getDistance()!=null)
                {
                    if (CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getDistance().length() > 0) {
                        String strDistance = CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getDistance();
                        double roundedDistance = Double.parseDouble(strDistance);
                        distance = (int) roundedDistance;
                    } else {
                        distance = 0;
                    }
                }else{
                    distance = 0;

                }

                lstParentOutletsChild = new ArrayList<>();
                if (CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets() != null) {
                    strOutletID = CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getId();
                    strOutletName = CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getName();


                    strOutletAddress = CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getAddress();
                    strOutletTimmings = CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getTimings();
                    strOutletDescription = CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getDescription();
                    if (CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getImage() != null) {
                        strOutletImage = CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getImage();
                    }
                    if (CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getLogo() != null) {
                        strOutletLogo = CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getLogo();
                    }
                    if (CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getPhone() != null) {
                        strOutletPhone = CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getPhone();
                    }

                    if (CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getTotal_outlets()==1){
                        if (CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers() != null &&
                                CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().size() > 0) {
                            isOutletExpedible = true;
                            for (int j = 0; j < CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().size(); j++) {
                                if (CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getActive().equalsIgnoreCase("1")) {
                                    String Cfestival;
                                    String Cspecial = "";


                                    if (CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getSpecial() != null
                                            && CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getSpecial().length() > 0) {
                                        Cspecial = CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getSpecial();
                                        if (Cspecial.equalsIgnoreCase("1")) {
                                            Gspecial = "1";
                                        }
                                        if (Cspecial.equalsIgnoreCase("1") &&
                                                CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getSpecialType() != null &&
                                                CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getSpecialType().length() > 0) {
                                            Cfestival = CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getSpecialType();
                                            if (Gfestival.length() == 0) {
                                                Gfestival = CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getSpecialType();
                                            }
                                        } else {
                                            Cfestival = "";
                                        }
                                    } else {
                                        Cspecial = "0";
                                        Cfestival = "";
                                    }

                                    if (Gspecial.equalsIgnoreCase("0")) {
                                        Gspecial = Cspecial;
                                    }


                                    String totalSaving;
                                    if (CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getApproxSaving() != null) {
                                        totalSaving = CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getApproxSaving();
                                    } else {
                                        totalSaving = "0";
                                    }

                                    float totalSavingsFloat = Float.parseFloat(totalSaving);
                                    int totalSavingsRounded = (int) totalSavingsFloat;
                                    String approxSavings, currencyUnit;

                                    currencyUnit = getResources().getString(R.string.txv_qatar_riyal);


                                    approxSavings = currencyUnit + " " + totalSavingsRounded;
                                    String savedPercentage;
                                    if (CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getPercentage_saving() != null) {
                                        savedPercentage = String.valueOf(CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getPercentage_saving());
                                    } else {
                                        savedPercentage = "00.00";
                                    }
                                    int approxPerRounded = 0;
                                    float approxPerFloat = Float.parseFloat(savedPercentage);
                                    approxPerRounded = (int) approxPerFloat;
                                    String savedPer =  "" + approxPerRounded;



                                    lstParentOutletsChild.add(new DModelParentList.Child(
                                            CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getId(),
                                            CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getTitle(),
                                            CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getCategory_ids(),
                                            CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getOutlet_name(),
                                            CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getDescription(),
                                            CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getImage(),
                                            Cfestival,
                                            Cspecial,
                                            CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getValidFor(),
                                            approxSavings,
                                            false, true,
                                            CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getRenew(),
                                            CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getRenewDate(),
                                            CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getEndDatetime(),
                                            CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).isCanSendGift(),
                                            CategoryOffers_WebHit_Get_getOutletsParents.responseObject.getData().get(i).getOutlets().get(0).getOffers().get(j).getDiscount_type(),
                                            savedPer
                                    ));
                                }
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
                        Gfestival,
                        Gspecial,
                        categoryId,
                        popularCategoryId,
                        interestId,
                        strSortBy,
                        distance,
                        isOutletExpedible,
                        isDistanceRequired,
                        lstParentOutletsChild
                ));
            }


            lsvOutlets.setVisibility(View.VISIBLE);
            lsvSearchOffers.setVisibility(View.GONE);
            txvNotFound.setVisibility(View.GONE);
            lsvOutlets.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
            if (!(lstParentOutlets != null && lstParentOutlets.size() > 0)) {
                lsvOutlets.setVisibility(View.GONE);
                lsvSearchOffers.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
            } else if (expandableParentOutletAdapter != null) {
                lsvOutlets.removeFooterView(lsvFooterView);
                expandableParentOutletAdapter.notifyDataSetChanged();
            } else {
                expandableParentOutletAdapter = new ExpandableParentOutletAdapter(getContext(), this, lstParentOutlets);
                lsvOutlets.setAdapter(expandableParentOutletAdapter);
            }

        } else {
            if (lstParentOutlets.size() > 0) {
                lsvOutlets.removeFooterView(lsvFooterView);
                shouldGetMoreOffers = false;
                expandableParentOutletAdapter.notifyDataSetChanged();
            } else {
                lsvOutlets.setVisibility(View.GONE);
                lsvSearchOffers.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
            }
        }
    }


    public void validateSearch(CategoryParentHubFragment _categoryParentHubFragment, String
            _strSearchKey) {
        categoryParentHubFragment = _categoryParentHubFragment;
        strTobeSearch = _strSearchKey;
        if (strTobeSearch.length() > 2) {
            requestNewOffersAutoComplete(1, strTobeSearch);
        } else {
            updateOutletLst();
        }
    }

    public void updateOutletLst() {
        lsvSearchOffers.setVisibility(View.GONE);
        if (lstParentOutlets != null && lstParentOutlets.size() > 0) {
            lsvOutlets.setVisibility(View.VISIBLE);
            txvNotFound.setVisibility(View.GONE);
        } else {
            txvNotFound.setVisibility(View.VISIBLE);
        }
        searchTags = "";
        AppConfig.getInstance().closeKeyboard(requireActivity());

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


    public void navToCategoryHubFragment(Bundle bundle) {
        if (getActivity() != null && isAdded()) {
//            Calling it from mainActivity:to resolve the crash occurs due to CleverTap lib version:4.0 .1
            ((MainUPActivity) getActivity()).navToCategoryHubFragment(bundle);
        }
    }

    public void navToMerchantDetailFragment(Bundle bundle) {
        if (getActivity() != null && isAdded()) {
//           Calling it from mainActivity:to resolve the crash occurs due to CleverTap lib version:4.0 .1
            ((MainUPActivity) getActivity()).navToMerchantDetailFragment(bundle);
        }
    }

    private void showOfferDetailAlertDialog(DModel_OfferDetail dModel_offerDetail) {
        OfferDetailAlertDialog cdd = new OfferDetailAlertDialog(requireContext(), this, dModel_offerDetail, null);
        cdd.show();
        cdd.setCancelable(true);
    }

}