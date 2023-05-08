package com.example.android_up_sdk.HomeAuxiliaries;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearbyOutletsPagerFragment extends Fragment implements View.OnClickListener {

    RelativeLayout rlBack, rlSearch;
    private ViewPager viewPager;
    TextView txvTitle;
    private String strSortBy, strCategoryName;

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
                switchTabs(position == 1);
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
        strCategoryName = requireContext().getResources().getString(R.string.frg_nearby_outlets);
        strSortBy = AppConfig.getInstance().mCategorySorting.getSortBy();
        listTitle = new ArrayList<>();
        listTitle.add(getString(R.string.btn_by_location));
        listTitle.add(getString(R.string.btn_alphabetically));

    }


    private void bindViews(View frg) {
        rlBack = frg.findViewById(R.id.frg_category_outlets_parent_hub_rl_back);
        rlSearch = frg.findViewById(R.id.frg_category_outlets_parent_hub_rl_btn_search);
        txvTitle = frg.findViewById(R.id.frg_category_outlets_parent_hub_txv_title);
        txvTitle.setText(strCategoryName);

        viewPager = frg.findViewById(R.id.frg_credential_viewpager);


        rlSearch.setVisibility(View.GONE);
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
        //Todo Change Adapter
        return (new NearbyOutletsPagerAdapter(getActivity(), getChildFragmentManager(), listTitle));
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
        }
    }




    public class NearbyOutletsPagerAdapter extends FragmentPagerAdapter {


        List<String> listTitle; // header titles

        Map<Integer, String> mFragmentTags;

        public NearbyOutletsPagerAdapter(Context _context, FragmentManager mgr, List<String> _listTitle) {
            super(mgr);
            this.listTitle = _listTitle;
            this.mFragmentTags = new HashMap<Integer, String>();
        }

        @Override
        public int getCount() {
            return this.listTitle.size();
        }

        @Override
        public Fragment getItem(int position) {

            Fragment frg = null;
            switch (position) {
                case AppConstt.ViewPagerState.CATEGORY_OUTLET:
                    frg = NearbyOutletsFragment.newInstance(position,AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION);
                    break;

                case AppConstt.ViewPagerState.CATEGORY_PARENT:
                    frg = NearbyOutletsFragment.newInstance(position,AppConstt.DEFAULT_VALUES.SORT_BY_ALPHABETICALLY);
                    break;

                default:
                    frg = NearbyOutletsFragment.newInstance(position,AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION);
                    break;
            }
            return frg;

        }

        @Override
        public String getPageTitle(int position) {
            return (this.listTitle.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                Fragment currFrg = (Fragment) obj;
                mFragmentTags.put(position, currFrg.getTag());
            }
            return obj;
        }


    }
}
