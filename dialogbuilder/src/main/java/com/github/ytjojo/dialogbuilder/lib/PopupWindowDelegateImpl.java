package com.github.ytjojo.dialogbuilder.lib;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class PopupWindowDelegateImpl implements WindowDelegate {

    PopupWindow popupWindow;
    Activity activity;


    @Override
    public void detachFromWindow() {
        popupWindow.dismiss();
    }

    @Override
    public void attachToWindow() {

        ViewGroup contentView = (ViewGroup) activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onCreate(Activity activity,View contentView) {
        this.activity = activity;
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setAnimationStyle(0);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setClippingEnabled(false);
        popupWindow.setOutsideTouchable(false);
        contentView.setFocusableInTouchMode(true);
        contentView.setFocusable(true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    @Override
    public void setOnKeyListener(final OnKeyListener l) {
        popupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return l.onKey(keyCode,event);
            }
        });
    }

    @Override
    public boolean isShowing(){

        if(popupWindow != null && !popupWindow.isShowing()){

          return true;
        }
        return false;
    }


}
