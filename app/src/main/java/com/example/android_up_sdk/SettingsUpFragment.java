package com.example.android_up_sdk;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.android_up_sdk.HomeAuxiliaries.OffersFragment;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;


public class SettingsUpFragment extends Fragment implements View.OnClickListener {

    Switch swLanguage;
    RelativeLayout rlUseOfferAgain,rlTerms;
    TextView tvDaysAccess;
    ImageView imvSettingClose;
    LinearLayout llBackToOrredoo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_settings_up, container, false);

        bindViews(view);

        return view;
    }

    private void bindViews(View view) {

        swLanguage = view.findViewById(R.id.frg_settings_sw_lang);
        rlUseOfferAgain = view.findViewById(R.id.frg_settings_use_offer_again);
        rlTerms = view.findViewById(R.id.frg_settings_terms_of_use);
        tvDaysAccess = view.findViewById(R.id.frg_settings_day_access);
        imvSettingClose = view.findViewById(R.id.frg_setting_close);
        llBackToOrredoo = view.findViewById(R.id.frg_setting_back_to_orredoo);
        tvDaysAccess.setText(AppConfig.getInstance().mUser.getUpAccess()+" "+getResources().getString(R.string.frg_settings_up_access_days));

        swLanguage.setChecked(AppConfig.getInstance().isAppLangArabic);
        swLanguage.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                ((MainUPActivity) requireContext()).setDefLanguage(AppConstt.AppLanguage.ARABIC);
            } else {
                ((MainUPActivity) requireContext()).setDefLanguage(AppConstt.AppLanguage.ENGLISH);
            }
            reStartActivity();

        });
        rlUseOfferAgain.setOnClickListener(this);
        rlTerms.setOnClickListener(this);
        imvSettingClose.setOnClickListener(this);
        llBackToOrredoo.setOnClickListener(this);
    }

    private void reStartActivity() {
            Intent intent = new Intent(requireActivity(), MainUPActivity.class);
            startActivity(intent);
            requireActivity().finish();
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.frg_settings_use_offer_again) {
            navToOffersFragment();
        } else if (id == R.id.frg_settings_terms_of_use) {
            ((MainUPActivity) requireActivity()).navToRulesOfPurchaseFragment();
        } else if (id == R.id.frg_setting_close) {
            FragmentManager fm = requireFragmentManager();
            fm.popBackStackImmediate();
        } else if (id == R.id.frg_setting_back_to_orredoo) {
            requireActivity().finish();
        }
    }

    private void navToOffersFragment() {
        FragmentTransaction ft = requireFragmentManager().beginTransaction();
        Fragment frg = new OffersFragment();
        ft.add(R.id.main_act_content_frame, frg, AppConstt.FrgTag.OffersFragment);
        ft.addToBackStack(AppConstt.FrgTag.OffersFragment);
        ft.hide(this);
        ft.commit();
    }

}