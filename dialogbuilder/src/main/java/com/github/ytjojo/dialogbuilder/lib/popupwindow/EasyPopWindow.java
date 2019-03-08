package com.github.ytjojo.dialogbuilder.lib.popupwindow;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.github.ytjojo.dialogbuilder.lib.SingleClickListener;

import androidx.annotation.ColorInt;


/**
 * Created by Administrator on 2017/10/30 0030.
 */

public class EasyPopWindow {
    PopupWindow mPopupWindow;
    View mRootView;
    FrameLayout mContentView;
    View mWindowBackView;
    Activity mActivity;
    View mAddedChildView;
    private int mDelayAnim = 150;
    boolean outsideTouchable = true;
    boolean useCustomAnim;
    @ColorInt
    int mWindowBackColor = 0x88000000;

    public void setWindowBackColor(@ColorInt int color) {
        this.mWindowBackColor = color;
        mWindowBackView.setBackgroundColor(color);
    }

    public void useCustomAnim() {
        useCustomAnim = true;
        if (mAddedChildView != null) {
            mAddedChildView.setTranslationY(0);
        }
    }

    public void setOutsideTouchable(boolean click) {
        if (!click) {
            mContentView.setOnClickListener(null);
        }
    }

    public EasyPopWindow(Activity context) {
        mActivity = (Activity) context;
        if (mActivity.getParent() != null) {
            ViewGroup contentView = (ViewGroup) mActivity.getParent().getWindow().findViewById(Window
                    .ID_ANDROID_CONTENT);
            mRootView = (ViewGroup) contentView.getChildAt(0);
            mActivity = mActivity.getParent();
        }
        if (mRootView == null) {
            ViewGroup contentView = (ViewGroup) mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
            mRootView = (ViewGroup) contentView.getChildAt(0);
        }
        generateContentView();
        mContentView.setOnClickListener(new SingleClickListener() {
            @Override
            public void onClickInternal(View v) {
                onBackpressed();
            }
        });
    }

    public boolean isShowing() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            return true;
        }
        return false;
    }

    boolean isAnimRuning;

    public boolean onBackpressed() {
        if (mPopupWindow == null || !mPopupWindow.isShowing()) {
            return true;
        }
        if (isAnimRuning) {
            return true;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f, 0f).setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                mWindowBackView.setAlpha(value);
                if (!useCustomAnim) {
                    mAddedChildView.setTranslationY((1f - value) * mAddedChildView.getMeasuredHeight());
                }
            }
        });
        valueAnimator.setStartDelay(mDelayAnim);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setRepeatCount(0);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                isAnimRuning = false;

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isAnimRuning = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
        isAnimRuning = true;
        return false;
    }

    public void onShowAnimStart() {
    }

    public void onShowAnimEnd() {

    }

    public final void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        isAnimRuning = false;
    }

    private void generateContentView() {
        mContentView = new FrameLayout(mActivity);
        mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .MATCH_PARENT);
        mWindowBackView = new View(mActivity);
        mWindowBackView.setBackgroundColor(mWindowBackColor);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        mContentView.addView(mWindowBackView, params);
        mPopupWindow.setContentView(mContentView);
        mPopupWindow.setAnimationStyle(0);
//        mPopupWindow.setFocusable(true);
//        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(false);
        mContentView.setFocusableInTouchMode(true);
        mContentView.setFocusable(true);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        mContentView.setForeground(new ColorDrawable(Color.BLACK));
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mContentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {

                    return true;
                }
                return false;
            }
        });

    }

    public void setContentView(View view) {
        if (view.getLayoutParams() == null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.BOTTOM;
        }
        mAddedChildView = view;
        mContentView.addView(view);
        mAddedChildView.setClickable(true);
    }

    public void setContentView(int layout) {
        View child = LayoutInflater.from(mContentView.getContext()).inflate(layout, mContentView, false);
        mAddedChildView = child;
        mContentView.addView(child);
        mAddedChildView.setClickable(true);
    }

    public void show() {
        if (!isViewCreated) {
            isViewCreated = true;
            onCreatView(mContentView);
        }
        mWindowBackView.setAlpha(0f);
        mWindowBackView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(300);
                if (!useCustomAnim) {
                    mAddedChildView.setTranslationY(mAddedChildView.getMeasuredHeight());
                }
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float value = (float) valueAnimator.getAnimatedValue();
                        mWindowBackView.setAlpha(value);
                        if (!useCustomAnim) {
                            mAddedChildView.setTranslationY(mAddedChildView.getMeasuredHeight() * (1f - value));
                        }
                    }
                });
                valueAnimator.setInterpolator(new DecelerateInterpolator());
                valueAnimator.setRepeatCount(0);
                valueAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        onShowAnimStart();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimRuning = false;
                        onShowAnimEnd();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        isAnimRuning = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                valueAnimator.start();
                mWindowBackView.getViewTreeObserver().removeOnPreDrawListener(this);
                isAnimRuning = true;
                return true;
            }
        });

        mPopupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
    }

    boolean isViewCreated;

    public void onCreatView(FrameLayout container) {


    }

    @SuppressWarnings("unchecked")
    public final  <E extends View> E findView(int id) {
        try {
            return (E) mContentView.findViewById(id);
        } catch (ClassCastException e) {

            throw e;
        }
    }

    /* Click listener that avoid double click event in short time*/
    public SingleClickListener mNoDoubleClickListener = new SingleClickListener() {

        @Override
        public void onClickInternal(View v) {
            EasyPopWindow.this.onClick(v);
        }
    };

    public void onClick(View v) {
        if (mOnClick != null) {
            mOnClick.onClick(v);
        }
    }

    /*Set all widget that need to implements OnClick() here*/
    protected void setClickableItems(View... views) {
        if (views != null && views.length > 0) {
            for (View v : views) {
                if (v != null) {
                    v.setOnClickListener((new SingleClickListener() {
                        @Override
                        public void onClickInternal(View v) {
                            EasyPopWindow.this.onClick(v);
                        }
                    }));
                }
            }
        }
    }

    /*Set all widget that need to implements OnClick() here*/
    protected void setClickableItems(int... residGroup) {
        if (residGroup != null && residGroup.length > 0) {
            for (int resid : residGroup) {
                if (resid != 0) {
                    findView(resid).setOnClickListener(new SingleClickListener() {
                        @Override
                        public void onClickInternal(View v) {
                            EasyPopWindow.this.onClick(v);
                        }
                    });
                }
            }
        }
    }

    private View.OnClickListener mOnClick;

    public void setOnClickListener(View.OnClickListener l) {
        this.mOnClick = l;
    }

}