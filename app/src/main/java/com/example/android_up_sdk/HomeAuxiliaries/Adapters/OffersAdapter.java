package com.example.android_up_sdk.HomeAuxiliaries.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;

import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModelOutletOffers;

import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OffersAdapter extends BaseAdapter {
    Context mContext;
    List<DModelOutletOffers> lstOffers;
    LayoutInflater inflater;





    public OffersAdapter(Context _mContext, Fragment _frg, List<DModelOutletOffers> _lstFavorites) {
        this.mContext = _mContext;
        this.lstOffers = _lstFavorites;


        inflater = LayoutInflater.from(_mContext);
    }

    @Override
    public int getCount() {
        return lstOffers.size();
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
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adptr_offers_lay, null);
            viewHolder = new ViewHolder();
            viewHolder.imvCategoryLogo = convertView.findViewById(R.id.adptr_fav_imv_category_logo);




            viewHolder.txvName = convertView.findViewById(R.id.adptr_fav_txv_offr_name);
            viewHolder.txvExpiryDate = convertView.findViewById(R.id.adptr_offer_txv_date);
            viewHolder.txvConfirmationNo = convertView.findViewById(R.id.adptr_offer_txv_confirmation_no);
            viewHolder.txvMerchantName = convertView.findViewById(R.id.adptr_fav_txv_merchant_address);
            viewHolder.txvSavings = convertView.findViewById(R.id.adptr_fav_txv_approx_savings);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }



        if (lstOffers.get(position).getMerchantLogo() !=null) {
            Picasso.get()
                    .load(AppConfig.getInstance().getBaseUrlImage() + lstOffers.get(position).getMerchantLogo())
                    .error(R.drawable.placeholder_icon)
                    .placeholder(R.drawable.placeholder_icon)
                    .into(viewHolder.imvCategoryLogo);
        }
        viewHolder.txvName.setText(lstOffers.get(position).getOfferName());
        viewHolder.txvMerchantName.setText(lstOffers.get(position).getMerchantName());
        viewHolder.txvConfirmationNo.setText(mContext.getResources().getString(R.string.confirmation_id)+ " "+lstOffers.get(position).getId());

        SimpleDateFormat format = new SimpleDateFormat();
        format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        SimpleDateFormat format2 = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

        try {
            Date expiryDate = format.parse(lstOffers.get(position).getExpiryDate());
            String strExpiryDate = format2.format(expiryDate);
            viewHolder.txvExpiryDate.setText(strExpiryDate);

        }
        catch (ParseException e) {
            viewHolder.txvExpiryDate.setText(lstOffers.get(position).getExpiryDate());
            e.printStackTrace();
        }

        if (lstOffers.get(position).getDiscountType().equalsIgnoreCase(AppConstt.DiscountTypeEnum.standard)){
            viewHolder.txvSavings.setText(mContext.getString(R.string.dlg_offer_detail_save_approx_text) + " " +
                    lstOffers.get(position).getApproxSavings());
        }
        else if (lstOffers.get(position).getDiscountType().equalsIgnoreCase(AppConstt.DiscountTypeEnum.percentage)){
            viewHolder.txvSavings.setText(
                    mContext.getString(R.string.dlg_offer_detail_save_approx_text)
                            +" "+
                    lstOffers.get(position).getApproxSavingPercentage()
            );
        }



        return convertView;
    }

    public static class ViewHolder {

        TextView txvName, txvMerchantName, txvSavings,txvExpiryDate,txvConfirmationNo;
        CircularImageView imvCategoryLogo;
    }
}

