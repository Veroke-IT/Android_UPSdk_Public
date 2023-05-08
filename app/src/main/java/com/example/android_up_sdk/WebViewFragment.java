package com.example.android_up_sdk;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android_up_sdk.Utils.AppConstt;


public class WebViewFragment extends Fragment implements View.OnClickListener {
    WebView webView;
    FragmentManager fragmentManager;
    String strExtra;
    String strTitle="";
    String strUrl;
    Dialog progressDialog;
    RelativeLayout rlBack;
    TextView txvTitle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_web_view, container, false);
        init();
        bindViews(view);

         if (strExtra.equalsIgnoreCase(AppConstt.OUTLET_MENU)) {
            showProgDialog();
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, final String url) {

                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            });

            webView.loadUrl(strUrl);
             strTitle = AppConstt.OUTLET_MENU;
        }
        else if (strExtra.equalsIgnoreCase(AppConstt.OUTLET_MENU_PDF)) {
            showProgDialog();
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, final String url) {

                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            });

            webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + strUrl);
             strTitle = AppConstt.OUTLET_MENU;
        }
        else if (strExtra.equalsIgnoreCase(AppConstt.PURCHASE_RULES)) {

            showProgDialog();
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, final String url) {

                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            });

            webView.loadUrl(AppConstt.RULES_OF_PURCHASE_URL);
             strTitle = (requireActivity().getResources().getString(R.string.frg_settings_terms_of_user));

        }

        txvTitle.setText(strTitle);
        webView.setBackgroundColor(Color.TRANSPARENT);

        return view;
    }

    private void init() {
        fragmentManager = requireActivity().getSupportFragmentManager();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            strExtra = bundle.getString("page");
            strUrl = bundle.getString("url", "");
        } else {
            strExtra = "";
            strUrl = "";
        }
    }

    void bindViews(View frg) {

        webView = frg.findViewById(R.id.webview);
        txvTitle = frg.findViewById(R.id.frg_web_view_txv_title);
        rlBack = frg.findViewById(R.id.frg_web_view_rl_back);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDomStorageEnabled(true);

        rlBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
       if (id == R.id.frg_web_view_rl_back) {
            FragmentManager fm = requireFragmentManager();
            fm.popBackStackImmediate();
        }
    }

    private void showProgDialog() {
        progressDialog = new Dialog(getActivity(), R.style.Theme_Android_UP_SDK);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dlg_progress);
        progressDialog.show();
    }

}