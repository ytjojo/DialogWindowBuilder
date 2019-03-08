package com.github.ytjojo.dialogbuilder.lib;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * @文件 fileName
 * @作者 jiulongteng
 * @日期 2019/3/7
 * @时间 19:32
 * @描述 description
 */
public class DecorViewDelegateImp implements WindowDelegate {

    Activity mActivity;
    ViewGroup mAndroidContentView;
    View mContentView;
    OnKeyListener mOnKeyListener;

    @Override
    public void attachToWindow() {
        mAndroidContentView.addView(mContentView);
        if(mOnKeyListener != null){
            mContentView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    return mOnKeyListener.onKey(keyCode,event);
                }
            });
        }
        mContentView.setFocusableInTouchMode(true);
        mContentView.setFocusable(true);
        mContentView.requestFocus();

    }

    @Override
    public void detachFromWindow() {
        mContentView.clearFocus();
        mAndroidContentView.removeView(mContentView);

    }

    @Override
    public void onCreate(Activity activity, View contentView) {
        mActivity = activity;
        mAndroidContentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
        this.mContentView = contentView;

    }

    @Override
    public boolean isShowing() {
        return mContentView.isShown();
    }

    @Override
    public void setOnKeyListener(OnKeyListener l) {

       this.mOnKeyListener =l;
    }
}
