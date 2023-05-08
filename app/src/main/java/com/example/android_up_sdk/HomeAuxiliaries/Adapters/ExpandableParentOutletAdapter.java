package com.example.android_up_sdk.HomeAuxiliaries.Adapters;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModelParentList;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_CategorySorting;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_Recent_Outlet;
import com.example.android_up_sdk.HomeAuxiliaries.NewBrandsFragment;
import com.example.android_up_sdk.HomeAuxiliaries.OutletParentsFragment;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ExpandableParentOutletAdapter extends BaseExpandableListAdapter {
     Context mContext;
     ArrayList<DModelParentList> lstOutletParents;
     String festivalRamdan;
     String festivalBurger;
     String festivalDelivery;
     String festivalBiryani;

     OutletParentsFragment outletParentsFragment;
     NewBrandsFragment newBrandsFragment;
     boolean isCategoryOutletParentsFragment;
     boolean shouldHideParentFragment;
     Fragment currentFragment;



    public ExpandableParentOutletAdapter(Context context, Fragment _frg, ArrayList<DModelParentList> _lstOutletParents) {
        this.mContext = context;
        this.lstOutletParents = _lstOutletParents;

        this.festivalRamdan = mContext.getResources().getString(R.string.festival_ramadan);
        this.festivalBurger = mContext.getResources().getString(R.string.festival_burger);
        this.festivalDelivery = mContext.getResources().getString(R.string.festival_food_delivery);
        this.festivalBiryani = mContext.getResources().getString(R.string.festival_biryani);

        this.currentFragment = _frg;
        this.shouldHideParentFragment = false;

        if (_frg instanceof OutletParentsFragment) {
            this.outletParentsFragment = (OutletParentsFragment) _frg;
            isCategoryOutletParentsFragment = true;
            this.shouldHideParentFragment = true;
        } else if (_frg instanceof NewBrandsFragment) {
            this.newBrandsFragment = (NewBrandsFragment) _frg;
            isCategoryOutletParentsFragment = false;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.lstOutletParents.get(groupPosition).getChild();//npt array so pos not required
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        ViewHolderChild viewHolderChild = null;
        if (convertView == null) {
            viewHolderChild = new ViewHolderChild();
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.adptr_expendible_merchintlst_child, null);
            viewHolderChild.imvLockOffer = convertView.findViewById(R.id.adptr_expendible_merchintlst_expfoodShowLockOffersIcon);
            viewHolderChild.txvSavings = convertView.findViewById(R.id.adptr_expendible_merchintlst_child_savings);
            viewHolderChild.rlFestival = convertView.findViewById(R.id.adptr_expendible_merchintlst_rl_festivalImage);
            viewHolderChild.imvFestival = convertView.findViewById(R.id.adptr_expendible_merchintlst_festivalImage);
            viewHolderChild.rlBottomShadowChild = convertView.findViewById(R.id.adptr_expendible_merchintlst_child_rl_bottom_space);
            viewHolderChild.rlChildBg = convertView.findViewById(R.id.adptr_expendible_merchintlst_child_rl_bg);
            viewHolderChild.txvName = convertView.findViewById(R.id.adptr_expendible_merchintlst_child_name);
            viewHolderChild.txvMerchantName = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_merchant_name);
            viewHolderChild.txvOfferUsed = convertView.findViewById(R.id.adptr_expendible_merchintlst_child_offer_used);
            viewHolderChild.rlContainerOutline = convertView.findViewById(R.id.mainLayout);
            convertView.setTag(viewHolderChild);
        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }

        if (lstOutletParents.get(groupPosition).getChild().get(childPosition).isCanRedeem()) {
            viewHolderChild.rlContainerOutline.setBackground(mContext.getDrawable(R.drawable.gray_background_border_3));
            viewHolderChild.txvSavings.setVisibility(View.VISIBLE);
            viewHolderChild.txvOfferUsed.setVisibility(View.GONE);
        } else {
            viewHolderChild.rlContainerOutline.setBackground(mContext.getDrawable(R.drawable.gray_background_border_3));
            viewHolderChild.txvSavings.setVisibility(View.GONE);
            viewHolderChild.txvOfferUsed.setVisibility(View.VISIBLE);
            if (lstOutletParents.get(groupPosition).getChild().get(childPosition).getRenew().equalsIgnoreCase(AppConstt.OfferRenewStatus.Renewable)) {
                if (lstOutletParents.get(groupPosition).getChild().get(childPosition).getRenewDate() != null &&
                        !lstOutletParents.get(groupPosition).getChild().get(childPosition).getRenewDate().equalsIgnoreCase("")) {
                    viewHolderChild.txvOfferUsed.setText(mContext.getResources().getString(R.string.offer_redeemed_desc) + " " + lstOutletParents.get(groupPosition).getChild().get(childPosition).getRenewDate());
                } else {
                    viewHolderChild.txvOfferUsed.setText(mContext.getResources().getString(R.string.dlg_offer_used));
                }
            } else if (lstOutletParents.get(groupPosition).getChild().get(childPosition).getRenew().equals(AppConstt.OfferRenewStatus.NonRenewable)) {
                viewHolderChild.txvOfferUsed.setText(mContext.getResources().getString(R.string.dlg_offer_used));
            }
        }

        if (lstOutletParents.get(groupPosition).getChild().get(childPosition).getStrOfferSpecial().equalsIgnoreCase("1")) {
            viewHolderChild.rlFestival.setVisibility(View.VISIBLE);
            if (lstOutletParents.get(groupPosition).getChild().get(childPosition).getStrOfferFestival().length() > 0) {
                if (lstOutletParents.get(groupPosition).getChild().get(childPosition).getStrOfferFestival().equalsIgnoreCase(festivalRamdan)) {
                    viewHolderChild.imvFestival.setBackgroundResource(R.drawable.icn_special_ramadan);
                } else if (lstOutletParents.get(groupPosition).getChild().get(childPosition).getStrOfferFestival().equalsIgnoreCase(festivalBiryani)) {
                    viewHolderChild.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.icn_special_biryani));
                } else if (lstOutletParents.get(groupPosition).getChild().get(childPosition).getStrOfferFestival().equalsIgnoreCase(festivalBurger)) {
                    viewHolderChild.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.icn_special_burger));
                } else if (lstOutletParents.get(groupPosition).getChild().get(childPosition).getStrOfferFestival().equalsIgnoreCase(festivalDelivery)) {
                    viewHolderChild.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.icn_special_delivery));
                } else {
                    viewHolderChild.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.icn_special_other));
                }
            } else {
                viewHolderChild.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.icn_special_other));
            }

        } else {
            viewHolderChild.rlFestival.setVisibility(View.GONE);
        }

        if (isLastChild) {
            viewHolderChild.rlChildBg.setBackground(mContext.getDrawable(R.drawable.bg_expdbl_chld_last));
            viewHolderChild.rlBottomShadowChild.setVisibility(View.VISIBLE);
        } else {
            viewHolderChild.rlChildBg.setBackground(mContext.getDrawable(R.drawable.bg_expdbl_chld_center));
            viewHolderChild.rlBottomShadowChild.setVisibility(View.GONE);
        }


        viewHolderChild.txvName.setText(lstOutletParents.get(groupPosition).getChild().get(childPosition).getStrOfferName());

        if (lstOutletParents.get(groupPosition).getChild().get(childPosition).getDiscountType().equalsIgnoreCase(AppConstt.DiscountTypeEnum.standard)){
            viewHolderChild.txvSavings.setText(mContext.getString(R.string.adptr_expendible_merchintlst_save) + " " +
                    lstOutletParents.get(groupPosition).getChild().get(childPosition).getApproxSavings());
        }
        else if (lstOutletParents.get(groupPosition).getChild().get(childPosition).getDiscountType().equalsIgnoreCase(AppConstt.DiscountTypeEnum.percentage)){
            viewHolderChild.txvSavings.setText(lstOutletParents.get(groupPosition).getChild().get(childPosition).getApproxSavingPercentage()+""+
                    mContext.getString(R.string.adptr_expendible_merchintlst_percentage)
            );
        }


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.lstOutletParents.get(groupPosition).getChild().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.lstOutletParents.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.lstOutletParents.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.adptr_expendible_merchintlst_group, null);

            viewHolder.merchant_name = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_merchant_name);
            viewHolder.merchantAddress = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_merchantAddress);
            viewHolder.merchantDistance = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_merchantDistance);
            viewHolder.llMerchantName = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_expllMerchntName1);
            viewHolder.llMerchantAddress = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_ll_address);
            viewHolder.llIndicatorDown = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_ll_down);
            viewHolder.llIndicatorUp = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_ll_up);
            viewHolder.merchantLogoImage = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_merchantLogoImage);
            viewHolder.rlGFestival = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_rl_groupFestivalImage);
            viewHolder.imvGFestival = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_groupFestivalImage);
            viewHolder.imvFwdArrow = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_group_arrow);
            viewHolder.rlGroupBg = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_rl_bg);
            viewHolder.rlMerchintDetail = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_rlMerchintDetail);
            viewHolder.rlArrowCntnr = convertView.findViewById(R.id.adptr_expendible_rl_arrow_cntnr);
            viewHolder.rlArrowCntnrClickable = convertView.findViewById(R.id.adptr_expendible_rl_arrow_cntnr_clickable1);
            viewHolder.viewExpDivider = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_exp_divider);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!lstOutletParents.get(groupPosition).isOutletExpendible()) {
            viewHolder.viewExpDivider.setVisibility(View.GONE);
            viewHolder.rlArrowCntnr.setVisibility(View.INVISIBLE);
            viewHolder.rlArrowCntnrClickable.setVisibility(View.VISIBLE);
            viewHolder.rlGroupBg.setBackground(mContext.getDrawable(R.drawable.bg_expdbl_grp_collapse));
            viewHolder.llMerchantAddress.setVisibility(View.VISIBLE);
        }
        else if (isExpanded) {
            viewHolder.viewExpDivider.setVisibility(View.VISIBLE);
            viewHolder.llMerchantAddress.setVisibility(View.VISIBLE);
            viewHolder.rlArrowCntnrClickable.setVisibility(View.INVISIBLE);
            viewHolder.rlArrowCntnr.setVisibility(View.VISIBLE);
            viewHolder.llIndicatorDown.setVisibility(View.GONE);
            viewHolder.llIndicatorUp.setVisibility(View.VISIBLE);
            viewHolder.rlGroupBg.setBackground(mContext.getDrawable(R.drawable.bg_expdbl_grp_expended));
        }
        else {
            viewHolder.viewExpDivider.setVisibility(View.GONE);
            viewHolder.llMerchantAddress.setVisibility(View.VISIBLE);
            viewHolder.rlArrowCntnrClickable.setVisibility(View.INVISIBLE);
            viewHolder.rlArrowCntnr.setVisibility(View.VISIBLE);
            viewHolder.llIndicatorDown.setVisibility(View.VISIBLE);
            viewHolder.llIndicatorUp.setVisibility(View.GONE);
            viewHolder.rlGroupBg.setBackground(mContext.getDrawable(R.drawable.bg_expdbl_grp_collapse));
        }

        if (lstOutletParents.get(groupPosition).getStrParentName() != null) {
            String[] strArray = lstOutletParents.get(groupPosition).getStrParentName().split("-");
            SpannableString spannedMerchantName = new SpannableString(lstOutletParents.get(groupPosition).getStrParentName());
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            spannedMerchantName.setSpan(boldSpan, 0, strArray[0].length(), Spanned.SPAN_COMPOSING);
            viewHolder.merchant_name.setText(spannedMerchantName);
        }

        if (!lstOutletParents.get(groupPosition).isOutletExpendible()) {
            viewHolder.merchantAddress.setText(mContext.getString(R.string.multiple_location));
        } else if (lstOutletParents.get(groupPosition).getStrOutletAddress() != null) {
            viewHolder.merchantAddress.setText("" + lstOutletParents.get(groupPosition).getStrOutletAddress());
        }

        if (lstOutletParents.get(groupPosition).isDistanceRequired()) {
            viewHolder.merchantDistance.setVisibility(View.VISIBLE);
            viewHolder.merchantAddress.setVisibility(View.GONE);
            int distance = lstOutletParents.get(groupPosition).getStrDistance();
            if (distance > 1000) {
                float distanceInKm = distance / 1000;
                int newDistance = (int) distanceInKm;
                viewHolder.merchantDistance.setText(
                        mContext.getResources().getString(R.string.adptr_expendible_merchintlst_with_in) + " " + newDistance + " km");
            } else {
                viewHolder.merchantDistance.setText(
                        mContext.getResources().getString(R.string.adptr_expendible_merchintlst_with_in) + " " + distance + " m");
            }
        } else {
            viewHolder.merchantDistance.setText("");
            viewHolder.merchantDistance.setVisibility(View.GONE);
        }

        if (lstOutletParents.get(groupPosition).getStrSpecial().equalsIgnoreCase("1")) {
            viewHolder.rlGFestival.setVisibility(View.GONE);
            if (lstOutletParents.get(groupPosition).getStrFestival().length() > 0) {
                if (lstOutletParents.get(groupPosition).getStrFestival().equalsIgnoreCase(festivalRamdan)) {
                    viewHolder.imvGFestival.setBackgroundResource(R.drawable.icn_special_ramadan);
                } else if (lstOutletParents.get(groupPosition).getStrFestival().equalsIgnoreCase(festivalBiryani)) {
                    viewHolder.imvGFestival.setBackground(mContext.getResources().getDrawable(R.drawable.icn_special_biryani));
                } else if (lstOutletParents.get(groupPosition).getStrFestival().equalsIgnoreCase(festivalBurger)) {
                    viewHolder.imvGFestival.setBackground(mContext.getResources().getDrawable(R.drawable.icn_special_burger));
                } else if (lstOutletParents.get(groupPosition).getStrFestival().equalsIgnoreCase(festivalDelivery)) {
                    viewHolder.imvGFestival.setBackground(mContext.getResources().getDrawable(R.drawable.icn_special_delivery));
                } else {
                    viewHolder.imvGFestival.setBackground(mContext.getResources().getDrawable(R.drawable.icn_special_other));
                }
            } else {
                viewHolder.imvGFestival.setBackground(mContext.getResources().getDrawable(R.drawable.icn_special_other));
            }
        } else {
            viewHolder.rlGFestival.setVisibility(View.GONE);
        }

        if (lstOutletParents.get(groupPosition).getStrOutletLogo() != null) {
            Picasso.get()
                    .load(AppConfig.getInstance().getBaseUrlThumbs() + lstOutletParents.get(groupPosition).getStrOutletLogo())
                    .error(R.drawable.rmv_place_holder)
                    .placeholder(R.drawable.rmv_place_holder)
                    .into(viewHolder.merchantLogoImage);
        } else {
            viewHolder.merchantLogoImage.setImageResource(R.drawable.rmv_place_holder);

        }


        viewHolder.rlMerchintDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if (lstOutletParents.get(groupPosition).isOutletExpendible()) {
                    bundle.putInt(AppConstt.BundleStrings.outletId, Integer.parseInt(lstOutletParents.get(groupPosition).getStrOutletId()));
                    bundle.putString(AppConstt.BundleStrings.merchantImage, lstOutletParents.get(groupPosition).getStrOutletImage());
                    bundle.putString(AppConstt.BundleStrings.merchantLogo, lstOutletParents.get(groupPosition).getStrOutletLogo());
                    bundle.putString(AppConstt.BundleStrings.offerName, lstOutletParents.get(groupPosition).getStrOutletName());
                    bundle.putString(AppConstt.BundleStrings.merchantName, lstOutletParents.get(groupPosition).getStrOutletAddress());
                    bundle.putString(AppConstt.BundleStrings.merchantTimmings, lstOutletParents.get(groupPosition).getStrOutletTimmings());
                    bundle.putString(AppConstt.BundleStrings.merchantDescription, lstOutletParents.get(groupPosition).getStrOutletDescription());
                    bundle.putString(AppConstt.BundleStrings.merchantPhone, lstOutletParents.get(groupPosition).getStrOutletPhone());
                    if (isCategoryOutletParentsFragment) {
                        if (outletParentsFragment != null) {
                            outletParentsFragment.navToMerchantDetailFragment(bundle);
                        }
                    } else {
                        if (newBrandsFragment != null) {
                            newBrandsFragment.navToMerchantDetailFragment(bundle);
                        }
                    }
                    AppConfig.getInstance().updateRecentViewed(new DModel_Recent_Outlet(lstOutletParents.get(groupPosition).getStrOutletId(),lstOutletParents.get(groupPosition).getStrOutletLogo(),
                            lstOutletParents.get(groupPosition).getStrOutletAddress(),lstOutletParents.get(groupPosition).getStrOutletName()));

                } else {

                    AppConfig.getInstance().mCategorySorting = new DModel_CategorySorting(
                            AppConfig.getInstance().mCategorySorting.getGenderSelectedSortType(),
                            lstOutletParents.get(groupPosition).getStrSortBy(),
                            lstOutletParents.get(groupPosition).getIntCategoryId(),
                            lstOutletParents.get(groupPosition).getStrParentName(),
                            lstOutletParents.get(groupPosition).getIntPopulrCategoryId(),
                            lstOutletParents.get(groupPosition).getStrParentId(),
                            "", "",
                            lstOutletParents.get(groupPosition).getInterestId());
                    if (isCategoryOutletParentsFragment) {
                        if (outletParentsFragment != null) {
                            outletParentsFragment.navToCategoryHubFragment(bundle);
                        }
                    } else {
                        if (newBrandsFragment != null) {
                            newBrandsFragment.navToCategoryHubFragment(bundle);
                        }
                    }
                }
            }
        });
        viewHolder.merchantLogoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if (lstOutletParents.get(groupPosition).isOutletExpendible()) {
                    bundle.putInt(AppConstt.BundleStrings.outletId, Integer.parseInt(lstOutletParents.get(groupPosition).getStrOutletId()));
                    bundle.putString(AppConstt.BundleStrings.merchantImage, lstOutletParents.get(groupPosition).getStrOutletImage());
                    bundle.putString(AppConstt.BundleStrings.merchantLogo, lstOutletParents.get(groupPosition).getStrOutletLogo());
                    bundle.putString(AppConstt.BundleStrings.offerName, lstOutletParents.get(groupPosition).getStrOutletName());
                    bundle.putString(AppConstt.BundleStrings.merchantName, lstOutletParents.get(groupPosition).getStrOutletAddress());
                    bundle.putString(AppConstt.BundleStrings.merchantTimmings, lstOutletParents.get(groupPosition).getStrOutletTimmings());
                    bundle.putString(AppConstt.BundleStrings.merchantDescription, lstOutletParents.get(groupPosition).getStrOutletDescription());
                    bundle.putString(AppConstt.BundleStrings.merchantPhone, lstOutletParents.get(groupPosition).getStrOutletPhone());
                    if (isCategoryOutletParentsFragment) {
                        if (outletParentsFragment != null) {
                            outletParentsFragment.navToMerchantDetailFragment(bundle);
                        }
                    } else {
                        if (newBrandsFragment != null) {
                            newBrandsFragment.navToMerchantDetailFragment(bundle);
                        }
                    }
                } else {
                    AppConfig.getInstance().mCategorySorting = new DModel_CategorySorting(
                            AppConfig.getInstance().mCategorySorting.getGenderSelectedSortType(),
                            lstOutletParents.get(groupPosition).getStrSortBy(),
                            lstOutletParents.get(groupPosition).getIntCategoryId(),
                            lstOutletParents.get(groupPosition).getStrParentName(),
                            lstOutletParents.get(groupPosition).getIntPopulrCategoryId(),
                            lstOutletParents.get(groupPosition).getStrParentId(),
                            "", "",
                            lstOutletParents.get(groupPosition).getInterestId());


                    if (isCategoryOutletParentsFragment) {
                        if (outletParentsFragment != null) {
                            outletParentsFragment.navToCategoryHubFragment(bundle);
                        }
                    } else {
                        if (newBrandsFragment != null) {
                            newBrandsFragment.navToCategoryHubFragment(bundle);
                        }
                    }
                }
            }
        });
        viewHolder.llMerchantName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if (lstOutletParents.get(groupPosition).isOutletExpendible()) {
                    bundle.putInt(AppConstt.BundleStrings.outletId, Integer.parseInt(lstOutletParents.get(groupPosition).getStrOutletId()));
                    bundle.putString(AppConstt.BundleStrings.merchantImage, lstOutletParents.get(groupPosition).getStrOutletImage());
                    bundle.putString(AppConstt.BundleStrings.merchantLogo, lstOutletParents.get(groupPosition).getStrOutletLogo());
                    bundle.putString(AppConstt.BundleStrings.offerName, lstOutletParents.get(groupPosition).getStrOutletName());
                    bundle.putString(AppConstt.BundleStrings.merchantName, lstOutletParents.get(groupPosition).getStrOutletAddress());
                    bundle.putString(AppConstt.BundleStrings.merchantTimmings, lstOutletParents.get(groupPosition).getStrOutletTimmings());
                    bundle.putString(AppConstt.BundleStrings.merchantDescription, lstOutletParents.get(groupPosition).getStrOutletDescription());
                    bundle.putString(AppConstt.BundleStrings.merchantPhone, lstOutletParents.get(groupPosition).getStrOutletPhone());
                    if (isCategoryOutletParentsFragment) {
                        if (outletParentsFragment != null) {
                            outletParentsFragment.navToMerchantDetailFragment(bundle);
                        }
                    } else {
                        if (newBrandsFragment != null) {
                            newBrandsFragment.navToMerchantDetailFragment(bundle);
                        }
                    }
                } else {
                    AppConfig.getInstance().mCategorySorting = new DModel_CategorySorting(
                            AppConfig.getInstance().mCategorySorting.getGenderSelectedSortType(),
                            lstOutletParents.get(groupPosition).getStrSortBy(),
                            lstOutletParents.get(groupPosition).getIntCategoryId(),
                            lstOutletParents.get(groupPosition).getStrParentName(),
                            lstOutletParents.get(groupPosition).getIntPopulrCategoryId(),
                            lstOutletParents.get(groupPosition).getStrParentId(),
                            "", "",
                            lstOutletParents.get(groupPosition).getInterestId());
                    if (isCategoryOutletParentsFragment) {
                        if (outletParentsFragment != null) {
                            outletParentsFragment.navToCategoryHubFragment(bundle);
                        }
                    } else {
                        if (newBrandsFragment != null) {
                            newBrandsFragment.navToCategoryHubFragment(bundle);
                        }
                    }
                }
            }
        });
        viewHolder.rlArrowCntnrClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if (lstOutletParents.get(groupPosition).isOutletExpendible()) {
                    bundle.putInt(AppConstt.BundleStrings.outletId, Integer.parseInt(lstOutletParents.get(groupPosition).getStrOutletId()));
                    bundle.putString(AppConstt.BundleStrings.merchantImage, lstOutletParents.get(groupPosition).getStrOutletImage());
                    bundle.putString(AppConstt.BundleStrings.merchantLogo, lstOutletParents.get(groupPosition).getStrOutletLogo());
                    bundle.putString(AppConstt.BundleStrings.offerName, lstOutletParents.get(groupPosition).getStrOutletName());
                    bundle.putString(AppConstt.BundleStrings.merchantName, lstOutletParents.get(groupPosition).getStrOutletAddress());
                    bundle.putString(AppConstt.BundleStrings.merchantTimmings, lstOutletParents.get(groupPosition).getStrOutletTimmings());
                    bundle.putString(AppConstt.BundleStrings.merchantDescription, lstOutletParents.get(groupPosition).getStrOutletDescription());
                    bundle.putString(AppConstt.BundleStrings.merchantPhone, lstOutletParents.get(groupPosition).getStrOutletPhone());
                    if (isCategoryOutletParentsFragment) {
                        if (outletParentsFragment != null) {
                            outletParentsFragment.navToMerchantDetailFragment(bundle);
                        }
                    } else {
                        if (newBrandsFragment != null) {
                            newBrandsFragment.navToMerchantDetailFragment(bundle);
                        }
                    }
                } else {
                    AppConfig.getInstance().mCategorySorting = new DModel_CategorySorting(
                            AppConfig.getInstance().mCategorySorting.getGenderSelectedSortType(),
                            lstOutletParents.get(groupPosition).getStrSortBy(),
                            lstOutletParents.get(groupPosition).getIntCategoryId(),
                            lstOutletParents.get(groupPosition).getStrParentName(),
                            lstOutletParents.get(groupPosition).getIntPopulrCategoryId(),
                            lstOutletParents.get(groupPosition).getStrParentId(),
                            "", "",
                            lstOutletParents.get(groupPosition).getInterestId());
                    if (isCategoryOutletParentsFragment) {
                        if (outletParentsFragment != null) {
                            outletParentsFragment.navToCategoryHubFragment(bundle);
                        }
                    } else {
                        if (newBrandsFragment != null) {
                            newBrandsFragment.navToCategoryHubFragment(bundle);
                        }
                    }
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolder {
        CircularImageView merchantLogoImage;
        ImageView imvGFestival, imvFwdArrow;
        TextView merchant_name, merchantAddress, merchantDistance;
        LinearLayout llIndicatorDown, llIndicatorUp, llMerchantName, llMerchantAddress;
        RelativeLayout rlGFestival, rlMerchintDetail, rlGroupBg, rlArrowCntnr, rlArrowCntnrClickable;
        View viewExpDivider;
    }

    class ViewHolderChild {
        TextView txvName, txvMerchantName, txvSavings, txvOfferUsed;
        ImageView imvLockOffer, imvFestival;
        RelativeLayout rlFestival, rlBottomShadowChild, rlChildBg, rlContainerOutline;

    }


}
