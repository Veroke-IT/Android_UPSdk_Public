package com.example.android_up_sdk.HomeAuxiliaries.Adapters;


import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.IAdapterCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MerchantMenuImagesAdapter extends RecyclerView.Adapter<MerchantMenuImagesAdapter.MyViewHolder> {

     IAdapterCallback iAdapterCallback;

     LayoutInflater inflater;
     ArrayList<String> mData;
     Context mContext;


    public MerchantMenuImagesAdapter(final IAdapterCallback iAdapterCallback, Context _context, ArrayList<String> data) {
        this.iAdapterCallback = iAdapterCallback;
        inflater = LayoutInflater.from(_context);
        this.mData = data;
        this.mContext = _context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adptr_merchant_menu_images, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        Picasso.get()
                .load(AppConfig.getInstance().getBaseUrlImage() + mData.get(position))
                .error(R.drawable.icn_placeholder_banner)
                .placeholder(R.drawable.icn_placeholder_banner)
                .into(viewHolder.imvThumbnail);

        viewHolder.rlParent.setLayoutParams(new RelativeLayout.LayoutParams(width/3, width/3));

        viewHolder.rlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iAdapterCallback.onAdapterEventFired(IAdapterCallback.EVENT_A, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // class to hold a reference to each item of RecyclerView
    class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlParent;
        ImageView imvThumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);

            rlParent = itemView.findViewById(R.id.layhompopcat_parent);
            imvThumbnail = itemView.findViewById(R.id.layhompopcat_imv_thumbnail);

        }

    }
}

