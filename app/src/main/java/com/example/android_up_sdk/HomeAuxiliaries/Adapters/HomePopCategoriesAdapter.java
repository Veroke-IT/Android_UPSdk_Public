package com.example.android_up_sdk.HomeAuxiliaries.Adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_PopupCat;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.IAdapterCallback;
import com.example.android_up_sdk.Utils.RoundedCornerImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomePopCategoriesAdapter extends RecyclerView.Adapter<HomePopCategoriesAdapter.MyViewHolder> {

    IAdapterCallback iAdapterCallback;

    LayoutInflater inflater;
    ArrayList<DModel_PopupCat> mData;
    float cornerRadius;
    Context mContext;


    public HomePopCategoriesAdapter(final IAdapterCallback iAdapterCallback, Context _context, ArrayList<DModel_PopupCat> data) {
        this.iAdapterCallback = iAdapterCallback;
        inflater = LayoutInflater.from(_context);
        this.mData = data;
        this.cornerRadius = AppConfig.getInstance().dpToPx(_context, 12);
        this.mContext = _context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.lay_home_sec_pop_cat, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;

        Picasso.get()
                .load(AppConfig.getInstance().getBaseUrlImage() + mData.get(position).getCatImage())
                .error(R.drawable.icn_placeholder_banner)
                .placeholder(R.drawable.icn_placeholder_banner)
                .into(viewHolder.imvThumbnail);

        viewHolder.txvTitle.setText(mData.get(position).getCatTitle());
        viewHolder.rlParent.setLayoutParams(new RelativeLayout.LayoutParams(width/3, width/3));

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

