package com.example.android_up_sdk.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.CustomAlertConfirmationInterface;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class CustomAlertDialog extends BottomSheetDialog implements View.OnClickListener, BottomSheetDialog.OnShowListener {

    String strTitle, strMsgToShow, strPositiveBtnText, strNegativeBtnText;
    boolean isNegativeBtnRequired;
    CustomAlertConfirmationInterface customAlertConfirmationInterface;

    public CustomAlertDialog(@NonNull Context context, final String _title, String _msgToShow, String _positiveBtnText,
                             String _negativeBtnText, boolean _isNegativeButtonRequired,
                             final CustomAlertConfirmationInterface _customDialogConfirmationListener) {
        super(context, R.style.SubMenuBottomSheetDialogTheme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dlg_custom_alert);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);


        this.strTitle = _title;
        this.strMsgToShow = _msgToShow;
        this.strPositiveBtnText = _positiveBtnText;
        this.strNegativeBtnText = _negativeBtnText;
        this.isNegativeBtnRequired = _isNegativeButtonRequired;
        this.customAlertConfirmationInterface = _customDialogConfirmationListener;
        bindViews();
    }


    private void bindViews() {
        TextView txvTitle = findViewById(R.id.dlg_cstm_alrt_title);
        TextView txvMessage = findViewById(R.id.dlg_cstm_alrt_text_msg);
        Button btnPositive = findViewById(R.id.dlg_cstm_alrt_btn_ok);
        Button btnNegative = findViewById(R.id.dlg_cstm_alrt_btn_cancel);
        View dividerView = findViewById(R.id.dlg_cstm_alrt_separator);

        if (strTitle != null && strTitle.length() > 0) {
            txvTitle.setText(strTitle);
        }else {
            txvTitle.setVisibility(View.GONE);
        }
        if (strMsgToShow != null && strMsgToShow.length() > 0) {
            txvMessage.setText(strMsgToShow);

        }
        if (strPositiveBtnText != null && strPositiveBtnText.length() > 0) {
            btnPositive.setText(strPositiveBtnText);
        }

        if (strPositiveBtnText != null && strPositiveBtnText.length() > 0) {
            btnPositive.setText(strPositiveBtnText);
        }

        if (isNegativeBtnRequired) {
            if (strNegativeBtnText != null && strNegativeBtnText.length() > 0) {
                btnNegative.setText(strNegativeBtnText);
            }
        } else {
            btnNegative.setVisibility(View.GONE);
            dividerView.setVisibility(View.GONE);
        }

        btnPositive.setOnClickListener(this);
        btnNegative.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.dlg_cstm_alrt_btn_ok) {
            if (customAlertConfirmationInterface != null)
                customAlertConfirmationInterface.callConfirmationDialogPositive();
            this.dismiss();
        } else if (id == R.id.dlg_cstm_alrt_btn_cancel) {
            if (customAlertConfirmationInterface != null)
                customAlertConfirmationInterface.callConfirmationDialogNegative();
            this.dismiss();
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        BottomSheetDialog sheetDialog = (BottomSheetDialog) dialog;

        FrameLayout bottomSheet = sheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

        sheetDialog.setCancelable(false);
        sheetDialog.setCanceledOnTouchOutside(true);
        BottomSheetBehavior<FrameLayout> behavior;
        if (bottomSheet != null) {
            behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setHideable(false);
        }


    }
}
