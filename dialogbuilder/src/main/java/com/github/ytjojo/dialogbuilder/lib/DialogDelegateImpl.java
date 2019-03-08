package com.github.ytjojo.dialogbuilder.lib;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class DialogDelegateImpl implements WindowDelegate {

    Activity activity;
    Dialog dialog;

    @Override
    public void attachToWindow() {
        dialog.show();
    }

    @Override
    public void detachFromWindow() {
        dialog.dismiss();

    }


    @Override
    public void onCreate(Activity activity,View contentView) {
        this.activity =  activity;
        dialog = new Dialog(activity,R.style.App_Global_Transparent_Dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setContentView(contentView);
        Window window = dialog.getWindow();
        window.setWindowAnimations(0);


        WindowManager.LayoutParams mParams = window.getAttributes();
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.height =activity.getResources().getDisplayMetrics().heightPixels;
        window.setAttributes(mParams);


    }

    @Override
    public void setOnKeyListener(final OnKeyListener l) {
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                return l.onKey(keyCode,event);
            }
        });
    }

    @Override
    public boolean isShowing() {
        if(dialog != null && dialog.isShowing()){
            return true;
        }
        return false;
    }
}
