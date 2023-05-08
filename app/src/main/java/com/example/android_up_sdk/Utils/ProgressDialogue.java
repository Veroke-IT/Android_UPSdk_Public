package com.example.android_up_sdk.Utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.example.android_up_sdk.R;

public class ProgressDialogue {
    CustomIOSTransparentProgressDialog iOSProgressDialogObj;

    public void startIOSLoader(Context context) {


        if (context != null) {
            this.iOSProgressDialogObj = new CustomIOSTransparentProgressDialog(context);

            ImageView imvLoader = this.iOSProgressDialogObj.findViewById(R.id.progressDialogImage);

            RotateAnimation rotate = new RotateAnimation(
                    0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );

            rotate.setDuration(1500);
            rotate.setRepeatCount(Animation.INFINITE);
            imvLoader.startAnimation(rotate);


            this.iOSProgressDialogObj.show();
        }
    }

    public void stopIOSLoader() {


        try {
            if ((this.iOSProgressDialogObj != null)) {
                if (this.iOSProgressDialogObj.isShowing()) {
                    this.iOSProgressDialogObj.dismiss();
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            // Handle or log or ignore
        } finally {
            this.iOSProgressDialogObj = null;
        }
    }

    public class CustomIOSTransparentProgressDialog extends Dialog {

        @SuppressWarnings("unused")
        private ImageView iv;

        public CustomIOSTransparentProgressDialog(Context context) {
            super(context, R.style.CustomIOSTransparentProgressDialog);
            WindowManager.LayoutParams wlmp = getWindow().getAttributes();
            wlmp.gravity = Gravity.CENTER_HORIZONTAL;
            getWindow().setAttributes(wlmp);
            setTitle(null);
            setCancelable(false);
            setOnCancelListener(null);
            setContentView(R.layout.custom_progress_layout);
        }


        @Override
        public void show() {
            super.show();

        }

        @Override
        public void dismiss() {
            super.dismiss();
        }
    }
}
