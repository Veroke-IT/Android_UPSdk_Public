package com.example.android_up_sdk.HomeAuxiliaries.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_NearbyOutlets;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.IAdapterCallback;
import com.example.android_up_sdk.Utils.RoundedCornerImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class HomeNearbyOutletsAdapter extends RecyclerView.Adapter<HomeNearbyOutletsAdapter.MyViewHolder> {

     IAdapterCallback iAdapterCallback;
     LayoutInflater inflater;
     ArrayList<DModel_NearbyOutlets> mData;
     float cornerRadius;
     Context mContext;


    public HomeNearbyOutletsAdapter(final IAdapterCallback iAdapterCallback, Context _context, ArrayList<DModel_NearbyOutlets> data) {

        this.iAdapterCallback = iAdapterCallback;
        inflater = LayoutInflater.from(_context);
        this.mData = data;
        this.cornerRadius = AppConfig.getInstance().dpToPx(_context, 12);
        this.mContext = _context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.lay_home_nearby_outlet, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {

        Picasso.get()
                .load(AppConfig.getInstance().getBaseUrlImage() + mData.get(position).getOutlet_image())
                .error(R.drawable.icn_placeholder_banner)
                .placeholder(R.drawable.icn_placeholder_banner)
                .into(viewHolder.imvThumbnail);

        viewHolder.txvFranchiseTitle.setText(mData.get(position).getCategory_name());

        viewHolder.txvTitle.setText(mData.get(position).getName());
        viewHolder.txvOfferCount.setText(mData.get(position).getOffersCounts()+" "+mContext.getResources().getString(R.string.frg_home_offers_nearby_count));



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
        TextView txvFranchiseTitle, txvTitle,txvOfferCount;

        public MyViewHolder(View itemView) {
            super(itemView);

            rlParent = itemView.findViewById(R.id.layhomenearbyoutlet_parent);
            imvThumbnail = itemView.findViewById(R.id.layhomenearbyoutlet_imv_thumbnail);
            imvThumbnail.setRadius(cornerRadius, true, true, true, true);

            txvFranchiseTitle = itemView.findViewById(R.id.layhomenearbyoutlet_txv_franchise);

            txvTitle = itemView.findViewById(R.id.layhomenearbyoutlet_txv_title);
            txvOfferCount = itemView.findViewById(R.id.layhomenearbyoutlet_txv_offer_count);
        }

    }
}

