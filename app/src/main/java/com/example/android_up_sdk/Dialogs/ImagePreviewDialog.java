package com.example.android_up_sdk.Dialogs;


import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.TouchImageView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImagePreviewDialog extends BottomSheetDialog implements BottomSheetDialog.OnShowListener {
    ViewPager viewPager;
    ArrayList<String> lstImages;
    RelativeLayout rlCross;
    ImageView imvCross, imvNext, imvPrevious;
    TextView txvCount;
    LinearLayout llCountContainer;
    int selectedPosition;

    public ImagePreviewDialog(@NonNull Context context, ArrayList<String> lstImages, int currentPos) {
        super(context, R.style.SubMenuBottomSheetDialogTheme);
        this.lstImages = lstImages;
        this.selectedPosition = currentPos;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dlg_image_preview);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


        initialize();
        populateData();
        this.onShow(this);


    }



    private void initialize() {
        viewPager =  findViewById(R.id.frg_full_screen_view_pagr);
        rlCross = findViewById(R.id.app_bar_rl_back);
        imvCross = findViewById(R.id.app_bar_imv_back);
        imvPrevious = findViewById(R.id.dlg_image_view_imv_previous);
        imvNext = findViewById(R.id.dlg_image_view_imv_next);
        txvCount = findViewById(R.id.dlg_image_view_txv_count);
        llCountContainer = findViewById(R.id.dlg_image_view_ll_counter);


    }


    private void populateData() {
        TouchImageAdapter offrDetailViewPagerAdapter = new TouchImageAdapter(lstImages);
        viewPager.setAdapter(offrDetailViewPagerAdapter);
        viewPager.setCurrentItem(selectedPosition);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedPosition = position;
                txvCount.setText(selectedPosition+1 + "/" + lstImages.size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        txvCount.setText(selectedPosition+1 + "/" + lstImages.size());
        imvNext.setOnClickListener(v -> {
            if (selectedPosition+1 <lstImages.size()){
                selectedPosition++;
                viewPager.setCurrentItem(selectedPosition);
            }
        });
        imvPrevious.setOnClickListener(v -> {
            if (selectedPosition >0){
                selectedPosition--;
                viewPager.setCurrentItem(selectedPosition);
            }
        });
        rlCross.setOnClickListener(v -> dismiss());


    }

    static class TouchImageAdapter extends PagerAdapter {

        ArrayList<String> lstAdThumbnails;

        TouchImageAdapter(ArrayList<String> _lstImages) {
            this.lstAdThumbnails = _lstImages;
        }

        @Override
        public int getCount() {
            return lstAdThumbnails.size();
        }

        @NonNull
        @Override
        public View instantiateItem(ViewGroup container, int position) {
            TouchImageView img = new TouchImageView(container.getContext());

            Picasso.get()
                    .load(AppConfig.getInstance().getBaseUrlImage() +  lstAdThumbnails.get(position))
                    .placeholder(R.drawable.icn_placeholder_banner)
                    .into(img);

            container.addView(img, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
            return img;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

    }

    @Override
    public void onShow(DialogInterface dialog) {
        BottomSheetDialog sheetDialog = (BottomSheetDialog) dialog;
        FrameLayout bottomSheet =  sheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

        // Right here!
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet)
                    .setState(BottomSheetBehavior.STATE_EXPANDED);
            BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(true);
        }



    }
}

