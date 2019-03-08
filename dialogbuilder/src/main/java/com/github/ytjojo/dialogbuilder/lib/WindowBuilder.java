package com.github.ytjojo.dialogbuilder.lib;

import android.animation.Animator;
import android.app.Activity;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.LayoutRes;


public class WindowBuilder {

    public final static int RELATIVE_LEFT_TO_LEFTOF = 1;
    public final static int RELATIVE_LEFT_TO_RIGHTOF = 2;
    public final static int RELATIVE_RIGHT_TO_LEFTOF = 4;
    public final static int RELATIVE_RIGHT_TO_RIGHTOF = 8;
    public final static int RELATIVE_TOP_TO_TOPOF = 1;
    public final static int RELATIVE_TOP_TO_BOTTOMOF = 2;
    public final static int RELATIVE_BOTTOM_TO_TOPOF = 4;
    public final static int RELATIVE_BOTTOM_TO_BOTTOMOF = 8;


    private int gravity = Gravity.CENTER;
    private boolean isCancelable;
    private boolean canceledOnTouchOutside;
    private Activity activity;
    private WindowDelegate viewVector;
    private AnimDelegate dialogDelegate;

    public WindowBuilder(Activity activity) {
        this.activity = activity;
        this.dialogDelegate = new AnimDelegate(activity, this);
    }

    public WindowBuilder setContentView(@LayoutRes int layout) {
        this.dialogDelegate.setContentView(LayoutInflater.from(activity).inflate(layout, dialogDelegate.getParentView(), false));
        return this;
    }

    public WindowBuilder setContentView(View view) {
        this.dialogDelegate.setContentView(view);
        return this;
    }


    public WindowBuilder setCancelable(boolean isCancelable) {
        this.isCancelable = isCancelable;
        return this;
    }

    public WindowBuilder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        this.canceledOnTouchOutside = canceledOnTouchOutside;
        return this;
    }

    public WindowBuilder setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public void setRelativePosition(View anchor, int horizontalFlag, int verticalFlag) {

    }

    public WindowBuilder setWindowBackgroudColor(@ColorInt int color) {
        dialogDelegate.getParentView().setWindowBackgroudColor(color);
        return this;
    }

    public WindowBuilder dialog() {
        viewVector = new DialogDelegateImpl();
        return this;
    }

    public WindowBuilder popupwindow() {
        viewVector = new PopupWindowDelegateImpl();
        return this;
    }

    public WindowBuilder windowDelegate(WindowDelegate delegate){
        this.viewVector = delegate;
        return this;
    }
    public WindowBuilder overLap() {
        return this;

    }

    private boolean isCreateCalled;

    long time;

    public WindowBuilder create() {
        viewVector.onCreate(activity, dialogDelegate.getParentView());

        dialogDelegate.addDismissAnimEndListener(new AnimDelegate.OnAnimEndListner() {
            @Override
            public void onEnd() {
                viewVector.detachFromWindow();
            }

            @Override
            public void onStart() {

            }
        });

        dialogDelegate.getParentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        viewVector.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {

                    dismiss();
                    return true;
                }
                return false;
            }
        });
        isCreateCalled = true;
        return this;
    }

    public WindowBuilder setLeftRightMargin(int leftRightMarginpx) {
        dialogDelegate.getParentView().setLeftMargin(leftRightMarginpx);
        dialogDelegate.getParentView().setRightMargin(leftRightMarginpx);
        return this;
    }

    public WindowBuilder setLeftRightMargin() {
        int leftRightMarginpx = activity.getResources().getDimensionPixelOffset(R.dimen.dialog_content_horizontal_margin);
        setLeftRightMargin(leftRightMarginpx);
        return this;
    }

    public WindowBuilder setMinTopMargin(int minTopMarginpx) {
        dialogDelegate.getParentView().setMinTopMargin(minTopMarginpx);
        return this;
    }

    public WindowBuilder setLayoutVerticalBias(@FloatRange(from = 0f, to = 1f) float verticalBias) {
        dialogDelegate.getParentView().setLayoutVerticalBias(verticalBias);
        return this;
    }

    public WindowBuilder setMinBottomMargin(int minBottomMargin) {
        dialogDelegate.getParentView().setMinBottomMargin(minBottomMargin);
        return this;
    }

    public WindowBuilder setWidthMatchScreen() {
        dialogDelegate.getParentView().setLeftMargin(0);
        dialogDelegate.getParentView().setRightMargin(0);
        return this;
    }

    public boolean isCancelable() {
        return isCancelable;
    }

    public boolean isCanceledOnTouchOutside() {
        return canceledOnTouchOutside;
    }

    public int getGravity() {
        return gravity;
    }

    public WindowDelegate getViewVector() {
        return viewVector;
    }

    public AnimDelegate getDialogDelegate() {
        return dialogDelegate;
    }

    public View getContentView() {
        return dialogDelegate.getParentView().getContentView();
    }

    public Activity getActivity() {
        return activity;
    }

    public WindowBuilder show() {
        if (!isCreateCalled) {
            create();
        }
        if (!viewVector.isShowing()) {
            viewVector.attachToWindow();
            dialogDelegate.startShowAnim();
        }
        return this;

    }

    public void dismiss() {
        if (viewVector.isShowing()) {
            dialogDelegate.startDismissAnim();
        }

    }


    @SuppressWarnings("unchecked")
    public <E extends View> E findView(int id) {
        try {
            return (E) dialogDelegate.getParentView().findViewById(id);
        } catch (ClassCastException e) {

            throw e;
        }
    }

    private SparseArray<View> viewSparseArray;

    public <E extends View> E getView(int id) {
        if (viewSparseArray == null) {
            viewSparseArray = new SparseArray<>();
        }

        View v = viewSparseArray.get(id);
        if (v == null) {
            v = findView(id);
            if (v == null) {
                return null;
            }
            viewSparseArray.put(id, v);

        }
        return (E) v;
    }

    public static WindowBuilder newBuilder(Activity activity) {
        return new WindowBuilder(activity);
    }
}
