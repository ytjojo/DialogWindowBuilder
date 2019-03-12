package com.github.ytjojo.dialogbuilder.lib.showtipsview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;

import com.github.ytjojo.dialogbuilder.lib.DecorViewDelegateImp;
import com.github.ytjojo.dialogbuilder.lib.WindowBuilder;

import java.util.ArrayList;

import androidx.annotation.ColorInt;

/**
 * @文件 fileName
 * @作者 jiulongteng
 * @日期 2019/3/11
 * @时间 19:25
 * @描述 description
 */
public class ShowTipsHelper {

    GuideView guideView;
    ArrayList<TipViewInfo> tipViewInfos;
    WindowBuilder builder;

    View rootView;

    public ShowTipsHelper(Activity context) {
        this.guideView = new GuideView(context);
        rootView = context.getWindow().getDecorView();
        rootView.addOnLayoutChangeListener(onLayoutChangeListener);
//        FrameLayout frameLayout =new FrameLayout(context);
//        frameLayout.addView(guideView);
//
//        ((ViewGroup)context.getWindow().findViewById(Window.ID_ANDROID_CONTENT)).addView(frameLayout);

        builder = WindowBuilder.newBuilder(context)
                .setContentView(guideView)
                .setWindowBackgroudColor(Color.TRANSPARENT)
                .setCornerRadus(0)
                .windowDelegate(new DecorViewDelegateImp());

        guideView.setOnDismissListener(new GuideView.OnDismissListener() {
            @Override
            public void onDismiss() {
                builder.dismiss();
                rootView.removeOnLayoutChangeListener(onLayoutChangeListener);
            }
        });
    }

    public ShowTipsHelper setOnDismissListener(GuideView.OnDismissListener l) {
        this.guideView.setOnDismissListener(l);
        return this;
    }

    public ShowTipsHelper setMaskColor(@ColorInt int color) {
        guideView.setMaskColor(color);
        return this;
    }

    public GuideView getGuideView() {
        return guideView;
    }

    public ShowTipsHelper addTipViewInfo(TipViewInfo info) {
        if (tipViewInfos == null) {
            tipViewInfos = new ArrayList<>();
        }
        tipViewInfos.add(info);
        return this;
    }

    public ShowTipsHelper showAll() {
        if (tipViewInfos != null) {
            guideView.setTipViewInfos(tipViewInfos);
            for (TipViewInfo info : tipViewInfos) {
                guideView.addView(info.getTipView());
            }
        }
        builder.show();
        return this;
    }

    public ShowTipsHelper show() {
        if (tipViewInfos != null && tipViewInfos.size() > 0) {
            guideView.setTipViewInfos(tipViewInfos);
            guideView.addView(tipViewInfos.get(0).getTipView());
        }
        builder.show();
        return this;
    }

    public static ShowTipsHelper create(Activity activity) {
        return new ShowTipsHelper(activity);
    }

    View.OnLayoutChangeListener onLayoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (left == oldLeft
                    && top == oldTop
                    && right == oldRight
                    && bottom == oldBottom) {
                return;
            }
            guideView.reLayout();

        }
    };
}
