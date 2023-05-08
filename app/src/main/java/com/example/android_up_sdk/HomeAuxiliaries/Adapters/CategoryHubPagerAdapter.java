package com.example.android_up_sdk.HomeAuxiliaries.Adapters;


import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.android_up_sdk.HomeAuxiliaries.OutletFragment;
import com.example.android_up_sdk.Utils.AppConstt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryHubPagerAdapter extends FragmentPagerAdapter {

    List<String> listTitle; // header titles
    Map<Integer, String> mFragmentTags;

    public CategoryHubPagerAdapter(Context _context, FragmentManager mgr, List<String> _listTitle) {
        super(mgr);
        this.listTitle = _listTitle;
        this.mFragmentTags = new HashMap<>();
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
                frg = OutletFragment.newInstance(position,AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION);
                break;

            case AppConstt.ViewPagerState.CATEGORY_PARENT:
                frg = OutletFragment.newInstance(position, AppConstt.DEFAULT_VALUES.SORT_BY_ALPHABETICALLY);
                break;

            default:
                frg = OutletFragment.newInstance(position, AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION);
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