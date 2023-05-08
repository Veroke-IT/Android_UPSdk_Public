package com.example.android_up_sdk.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.CustomAlertConfirmationInterface;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class CustomBottomSheetDialog extends BottomSheetDialog implements View.OnClickListener, BottomSheetDialog.OnShowListener {




    final RelativeLayout relativeLayoutForm;
    CustomAlertConfirmationInterface customAlertConfirmationInterface;
    boolean isNegativeBtnRequired;
    String  strPositiveBtnText, strNegativeBtnText;
    final RelativeLayout newView;

    public CustomBottomSheetDialog(@NonNull Context _context, String _positiveBtnText,
                                   String _negativeBtnText, boolean _isNegativeButtonRequired,
                                   final CustomAlertConfirmationInterface _customDialogConfirmationListener, RelativeLayout layout) {
        super(_context, R.style.SubMenuBottomSheetDialogTheme);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.custom_bottom_sheet);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        this.strPositiveBtnText = _positiveBtnText;
        this.strNegativeBtnText = _negativeBtnText;
        this.isNegativeBtnRequired = _isNegativeButtonRequired;
        this.customAlertConfirmationInterface = _customDialogConfirmationListener;


        relativeLayoutForm = (RelativeLayout) this.findViewById(R.id.rl_bottom_sheet);
        newView = (RelativeLayout) layout;
        newView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        assert relativeLayoutForm != null;
        relativeLayoutForm.addView(newView);
        bindViews();



    }


    private void bindViews() {

        Button btnPositive = findViewById(R.id.dialog_positive_btn);
        Button btnNegative = findViewById(R.id.dialog_negative_btn);
        View dividerView = findViewById(R.id.dlg_bottom_sheet_separator);


        if (strPositiveBtnText != null && strPositiveBtnText.length() > 0) {
            btnPositive.setText(strPositiveBtnText);
        }

        if (isNegativeBtnRequired) {
            if (strNegativeBtnText != null && strNegativeBtnText.length() > 0) {
                btnNegative.setText(strNegativeBtnText);
            }
        }else {
            btnNegative.setVisibility(View.GONE);
            dividerView.setVisibility(View.GONE);
        }

        btnPositive.setOnClickListener(this);
        btnNegative.setOnClickListener(this);

    }




    @Override
    public void onShow(DialogInterface dialog) {
        BottomSheetDialog sheetDialog = (BottomSheetDialog) dialog;

        FrameLayout bottomSheet = (FrameLayout) sheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

        sheetDialog.setCancelable(false);
        sheetDialog.setCanceledOnTouchOutside(true);
        BottomSheetBehavior<FrameLayout> behavior;
        if (bottomSheet != null) {
            behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setHideable(false);
        }


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.dialog_positive_btn) {
            if (customAlertConfirmationInterface != null)
                customAlertConfirmationInterface.callConfirmationDialogPositive();
            this.dismiss();
        } else if (id == R.id.dialog_negative_btn) {
            if (customAlertConfirmationInterface != null)
                customAlertConfirmationInterface.callConfirmationDialogNegative();
            this.dismiss();
        }
    }
}
