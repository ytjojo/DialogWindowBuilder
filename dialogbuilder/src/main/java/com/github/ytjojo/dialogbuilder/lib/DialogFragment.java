package com.github.ytjojo.dialogbuilder.lib;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @文件 fileName
 * @作者 jiulongteng
 * @日期 2019/3/8
 * @时间 13:56
 * @描述 description
 */
public class DialogFragment extends androidx.fragment.app.DialogFragment {

    Dialog mDialog;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return mDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setDialog(Dialog dialog){
        this.mDialog = dialog;
    }
}
