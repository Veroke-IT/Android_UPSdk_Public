package com.example.android_up_sdk.HomeAuxiliaries;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android_up_sdk.HomeAuxiliaries.Adapters.CategoryParentHubPagerAdapter;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;

import java.util.ArrayList;
import java.util.List;

public class CategoryParentHubFragment extends Fragment implements View.OnClickListener {

    RelativeLayout rlBack, rlSearch, rlSearchCenter, rlProgressBar, rlClose;
    private ViewPager viewPager;
    private TextView txvTitle;
    private EditText edtSearch;
    private String strCategoryName, strSortBy;
    private List<String> listTitle;
    private RelativeLayout rlNearby, rlAlphabetical;
    private CheckBox cbNearby, cbAlphabetical;
    private TextView txvNearby, txvAlphabetical;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_category_hub, container, false);
        init();
        bindViews(view);

        viewPager.setSaveFromParentEnabled(false);
        viewPager.setAdapter(buildAdapter());

        if (strSortBy == AppConstt.DEFAULT_VALUES.SORT_BY_ALPHABETICALLY) {
            setCurrentPage(1);
        } else {
            setCurrentPage(0);
        }

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

        return view;
    }


    public void setCurrentPage(int _position) {
        viewPager.setCurrentItem(_position, true);
        switchTabs(_position == 1);
    }

    private void init() {
        strCategoryName = AppConfig.getInstance().mCategorySorting.getCategoryName();
        strSortBy = AppConfig.getInstance().mCategorySorting.getSortBy();
        listTitle = new ArrayList<>();
        listTitle.add(getString(R.string.btn_by_location));
        listTitle.add(getString(R.string.btn_alphabetically));

    }


    private void bindViews(View frg) {
        rlBack = frg.findViewById(R.id.frg_category_outlets_parent_hub_rl_back);
        rlSearch = frg.findViewById(R.id.frg_category_outlets_parent_hub_rl_btn_search);
        rlSearchCenter = frg.findViewById(R.id.frg_category_outlets_parent_hub_rl_search_cntnr);
        rlProgressBar = frg.findViewById(R.id.frg_category_outlets_parent_hub_rl_progressbar);
        rlClose = frg.findViewById(R.id.frg_category_outlets_parent_hub_rl_close);
        edtSearch = frg.findViewById(R.id.frg_category_outlets_parent_hub_edt_search);
        txvTitle = frg.findViewById(R.id.frg_category_outlets_parent_hub_txv_title);
        txvTitle.setText(strCategoryName);

        viewPager = frg.findViewById(R.id.frg_credential_viewpager);

        rlSearch.setOnClickListener(this);
        rlClose.setOnClickListener(this);
        rlBack.setOnClickListener(this);

        rlNearby = frg.findViewById(R.id.frg_category_outlets_parent_hub_rl_tab_nearby);
        cbNearby = frg.findViewById(R.id.frg_category_outlets_parent_hub_cb_tab_nearby);
        txvNearby = frg.findViewById(R.id.frg_category_outlets_parent_hub_txv_tab_nearby);

        rlAlphabetical = frg.findViewById(R.id.frg_category_outlets_parent_hub_rl_tab_alphabetical);
        cbAlphabetical = frg.findViewById(R.id.frg_category_outlets_parent_hub_cb_tab_alphabetical);
        txvAlphabetical = frg.findViewById(R.id.frg_category_outlets_parent_hub_txv_tab_alphabetical);

        rlNearby.setOnClickListener(this);
        rlAlphabetical.setOnClickListener(this);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtSearch.getText().toString().length() > 2) {
                    getSearchLst(viewPager.getCurrentItem());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
        return (new CategoryParentHubPagerAdapter(getActivity(), getChildFragmentManager(), listTitle));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.frg_category_outlets_parent_hub_rl_tab_nearby) {
            setCurrentPage(0);
        } else if (id == R.id.frg_category_outlets_parent_hub_rl_tab_alphabetical) {
            setCurrentPage(1);
        } else if (id == R.id.frg_category_outlets_parent_hub_rl_back) {
            AppConfig.getInstance().closeKeyboard(requireActivity());
            FragmentManager fm = requireFragmentManager();
            fm.popBackStackImmediate();
        } else if (id == R.id.frg_category_outlets_parent_hub_rl_btn_search) {
            txvTitle.setVisibility(View.GONE);
            rlSearch.setVisibility(View.GONE);
            rlSearchCenter.setVisibility(View.VISIBLE);
            rlClose.setVisibility(View.VISIBLE);
            edtSearch.requestFocus();
            AppConfig.getInstance().openKeyboard(requireContext());
        } else if (id == R.id.frg_category_outlets_parent_hub_rl_close) {
            txvTitle.setVisibility(View.VISIBLE);
            rlSearch.setVisibility(View.VISIBLE);
            rlSearchCenter.setVisibility(View.GONE);
            rlClose.setVisibility(View.GONE);
            updateOutletLst(0);
            updateOutletLst(1);
        }
    }


    public void getSearchLst(int position) {
        Fragment currFrg;
        switch (position) {

            case AppConstt.ViewPagerState.CATEGORY_OUTLET:
                //Access specific fragment
                currFrg = ((CategoryParentHubPagerAdapter) viewPager.getAdapter()).getFragment(
                        AppConstt.ViewPagerState.CATEGORY_OUTLET);
                ((OutletFragment) currFrg).validateSearch(this, edtSearch.getText().toString());
                break;

            case AppConstt.ViewPagerState.CATEGORY_PARENT:
                //Access specific fragment
                currFrg = ((CategoryParentHubPagerAdapter) viewPager.getAdapter()).getFragment(
                        AppConstt.ViewPagerState.CATEGORY_PARENT);
                ((OutletParentsFragment) currFrg).validateSearch(this, edtSearch.getText().toString());
                break;
        }

    }

    public void updateOutletLst(int position) {
        Fragment currFrg;
        switch (position) {

            case AppConstt.ViewPagerState.CATEGORY_OUTLET:
                //Access specific fragment
                currFrg = ((CategoryParentHubPagerAdapter) viewPager.getAdapter()).getFragment(
                        AppConstt.ViewPagerState.CATEGORY_OUTLET);
                ((OutletFragment) currFrg).updateOutletLst();
                break;

            case AppConstt.ViewPagerState.CATEGORY_PARENT:
                //Access specific fragment
                currFrg = ((CategoryParentHubPagerAdapter) viewPager.getAdapter()).getFragment(
                        AppConstt.ViewPagerState.CATEGORY_PARENT);
                ((OutletParentsFragment) currFrg).updateOutletLst();
                break;
        }

    }

    public void updateSearchBar() {
        rlProgressBar.setVisibility(View.GONE);
        rlClose.setVisibility(View.VISIBLE);

    }

}