package com.example.android_up_sdk.HomeAuxiliaries.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_Collection;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.IAdapterCallback;
import com.example.android_up_sdk.Utils.RoundedCornerImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeDetailCollectionsAdapter extends RecyclerView.Adapter<HomeDetailCollectionsAdapter.MyViewHolder> {

    IAdapterCallback iAdapterCallback;

    LayoutInflater inflater;
    ArrayList<DModel_Collection> mData;
    float cornerRadius;
    Context mContext;


    public HomeDetailCollectionsAdapter(final IAdapterCallback iAdapterCallback, Context _context, ArrayList<DModel_Collection> data) {
        this.iAdapterCallback = iAdapterCallback;
        inflater = LayoutInflater.from(_context);
        this.mData = data;
        this.cornerRadius = AppConfig.getInstance().dpToPx(_context, 12);
        this.mContext = _context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.lay_collection_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {
        RelativeLayout.LayoutParams layoutParamsFirstItem = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsFirstItem.setMarginStart(AppConfig.getInstance().dpToPx(mContext, 10));

        RelativeLayout.LayoutParams layoutParamsLastItem = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsLastItem.setMarginEnd(AppConfig.getInstance().dpToPx(mContext, 10));

        if (position == 0) {
            viewHolder.rlParent.setLayoutParams(layoutParamsFirstItem);
        } else if (position == mData.size() - 1) {
            viewHolder.rlParent.setLayoutParams(layoutParamsLastItem);
        }

        Picasso.get()
                .load(AppConfig.getInstance().getBaseUrlImage() + mData.get(position).getCollectionImage())
                .error(R.drawable.icn_placeholder_banner)
                .placeholder(R.drawable.icn_placeholder_banner)
                .into(viewHolder.imvThumbnail);

        viewHolder.txvTitle.setText(mData.get(position).getCollectionName());

        viewHolder.rlParent.setOnClickListener(v -> iAdapterCallback.onAdapterEventFired(IAdapterCallback.EVENT_A, position));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // class to hold a reference to each item of RecyclerView
    class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlParent;
        RoundedCornerImageView imvThumbnail;
        TextView txvTitle;

        public MyViewHolder(View itemView) {
            super(itemView);

            rlParent = itemView.findViewById(R.id.layhompopcat_parent);
            imvThumbnail = itemView.findViewById(R.id.layhompopcat_imv_thumbnail);
            imvThumbnail.setRadius(cornerRadius);
            txvTitle = itemView.findViewById(R.id.layhompopcat_txv_title);
        }

    }
}
