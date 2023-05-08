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

import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_Recent_Outlet;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.IAdapterCallback;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeRecentViewedAdapter extends RecyclerView.Adapter<HomeRecentViewedAdapter.MyViewHolder> {

    IAdapterCallback iAdapterCallback;

    LayoutInflater inflater;
    ArrayList<DModel_Recent_Outlet> mData;
    float cornerRadius;
    Context mContext;

    public HomeRecentViewedAdapter(final IAdapterCallback iAdapterCallback, Context _context, ArrayList<DModel_Recent_Outlet> data) {
        this.iAdapterCallback = iAdapterCallback;
        this.inflater = LayoutInflater.from(_context);
        this.mData = data;
        this.mContext = _context;
        this.cornerRadius = AppConfig.getInstance().dpToPx(_context, 5);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.lay_home_sec_recent, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {


        Picasso.get()
                .load(AppConfig.getInstance().getBaseUrlImage() + mData.get(position).getMerchantsLogoUrl())
                .error(R.drawable.placeholder_icon)
                .placeholder(R.drawable.placeholder_icon)
                .into(viewHolder.imvFranchiseIcon);

        if (mData.get(position).getOfferName()!=null && !mData.get(position).getOfferName().isEmpty()){
            viewHolder.txvFranchiseTitle.setText(mData.get(position).getOfferName());
        }
        else if (mData.get(position).getMerchantName()!=null && !mData.get(position).getMerchantName().isEmpty()){
            viewHolder.txvFranchiseTitle.setText(mData.get(position).getMerchantName());
        }
        else {
            viewHolder.txvFranchiseTitle.setText(mData.get(position).getOfferName()+"\n"+mData.get(position).getMerchantName());
        }


        viewHolder.rlParent.setOnClickListener(v -> iAdapterCallback.onAdapterEventFired(IAdapterCallback.EVENT_A, position));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    // class to hold a reference to each item of RecyclerView
    class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlParent;
        CircularImageView imvFranchiseIcon;
        TextView txvFranchiseTitle;

        public MyViewHolder(View itemView) {
            super(itemView);

            rlParent = itemView.findViewById(R.id.layhomerecentsection_parent);

            imvFranchiseIcon = itemView.findViewById(R.id.layhomerecentsection_imv_thumbnail);
            txvFranchiseTitle = itemView.findViewById(R.id.layhomerecentsection_txv_franchise);

        }

    }
}


