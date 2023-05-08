package com.example.android_up_sdk.HomeAuxiliaries;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.example.android_up_sdk.Dialogs.OfferDetailAlertDialog;
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.HomeCategoriesAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.HomeNearbyOutletsAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.HomeNewBrandsAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.HomePopCategoriesAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.HomeRecentViewedAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.HomeUseAgainAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_Categories;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_CategorySorting;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_NearbyOutlets;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_NewBrands;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_OfferDetail;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_OfferUseAgain;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_PopupCat;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_Recent_Outlet;
import com.example.android_up_sdk.HomeAuxiliaries.WebServices.Home_WebHit_Post_HomeApiEssential;
import com.example.android_up_sdk.HomeAuxiliaries.WebServices.Home_WebHit_Post_HomeApiExtra;
import com.example.android_up_sdk.MerchantDetailFragment;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.SettingsUpFragment;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.example.android_up_sdk.Utils.IAdapterCallback;
import com.example.android_up_sdk.Utils.IWebCallback;
import com.example.android_up_sdk.Utils.ProgressDialogue;
import java.util.ArrayList;


public class HomeUpFragment extends Fragment implements View.OnClickListener {

    RelativeLayout rlHomeSearch;
    NestedScrollView svHomeCenter;
    TextView  txvWhereUSave;
    RecyclerView rcvCategories;
    LayoutInflater mInflater;
    LinearLayout llSectionContainer;

    //RecyclerViews
    RecyclerView rcvUseAgainOffers,
            rcvRecentSection,
            rcvNearbyOutlets,
            rcvNewBrands,
            rcvPopCategories;

    //Adapters
    HomeCategoriesAdapter adapterCategories;
    HomeUseAgainAdapter adapterUseAgainOffers;
    HomeNearbyOutletsAdapter adapterNearbyOutlets;
    HomeRecentViewedAdapter adapterRecentSection;
    HomePopCategoriesAdapter adapterPopCategories;
    HomeNewBrandsAdapter adapterNewBrands;

    //Lists
    ArrayList<DModel_Categories> lstCategories;
    ArrayList<DModel_OfferUseAgain> lstOffersUseAgain;
    ArrayList<DModel_NearbyOutlets> lstNearbyOutlets;
    ArrayList<DModel_Recent_Outlet> lstRecentViewedSection;
    ArrayList<DModel_PopupCat> lstPopCategories;
    ArrayList<DModel_NewBrands> lstNewBrands;

    LinearLayout llNewBrands,
            llUseAgainOffers,
            llPopCategories,
            llPopCatRCVParent,
            llRecentViewedSection,
            llNearbyOutlets;

    ViewSkeletonScreen skeleton;
    ProgressDialogue progressDialogue;
    int height, width;
    boolean isPopCatExpanded;
    DisplayMetrics displayMetrics;
    private DModel_OfferDetail dModel_offerDetail;

    //Ui
    ImageView homeSettings;

