package com.example.android_up_sdk.HomeAuxiliaries.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_OfferUseAgain;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.example.android_up_sdk.Utils.IAdapterCallback;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeUseAgainAdapter extends RecyclerView.Adapter<HomeUseAgainAdapter.MyViewHolder> {

    IAdapterCallback iAdapterCallback;

    LayoutInflater inflater;
    ArrayList<DModel_OfferUseAgain> mData;

    Context mContext;

    public HomeUseAgainAdapter(final IAdapterCallback iAdapterCallback, Context _context, ArrayList<DModel_OfferUseAgain> data) {
        this.iAdapterCallback = iAdapterCallback;
        this.inflater = LayoutInflater.from(_context);
        this.mData = data;
        this.mContext = _context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.lay_home_sec_use_again_offer, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {

        Picasso.get()
                .load(AppConfig.getInstance().getBaseUrlImage() + mData.get(position).getMerchantLogo())
                .error(R.drawable.icn_placeholder_banner)
                .placeholder(R.drawable.icn_placeholder_banner)
                .into(viewHolder.imvThumbnail);

        viewHolder.txvFranchiseTitle.setText(mData.get(position).getMerchantName());

        viewHolder.txvTitle.setText(mData.get(position).getOfferName());
        if (mData.get(position).getDiscountType().equalsIgnoreCase(AppConstt.DiscountTypeEnum.standard)) {
            viewHolder.txvSavings.setText(mContext.getString(R.string.adptr_expendible_merchintlst_save) + " " +
                    mData.get(position).getApproxSaving());
        } else if (mData.get(position).getDiscountType().equalsIgnoreCase(AppConstt.DiscountTypeEnum.percentage)) {
            viewHolder.txvSavings.setText(mData.get(position).getApproxSavingPercentage() + "" +
                    mContext.getString(R.string.adptr_expendible_merchintlst_percentage)
            );
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
        CircularImageView imvThumbnail;
        TextView txvFranchiseTitle, txvTitle, txvSavings;


        public MyViewHolder(View itemView) {
            super(itemView);

            rlParent = itemView.findViewById(R.id.layhomeuseagian_parent);
            imvThumbnail = itemView.findViewById(R.id.layhomeuseagian_imv_thumbnail);

            txvFranchiseTitle = itemView.findViewById(R.id.layhomeuseagian_txv_franchise);

            txvTitle = itemView.findViewById(R.id.layhomeuseagian_txv_title);
            txvSavings = itemView.findViewById(R.id.layhomeuseagian_txv_savings);

        }

    }
}

