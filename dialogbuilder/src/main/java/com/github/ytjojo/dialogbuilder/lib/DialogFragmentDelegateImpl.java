package com.github.ytjojo.dialogbuilder.lib;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

/**
 * @文件 fileName
 * @作者 jiulongteng
 * @日期 2019/3/8
 * @时间 14:00
 * @描述 description
 */
public class DialogFragmentDelegateImpl implements WindowDelegate {
    DialogFragment mDialogFragment;

    FragmentManager mManager;
    Dialog mDialog;
    @Override
    public void attachToWindow() {
        mDialogFragment.show(mManager.beginTransaction(),"dialogfragment");
    }

    @Override
    public void detachFromWindow() {
        mDialogFragment.dismiss();
    }

    @Override
    public void onCreate(Activity activity, View contentView) {
        mDialogFragment = new DialogFragment();

        final Dialog dialog = new Dialog(activity, R.style.App_Global_Transparent_Dialog);
        mDialog = dialog;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setContentView(contentView);
        Window window = dialog.getWindow();
        window.setWindowAnimations(0);


        WindowManager.LayoutParams mParams = window.getAttributes();
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.height =activity.getResources().getDisplayMetrics().heightPixels;
        window.setAttributes(mParams);
        mDialogFragment.setDialog(dialog);
        AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
        mManager = appCompatActivity.getSupportFragmentManager();
    }

    @Override
    public boolean isShowing() {
        if(mDialogFragment ==null || mDialogFragment.getDialog() == null){
            return false;
        }
        return mDialogFragment.getDialog().isShowing();
    }

    @Override
    public void setOnKeyListener(OnKeyListener l) {
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                return l.onKey(keyCode,event);
            }
        });
    }
}