    private String strHomeApiErrorMsg;
    private boolean  isHomeExtraResponded,isHomeEssentialResponded;
    private FragmentManager mFrgmgr;
    private Fragment preFrg;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_up_home, container, false);
        OurOnCreate(view);

        return view;
    }

    private void initialize() {
        progressDialogue = new ProgressDialogue();
        lstCategories = new ArrayList<>();
        lstOffersUseAgain = new ArrayList<>();
        lstNearbyOutlets = new ArrayList<>();
        lstRecentViewedSection = new ArrayList<>();
        lstPopCategories = new ArrayList<>();

        lstNewBrands = new ArrayList<>();
        dModel_offerDetail = new DModel_OfferDetail();

        mFrgmgr = requireActivity().getSupportFragmentManager();
        String tag = returnStackFragment();
        preFrg = mFrgmgr.findFragmentByTag(tag);


        isPopCatExpanded = false;
        isHomeExtraResponded = false;
        isHomeEssentialResponded = false;
        strHomeApiErrorMsg = "";
        displayMetrics = getResources().getDisplayMetrics();
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


        lstRecentViewedSection= AppConfig.getInstance().loadRecentViewed();
    }

    private void clearAllLists() {
        lstCategories.clear();
        lstOffersUseAgain.clear();
        lstNearbyOutlets.clear();
        lstPopCategories.clear();
        lstNewBrands.clear();

    }

    @SuppressLint("InflateParams")
    private void bindViews(View frg) {
        rlHomeSearch = frg.findViewById(R.id.frg_homold_tool_bar_rl_search);
        svHomeCenter = frg.findViewById(R.id.frg_home_scrollview_cntnr);
        txvWhereUSave = frg.findViewById(R.id.frg_home_txv_categories_ttl);
        homeSettings = frg.findViewById(R.id.frg_home_settings_iv);


        llSectionContainer = frg.findViewById(R.id.frg_ll_section_container);
        rcvCategories = frg.findViewById(R.id.frg_home_rcv_categories);


        mInflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        llNewBrands = (LinearLayout) mInflater.inflate(R.layout.lay_home_sec, null);
        llUseAgainOffers = (LinearLayout) mInflater.inflate(R.layout.lay_home_sec, null);
        llRecentViewedSection = (LinearLayout) mInflater.inflate(R.layout.lay_home_sec, null);
        llNearbyOutlets = (LinearLayout) mInflater.inflate(R.layout.lay_home_sec, null);
        llPopCategories = (LinearLayout) mInflater.inflate(R.layout.lay_home_sec_view_pop_cat, null);



        rlHomeSearch.setOnClickListener(this);
        homeSettings.setOnClickListener(this);


    }

    private void OurOnCreate(View view) {
        initialize();
        bindViews(view);
        skeleton = Skeleton.bind(svHomeCenter).load(R.layout.lay_skeleton_home).color(R.color.skeleton_grey).angle(0).duration(1000).show();
        skeleton.hide();


        requestHomeApiEssential();


    }

    //region Api Callings

    private void requestHomeApiExtra() {
        isHomeExtraResponded = false;
        Home_WebHit_Post_HomeApiExtra Home_WebHit_Post_homeApiExtra = new Home_WebHit_Post_HomeApiExtra();
        Home_WebHit_Post_homeApiExtra.requestHomeApiExtra(getContext(), new IWebCallback() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                if (isSuccess) {
                    isHomeExtraResponded = true;


                } else {
                    if (!strHomeApiErrorMsg.equalsIgnoreCase(strMsg)) {
                        strHomeApiErrorMsg = strMsg;
                        AppConfig.getInstance().showAlertDialog(requireActivity(),null, strMsg, null, null, false, true, null);
                    }
                }
            }

            @Override
            public void onWebException(Exception ex) {
                if (!strHomeApiErrorMsg.equalsIgnoreCase(ex.getMessage())) {
                    strHomeApiErrorMsg = ex.getMessage();
                    AppConfig.getInstance().showAlertDialog(requireActivity(),null, AppConfig.getInstance().getNetworkExceptionMessage(ex.getMessage()), null, null, false, true, null);
                }
            }
        });


    }
    @SuppressLint("HardwareIds")
    private void requestHomeApiEssential() {

        if (skeleton != null)
            skeleton.show();

        clearAllLists();
        llSectionContainer.removeAllViews();

        requestHomeApiExtra();
        String android_device_id = "";
        try {
            android_device_id = Settings.Secure.getString(requireContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        isHomeEssentialResponded=false;
        Home_WebHit_Post_HomeApiEssential Home_WebHit_Post_homeApiEssential = new Home_WebHit_Post_HomeApiEssential();
        Home_WebHit_Post_homeApiEssential.requestHomeApiEssential(getContext(), new IWebCallback() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                if (skeleton!=null)
                    skeleton.hide();
                if (isSuccess) {
                    isHomeEssentialResponded=true;
//                    if (isHomeEssentialResponded && isHomeExtraResponded){
                        initializeHomeEssentialData();
                        initializeHomeExtraData();
                        populateHomeData();
//                    }

                }
                else {
                    if (!strHomeApiErrorMsg.equalsIgnoreCase(strMsg)) {
                        strHomeApiErrorMsg = strMsg;
                        AppConfig.getInstance().showAlertDialog(requireActivity(),null, strMsg, null, null, false, true, null);
                    }

                }
            }

            @Override
            public void onWebException(Exception ex) {
                if (skeleton!=null)
                    skeleton.hide();
                if (!strHomeApiErrorMsg.equalsIgnoreCase(ex.getMessage())) {
                    strHomeApiErrorMsg = ex.getMessage();
                    AppConfig.getInstance().showAlertDialog(requireActivity(),null, AppConfig.getInstance().getNetworkExceptionMessage(ex.getMessage()), null, null, false, true, null);
                }

            }

        }, android_device_id);

    }

    //endregion

    //region Home Data Populate
    private void populateHomeData() {

        if (lstOffersUseAgain.size() > 0) {
            addUseAgainOffers();
        } else {
            llSectionContainer.removeView(llUseAgainOffers);
        }

        if (lstRecentViewedSection.size() > 0) {
            addRecentViewedOutlets();
        } else {
            llSectionContainer.removeView(llRecentViewedSection);
        }


        if (lstNearbyOutlets.size() > 0) {
            addNearbyOutlets();
        } else {
            llSectionContainer.removeView(llNearbyOutlets);
        }

        if (lstPopCategories.size() > 0) {
            addPopCategories();
        } else {
            llSectionContainer.removeView(llPopCategories);
        }


        if (lstNewBrands.size() > 0) {
            addNewBrands();
        } else {
            llSectionContainer.removeView(llNewBrands);
        }




    }


    private void addUseAgainOffers() {

        //Title
        String strUseAgainOffers = requireContext().getString(R.string.frg_home_use_again);

        TextView txvTitle = llUseAgainOffers.findViewById(R.id.layhomsec_txv_ttl);
        txvTitle.setText(strUseAgainOffers);

        //ViewAll button
        RelativeLayout btnViewAll = llUseAgainOffers.findViewById(R.id.layhomsec_btn_viewall);

        btnViewAll.setOnClickListener(v -> {
            String tag = returnStackFragment();
            Fragment previousFrg = mFrgmgr.findFragmentByTag(tag);


            navToOffersFragment(previousFrg);

        });

        //RecyclerView
        rcvUseAgainOffers = llUseAgainOffers.findViewById(R.id.layhomsec_rcv_item);
        adapterUseAgainOffers = new HomeUseAgainAdapter((eventId, position) -> {

            switch (eventId) {
                case IAdapterCallback.EVENT_A:
                    //Clicked on parent
                    int offerId = Integer.parseInt(lstOffersUseAgain.get(position).getOfferId());
                    String saveApprox = lstOffersUseAgain.get(position).getApproxSaving();

                    dModel_offerDetail = new DModel_OfferDetail(offerId,
                            position,
                            0,
                            "lstOffersUseAgain",
                            saveApprox,
                            lstOffersUseAgain.get(position).getOfferName(),
                            lstOffersUseAgain.get(position).getOfferFreeDescription(),
                            lstOffersUseAgain.get(position).getCategoryId(),
                            lstOffersUseAgain.get(position).getMerchantName(),
                            lstOffersUseAgain.get(position).getMerchantAddress(),
                            lstOffersUseAgain.get(position).getMerchantId(),
                            lstOffersUseAgain.get(position).getMerchantLogo(),
                            lstOffersUseAgain.get(position).getPersonsDetail(),
                            lstOffersUseAgain.get(position).isFavourite(),
                            lstOffersUseAgain.get(position).isCanRedeem(),
                            lstOffersUseAgain.get(position).getExpiryDate(),
                            lstOffersUseAgain.get(position).isCanSendGift(),
                            lstOffersUseAgain.get(position).getDiscountType(),
                            lstOffersUseAgain.get(position).getApproxSavingPercentage()
                    );
                    showOfferDetailAlertDialog(dModel_offerDetail);

                    AppConfig.getInstance().loadRecentViewed();
                    if (adapterRecentSection!=null){
                        addRecentViewedOutlets();
                        adapterRecentSection.notifyDataSetChanged();
                    }
                    break;

                case IAdapterCallback.EVENT_B:

                    break;

            }
        }, getActivity(), lstOffersUseAgain);
        rcvUseAgainOffers.setAdapter(adapterUseAgainOffers);
        rcvUseAgainOffers.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        //Add newly inflated view to required parent
        if (llUseAgainOffers.getParent() == null) {
            llSectionContainer.addView(llUseAgainOffers);
        }

    }

    private void addNearbyOutlets() {

        //Title
        String strNearbyOutlets = requireContext().getString(R.string.frg_home_offers_nearby);

        TextView txvTitle = llNearbyOutlets.findViewById(R.id.layhomsec_txv_ttl);
        txvTitle.setText(strNearbyOutlets);

        //ViewAll button
        RelativeLayout btnViewAll = llNearbyOutlets.findViewById(R.id.layhomsec_btn_viewall);

        btnViewAll.setOnClickListener(v -> {
            String tag = returnStackFragment();
            Fragment previousFrg = mFrgmgr.findFragmentByTag(tag);
            AppConfig.getInstance().mCategorySorting = new DModel_CategorySorting(
                    AppConstt.DEFAULT_VALUES.Both,
                    AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION,
                    0,
                    "",
                    0,
                    "", "", "", 0);
            navToNearbyOutletsFragment(previousFrg);

        });

        //RecyclerView
        rcvNearbyOutlets = llNearbyOutlets.findViewById(R.id.layhomsec_rcv_item);
        adapterNearbyOutlets = new HomeNearbyOutletsAdapter((eventId, position) -> {

            switch (eventId) {
                case IAdapterCallback.EVENT_A:
                    //Clicked on parent
                    Bundle bundle = new Bundle();
                    if (lstNearbyOutlets.get(position).getIsMultipleChild()) {

                        int catdegory_id = Integer.parseInt(lstNearbyOutlets.get(position).getCategory_id());
                        AppConfig.getInstance().mCategorySorting = new DModel_CategorySorting(
                                AppConstt.DEFAULT_VALUES.Both,
                                AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION,
                                catdegory_id,
                                lstNearbyOutlets.get(position).getParent_outlet_name(),
                                0,
                                lstNearbyOutlets.get(position).getParent_outlet_id(),
                                "", "", 0);


                        navToCategoryHubFragment(bundle);
                    } else {
                        String tag = returnStackFragment();
                        Fragment previousFrg = mFrgmgr.findFragmentByTag(tag);
                        bundle.putInt(AppConstt.BundleStrings.outletId, Integer.parseInt(lstNearbyOutlets.get(position).getId()));
                        navToMerchantDetailFragment(previousFrg, bundle);

                        AppConfig.getInstance().updateRecentViewed(new DModel_Recent_Outlet(lstNearbyOutlets.get(position).getId(), lstNearbyOutlets.get(position).getOutlet_image(),
                                lstNearbyOutlets.get(position).getName(), ""));

                    }


                    break;

                case IAdapterCallback.EVENT_B:
                    //Not being fired now
                    break;

            }

        }, getActivity(), lstNearbyOutlets);
        rcvNearbyOutlets.setAdapter(adapterNearbyOutlets);
        rcvNearbyOutlets.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        //Add newly inflated view to required parent
        if (llNearbyOutlets.getParent() == null) {
            llSectionContainer.addView(llNearbyOutlets);
        }

    }

    private void addRecentViewedOutlets() {

        //Title
        String strRecentViewed = requireContext().getString(R.string.frg_home_recent_viewed);

        TextView txvTitle = llRecentViewedSection.findViewById(R.id.layhomsec_txv_ttl);
        txvTitle.setText(strRecentViewed);

        //ViewAll button

        RelativeLayout btnViewAll = llRecentViewedSection.findViewById(R.id.layhomsec_btn_viewall);
        btnViewAll.setVisibility(View.INVISIBLE);

        //RecyclerView

        rcvRecentSection = llRecentViewedSection.findViewById(R.id.layhomsec_rcv_item);
        adapterRecentSection = new HomeRecentViewedAdapter((eventId, position) -> {

            switch (eventId) {
                case IAdapterCallback.EVENT_A:
                    Bundle b = new Bundle();
                    b.putInt(AppConstt.BundleStrings.outletId, Integer.parseInt(lstRecentViewedSection.get(position).getId()));

                    navToMerchantDetailFragment(preFrg, b);

                    break;


            }
        }, getActivity(), AppConfig.getInstance().lstRecentViewed);
        rcvRecentSection.setAdapter(adapterRecentSection);
        rcvRecentSection.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        //Add newly inflated view to required parent
        if (llRecentViewedSection.getParent() == null) {
            llSectionContainer.addView(llRecentViewedSection);
        }

    }

    private void addNewBrands() {
        //Title
        String strNewBrands = requireContext().getResources().getString(R.string.frg_home_new_brands);
        TextView txvTitle = llNewBrands.findViewById(R.id.layhomsec_txv_ttl);
        txvTitle.setText(strNewBrands);

        //ViewAll button
        RelativeLayout btnViewAll = llNewBrands.findViewById(R.id.layhomsec_btn_viewall);
        btnViewAll.setOnClickListener(v -> {

            String tag = returnStackFragment();
            Fragment previousFrg = mFrgmgr.findFragmentByTag(tag);
            AppConfig.getInstance().mCategorySorting = new DModel_CategorySorting(
                    AppConstt.DEFAULT_VALUES.Both,
                    AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION,
                    0,
                    "",
                    0,
                    "", "", "", 0);
            navToNewBrandsFragment(previousFrg);

        });

        //RecyclerView
        rcvNewBrands = llNewBrands.findViewById(R.id.layhomsec_rcv_item);

        adapterNewBrands = new HomeNewBrandsAdapter((eventId, position) -> {

            switch (eventId) {
                case IAdapterCallback.EVENT_A:
                    //Clicked on parent
                    Bundle bundle = new Bundle();
                    if (lstNewBrands.get(position).isMultipleChild()) {

                        AppConfig.getInstance().mCategorySorting = new DModel_CategorySorting(
                                AppConstt.DEFAULT_VALUES.Both,
                                AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION,
                                lstNewBrands.get(position).getCategory_id(),
                                lstNewBrands.get(position).getParent_outlet_name(),
                                0,
                                lstNewBrands.get(position).getParents_id(),
                                "", "", 0);

                        navToCategoryHubFragment(bundle);
                    }
                    else {
                        String tag = returnStackFragment();
                        Fragment previousFrg = mFrgmgr.findFragmentByTag(tag);
                        bundle.putInt(AppConstt.BundleStrings.outletId, Integer.parseInt(lstNewBrands.get(position).getOutlet_id()));
                        navToMerchantDetailFragment(previousFrg, bundle);

                        AppConfig.getInstance().updateRecentViewed(new DModel_Recent_Outlet(lstNewBrands.get(position).getOutlet_id(), lstNewBrands.get(position).getOutlet_logo(),
                                lstNewBrands.get(position).getParent_outlet_name(), ""));

                    }
//

                    break;

                case IAdapterCallback.EVENT_B:
                    //Not being fired now
                    break;

            }
        }, getActivity(), lstNewBrands);
        rcvNewBrands.setAdapter(adapterNewBrands);
        rcvNewBrands.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rcvNewBrands.setNestedScrollingEnabled(false);

        //Add newly inflated view to required parent
        if (llNewBrands.getParent() == null) {
            llSectionContainer.addView(llNewBrands);
        }
    }

    private void addPopCategories() {
        //Title
        String strPopularCategories = requireContext().getResources().getString(R.string.frg_home_pop_categries);
        TextView txvTitle = llPopCategories.findViewById(R.id.layhomsec_txv_ttl);
        txvTitle.setText(strPopularCategories);

        //ViewAll button
        RelativeLayout btnViewAll = llPopCategories.findViewById(R.id.layhomsec_btn_viewall);
        btnViewAll.setVisibility(View.INVISIBLE);

        RelativeLayout btnViewAllBig = llPopCategories.findViewById(R.id.layhomsec_btn_viewall_popcat);

        TextView txvViewAll = llPopCategories.findViewById(R.id.layhomsec_txv_viewall_popcat);
        ImageView imvViewAll = llPopCategories.findViewById(R.id.imv_arrow_down);

        llPopCatRCVParent = llPopCategories.findViewById(R.id.ll_rcv_parent);

        //RecyclerView
        rcvPopCategories = llPopCategories.findViewById(R.id.layhomsec_rcv_item);

        adapterPopCategories = new HomePopCategoriesAdapter((eventId, position) -> {

            switch (eventId) {
                case IAdapterCallback.EVENT_A:
                    AppConfig.getInstance().mCategorySorting = new DModel_CategorySorting(
                            AppConstt.DEFAULT_VALUES.Both,
                            AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION,
                            0,
                            lstPopCategories.get(position).getCatTitle(),
                            lstPopCategories.get(position).getCatId(),
                            "", "", "", 0);

                    navToCategoryParentHubFragment();
                    break;

                case IAdapterCallback.EVENT_B:
                    //Not being fired now
                    break;

            }
        }, getActivity(), lstPopCategories);
        rcvPopCategories.setAdapter(adapterPopCategories);

        int rcvContainerHeight = lstPopCategories.size() <= 3 ? width / 3 : 2 * width / 3;
        llPopCatRCVParent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, rcvContainerHeight));
        txvViewAll.setText(getResources().getString(R.string.btn_view_all));
        imvViewAll.setRotation(0);
        isPopCatExpanded = false;

        if (lstPopCategories.size() <= 6) {
            btnViewAllBig.setVisibility(View.GONE);
        } else {
            btnViewAllBig.setVisibility(View.VISIBLE);
        }

        btnViewAllBig.setOnClickListener(v -> {
            if (isPopCatExpanded) {
                AppConfig.slideView(llPopCatRCVParent, llPopCatRCVParent.getLayoutParams().height, (width / 3) * 2, 300);
                llPopCatRCVParent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (width / 3) * 2));
                txvViewAll.setText(getResources().getString(R.string.btn_view_all));
                imvViewAll.setRotation(0);
                isPopCatExpanded = false;

            } else {
                AppConfig.slideView(llPopCatRCVParent, llPopCatRCVParent.getLayoutParams().height, (width / 3) * (lstPopCategories.size() % 3 > 0 ? (lstPopCategories.size() / 3) + 1 : lstPopCategories.size() / 3), 300);
                llPopCatRCVParent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (width / 3) * (lstPopCategories.size() % 3 > 0 ? (lstPopCategories.size() / 3) + 1 : lstPopCategories.size() / 3)));
                txvViewAll.setText(getResources().getString(R.string.btn_view_less));
                imvViewAll.setRotation(180);
                isPopCatExpanded = true;
            }

        });

        rcvPopCategories.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rcvPopCategories.setNestedScrollingEnabled(false);
        //Add newly inflated view to required parent
        if (llPopCategories.getParent() == null) {
            llSectionContainer.addView(llPopCategories);
        }
    }

    private void initializeHomeEssentialData() {

        if (Home_WebHit_Post_HomeApiEssential.responseObject != null &&
                Home_WebHit_Post_HomeApiEssential.responseObject.getData() != null) {

            txvWhereUSave.setVisibility(View.VISIBLE);
            
            }

            //Initializing categories
            lstCategories.clear();
            if (Home_WebHit_Post_HomeApiExtra.responseObject.getData().getCategories() != null &&
                    Home_WebHit_Post_HomeApiExtra.responseObject.getData().getCategories().size() > 0) {
                for (int i = 0; i < Home_WebHit_Post_HomeApiExtra.responseObject.getData().getCategories().size(); i++) {
                    String catName = "";
                    String catgeoryName = "";
                    if (AppConfig.getInstance().mLanguage.equalsIgnoreCase(AppConstt.AppLanguage.ARABIC)) {
                        if (Home_WebHit_Post_HomeApiExtra.responseObject.getData().getCategories().get(i).getName_ar() != null) {
                            catName = Home_WebHit_Post_HomeApiExtra.responseObject.getData().getCategories().get(i).getName_ar();
                            try {
                                String[] separated = catName.split(" و ");
                                catgeoryName = separated[0] + " و \n" + separated[1].trim();
                            } catch (Exception e) {
                                catgeoryName = catName;
                                e.printStackTrace();
                            }

                        } else {
                            catName = Home_WebHit_Post_HomeApiExtra.responseObject.getData().getCategories().get(i).getName();
                            try {
                                String[] separated = catName.split("&");
                                catgeoryName = separated[0] + "& \n" + separated[1].trim();
                            } catch (Exception e) {
                                catgeoryName = catName;
                                e.printStackTrace();
                            }
                        }
                    } else {
                        catName = Home_WebHit_Post_HomeApiExtra.responseObject.getData().getCategories().get(i).getName();
                        try {
                            String[] separated = catName.split("&");
                            catgeoryName = separated[0] + "& \n" + separated[1].trim();
                        } catch (Exception e) {
                            catgeoryName = catName;
                            e.printStackTrace();
                        }
                    }
                    lstCategories.add(new DModel_Categories(
                            Home_WebHit_Post_HomeApiExtra.responseObject.getData().getCategories().get(i).getId(),
                            catgeoryName,
                            catName,
                            Home_WebHit_Post_HomeApiExtra.responseObject.getData().getCategories().get(i).getImage(),
                            Home_WebHit_Post_HomeApiExtra.responseObject.getData().getCategories().get(i).getLogo()

                    ));
                }

                adapterCategories = new HomeCategoriesAdapter(new IAdapterCallback() {
                    @Override
                    public void onAdapterEventFired(int eventId, int position) {
                        switch (eventId) {
                            case IAdapterCallback.EVENT_A:
                                //Clicked on parent
                                AppConfig.getInstance().mCategorySorting = new DModel_CategorySorting(
                                        AppConstt.DEFAULT_VALUES.Both,
                                        AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION,
                                        lstCategories.get(position).getCategoryID(),
                                        lstCategories.get(position).getCategoryTitle(),
                                        0,
                                        "", "", "", 0);

                                navToHomeDetailFragment();
                                break;

                            case IAdapterCallback.EVENT_B:
                                //Not being fired now
                                break;

                        }
                    }
                }, getActivity(), lstCategories);
                rcvCategories.setAdapter(adapterCategories);
                rcvCategories.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            }


            // Initializing Offer Used Again Section
            lstOffersUseAgain.clear();
            if (Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain() != null &&
                    Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().size() > 0) {

                String currencyUnit = getResources().getString(R.string.txv_qatar_riyal);



                for (int i = 0; i < Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().size(); i++) {

                    String aproxSavings;
                    if (Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().get(i).getApproxSaving() != null) {
                        aproxSavings = String.valueOf(Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().get(i).getApproxSaving());
                    } else {
                        aproxSavings = "00.00";
                    }
                    int approxSavingsRounded = 0;
                    float approxSavingsFloat = Float.parseFloat(aproxSavings);
                    approxSavingsRounded = (int) approxSavingsFloat;
                    String saved = currencyUnit + " " + approxSavingsRounded;

                    String savedPercentage;
                    if (Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().get(i).getPercentage_saving() != null) {
                        savedPercentage = String.valueOf(Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().get(i).getPercentage_saving());
                    } else {
                        savedPercentage = "00.00";
                    }
                    int approxPerRounded = 0;
                    float approxPerFloat = Float.parseFloat(savedPercentage);
                    approxPerRounded = (int) approxPerFloat;
                    String savedPer =  "" + approxPerRounded;



                    lstOffersUseAgain.add(new DModel_OfferUseAgain(
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().get(i).getId(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().get(i).getImage(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().get(i).getCategoryId(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().get(i).getCategoryName(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().get(i).getCategoryLogo(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().get(i).getOutletName(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().get(i).getOutletAddress(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().get(i).getOutletId(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().get(i).getOutletLogo(),
                            saved,
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().get(i).getTitle(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().get(i).getDescription(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().get(i).getValidFor(),
                            false,
                            true,
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().get(i).getEndDatetime(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().get(i).isCanSendGift(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getOfferUsedAgain().get(i).getDiscountType(),
                            savedPer
                    ));
                }
            }


            //Initializing Nearby outlets Section
            lstNearbyOutlets.clear();
            if (Home_WebHit_Post_HomeApiEssential.responseObject.getData().getNearbyOutlets() != null &&
                    Home_WebHit_Post_HomeApiEssential.responseObject.getData().getNearbyOutlets().size() > 0) {
                for (int i = 0; i < Home_WebHit_Post_HomeApiEssential.responseObject.getData().getNearbyOutlets().size(); i++) {

                    boolean isMultipleChild=(Home_WebHit_Post_HomeApiEssential.responseObject.getData().getNearbyOutlets().get(i).getIsMultipleChild().equalsIgnoreCase("1"));
                    lstNearbyOutlets.add(new DModel_NearbyOutlets(
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getNearbyOutlets().get(i).getId(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getNearbyOutlets().get(i).getName(),
                            isMultipleChild,
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getNearbyOutlets().get(i).getOffersCount(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getNearbyOutlets().get(i).getDistance(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getNearbyOutlets().get(i).getLinked_outlet_category().get(0).getId(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getNearbyOutlets().get(i).getLinked_outlet_category().get(0).getName(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getNearbyOutlets().get(i).getLinked_outlet_category().get(0).getName_ar(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getNearbyOutlets().get(i).getLinked_outlet_category().get(0).getImage(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getNearbyOutlets().get(i).getLinked_outlet_category().get(0).getImage_v2(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getNearbyOutlets().get(i).getLinked_outlet_category().get(0).getLogo(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getNearbyOutlets().get(i).getImage(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getNearbyOutlets().get(i).getOutlets_parents().getId(),
                            Home_WebHit_Post_HomeApiEssential.responseObject.getData().getNearbyOutlets().get(i).getOutlets_parents().getName()
                    ));
                }
            }

        }

    private void initializeHomeExtraData() {
        if (Home_WebHit_Post_HomeApiExtra.responseObject != null &&
                Home_WebHit_Post_HomeApiExtra.responseObject.getData() != null) {


            // Initializing Popular Categories
            if (Home_WebHit_Post_HomeApiExtra.responseObject.getData().getPopularCategories() != null &&
                    Home_WebHit_Post_HomeApiExtra.responseObject.getData().getPopularCategories().size() > 0) {
                lstPopCategories.clear();
                for (int i = 0; i < Home_WebHit_Post_HomeApiExtra.responseObject.getData().getPopularCategories().size(); i++) {
                    String catName = "";
                    if (AppConfig.getInstance().mLanguage.equalsIgnoreCase(AppConstt.AppLanguage.ARABIC)) {
                        if (Home_WebHit_Post_HomeApiExtra.responseObject.getData().getPopularCategories().get(i).getName_ar() != null) {
                            catName = Home_WebHit_Post_HomeApiExtra.responseObject.getData().getPopularCategories().get(i).getName_ar();
                        } else {
                            catName = Home_WebHit_Post_HomeApiExtra.responseObject.getData().getPopularCategories().get(i).getName();
                        }
                    } else {
                        catName = Home_WebHit_Post_HomeApiExtra.responseObject.getData().getPopularCategories().get(i).getName();
                    }

                    lstPopCategories.add(new DModel_PopupCat(
                            Home_WebHit_Post_HomeApiExtra.responseObject.getData().getPopularCategories().get(i).getId(),
                            catName,
                            Home_WebHit_Post_HomeApiExtra.responseObject.getData().getPopularCategories().get(i).getImage()
                    ));
                }
            }

            // Initializing New Brands
            lstNewBrands.clear();
            if (Home_WebHit_Post_HomeApiExtra.responseObject.getData().getNewBrands() != null &&
                    Home_WebHit_Post_HomeApiExtra.responseObject.getData().getNewBrands().size() > 0) {
                for (int i = 0; i < Home_WebHit_Post_HomeApiExtra.responseObject.getData().getNewBrands().size(); i++) {
                    lstNewBrands.add(new DModel_NewBrands(
                            Home_WebHit_Post_HomeApiExtra.responseObject.getData().getNewBrands().get(i).getParentOutletName(),
                            Home_WebHit_Post_HomeApiExtra.responseObject.getData().getNewBrands().get(i).getOutletId(),
                            Home_WebHit_Post_HomeApiExtra.responseObject.getData().getNewBrands().get(i).getParentsId(),
                            Home_WebHit_Post_HomeApiExtra.responseObject.getData().getNewBrands().get(i).getOutletImage(),
                            Home_WebHit_Post_HomeApiExtra.responseObject.getData().getNewBrands().get(i).getOutletLogo(),
                            Home_WebHit_Post_HomeApiExtra.responseObject.getData().getNewBrands().get(i).getOutletType(),
                            Home_WebHit_Post_HomeApiExtra.responseObject.getData().getNewBrands().get(i).isMultipleChild(),
                            Home_WebHit_Post_HomeApiExtra.responseObject.getData().getNewBrands().get(i).getCategoryId(),
                            Home_WebHit_Post_HomeApiExtra.responseObject.getData().getNewBrands().get(i).getCategoryName(),
                            Home_WebHit_Post_HomeApiExtra.responseObject.getData().getNewBrands().get(i).getCategoryImage(),
                            Home_WebHit_Post_HomeApiExtra.responseObject.getData().getNewBrands().get(i).getCategoryLogo()
                    ));
                }
            }



        }
    }

    //endregion

    //region Dialogs


    private void showOfferDetailAlertDialog(DModel_OfferDetail dModel_offerDetail) {

        OfferDetailAlertDialog cdd = new OfferDetailAlertDialog(requireActivity(), this, dModel_offerDetail, null);
        cdd.show();
        cdd.setCancelable(true);
    }

    //endregion


    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.frg_home_settings_iv) {
            navToSettingsFragment();
        } else if (id == R.id.frg_homold_tool_bar_rl_search) {
            navToSearchOffersFragment();
        }


    }

    //region Navigation's

    public void navToHomeDetailFragment() {
        FragmentTransaction ft = requireFragmentManager().beginTransaction();
        Fragment frg = new HomeDetailFragment();
        ft.add(R.id.main_act_content_frame, frg, AppConstt.FrgTag.HomeDetailFragment);
        ft.addToBackStack(AppConstt.FrgTag.HomeDetailFragment);
        ft.hide(this);
        ft.commit();
    }

    private void navToSettingsFragment() {
        FragmentTransaction ft = requireFragmentManager().beginTransaction();

        if (AppConfig.getInstance().isAppLangArabic)
            ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                    R.anim.enter_from_right, R.anim.exit_to_left);
        else
            ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                    R.anim.enter_from_left, R.anim.exit_to_right);
        Fragment frg = new SettingsUpFragment();
        ft.add(R.id.main_act_content_frame, frg, AppConstt.FrgTag.SettingsUpFragment);
        ft.addToBackStack(AppConstt.FrgTag.SettingsUpFragment);
        ft.hide(this);
        ft.commit();

    }

    private void navToSearchOffersFragment() {
        FragmentTransaction ft = requireFragmentManager().beginTransaction();
        if (AppConfig.getInstance().isAppLangArabic)
            ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                    R.anim.enter_from_right, R.anim.exit_to_left);
        else
            ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                    R.anim.enter_from_left, R.anim.exit_to_right);
        Fragment frg = new SearchOffersFragment();
        ft.add(R.id.main_act_content_frame, frg, AppConstt.FrgTag.SearchOffersFragment);
        ft.addToBackStack(AppConstt.FrgTag.SearchOffersFragment);
        ft.hide(this);
        ft.commit();
    }

    private void navToMerchantDetailFragment(Fragment _previousFrg, Bundle _bundle) {
        if (getActivity() != null && isAdded()) {
            FragmentTransaction ft = requireFragmentManager().beginTransaction();
            Fragment frg = new MerchantDetailFragment();
            frg.setArguments(_bundle);
            ft.add(R.id.main_act_content_frame, frg, AppConstt.FrgTag.MerchantDetailFragment);
            ft.addToBackStack(AppConstt.FrgTag.MerchantDetailFragment);
            ft.hide(_previousFrg);
            ft.commit();
        }
    }

    private void navToNewBrandsFragment(Fragment _previousFrg) {
        FragmentTransaction ft = requireFragmentManager().beginTransaction();
        if (AppConfig.getInstance().isAppLangArabic)
            ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                    R.anim.enter_from_right, R.anim.exit_to_left);
        else
            ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                    R.anim.enter_from_left, R.anim.exit_to_right);
        Fragment frg = new NewBrandsPagerFragment();
        ft.add(R.id.main_act_content_frame, frg, AppConstt.FrgTag.NewBrandsPagerFragment);
        ft.addToBackStack(AppConstt.FrgTag.NewBrandsPagerFragment);
        ft.hide(_previousFrg);
        ft.commit();
    }

    private void navToNearbyOutletsFragment(Fragment _previousFrg) {
        FragmentTransaction ft = requireFragmentManager().beginTransaction();
        if (AppConfig.getInstance().isAppLangArabic)
            ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                    R.anim.enter_from_right, R.anim.exit_to_left);
        else
            ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                    R.anim.enter_from_left, R.anim.exit_to_right);
        Fragment frg = new NearbyOutletsPagerFragment();
        ft.add(R.id.main_act_content_frame, frg, AppConstt.FrgTag.NearbyOutletsPagerFragment);
        ft.addToBackStack(AppConstt.FrgTag.NearbyOutletsPagerFragment);
        ft.hide(_previousFrg);
        ft.commit();
    }

    private void navToCategoryParentHubFragment() {
        FragmentTransaction ft = requireFragmentManager().beginTransaction();
        if (AppConfig.getInstance().isAppLangArabic)
            ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                    R.anim.enter_from_right, R.anim.exit_to_left);
        else
            ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                    R.anim.enter_from_left, R.anim.exit_to_right);
        Fragment frg = new CategoryParentHubFragment();
        ft.add(R.id.main_act_content_frame, frg, AppConstt.FrgTag.CategoryParentHubFragment);
        ft.addToBackStack(AppConstt.FrgTag.CategoryParentHubFragment);
        ft.hide(this);
        ft.commit();
    }

    private void navToCategoryHubFragment(Bundle _bundle) {
        FragmentTransaction ft = requireFragmentManager().beginTransaction();
        if (AppConfig.getInstance().isAppLangArabic)
            ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                    R.anim.enter_from_right, R.anim.exit_to_left);
        else
            ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                    R.anim.enter_from_left, R.anim.exit_to_right);
        Fragment frg = new CategoryHubFragment();
        frg.setArguments(_bundle);
        ft.add(R.id.main_act_content_frame, frg, AppConstt.FrgTag.CategoryHubFragment);
        ft.addToBackStack(AppConstt.FrgTag.CategoryHubFragment);
        ft.hide(this);
        ft.commit();
    }

    private void navToOffersFragment(Fragment _previousFrg) {
        FragmentTransaction ft = requireFragmentManager().beginTransaction();
        Fragment frg = new OffersFragment();
        ft.add(R.id.main_act_content_frame, frg, AppConstt.FrgTag.OffersFragment);
        ft.addToBackStack(AppConstt.FrgTag.OffersFragment);
        ft.hide(_previousFrg);
        ft.commit();
    }

    public String returnStackFragment() {
        int index = mFrgmgr.getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntry = null;
        String tag = "";
        if (index >= 0) {
            backEntry = mFrgmgr.getBackStackEntryAt(index);
            tag = backEntry.getName();
        }
        return tag;
    }

    //endregion

    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            if (getActivity() != null && isAdded()) {
                AppConfig.getInstance().closeKeyboard(getActivity());
                AppConfig.getInstance().loadRecentViewed();
                lstRecentViewedSection = AppConfig.getInstance().loadRecentViewed();
                if (lstRecentViewedSection.size() > 0) {
                    if (adapterRecentSection != null) {
                        addRecentViewedOutlets();
                        adapterRecentSection.notifyDataSetChanged();
                    }
                    else {
                        requireFragmentManager().beginTransaction().detach(this).commit();
                        requireFragmentManager().beginTransaction().attach(this).commit();
                    }
                }
                else {
                    adapterRecentSection = null;
                    if (llRecentViewedSection != null && llSectionContainer != null) {
                        llSectionContainer.removeView(llRecentViewedSection);
                    }
                }

            }

        }
    }


}