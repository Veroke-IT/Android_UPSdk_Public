package com.example.android_up_sdk;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_up_sdk.Dialogs.ImagePreviewDialog;
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.MerchantMenuImagesAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.WebServices.MerchantDetail_WebHit_Get_getMerchantDetail;
import com.example.android_up_sdk.Utils.IAdapterCallback;

import java.util.ArrayList;


public class MerchantMenuImagesFragment extends Fragment {
    RecyclerView rcvMenuImages;
    MerchantMenuImagesAdapter merchantMenuImagesAdapter;
    ArrayList<String> merchantMenuImages;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_merchant_menu_images, container, false);
        bindViews(view);
        return view;
    }


    private void bindViews(View frg) {
        rcvMenuImages = frg.findViewById(R.id.frg_merchant_menu_rcv_images);
        merchantMenuImages = new ArrayList<>();
        for (int i = 0; i < MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOutlet_menu().size(); i++) {
            merchantMenuImages.add(MerchantDetail_WebHit_Get_getMerchantDetail.responseObject.getData().get(0).getOutlet_menu().get(i).getFile());
        }
        merchantMenuImagesAdapter = new MerchantMenuImagesAdapter((eventId, position) -> showImagePreviewDialog(merchantMenuImages, position), getActivity(), merchantMenuImages);
        rcvMenuImages.setAdapter(merchantMenuImagesAdapter);
        rcvMenuImages.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
    }

    public void showImagePreviewDialog(ArrayList<String> lstImages, int selectedPosition) {
        ImagePreviewDialog imagePreviewDialog = new ImagePreviewDialog(requireContext(), lstImages, selectedPosition);
        imagePreviewDialog.show();
        imagePreviewDialog.setCancelable(true);

    }
}