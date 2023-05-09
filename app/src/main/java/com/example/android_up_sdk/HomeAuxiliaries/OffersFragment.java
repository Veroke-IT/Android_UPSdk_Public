package com.example.android_up_sdk.HomeAuxiliaries;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android_up_sdk.Dialogs.OfferDetailAlertDialog;
import com.example.android_up_sdk.HomeAuxiliaries.Adapters.OffersAdapter;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModelOutletOffers;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_OfferDetail;
import com.example.android_up_sdk.HomeAuxiliaries.WebServices.NewOffers_WebHit_Get_getOffers;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.example.android_up_sdk.Utils.IWebCallback;
import com.example.android_up_sdk.Utils.ProgressDialogue;

import java.util.ArrayList;


public class OffersFragment extends Fragment implements View.OnClickListener {

    RelativeLayout rlBack;
    private ListView lsvNewOffers;
    private ArrayList<DModelOutletOffers> lstNewOffers;
    private OffersAdapter offersAdapter;
    private TextView txvNotFound;
    private View lsvFooterView;
    private DModel_OfferDetail dModel_offerDetail;
    private int page;

    private boolean shouldGetMoreOffers, isAlreadyFetchingOffers;

    ProgressDialogue progressDialogue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_offers, container, false);

        initialize();
        bindViews(view);
        lsvNewOffers.setOnItemClickListener((parent, view1, position, id) -> {

            int offerId = Integer.parseInt(lstNewOffers.get(position).getId());
            String saveApprox = lstNewOffers.get(position).getApproxSavings();

            dModel_offerDetail = new DModel_OfferDetail(offerId,
                    position,
                    0,
                    "lstNewOffers",
                    saveApprox,
                    lstNewOffers.get(position).getOfferName(),
                    lstNewOffers.get(position).getOfferDescription(),
                    lstNewOffers.get(position).getCategoryId(),
                    lstNewOffers.get(position).getMerchantName(),
                    lstNewOffers.get(position).getMerchantAddress(),
                    lstNewOffers.get(position).getMerchantId(),
                    lstNewOffers.get(position).getMerchantLogo(),
                    lstNewOffers.get(position).getDetailNEclusion(),
                    lstNewOffers.get(position).isFav(),
                    lstNewOffers.get(position).isCanRedeem(),
                    lstNewOffers.get(position).getExpiryDate(),
                    lstNewOffers.get(position).isCanSendGift(),
                    lstNewOffers.get(position).getDiscountType(),
                    lstNewOffers.get(position).getApproxSavingPercentage()
            );
            showOfferDetailAlertDialog(dModel_offerDetail);
        });


        lsvNewOffers.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (shouldGetMoreOffers && !isAlreadyFetchingOffers) {
                    if (offersAdapter != null) {
                        if ((lsvNewOffers.getLastVisiblePosition() == (offersAdapter.getCount() - 1))) {
                            page++;
                            lsvNewOffers.addFooterView(lsvFooterView);
                            offersAdapter.notifyDataSetChanged();
                            lsvNewOffers.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
                            requestNewOffers(page, false);
                        }
                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        return view;
    }

    private void initialize() {
        page = 1;
        shouldGetMoreOffers = true;
        isAlreadyFetchingOffers = false;
        dModel_offerDetail = new DModel_OfferDetail();
        progressDialogue = new ProgressDialogue();
        lstNewOffers = new ArrayList<>();

    }

    private void bindViews(View frg) {
        txvNotFound = frg.findViewById(R.id.frg_new_offers_txv_nt_found);
        lsvNewOffers = frg.findViewById(R.id.frg_new_offers_lst_view);
        rlBack = frg.findViewById(R.id.frg_offers_rl_back);
        lsvFooterView = ((LayoutInflater) requireContext().getSystemService(requireContext().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.lsv_footer, null, false);

        requestNewOffers(page, true);
        rlBack.setOnClickListener(this);

    }

    private void showOfferDetailAlertDialog(DModel_OfferDetail dModel_offerDetail) {
        OfferDetailAlertDialog cdd = new OfferDetailAlertDialog(requireContext(), this, dModel_offerDetail, null);
        cdd.show();
        cdd.setCancelable(true);
    }
    private void requestNewOffers(int _page, boolean _shouldClearLst) {
        if (_shouldClearLst) {
            progressDialogue.startIOSLoader(requireActivity());
            lstNewOffers.clear();
            offersAdapter = null;
        }
        isAlreadyFetchingOffers = true;
        NewOffers_WebHit_Get_getOffers NewOffers_WebHit_Get_getOffers = new NewOffers_WebHit_Get_getOffers();
        NewOffers_WebHit_Get_getOffers.requestNewOffers(getContext(), new IWebCallback() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDialogue.stopIOSLoader();
                isAlreadyFetchingOffers = false;

                if (isSuccess) {
                    updateList();
                } else if (strMsg.equalsIgnoreCase(AppConstt.ServerStatus.DATABASE_NOT_FOUND + "")) {
                    shouldGetMoreOffers = false;
                    if (lstNewOffers.size() == 0) {
                        lsvNewOffers.setVisibility(View.GONE);
                        txvNotFound.setVisibility(View.VISIBLE);
                    } else {
                        lsvNewOffers.removeFooterView(lsvFooterView);
                        offersAdapter.notifyDataSetChanged();
                    }
                } else {
                    lsvNewOffers.setVisibility(View.GONE);
                    txvNotFound.setVisibility(View.VISIBLE);
                    AppConfig.getInstance().showAlertDialog(requireActivity(),null, strMsg, null, null, false, true, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDialogue.stopIOSLoader();
                isAlreadyFetchingOffers = false;
                lsvNewOffers.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
                AppConfig.getInstance().showAlertDialog(requireActivity(),null, AppConfig.getInstance().getNetworkExceptionMessage(ex.getMessage()), null, null, false, true, null);
            }

        }, _page);
    }

    private void updateList() {


        if (NewOffers_WebHit_Get_getOffers.responseObject != null &&
                NewOffers_WebHit_Get_getOffers.responseObject.getData() != null &&
                NewOffers_WebHit_Get_getOffers.responseObject.getData().size() > 0) {

            if (NewOffers_WebHit_Get_getOffers.responseObject.getData().size() < 20) {
                shouldGetMoreOffers = false;
            }


            for (int i = 0; i < NewOffers_WebHit_Get_getOffers.responseObject.getData().size(); i++) {
                String festival;
                if (NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getSpecialType() != null) {
                    festival = NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getSpecialType();
                } else {
                    festival = "";
                }


                String totalSaving = NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getApproxSaving();
                float totalSavingsFloat = Float.parseFloat(totalSaving);
                int totalSavingsRounded = (int) totalSavingsFloat;

                String approxSavings, currencyUnit;

                currencyUnit = getResources().getString(R.string.txv_qatar_riyal);

                approxSavings = currencyUnit + " " + totalSavingsRounded;
                String savedPercentage;
                if (NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getPercentage_saving() != null) {
                    savedPercentage = String.valueOf(NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getPercentage_saving());
                } else {
                    savedPercentage = "00.00";
                }
                int approxPerRounded = 0;
                float approxPerFloat = Float.parseFloat(savedPercentage);
                approxPerRounded = (int) approxPerFloat;
                String savedPer =  "" + approxPerRounded;



                boolean canRedeem = NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).isRedeeme();

                String strImageUrl = "";
                if (NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getImage() != null) {
                    strImageUrl = NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getImage();
                }
                lstNewOffers.add(new DModelOutletOffers(
                        NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getId(),
                        strImageUrl,
                        NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getTitle(),
                        NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getDescription(),
                        NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getOutlet_name(),
                        NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getOutlet_address(),
                        NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getOutletId(),
                        NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getLogo(),
                        NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getCategory_id(),
                        strImageUrl,
                        0,
                        NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getSpecial(),
                        festival,
                        NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getValidFor(),
                        approxSavings,
                        false,
                        false,
                        canRedeem,
                        NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getRenew(),
                        NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getRenewDate(),
                        NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getEndDatetime(),
                        NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).isCanSendGift(),
                        NewOffers_WebHit_Get_getOffers.responseObject.getData().get(i).getDiscount_type(),
                        savedPer
                ));
            }

            lsvNewOffers.setVisibility(View.VISIBLE);
            txvNotFound.setVisibility(View.GONE);
            lsvNewOffers.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);

            if (offersAdapter != null) {
                lsvNewOffers.removeFooterView(lsvFooterView);
                offersAdapter.notifyDataSetChanged();
            } else {
                offersAdapter = new OffersAdapter(getContext(), this, lstNewOffers);
                lsvNewOffers.setAdapter(offersAdapter);
            }
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.frg_offers_rl_back) {
            AppConfig.getInstance().closeKeyboard(requireActivity());
            FragmentManager fm = requireFragmentManager();
            fm.popBackStackImmediate();
        }

    }
}