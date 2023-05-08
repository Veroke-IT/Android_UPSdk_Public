package com.example.android_up_sdk.HomeAuxiliaries;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android_up_sdk.HomeAuxiliaries.Adapters.CategoryParentHubPagerAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.HomeDetailCollectionsAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_CategorySorting;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_Collection;
import com.example.android_up_sdk.HomeAuxiliaries.WebServices.HomeDetail_WebHit_Post_homeApiDetails;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.example.android_up_sdk.Utils.IAdapterCallback;
import com.example.android_up_sdk.Utils.IWebCallback;
import com.example.android_up_sdk.Utils.ProgressDialogue;
import java.util.ArrayList;
import java.util.List;


public class HomeDetailFragment extends Fragment implements View.OnClickListener {

    TextView txvTitle;
    private int strCategoryId;
    String strCategoryName;
    RelativeLayout rlHomeSearch, rlBack;
    LayoutInflater mInflater;
    LinearLayout llSectionContainer;

    RecyclerView rcvPopCategories;

    HomeDetailCollectionsAdapter adapterCollections;
    ArrayList<DModel_Collection> lstCollections;

    private LinearLayout  llCollections;


    private ProgressDialogue progressDialogue;

    private ViewPager viewPager;
    private List<String> listTitle;
    private RelativeLayout rlNearby, rlAlphabetical;
    private CheckBox cbNearby, cbAlphabetical;
    private TextView txvNearby, txvAlphabetical;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home_detail, container, false);
        OurOnCreate(view);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchTabs(position==1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        requestHomeApiDetail();


        return view;
    }



    private void initialize() {
        progressDialogue = new ProgressDialogue();
        lstCollections = new ArrayList<>();
        strCategoryId = AppConfig.getInstance().mCategorySorting.getCategoryId();
        strCategoryName = AppConfig.getInstance().mCategorySorting.getCategoryName();
        listTitle = new ArrayList<>();
        listTitle.add(getString(R.string.btn_by_location));
        listTitle.add(getString(R.string.btn_alphabetically));
    }

    private void bindViews(View frg) {
        rlHomeSearch = frg.findViewById(R.id.frg_home_detail_tool_bar_rl_search);
        txvTitle = frg.findViewById(R.id.frg_home_detail_tool_bar_txv_title);

        llSectionContainer = frg.findViewById(R.id.frg_ll_section_container);
        rlBack = frg.findViewById(R.id.rg_home_detail_tool_bar_rl_back);

        viewPager = frg.findViewById(R.id.frg_credential_viewpager);

        mInflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        llCollections = (LinearLayout) mInflater.inflate(R.layout.lay_home_sec, null);
        rlNearby = frg.findViewById(R.id.frg_category_outlets_parent_hub_rl_tab_nearby);
        cbNearby = frg.findViewById(R.id.frg_category_outlets_parent_hub_cb_tab_nearby);
        txvNearby = frg.findViewById(R.id.frg_category_outlets_parent_hub_txv_tab_nearby);

        rlAlphabetical = frg.findViewById(R.id.frg_category_outlets_parent_hub_rl_tab_alphabetical);
        cbAlphabetical = frg.findViewById(R.id.frg_category_outlets_parent_hub_cb_tab_alphabetical);
        txvAlphabetical = frg.findViewById(R.id.frg_category_outlets_parent_hub_txv_tab_alphabetical);

        rlNearby.setOnClickListener(this);
        rlAlphabetical.setOnClickListener(this);

        txvTitle.setText(AppConfig.getInstance().mCategorySorting.getCategoryName());
        txvTitle.setAllCaps(true);

        rlBack.setOnClickListener(this);
        rlHomeSearch.setOnClickListener(this);
    }

    private void OurOnCreate(View view) {
        initialize();
        bindViews(view);

    }

    //region Adapters

    private void updateAdapter(String title) {
        viewPager.setSaveFromParentEnabled(false);
        viewPager.setAdapter(buildAdapter());
        setCurrentPage(0);
        txvTitle.setText(title);
    }

    public void switchTabs(boolean isAlphabetical) {
        if (isAlphabetical) {
            txvNearby.setTextColor(getResources().getColor(R.color.text_black));
            cbNearby.setChecked(false);
            rlNearby.setEnabled(true);
            txvAlphabetical.setTextColor(getResources().getColor(R.color.white));
            cbAlphabetical.setChecked(true);
            rlAlphabetical.setEnabled(false);
        } else {
            txvAlphabetical.setTextColor(getResources().getColor(R.color.text_black));
            cbAlphabetical.setChecked(false);
            rlAlphabetical.setEnabled(true);
            txvNearby.setTextColor(getResources().getColor(R.color.white));
            cbNearby.setChecked(true);
            rlNearby.setEnabled(false);
        }
    }

    private PagerAdapter buildAdapter() {
        return (new CategoryParentHubPagerAdapter(requireActivity(), getChildFragmentManager(), listTitle));
    }

    public void setCurrentPage(int _position) {
        viewPager.setCurrentItem(_position, true);
        switchTabs(_position == 1);
    }
    //endregion

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.frg_home_detail_tool_bar_rl_search) {
            navToSearchOffersFragment();
        } else if (id == R.id.rg_home_detail_tool_bar_rl_back) {
            requireFragmentManager().popBackStackImmediate();
        } else if (id == R.id.frg_category_outlets_parent_hub_rl_tab_nearby) {
            setCurrentPage(0);
        } else if (id == R.id.frg_category_outlets_parent_hub_rl_tab_alphabetical) {
            setCurrentPage(1);
        }
    }

    private void addCollections() {
        //Title
        TextView txvTitle = llCollections.findViewById(R.id.layhomsec_txv_ttl);
        txvTitle.setVisibility(View.INVISIBLE);

        //ViewAll button
        RelativeLayout btnViewAll = llCollections.findViewById(R.id.layhomsec_btn_viewall);
        btnViewAll.setVisibility(View.GONE);

        //RecyclerView
        rcvPopCategories = llCollections.findViewById(R.id.layhomsec_rcv_item);
        adapterCollections = new HomeDetailCollectionsAdapter((eventId, position) -> {

            switch (eventId) {
                case IAdapterCallback.EVENT_A:

                    AppConfig.getInstance().mCategorySorting = new DModel_CategorySorting(
                            AppConstt.DEFAULT_VALUES.Both,
                            AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION,
                            strCategoryId,
                            lstCollections.get(position).getCollectionName(),
                            0, "",
                            lstCollections.get(position).getCollectionId() + "",
                            "", 0);
                    updateAdapter(lstCollections.get(position).getCollectionName());
                    break;

                case IAdapterCallback.EVENT_B:
                    //Not being fired now
                    break;

            }
        }, getActivity(), lstCollections);
        rcvPopCategories.setAdapter(adapterCollections);
        rcvPopCategories.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        if (lstCollections.size() == 0) {
            rcvPopCategories.setVisibility(View.GONE);
        }

        //Add newly inflated view to required parent
        if (llCollections.getParent() == null) {
            llSectionContainer.addView(llCollections);
        }
    }

    //region Navigation Function

    private void navToSearchOffersFragment() {
        FragmentTransaction ft = requireFragmentManager().beginTransaction();
        Fragment frg = new SearchOffersFragment();
        ft.add(R.id.main_act_content_frame, frg, AppConstt.FrgTag.SearchOffersFragment);
        ft.addToBackStack(AppConstt.FrgTag.SearchOffersFragment);
        ft.hide(this);
        ft.commit();
    }


    //endregion

    //region Web Hits
    private void requestHomeApiDetail() {
        progressDialogue.startIOSLoader(requireContext());


        HomeDetail_WebHit_Post_homeApiDetails homeDetail_webHit_post_homeApiDetails = new HomeDetail_WebHit_Post_homeApiDetails();
        homeDetail_webHit_post_homeApiDetails.requestHomeDetail(getContext(), new IWebCallback() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {

                if (isSuccess) {

                    initializeHomeDetailData();
                    populateHomeData();
                    progressDialogue.stopIOSLoader();
                } else {
                    progressDialogue.stopIOSLoader();
                    AppConfig.getInstance().showAlertDialog(requireActivity(),null, strMsg, null, null, false, true, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDialogue.stopIOSLoader();
                AppConfig.getInstance().showAlertDialog(requireActivity(),null, AppConfig.getInstance().getNetworkExceptionMessage(ex.getMessage()), null, null, false, true, null);
            }
        }, strCategoryId);
    }

    //endregion

    //region Value Update Functions
    private void initializeHomeDetailData() {
        if (HomeDetail_WebHit_Post_homeApiDetails.responseObject != null &&
                HomeDetail_WebHit_Post_homeApiDetails.responseObject.getData() != null) {



            lstCollections.clear();
            if (HomeDetail_WebHit_Post_homeApiDetails.responseObject.getData().getCollection() != null &&
                    HomeDetail_WebHit_Post_homeApiDetails.responseObject.getData().getCollection().size() > 0) {

                for (int i = 0; i < HomeDetail_WebHit_Post_homeApiDetails.responseObject.getData().getCollection().size(); i++) {
                    String collectnName;
                    if (AppConfig.getInstance().isAppLangArabic) {
                        collectnName = HomeDetail_WebHit_Post_homeApiDetails.responseObject.getData().getCollection().get(i).getName_ar();
                    } else {
                        collectnName = HomeDetail_WebHit_Post_homeApiDetails.responseObject.getData().getCollection().get(i).getName();
                    }
                    lstCollections.add(new DModel_Collection(
                            HomeDetail_WebHit_Post_homeApiDetails.responseObject.getData().getCollection().get(i).getId(),
                            HomeDetail_WebHit_Post_homeApiDetails.responseObject.getData().getCollection().get(i).getImage(),
                            collectnName
                    ));
                }

                AppConfig.getInstance().mCategorySorting = new DModel_CategorySorting(
                        AppConstt.DEFAULT_VALUES.Both,
                        AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION,
                        strCategoryId,
                        lstCollections.get(0).getCollectionName(),
                        0, "",
                        lstCollections.get(0).getCollectionId() + "",
                        "", 0);
                updateAdapter(lstCollections.get(0).getCollectionName());
            }

        }
    }

    private void populateHomeData() {
        if (lstCollections.size() > 0) {
            addCollections();
        } else {
            llSectionContainer.removeView(llCollections);
        }

    }
    //endregion
}