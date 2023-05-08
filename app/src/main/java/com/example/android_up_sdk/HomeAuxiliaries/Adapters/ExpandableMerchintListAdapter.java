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

import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModelMerchintList;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_Recent_Outlet;
import com.example.android_up_sdk.HomeAuxiliaries.NearbyOutletsFragment;
import com.example.android_up_sdk.HomeAuxiliaries.NewBrandsOutletFragment;
import com.example.android_up_sdk.HomeAuxiliaries.OutletFragment;
import com.example.android_up_sdk.HomeAuxiliaries.SearchOffersFragment;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ExpandableMerchintListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<DModelMerchintList> lstOutletOffers;
    private String festivalRamdan;
    private String festivalBurger;
    private String festivalDelivery;
    private String festivalBiryani;
    private boolean isSubscribed, isOutletFragmnt;
    private OutletFragment outletFragment;
    private SearchOffersFragment searchOffersFragment;
    private NewBrandsOutletFragment newBrandsOutletFragment;
    private NearbyOutletsFragment nearbyOutletsFragment;

    private Fragment currentFragment;
    private boolean shouldHideParentFragment;

    public ExpandableMerchintListAdapter(Context context, Fragment _frg, ArrayList<DModelMerchintList> _lstOutletOffers) {
        this.mContext = context;
        this.lstOutletOffers = _lstOutletOffers;
        this.festivalRamdan = mContext.getResources().getString(R.string.festival_ramadan);
        this.festivalBurger = mContext.getResources().getString(R.string.festival_burger);
        this.festivalDelivery = mContext.getResources().getString(R.string.festival_food_delivery);
        this.festivalBiryani = mContext.getResources().getString(R.string.festival_biryani);

        this.currentFragment = _frg;
        this.shouldHideParentFragment = false;

        if (_frg instanceof OutletFragment) {
            this.outletFragment = (OutletFragment) _frg;
            this.shouldHideParentFragment = true;
            isOutletFragmnt = true;
        }
        else if (_frg instanceof NewBrandsOutletFragment) {
            this.newBrandsOutletFragment = (NewBrandsOutletFragment) _frg;
            isOutletFragmnt = false;
        }
        else if (_frg instanceof NearbyOutletsFragment) {
            this.nearbyOutletsFragment = (NearbyOutletsFragment) _frg;
            isOutletFragmnt = false;
        }
        else if (_frg instanceof SearchOffersFragment) {
            this.searchOffersFragment = (SearchOffersFragment) _frg;
            isOutletFragmnt = false;
        } else {
            isOutletFragmnt = false;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.lstOutletOffers.get(groupPosition).getChild();
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
            viewHolderChild.rlBottomShadowChild = convertView.findViewById(R.id.adptr_expendible_merchintlst_child_rl_bottom_space);
            viewHolderChild.rlChildBg = convertView.findViewById(R.id.adptr_expendible_merchintlst_child_rl_bg);
            viewHolderChild.rlFestival = convertView.findViewById(R.id.adptr_expendible_merchintlst_rl_festivalImage);
            viewHolderChild.imvFestival = convertView.findViewById(R.id.adptr_expendible_merchintlst_festivalImage);
            viewHolderChild.txvName = convertView.findViewById(R.id.adptr_expendible_merchintlst_child_name);
            viewHolderChild.txvMerchantName = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_merchant_name);
            viewHolderChild.txvSavings = convertView.findViewById(R.id.adptr_expendible_merchintlst_child_savings);
            viewHolderChild.txvOfferUsed = convertView.findViewById(R.id.adptr_expendible_merchintlst_child_offer_used);
            viewHolderChild.rlContainerOutline = convertView.findViewById(R.id.mainLayout);
            convertView.setTag(viewHolderChild);
        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }


        if (isSubscribed) {
            viewHolderChild.imvLockOffer.setVisibility(View.INVISIBLE);

        }
        else {
            viewHolderChild.imvLockOffer.setVisibility(View.VISIBLE);

        }
        if (lstOutletOffers.get(groupPosition).getChild().get(childPosition).isCanRedeem()) {
            viewHolderChild.rlContainerOutline.setBackground(mContext.getDrawable(R.drawable.gray_background_border_3));
            viewHolderChild.txvSavings.setVisibility(View.VISIBLE);
            viewHolderChild.txvOfferUsed.setVisibility(View.GONE);
        } else {
            viewHolderChild.rlContainerOutline.setBackground(mContext.getDrawable(R.drawable.gray_background_border_3));
            viewHolderChild.txvSavings.setVisibility(View.GONE);
            viewHolderChild.txvOfferUsed.setVisibility(View.VISIBLE);
            if (lstOutletOffers.get(groupPosition).getChild().get(childPosition).getRenew().equalsIgnoreCase(AppConstt.OfferRenewStatus.Renewable)) {
                if (lstOutletOffers.get(groupPosition).getChild().get(childPosition).getRenewDate() != null &&
                        !lstOutletOffers.get(groupPosition).getChild().get(childPosition).getRenewDate().equalsIgnoreCase("")) {
                    viewHolderChild.txvOfferUsed.setText(mContext.getResources().getString(R.string.offer_redeemed_desc) + " " + lstOutletOffers.get(groupPosition).getChild().get(childPosition).getRenewDate());
                } else {
                    viewHolderChild.txvOfferUsed.setText(mContext.getResources().getString(R.string.dlg_offer_used));
                }
            } else if (lstOutletOffers.get(groupPosition).getChild().get(childPosition).getRenew().equals(AppConstt.OfferRenewStatus.NonRenewable)) {
                viewHolderChild.txvOfferUsed.setText(mContext.getResources().getString(R.string.dlg_offer_used));
            }
        }


        if (lstOutletOffers.get(groupPosition).getChild().get(childPosition).getSpecial().equalsIgnoreCase("1")) {
            viewHolderChild.rlFestival.setVisibility(View.VISIBLE);
            if (lstOutletOffers.get(groupPosition).getChild().get(childPosition).getFestival().length() > 0) {
                if (lstOutletOffers.get(groupPosition).getChild().get(childPosition).getFestival().equalsIgnoreCase(festivalRamdan)) {
                    viewHolderChild.imvFestival.setBackgroundResource(R.drawable.icn_special_ramadan);
                } else if (lstOutletOffers.get(groupPosition).getChild().get(childPosition).getFestival().equalsIgnoreCase(festivalBiryani)) {
                    viewHolderChild.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.icn_special_biryani));
                } else if (lstOutletOffers.get(groupPosition).getChild().get(childPosition).getFestival().equalsIgnoreCase(festivalBurger)) {
                    viewHolderChild.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.icn_special_burger));
                } else if (lstOutletOffers.get(groupPosition).getChild().get(childPosition).getFestival().equalsIgnoreCase(festivalDelivery)) {
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

        viewHolderChild.txvName.setText(lstOutletOffers.get(groupPosition).getChild().get(childPosition).getName());

        if (lstOutletOffers.get(groupPosition).getChild().get(childPosition).getDiscountType().equalsIgnoreCase(AppConstt.DiscountTypeEnum.standard)){
            viewHolderChild.txvSavings.setText(mContext.getString(R.string.adptr_expendible_merchintlst_save) + " " +
                    lstOutletOffers.get(groupPosition).getChild().get(childPosition).getApproxSavings());
        }
        else if (lstOutletOffers.get(groupPosition).getChild().get(childPosition).getDiscountType().equalsIgnoreCase(AppConstt.DiscountTypeEnum.percentage)){
            viewHolderChild.txvSavings.setText(lstOutletOffers.get(groupPosition).getChild().get(childPosition).getApproxSavingPercentage()+""+
                    mContext.getString(R.string.adptr_expendible_merchintlst_percentage)
            );
        }


        if (isLastChild) {
            viewHolderChild.rlChildBg.setBackground(mContext.getDrawable(R.drawable.bg_expdbl_chld_last));
            viewHolderChild.rlBottomShadowChild.setVisibility(View.VISIBLE);
        } else {
            viewHolderChild.rlChildBg.setBackground(mContext.getDrawable(R.drawable.bg_expdbl_chld_center));
            viewHolderChild.rlBottomShadowChild.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.lstOutletOffers.get(groupPosition).getChild().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.lstOutletOffers.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.lstOutletOffers.size();
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
            viewHolder.llMerchantName = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_expllMerchntName);
            viewHolder.llIndicatorDown = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_ll_down);
            viewHolder.llIndicatorUp = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_ll_up);
            viewHolder.llMerchantAddress = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_ll_address);
            viewHolder.merchantLogoImage = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_merchantLogoImage);
            viewHolder.rlGFestival = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_rl_groupFestivalImage);
            viewHolder.imvGFestival = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_groupFestivalImage);
            viewHolder.rlGroupBg = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_rl_bg);
            viewHolder.imvFwdArrow = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_group_arrow);
            viewHolder.rlMerchintDetail = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_rlMerchintDetail);
            viewHolder.viewExpDivider = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_exp_divider);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (isExpanded) {
            viewHolder.viewExpDivider.setVisibility(View.VISIBLE);
            viewHolder.llIndicatorDown.setVisibility(View.GONE);
            viewHolder.llIndicatorUp.setVisibility(View.VISIBLE);
            viewHolder.rlGroupBg.setBackground(mContext.getDrawable(R.drawable.bg_expdbl_grp_expended));
        } else {
            viewHolder.viewExpDivider.setVisibility(View.GONE);
            viewHolder.llIndicatorDown.setVisibility(View.VISIBLE);
            viewHolder.llIndicatorUp.setVisibility(View.GONE);
            viewHolder.rlGroupBg.setBackground(mContext.getDrawable(R.drawable.bg_expdbl_grp_collapse));
        }

        if (lstOutletOffers.get(groupPosition).getMerchantName() != null) {
            String[] strArray = lstOutletOffers.get(groupPosition).getMerchantName().split("-");
            SpannableString spannedMerchantName = new SpannableString(lstOutletOffers.get(groupPosition).getMerchantName());
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            spannedMerchantName.setSpan(boldSpan, 0, strArray[0].length(), Spanned.SPAN_COMPOSING);
//            viewHolder.merchant_name.setText(spannedMerchantName);
            viewHolder.merchant_name.setText(lstOutletOffers.get(groupPosition).getMerchantName());
        }
        if (lstOutletOffers.get(groupPosition).getChild().size() == 0) {
            viewHolder.llMerchantAddress.setVisibility(View.GONE);
        } else {
            if (lstOutletOffers.get(groupPosition).getMerchantAddress() != null) {
                viewHolder.llMerchantAddress.setVisibility(View.VISIBLE);
                viewHolder.merchantAddress.setText("" + lstOutletOffers.get(groupPosition).getMerchantAddress());
            } else {
                viewHolder.llMerchantAddress.setVisibility(View.GONE);
            }
        }

        if (lstOutletOffers.get(groupPosition).isDistanceRequired()) {
            viewHolder.merchantDistance.setVisibility(View.VISIBLE);
            viewHolder.merchantAddress.setVisibility(View.GONE);
            int distance = lstOutletOffers.get(groupPosition).getMerchantDistance();
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

        if (lstOutletOffers.get(groupPosition).getStrSpecial().equalsIgnoreCase("1")) {
            viewHolder.rlGFestival.setVisibility(View.GONE);
            if (lstOutletOffers.get(groupPosition).getFestival().length() > 0) {
                if (lstOutletOffers.get(groupPosition).getFestival().equalsIgnoreCase(festivalRamdan)) {
                    viewHolder.imvGFestival.setBackgroundResource(R.drawable.icn_special_ramadan);
                } else if (lstOutletOffers.get(groupPosition).getFestival().equalsIgnoreCase(festivalBiryani)) {
                    viewHolder.imvGFestival.setBackground(mContext.getResources().getDrawable(R.drawable.icn_special_biryani));
                } else if (lstOutletOffers.get(groupPosition).getFestival().equalsIgnoreCase(festivalBurger)) {
                    viewHolder.imvGFestival.setBackground(mContext.getResources().getDrawable(R.drawable.icn_special_burger));
                } else if (lstOutletOffers.get(groupPosition).getFestival().equalsIgnoreCase(festivalDelivery)) {
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


        if (lstOutletOffers.get(groupPosition).getMerchantsLogoUrl() != null) {
            Picasso.get()
                    .load(AppConfig.getInstance().getBaseUrlThumbs() + lstOutletOffers.get(groupPosition).getMerchantsLogoUrl())
                    .error(R.drawable.rmv_place_holder)
                    .placeholder(R.drawable.rmv_place_holder)
                    .into(viewHolder.merchantLogoImage);
        } else {
            viewHolder.merchantLogoImage.setImageResource(R.drawable.rmv_place_holder);

        }

        viewHolder.rlMerchintDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String outletId = lstOutletOffers.get(groupPosition).getId();
                String merchantImage = lstOutletOffers.get(groupPosition).getMerchantsImageUrl();
                String merchantLogo = lstOutletOffers.get(groupPosition).getMerchantsLogoUrl();
                String offerName = lstOutletOffers.get(groupPosition).getMerchantName();
                String merchantName = lstOutletOffers.get(groupPosition).getMerchantAddress();
                String merchantTimmings = lstOutletOffers.get(groupPosition).getMerchantTimmings();
                String merchantDescription = lstOutletOffers.get(groupPosition).getMerchantDescription();
                String merchantPhone = lstOutletOffers.get(groupPosition).getMerchantsPhone();

                Bundle b = new Bundle();
                b.putInt(AppConstt.BundleStrings.outletId, Integer.parseInt(outletId));
                b.putString(AppConstt.BundleStrings.merchantImage, merchantImage);
                b.putString(AppConstt.BundleStrings.merchantLogo, merchantLogo);
                b.putString(AppConstt.BundleStrings.offerName, offerName);
                b.putString(AppConstt.BundleStrings.merchantName, merchantName);
                b.putString(AppConstt.BundleStrings.merchantTimmings, merchantTimmings);
                b.putString(AppConstt.BundleStrings.merchantDescription, merchantDescription);
                b.putString(AppConstt.BundleStrings.merchantPhone, merchantPhone);

                if (isOutletFragmnt) {
                    outletFragment.navToMerchantDetailFragment(b);
                } else {
                    if (searchOffersFragment != null)
                        searchOffersFragment.navToMerchantDetailFragment(b);
                    else if (newBrandsOutletFragment != null)
                        newBrandsOutletFragment.navToMerchantDetailFragment(b);
                    else if (nearbyOutletsFragment != null)
                        nearbyOutletsFragment.navToMerchantDetailFragment(b);
                }
                AppConfig.getInstance().updateRecentViewed(new DModel_Recent_Outlet(lstOutletOffers.get(groupPosition).getId(),lstOutletOffers.get(groupPosition).getMerchantsLogoUrl(),
                        lstOutletOffers.get(groupPosition).getMerchantAddress(),lstOutletOffers.get(groupPosition).getMerchantName()));

            }
        });
        viewHolder.merchantLogoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String outletId = lstOutletOffers.get(groupPosition).getId();
                String merchantImage = lstOutletOffers.get(groupPosition).getMerchantsImageUrl();
                String merchantLogo = lstOutletOffers.get(groupPosition).getMerchantsLogoUrl();
                String offerName = lstOutletOffers.get(groupPosition).getMerchantName();
                String merchantName = lstOutletOffers.get(groupPosition).getMerchantAddress();
                String merchantTimmings = lstOutletOffers.get(groupPosition).getMerchantTimmings();
                String merchantDescription = lstOutletOffers.get(groupPosition).getMerchantDescription();
                String merchantPhone = lstOutletOffers.get(groupPosition).getMerchantsPhone();

                Bundle b = new Bundle();
                b.putInt(AppConstt.BundleStrings.outletId, Integer.parseInt(outletId));
                b.putString(AppConstt.BundleStrings.merchantImage, merchantImage);
                b.putString(AppConstt.BundleStrings.merchantLogo, merchantLogo);
                b.putString(AppConstt.BundleStrings.offerName, offerName);
                b.putString(AppConstt.BundleStrings.merchantName, merchantName);
                b.putString(AppConstt.BundleStrings.merchantTimmings, merchantTimmings);
                b.putString(AppConstt.BundleStrings.merchantDescription, merchantDescription);
                b.putString(AppConstt.BundleStrings.merchantPhone, merchantPhone);

                if (isOutletFragmnt) {
                    outletFragment.navToMerchantDetailFragment(b);
                } else {
                    if (searchOffersFragment != null)
                        searchOffersFragment.navToMerchantDetailFragment(b);
                    else if (newBrandsOutletFragment != null)
                        newBrandsOutletFragment.navToMerchantDetailFragment(b);
                    else if (nearbyOutletsFragment != null)
                        nearbyOutletsFragment.navToMerchantDetailFragment(b);
                }
            }
        });
        viewHolder.llMerchantName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String outletId = lstOutletOffers.get(groupPosition).getId();
                String merchantImage = lstOutletOffers.get(groupPosition).getMerchantsImageUrl();
                String merchantLogo = lstOutletOffers.get(groupPosition).getMerchantsLogoUrl();
                String offerName = lstOutletOffers.get(groupPosition).getMerchantName();
                String merchantName = lstOutletOffers.get(groupPosition).getMerchantAddress();
                String merchantTimmings = lstOutletOffers.get(groupPosition).getMerchantTimmings();
                String merchantDescription = lstOutletOffers.get(groupPosition).getMerchantDescription();
                String merchantPhone = lstOutletOffers.get(groupPosition).getMerchantsPhone();

                Bundle b = new Bundle();
                b.putInt(AppConstt.BundleStrings.outletId, Integer.parseInt(outletId));
                b.putString(AppConstt.BundleStrings.merchantImage, merchantImage);
                b.putString(AppConstt.BundleStrings.merchantLogo, merchantLogo);
                b.putString(AppConstt.BundleStrings.offerName, offerName);
                b.putString(AppConstt.BundleStrings.merchantName, merchantName);
                b.putString(AppConstt.BundleStrings.merchantTimmings, merchantTimmings);
                b.putString(AppConstt.BundleStrings.merchantDescription, merchantDescription);
                b.putString(AppConstt.BundleStrings.merchantPhone, merchantPhone);

                if (isOutletFragmnt) {
                    outletFragment.navToMerchantDetailFragment(b);
                } else {
                    if (searchOffersFragment != null)
                        searchOffersFragment.navToMerchantDetailFragment(b);
                    else if (newBrandsOutletFragment != null)
                        newBrandsOutletFragment.navToMerchantDetailFragment(b);
                    else if (nearbyOutletsFragment != null)
                        nearbyOutletsFragment.navToMerchantDetailFragment(b);
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
        ImageView imvGFestival, imvFwdArrow, imvBottomShadow;
        TextView merchant_name, merchantAddress, merchantDistance;
        LinearLayout llIndicatorDown, llIndicatorUp, llMerchantAddress, llMerchantName;
        RelativeLayout rlGFestival, rlMerchintDetail, rlGroupBg;
        View viewExpDivider;
    }

    class ViewHolderChild {
        TextView txvName, txvMerchantName, txvSavings, txvOfferUsed;
        ImageView imvLockOffer, imvFestival;
        RelativeLayout rlFestival, rlBottomShadowChild, rlChildBg, rlContainerOutline;
    }


}

