package com.example.android_up_sdk.HomeAuxiliaries.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModelOutletOffers;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConstt;

import java.util.ArrayList;

public class OutletOffersAdapter extends BaseAdapter {
     ArrayList<DModelOutletOffers> categoriesList;
     Context mContext;
     String festivalRamadan;
     String festivalBurger;
     String festivalDelivery;
     String festivalBiryani;


    public OutletOffersAdapter(Context context, Fragment _frg, ArrayList<DModelOutletOffers> categoriesList) {
        this.mContext = context;
        this.categoriesList = categoriesList;
        this.festivalRamadan = mContext.getResources().getString(R.string.festival_ramadan);
        this.festivalBurger = mContext.getResources().getString(R.string.festival_burger);
        this.festivalDelivery = mContext.getResources().getString(R.string.festival_food_delivery);
        this.festivalBiryani = mContext.getResources().getString(R.string.festival_biryani);

    }

    @Override
    public int getCount() {
        return categoriesList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.list_item_outlet_offers, null);
            viewHolder.txvOfferName = convertView.findViewById(R.id.lst_item_new_offer_name_offer);
            viewHolder.txvApproxSavings = convertView.findViewById(R.id.lst_item_new_offer_approx_savings);
            viewHolder.imvLocked = convertView.findViewById(R.id.frg_outlet_offers_list_imvLocked);
            viewHolder.imvFestival = convertView.findViewById(R.id.frg_outlet_oofers_imvSpecial);
            viewHolder.txvOfferUsed = convertView.findViewById(R.id.lst_item_new_offer_used);
            viewHolder.rlContainer = convertView.findViewById(R.id.lst_item_new_offer_rl_outline);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }




        if (categoriesList.get(position).isCanRedeem()) {
            viewHolder.txvOfferUsed.setVisibility(View.GONE);
            viewHolder.txvApproxSavings.setVisibility(View.VISIBLE);
            viewHolder.txvOfferName.setTextColor(mContext.getResources().getColor(R.color.text_black));
        }
        else {
            viewHolder.txvApproxSavings.setVisibility(View.GONE);
            viewHolder.txvOfferUsed.setVisibility(View.VISIBLE);
            viewHolder.txvOfferName.setTextColor(mContext.getResources().getColor(R.color.text_gray_unselected_menu));
            if (categoriesList.get(position).getRenew().equalsIgnoreCase(AppConstt.OfferRenewStatus.Renewable)) {
                if(categoriesList.get(position).getRenewDate()!= null &&
                        !categoriesList.get(position).getRenewDate().equalsIgnoreCase("")){
                    viewHolder.txvOfferUsed.setText(mContext.getResources().getString(R.string.offer_redeemed_desc) + " " + categoriesList.get(position).getRenewDate());
                } else {
                    viewHolder.txvOfferUsed.setText(mContext.getResources().getString(R.string.dlg_offer_used));
                }
            } else if (categoriesList.get(position).getRenew().equals(AppConstt.OfferRenewStatus.NonRenewable)) {
                viewHolder.txvOfferUsed.setText(mContext.getResources().getString(R.string.dlg_offer_used));
            }
        }

        viewHolder.txvOfferName.setText(categoriesList.get(position).getOfferName());
        if (categoriesList.get(position).getDiscountType().equalsIgnoreCase(AppConstt.DiscountTypeEnum.standard)){
            viewHolder.txvApproxSavings.setText(mContext.getString(R.string.adptr_expendible_merchintlst_save) + " " +
                    categoriesList.get(position).getApproxSavings());
        }
        else if (categoriesList.get(position).getDiscountType().equalsIgnoreCase(AppConstt.DiscountTypeEnum.percentage)){
            viewHolder.txvApproxSavings.setText(categoriesList.get(position).getApproxSavingPercentage()+""+
                    mContext.getString(R.string.adptr_expendible_merchintlst_percentage)
            );
        }

        if (categoriesList.get(position).getSpecial().equalsIgnoreCase("1")) {
            viewHolder.imvFestival.setVisibility(View.VISIBLE);
            if (categoriesList.get(position).getFestival().length() > 0) {
                if (categoriesList.get(position).getFestival().equalsIgnoreCase(festivalRamadan)) {
                    viewHolder.imvFestival.setBackgroundResource(R.drawable.icn_special_ramadan);
                } else if (categoriesList.get(position).getFestival().equalsIgnoreCase(festivalBiryani)) {
                    viewHolder.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.icn_special_biryani));
                } else if (categoriesList.get(position).getFestival().equalsIgnoreCase(festivalBurger)) {
                    viewHolder.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.icn_special_burger));
                } else if (categoriesList.get(position).getFestival().equalsIgnoreCase(festivalDelivery)) {
                    viewHolder.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.icn_special_delivery));
                } else {
                    viewHolder.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.icn_special_other));
                }
            } else {
                viewHolder.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.icn_special_other));
            }
        } else {
            viewHolder.imvFestival.setVisibility(View.GONE);
        }


        return convertView;
    }

    class ViewHolder {
        ImageView imvLocked, imvFestival;
        TextView txvOfferName, txvApproxSavings, txvOfferUsed;
        RelativeLayout rlContainer;
    }

}