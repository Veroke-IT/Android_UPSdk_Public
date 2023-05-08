package com.example.android_up_sdk.HomeAuxiliaries.Adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.android_up_sdk.HomeAuxiliaries.OutletFragment;
import com.example.android_up_sdk.HomeAuxiliaries.OutletParentsFragment;
import com.example.android_up_sdk.Utils.AppConstt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class CategoryParentHubPagerAdapter extends FragmentPagerAdapter {


     List<String> listTitle; // header titles
     FragmentManager mFragmentManager;
     Map<Integer, String> mFragmentTags;

    public CategoryParentHubPagerAdapter(Context _context, FragmentManager mgr, List<String> _listTitle) {
        super(mgr);

        this.listTitle = _listTitle;
        this.mFragmentManager = mgr;
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
                frg = OutletParentsFragment.newInstance(position,AppConstt.DEFAULT_VALUES.SORT_BY_ALPHABETICALLY);
                break;

            default:
                frg = OutletFragment.newInstance(position,AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION);
                break;
        }
        return frg;

    }

    @Override
    public String getPageTitle(int position) {
        return (this.listTitle.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            Fragment currFrg = (Fragment) obj;
            mFragmentTags.put(position, currFrg.getTag());
        }
        return obj;
    }

    public Fragment getFragment(int position) {
        String tag = mFragmentTags.get(position);

        return tag == null ? null : mFragmentManager.findFragmentByTag(tag);

    }

}