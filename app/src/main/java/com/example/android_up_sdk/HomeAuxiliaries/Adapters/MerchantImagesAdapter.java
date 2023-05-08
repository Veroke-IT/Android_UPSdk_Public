package com.example.android_up_sdk.HomeAuxiliaries.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.android_up_sdk.HomeAuxiliaries.WebServices.MerchantDetail_WebHit_Get_getMerchantDetail;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MerchantImagesAdapter extends PagerAdapter {
    private static LayoutInflater inflater = null;
    Context context;
    DisplayMetrics metrics;
    List<MerchantDetail_WebHit_Get_getMerchantDetail.ResponseModel.DataItem.Outlet_images> listData;

    public MerchantImagesAdapter(Context context, List<MerchantDetail_WebHit_Get_getMerchantDetail.ResponseModel.DataItem.Outlet_images> listData) {
        this.context = context;
        if (context != null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        this.listData = listData;
        metrics = context.getResources().getDisplayMetrics();;

    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = inflater.inflate(R.layout.lay_item_merchant_images, container, false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.imvBanner = itemView.findViewById(R.id.lay_item_merchant_images_imv_banner);

        int imgHeight = (int) (metrics.widthPixels/(1.5));
        viewHolder.imvBanner.setLayoutParams(new RelativeLayout.LayoutParams(metrics.widthPixels, imgHeight)); // for ratio 720 x 480 banner

        Picasso.get().load(AppConfig.getInstance().getBaseUrlImage() + listData.get(position).getFile()).error(R.drawable.icn_placeholder_banner)
                .resize(720,480)
                .placeholder(R.drawable.icn_placeholder_banner).into(viewHolder.imvBanner);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public static class ViewHolder {
        private ImageView imvBanner;
    }
}
