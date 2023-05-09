package com.example.android_up_sdk.HomeAuxiliaries;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.android_up_sdk.Dialogs.OfferDetailAlertDialog;
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.ExpandableMerchintListAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.RecentSearchOffersAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.SearchOffersAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModelMerchintList;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_OfferDetail;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_Recent_Outlet;
import com.example.android_up_sdk.HomeAuxiliaries.WebServices.CategoryOffers_WebHit_Get_getOutlet;
import com.example.android_up_sdk.HomeAuxiliaries.WebServices.SearchOffers_WebHit_Get_TrendingSearchTag;
import com.example.android_up_sdk.HomeAuxiliaries.WebServices.SearchOffers_WebHit_Get_getOffersSearchTags;
import com.example.android_up_sdk.MerchantDetailFragment;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.example.android_up_sdk.Utils.IAdapterCallback;
import com.example.android_up_sdk.Utils.IWebCallback;
import com.example.android_up_sdk.Utils.ProgressDialogue;
import java.util.ArrayList;


public class SearchOffersFragment extends Fragment implements View.OnClickListener {

    ProgressDialogue progressDialogue;
    private ListView lsvSearchOffers;
    private ListView lsvRecentSearches;
    private ExpandableListView lsvOutlets;
    private ExpandableMerchintListAdapter expandableMerchintListAdapter;
    private SearchOffersAdapter searchOffersAdapter;
    private RecentSearchOffersAdapter recentSearchOffersAdapter;
    private ArrayList<DModelMerchintList> lstOutlets;
    ArrayList<DModelMerchintList.Child> lstChild;
    private ArrayList<String> lstSearchOffers;
    private TextView txvTitle;
    private TextView txvNotFound, txvCancel;
    private String strSearchKey;
    private EditText edtSearch;
    private int categoryId;
    private DModel_OfferDetail dModel_offerDetail;
    private LinearLayout llClose, llProgressBar;
    private View lsvFooterView;
    private int page, listSizeOffset;
    private boolean shouldPopulateSearchTags, shouldGetMoreOffers, isAlreadyFetchingOffers;
    private TextView txvTrendingSearch;

