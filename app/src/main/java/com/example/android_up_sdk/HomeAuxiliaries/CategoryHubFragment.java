package com.example.android_up_sdk.HomeAuxiliaries;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android_up_sdk.HomeAuxiliaries.Adapters.CategoryHubPagerAdapter;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CategoryHubFragment extends Fragment implements View.OnClickListener {

    RelativeLayout rlBack, rlSearch, rlSearchCenter;
    private ViewPager viewPager;
    TextView txvTitle;
    EditText edtSearch;
    String strCategoryName, strParentId, strSortBy;
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

        if (Objects.equals(strSortBy, AppConstt.DEFAULT_VALUES.SORT_BY_ALPHABETICALLY)) {
            setCurrentPage(1);
        }
        else {
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

    private void init() {
        strCategoryName = AppConfig.getInstance().mCategorySorting.getCategoryName();
        strParentId = AppConfig.getInstance().mCategorySorting.getParentId();
        strSortBy = AppConfig.getInstance().mCategorySorting.getSortBy();
        listTitle = new ArrayList<>();
        listTitle.add(getString(R.string.btn_by_location));
        listTitle.add(getString(R.string.btn_alphabetically));
    }


    private void bindViews(View frg) {
        rlBack = frg.findViewById(R.id.frg_category_outlets_parent_hub_rl_back);
        rlSearch = frg.findViewById(R.id.frg_category_outlets_parent_hub_rl_btn_search);
        rlSearchCenter = frg.findViewById(R.id.frg_category_outlets_parent_hub_rl_search_cntnr);
        edtSearch = frg.findViewById(R.id.frg_category_outlets_parent_hub_edt_search);
        txvTitle = frg.findViewById(R.id.frg_category_outlets_parent_hub_txv_title);
        txvTitle.setText(strCategoryName);

        viewPager = frg.findViewById(R.id.frg_credential_viewpager);

        rlSearch.setVisibility(View.GONE);
        rlSearchCenter.setVisibility(View.GONE);


        rlBack.setOnClickListener(this);

        rlNearby = frg.findViewById(R.id.frg_category_outlets_parent_hub_rl_tab_nearby);
        cbNearby = frg.findViewById(R.id.frg_category_outlets_parent_hub_cb_tab_nearby);
        txvNearby = frg.findViewById(R.id.frg_category_outlets_parent_hub_txv_tab_nearby);

        rlAlphabetical = frg.findViewById(R.id.frg_category_outlets_parent_hub_rl_tab_alphabetical);
        cbAlphabetical = frg.findViewById(R.id.frg_category_outlets_parent_hub_cb_tab_alphabetical);
        txvAlphabetical = frg.findViewById(R.id.frg_category_outlets_parent_hub_txv_tab_alphabetical);

        rlNearby.setOnClickListener(this);
        rlAlphabetical.setOnClickListener(this);
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
        return (new CategoryHubPagerAdapter(requireActivity(), getChildFragmentManager(), listTitle));
    }

    public void setCurrentPage(int _position) {
        viewPager.setCurrentItem(_position, true);
        switchTabs(_position == 1);
    }

    @SuppressLint("NonConstantResourceId")
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
        }
    }

}