package com.example.android_up_sdk.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_up_sdk.HomeAuxiliaries.Adapters.PhoneNumberAdapter;
import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.CustomAlertMultipleMobileInterface;
import com.example.android_up_sdk.Utils.IAdapterCallback;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import java.util.ArrayList;

public class MultipleMobileNumberDialog extends BottomSheetDialog implements BottomSheetDialog.OnShowListener {

    ArrayList<String> lstPhoneNumber;
    CustomAlertMultipleMobileInterface customAlertConfirmationInterface;

    public MultipleMobileNumberDialog(@NonNull Context context, final ArrayList<String> lstPhoneNumber, final CustomAlertMultipleMobileInterface _customDialogConfirmationListener) {
        super(context, R.style.SubMenuBottomSheetDialogTheme);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dlg_multiple_phone_number);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        this.lstPhoneNumber = lstPhoneNumber;
        this.customAlertConfirmationInterface = _customDialogConfirmationListener;
        bindViews();
    }

    private void bindViews() {
        RecyclerView rcvPhoneNumbers = findViewById(R.id.dlg_multiple_phone_number_rcv);



        PhoneNumberAdapter phoneNumberAdapter = new PhoneNumberAdapter(getContext(), lstPhoneNumber, (eventId, position) -> {
            if (eventId == IAdapterCallback.EVENT_A) {
                customAlertConfirmationInterface.callConfirmationDialogPositive(position);
            }
        });
        if (rcvPhoneNumbers != null) {
            rcvPhoneNumbers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            rcvPhoneNumbers.setAdapter(phoneNumberAdapter);
        }


    }

    @Override
    public void onShow(DialogInterface dialog) {
        BottomSheetDialog sheetDialog = (BottomSheetDialog) dialog;

        FrameLayout bottomSheet =  sheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

        sheetDialog.setCancelable(false);
        sheetDialog.setCanceledOnTouchOutside(true);
        BottomSheetBehavior<FrameLayout> behavior;
        if (bottomSheet != null) {
            behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setHideable(false);
        }


    }


}