    private RelativeLayout rlBackBtn;
    boolean isTrendingSearchShowing;
    boolean isSearchFromClickEvent;
    boolean isWaitingForResult;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search_offers, container, false);
        initialize();
        bindViews(view);

        onCreateFunctions();
        requestTrendingSearchTags();
        return view;
    }

    private void initialize() {
        isSearchFromClickEvent = false;
        page = 1;
        strSearchKey = "";
        categoryId = AppConfig.getInstance().mCategorySorting.getCategoryId();
        dModel_offerDetail = new DModel_OfferDetail();
        shouldGetMoreOffers = true;
        isAlreadyFetchingOffers = false;
        shouldPopulateSearchTags = true;
        isWaitingForResult = false;
        progressDialogue = new ProgressDialogue();
        lstSearchOffers = new ArrayList<>();
        AppConfig.getInstance().loadRecentSearches();
        recentSearchOffersAdapter = new RecentSearchOffersAdapter(getContext(), AppConfig.getInstance().lstInstoreRecentSearches, (eventId, position) -> {
            switch (eventId) {
                case IAdapterCallback.EVENT_A:
                    AppConfig.getInstance().removeRecentSearch(position);
                    recentSearchOffersAdapter.notifyDataSetChanged();
                    break;
                case IAdapterCallback.EVENT_B:
                    if (AppConfig.getInstance().lstInstoreRecentSearches != null && AppConfig.getInstance().lstInstoreRecentSearches.size() > 0 && position <= AppConfig.getInstance().lstInstoreRecentSearches.size() - 1) {
                        strSearchKey = AppConfig.getInstance().lstInstoreRecentSearches.get(position);
                        strSearchKey = strSearchKey.replace("’", "'");
                        isSearchFromClickEvent = true;
                        edtSearch.setText(strSearchKey);
                        edtSearch.clearFocus();
                        AppConfig.getInstance().closeKeyboard(requireActivity());
                        page = 1;
                        listSizeOffset = 0;
                        lstOutlets.clear();
                        shouldGetMoreOffers = true;
                        requestCategoryOffers(page, categoryId, true, true, strSearchKey);
                    }
                    AppConfig.getInstance().closeKeyboard(requireActivity());
                    txvTrendingSearch.setVisibility(View.GONE);
                    lsvSearchOffers.setVisibility(View.GONE);
                    lsvRecentSearches.setVisibility(View.GONE);
                    break;
            }

        });
        lstOutlets = new ArrayList<>();
    }

    private void bindViews(View frg) {
        txvTrendingSearch = frg.findViewById(R.id.frg_searchoffr_txv_recent_searches);
        txvTitle = frg.findViewById(R.id.app_bar_txv_title);
        txvNotFound = frg.findViewById(R.id.frg_searchoffr_offers_txv_nt_found);
        txvCancel = frg.findViewById(R.id.frg_searchoffr_txv_cancel);
        txvCancel.setOnClickListener(this);
        lsvOutlets = frg.findViewById(R.id.frg_searchoffr_offers_lst_view);
        lsvSearchOffers = frg.findViewById(R.id.frg_searchoffr_lsv_autocomplte);
        lsvRecentSearches = frg.findViewById(R.id.frg_searchoffr_lsv_recent_searches);
        lsvRecentSearches.setAdapter(recentSearchOffersAdapter);
        edtSearch = frg.findViewById(R.id.frg_searchoffr_edt_search);
        llClose = frg.findViewById(R.id.frg_searchoffr_ll_close);
        llProgressBar = frg.findViewById(R.id.frg_searchoffr_ll_progressbar);

        llClose.setOnClickListener(this);
        lsvFooterView = ((LayoutInflater) requireContext().getSystemService(requireContext().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.lsv_footer, null, false);
        rlBackBtn = frg.findViewById(R.id.app_bar_rl_back);
        rlBackBtn.setOnClickListener(this);

    }

    private void onCreateFunctions() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    llClose.setVisibility(View.VISIBLE);
                } else {
                    llClose.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                lsvRecentSearches.setVisibility(View.GONE);
                if (s.length() > 2) {
                    strSearchKey = edtSearch.getText().toString();
                    strSearchKey = strSearchKey.trim();
                    if (!strSearchKey.equalsIgnoreCase("")) {
                        if (!isSearchFromClickEvent && !isWaitingForResult) {
                            shouldPopulateSearchTags = true;
                            llClose.setVisibility(View.GONE);
                            llProgressBar.setVisibility(View.VISIBLE);
                            txvNotFound.setVisibility(View.GONE);
                            requestNewOffersAutoComplete(strSearchKey, categoryId);
                        } else {
                            isSearchFromClickEvent = false;
                        }
                    }
                } else if (s.length() == 0) {
                    lsvSearchOffers.setVisibility(View.GONE);
                    lsvRecentSearches.setVisibility(View.VISIBLE);
                    lsvOutlets.setVisibility(View.GONE);
                } else {
                    lsvSearchOffers.setVisibility(View.GONE);
                    lsvRecentSearches.setVisibility(View.GONE);

                }
            }
        });

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                shouldPopulateSearchTags = false;
                strSearchKey = edtSearch.getText().toString();
                strSearchKey = strSearchKey.trim();
                if (!strSearchKey.equalsIgnoreCase("")) {
                    strSearchKey = strSearchKey.replace("’", "'");
                    requestCategoryOffers(page, categoryId, true, true, strSearchKey);
                    AppConfig.getInstance().closeKeyboard(requireActivity());

                }
                return true;
            }
            return false;

        });

        edtSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                AppConfig.getInstance().openKeyboard(requireContext());
                txvNotFound.setVisibility(View.GONE);
                txvTrendingSearch.setVisibility(View.GONE);
                lsvSearchOffers.setVisibility(View.GONE);

                if (edtSearch.getText() == null || edtSearch.getText().length() == 0) {
                    lsvRecentSearches.setVisibility(View.VISIBLE);
                    lsvOutlets.setVisibility(View.GONE);
                }

                txvCancel.setVisibility(View.VISIBLE);
                rlBackBtn.setVisibility(View.VISIBLE);
                txvTitle.setVisibility(View.GONE);
            } else {


            }
        });

        lsvOutlets.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            int offerId = Integer.parseInt(lstOutlets.get(groupPosition).getChild().get(childPosition).getProductId());
            String saveApprox = lstOutlets.get(groupPosition).getChild().get(childPosition).getApproxSavings();

            String strAddress = lstOutlets.get(groupPosition).getMerchantName() + " - " + lstOutlets.get(groupPosition).getMerchantAddress();
            dModel_offerDetail = new DModel_OfferDetail(offerId,
                    groupPosition,
                    childPosition,
                    "lstOutlets",
                    saveApprox,
                    lstOutlets.get(groupPosition).getChild().get(childPosition).getName(),
                    lstOutlets.get(groupPosition).getChild().get(childPosition).getDescription(),
                    lstOutlets.get(groupPosition).getChild().get(childPosition).getCategoryId(),
                    lstOutlets.get(groupPosition).getChild().get(childPosition).getOutletName(),
                    lstOutlets.get(groupPosition).getMerchantAddress(),
                    lstOutlets.get(groupPosition).getId(),
                    lstOutlets.get(groupPosition).getMerchantsLogoUrl(),
                    lstOutlets.get(groupPosition).getChild().get(childPosition).getDetailNExclusions(),
                    lstOutlets.get(groupPosition).getChild().get(childPosition).isFavorite(),
                    lstOutlets.get(groupPosition).getChild().get(childPosition).isCanRedeem(),
                    lstOutlets.get(groupPosition).getChild().get(childPosition).getExpiryDate(),
                    lstOutlets.get(groupPosition).getChild().get(childPosition).isCanSendGift(),
                    lstOutlets.get(groupPosition).getChild().get(childPosition).getDiscountType(),
                    lstOutlets.get(groupPosition).getChild().get(childPosition).getApproxSavingPercentage()
            );
            showOfferDetailAlertDialog(dModel_offerDetail);
            return false;
        });

        lsvSearchOffers.setOnItemClickListener((parent, view, position, id) -> {
            if (lstSearchOffers != null && lstSearchOffers.size() > 0 && position <= lstSearchOffers.size() - 1) {
                strSearchKey = lstSearchOffers.get(position);
                strSearchKey = strSearchKey.replace("’", "'");
                isSearchFromClickEvent = true;
                edtSearch.setText(strSearchKey);
                page = 1;
                listSizeOffset = 0;
                lstOutlets.clear();
                if (!isTrendingSearchShowing) {
                    AppConfig.getInstance().updateRecentSearches(strSearchKey);
                    recentSearchOffersAdapter.notifyDataSetChanged();
                }
                shouldGetMoreOffers = true;
                requestCategoryOffers(page, categoryId, true, true, strSearchKey);
            }
            AppConfig.getInstance().closeKeyboard(requireActivity());
            txvTrendingSearch.setVisibility(View.GONE);
            lsvSearchOffers.setVisibility(View.GONE);

        });

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
                            requestCategoryOffers(page, categoryId, false, false, strSearchKey);
                        }
                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        //Following Both listener is for adjusting the list size
        lsvOutlets.setOnGroupExpandListener(groupPosition -> {

            listSizeOffset = listSizeOffset + lstOutlets.get(groupPosition).getChild().size();
            AppConfig.getInstance().updateRecentViewed(new DModel_Recent_Outlet(lstOutlets.get(groupPosition).getId(),lstOutlets.get(groupPosition).getMerchantsLogoUrl(),
                    lstOutlets.get(groupPosition).getMerchantAddress(),lstOutlets.get(groupPosition).getMerchantName()));

        });

        lsvOutlets.setOnGroupCollapseListener(groupPosition -> {
            listSizeOffset = listSizeOffset - lstOutlets.get(groupPosition).getChild().size();

        });
    }

    //region Api Callings

    private void requestNewOffersAutoComplete(String _searchKey, int categoryId) {
        isWaitingForResult = true;

        isAlreadyFetchingOffers = true;
        SearchOffers_WebHit_Get_getOffersSearchTags SearchOffers_WebHit_Get_getOffersSearchTags = new SearchOffers_WebHit_Get_getOffersSearchTags();
        SearchOffers_WebHit_Get_getOffersSearchTags.requestSearchOffers(getContext(), new IWebCallback() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                llProgressBar.setVisibility(View.GONE);
                llClose.setVisibility(View.VISIBLE);
                isAlreadyFetchingOffers = false;
                if (isSuccess) {
                    if (shouldPopulateSearchTags) {
                        updateListAutoComplete();
                    }
                } else if ((strMsg.equalsIgnoreCase(AppConstt.ServerStatus.DATABASE_NOT_FOUND + "")) ||
                        (strMsg.equalsIgnoreCase(AppConstt.ServerStatus.INTERNAL_SERVER_ERROR + ""))) {

                    lsvOutlets.setVisibility(View.GONE);
                    txvTrendingSearch.setVisibility(View.GONE);
                    lsvSearchOffers.setVisibility(View.GONE);
                    txvNotFound.setVisibility(View.VISIBLE);

                } else {
                    lsvOutlets.setVisibility(View.GONE);
                    txvTrendingSearch.setVisibility(View.GONE);
                    lsvSearchOffers.setVisibility(View.GONE);
                    txvNotFound.setVisibility(View.VISIBLE);
                }
                isWaitingForResult = false;
            }

            @Override
            public void onWebException(Exception ex) {
                llProgressBar.setVisibility(View.GONE);
                llClose.setVisibility(View.VISIBLE);
                isAlreadyFetchingOffers = false;
                lsvOutlets.setVisibility(View.GONE);
                txvTrendingSearch.setVisibility(View.GONE);
                lsvSearchOffers.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
                isWaitingForResult = false;
            }

        }, 0, _searchKey, categoryId);
    }

    private void updateListAutoComplete() {
        if (SearchOffers_WebHit_Get_getOffersSearchTags.responseObject != null &&
                SearchOffers_WebHit_Get_getOffersSearchTags.responseObject.getData().getTags() != null &&
                SearchOffers_WebHit_Get_getOffersSearchTags.responseObject.getData().getTags().size() > 0 &&
                edtSearch.getText().toString().length() > 0) {

            lstSearchOffers.clear();
            isTrendingSearchShowing = false;
            for (int i = 0; i < SearchOffers_WebHit_Get_getOffersSearchTags.responseObject.getData().getTags().size(); i++) {
                lstSearchOffers.add(SearchOffers_WebHit_Get_getOffersSearchTags.responseObject.getData().getTags().get(i));
            }

            lsvOutlets.setVisibility(View.GONE);
            txvNotFound.setVisibility(View.GONE);
            txvTrendingSearch.setVisibility(View.GONE);
            lsvSearchOffers.setVisibility(View.VISIBLE);
            if (searchOffersAdapter != null) {
                searchOffersAdapter.notifyDataSetChanged();
            } else {
                searchOffersAdapter = new SearchOffersAdapter(getContext(), lstSearchOffers);
                lsvSearchOffers.setAdapter(searchOffersAdapter);
                txvTrendingSearch.setVisibility(View.GONE);
            }

        } else {
            lsvOutlets.setVisibility(View.GONE);
            txvTrendingSearch.setVisibility(View.GONE);
            lsvSearchOffers.setVisibility(View.GONE);
            txvNotFound.setVisibility(View.VISIBLE);
        }
    }

    private void requestTrendingSearchTags() {
        progressDialogue.startIOSLoader(requireActivity());

        SearchOffers_WebHit_Get_TrendingSearchTag searchOffers_webHit_get_trendingSearchTag = new SearchOffers_WebHit_Get_TrendingSearchTag();
        searchOffers_webHit_get_trendingSearchTag.requestTrendingSearchTags(getContext(), new IWebCallback() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                showTrendingSearchTagsResult(isSuccess);
            }

            @Override
            public void onWebException(Exception ex) {
                showTrendingSearchTagsResult(false);

            }
        });
    }

    private void showTrendingSearchTagsResult(boolean isSuccess) {
        progressDialogue.stopIOSLoader();
        if (isSuccess) {
            if (SearchOffers_WebHit_Get_TrendingSearchTag.responseObject != null &&
                    SearchOffers_WebHit_Get_TrendingSearchTag.responseObject.getData() != null &&
                    SearchOffers_WebHit_Get_TrendingSearchTag.responseObject.getData().size() > 0) {
                lstSearchOffers.clear();
                isTrendingSearchShowing = true;
                for (int i = 0; i < SearchOffers_WebHit_Get_TrendingSearchTag.responseObject.getData().size(); i++) {
                    lstSearchOffers.add(SearchOffers_WebHit_Get_TrendingSearchTag.responseObject.getData().get(i).getText());
                }
                lsvOutlets.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.GONE);
                lsvSearchOffers.setVisibility(View.VISIBLE);
                txvTrendingSearch.setVisibility(View.VISIBLE);
                if (searchOffersAdapter != null) {
                    searchOffersAdapter.notifyDataSetChanged();
                } else {
                    searchOffersAdapter = new SearchOffersAdapter(getContext(), lstSearchOffers);
                    lsvSearchOffers.setAdapter(searchOffersAdapter);
                }
            } else {
                lsvOutlets.setVisibility(View.GONE);
                txvTrendingSearch.setVisibility(View.GONE);
                lsvSearchOffers.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.GONE);
            }
        } else {
            lsvOutlets.setVisibility(View.GONE);
            txvTrendingSearch.setVisibility(View.GONE);
            lsvSearchOffers.setVisibility(View.GONE);
            txvNotFound.setVisibility(View.GONE);
        }

    }

    private void requestCategoryOffers(int _page, final int _categoryId, final boolean _shouldClearLst, final boolean _shouldShowLoader, final String _strSearchTags) {
        if (_shouldShowLoader) {
            progressDialogue.startIOSLoader(requireActivity());
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
                        lstOutlets.clear();
                        expandableMerchintListAdapter = null;
                    }
                    updateList();
                } else if (strMsg.equalsIgnoreCase(AppConstt.ServerStatus.DATABASE_NOT_FOUND + "")) {
                    shouldGetMoreOffers = false;
                    if (lstOutlets.size() == 0) {
                        lsvOutlets.setVisibility(View.GONE);
                        txvTrendingSearch.setVisibility(View.GONE);
                        lsvSearchOffers.setVisibility(View.GONE);
                        txvNotFound.setVisibility(View.VISIBLE);
                    } else {
                        lsvOutlets.removeFooterView(lsvFooterView);
                        expandableMerchintListAdapter.notifyDataSetChanged();
                    }
                } else {
                    lsvOutlets.setVisibility(View.GONE);
                    txvTrendingSearch.setVisibility(View.GONE);
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

        }, _page, _categoryId, 0, "", "", AppConstt.DEFAULT_VALUES.SORT_BY_ALPHABETICALLY, "2", 0, 0, false, _strSearchTags, "", 0);
    }

    private void updateList() {
        boolean isDistanceRequired;
        isDistanceRequired = false;

        if (CategoryOffers_WebHit_Get_getOutlet.responseObject != null &&
                CategoryOffers_WebHit_Get_getOutlet.responseObject.getData() != null &&
                CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().size() > 0) {
            //Saving Recent search key
            AppConfig.getInstance().updateRecentSearches(strSearchKey);
            recentSearchOffersAdapter.notifyDataSetChanged();

            if (CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().size() < 20) {
                shouldGetMoreOffers = false;
            }

            for (int i = 0; i < CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().size(); i++) {
                if (CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers() != null) {
                    String Gfestival = "";
                    lstChild = new ArrayList<>();
                    for (int j = 0; j < CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().size(); j++) {
                        String Cfestival;
                        String special = "";
                        if (CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecial() != null
                                && CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecial().length() > 0) {
                            special = CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecial();
                        } else {
                            if (Gfestival.length() == 0) {
                                special = "0";
                            }
                        }


                        if (CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecialType() != null &&
                                CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecialType().length() > 0) {
                            Cfestival = CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecialType();
                            if (Gfestival.length() == 0) {
                                Gfestival = CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecialType();
                            }
                        } else {
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


                        boolean canRedeem = CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).isRedeeme();

                        String strImageUrl = "";
                        if (CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getImage() != null) {
                            strImageUrl = CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getImage();
                        }
                        lstChild.add(new DModelMerchintList.Child(
                                CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getId(),
                                strImageUrl,
                                CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getTitle(),
                                CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getDescription(),
                                Cfestival,
                                "",
                                "",
                                special,
                                CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getValidFor(),
                                approxSavings,
                                CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getCategory_ids(),
                                CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getOutlet_name(),
                                false,
                                canRedeem,
                                CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getRenew(),
                                CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getRenewDate(),
                                CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getEndDatetime(),
                                CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).isCanSendGift(),
                                CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getDiscount_type(),
                                savedPer
                        ));

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

                    lstOutlets.add(new DModelMerchintList(
                            CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getId(),
                            CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getName(),
                            CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getAddress(),
                            CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getTimings(),
                            CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getDescription(),
                            distance,
                            isDistanceRequired,
                            CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getSpecial(),
                            Gfestival,
                            strImageUrl,
                            CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getLogo(),
                            CategoryOffers_WebHit_Get_getOutlet.responseObject.getData().get(i).getPhone(),
                            lstChild
                    ));

                }
            }


            rlBackBtn.setVisibility(View.VISIBLE);
            lsvRecentSearches.setVisibility(View.GONE);
            txvTitle.setVisibility(View.VISIBLE);
            txvCancel.setVisibility(View.GONE);
            lstSearchOffers.clear();
            AppConfig.getInstance().closeKeyboard(requireActivity());
            edtSearch.clearFocus();

            lsvOutlets.setVisibility(View.VISIBLE);
            txvTrendingSearch.setVisibility(View.GONE);
            lsvSearchOffers.setVisibility(View.GONE);
            txvNotFound.setVisibility(View.GONE);
            lsvOutlets.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);

            if (expandableMerchintListAdapter != null) {
                lsvOutlets.removeFooterView(lsvFooterView);
                expandableMerchintListAdapter.notifyDataSetChanged();
            } else {
                expandableMerchintListAdapter = new ExpandableMerchintListAdapter(getContext(), this, lstOutlets);
                lsvOutlets.setAdapter(expandableMerchintListAdapter);
            }
        } else {
            txvNotFound.setVisibility(View.VISIBLE);
        }
    }

    //endregion


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.frg_searchoffr_txv_cancel) {
            edtSearch.setText("");
            rlBackBtn.setVisibility(View.VISIBLE);
            lsvRecentSearches.setVisibility(View.GONE);
            txvTitle.setVisibility(View.VISIBLE);
            txvCancel.setVisibility(View.GONE);
            lstOutlets.clear();
            if (expandableMerchintListAdapter != null)
                expandableMerchintListAdapter.notifyDataSetChanged();
            lstSearchOffers.clear();
            showTrendingSearchTagsResult(true);
            AppConfig.getInstance().closeKeyboard(requireActivity());
            edtSearch.clearFocus();
        } else if (id == R.id.app_bar_rl_back) {
            requireActivity().onBackPressed();
        } else if (id == R.id.frg_searchoffr_ll_close) {
            edtSearch.setText("");
            strSearchKey = "";
            lstSearchOffers.clear();
            if (searchOffersAdapter != null)
                searchOffersAdapter.notifyDataSetChanged();
            lstOutlets.clear();
            if (expandableMerchintListAdapter != null)
                expandableMerchintListAdapter.notifyDataSetChanged();
            lsvRecentSearches.setVisibility(View.VISIBLE);
            txvNotFound.setVisibility(View.GONE);
            edtSearch.requestFocus();
        }
    }

    private void showOfferDetailAlertDialog(DModel_OfferDetail dModel_offerDetail) {
        if (getActivity() != null) {
            OfferDetailAlertDialog cdd = new OfferDetailAlertDialog(requireContext(), this, dModel_offerDetail, null);
            cdd.show();
            cdd.setCancelable(true);
        }
    }



    public void navToMerchantDetailFragment(Bundle b) {
        Fragment fr = new MerchantDetailFragment();
        fr.setArguments(b);
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.main_act_content_frame, fr, AppConstt.FrgTag.MerchantDetailFragment);
        ft.addToBackStack(AppConstt.FrgTag.MerchantDetailFragment);
        ft.hide(this);
        ft.commit();

    }
}