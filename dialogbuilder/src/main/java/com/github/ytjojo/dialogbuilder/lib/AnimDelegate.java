package com.github.ytjojo.dialogbuilder.lib;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;

import androidx.annotation.CallSuper;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;

public class AnimDelegate {
    protected Context mContext;

    protected View mContentView;

    protected ScreenLayout mParentView;

    private int mBackAnimDelay = 150;

    protected boolean isAnimRunning;
    WindowBuilder windowBuilder;

    public AnimDelegate(Context context, WindowBuilder windowBuilder) {
        this.mContext = context;
        this.windowBuilder = windowBuilder;
    }

    @CallSuper
    public void onShowAnimStart() {
        isAnimRunning = true;
        for (OnAnimEndListner l : mShowAnimEndListeners) {
            l.onStart();
        }
    }

    @CallSuper
    public void onShowAnimEnd() {
        isAnimRunning = false;
        for (OnAnimEndListner l : mShowAnimEndListeners) {
            l.onEnd();
        }
    }

    @CallSuper
    public void onDismissAnimBegin() {
        isAnimRunning = true;
        for (OnAnimEndListner l : mDismissAnimEndListeners) {
            l.onStart();
        }
    }

    @CallSuper
    public void onDismissAnimEnd() {
        isAnimRunning = false;
        for (OnAnimEndListner l : mDismissAnimEndListeners) {
            l.onEnd();
        }
    }

    public void onViewCreated() {


    }


    final public boolean isAnimRunning() {
        return isAnimRunning;
    }

    final public void startShowAnim() {
        mParentView.getBackgroundView().setImageAlpha(0);
        mContentView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (mContentView.getMeasuredHeight() > 0 && mContentView.getViewTreeObserver().isAlive()) {
                    mContentView.getViewTreeObserver().removeOnPreDrawListener(this);
                    startShowAnimInternal();
                    return true;
                }

                return true;
            }
        });


    }

    private Animator getShowBackgroundAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 255).setDuration(250);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                mParentView.getBackgroundView().setImageAlpha(value);
            }
        });
        valueAnimator.setInterpolator(new FastOutLinearInInterpolator());
        valueAnimator.setRepeatCount(0);
        mParentView.getBackgroundView().setImageAlpha(0);
        return valueAnimator;
    }

    private void startShowAnimInternal() {
        if (isAnimRunning) {
            return;
        }
        Animator.AnimatorListener listener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                onShowAnimStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onShowAnimEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isAnimRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };


        if (!onCreateShowAnim(listener) && mShowAnimationGenerator == null) {
            startDefaultShowAnim(listener);
        } else {
            mShowAnimationGenerator.onStartAnim(this, windowBuilder, listener);
        }

        isAnimRunning = true;

    }



    public void startDismissAnim() {

        Animator.AnimatorListener listener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                onDismissAnimBegin();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onDismissAnimEnd();


            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isAnimRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
        if (!onCreateDismissAnim(listener) && mDIsmissAnimationGenerator == null) {
            startDefaultDismissAnim(listener);
        } else {
            mDIsmissAnimationGenerator.onStartAnim(this, windowBuilder, listener);
        }
        isAnimRunning = true;
    }

    private void startDefaultShowAnim(Animator.AnimatorListener l) {

        if(getParentView().getContentView().getBottom()==getParentView().getMeasuredHeight()){

            startBottomTranslationIn(l);
        }else {
            zoomIn(l);
        }
    }
    private void startDefaultDismissAnim(Animator.AnimatorListener l) {
        if(getParentView().getContentView().getBottom()==getParentView().getMeasuredHeight()){
            startBottomTranslationOut(l);
        }else {
            zoomOut(l);
        }
    }
    private void zoomIn(Animator.AnimatorListener l){
        Animator showBackgroundAnim = getShowBackgroundAnim();
        showBackgroundAnim.addListener(l);
        showBackgroundAnim.start();
        final View v = getParentView().getContentView();
        v.setPivotX(0.5f * v.getMeasuredWidth());
        v.setPivotY(0.5f * v.getMeasuredHeight());
        v.setScaleX(0.5f);
        v.setScaleY(0.5f);
        v.setAlpha(0f);
        v.animate().scaleX(1f).scaleY(1f).setDuration(250).start();
        v.animate().alpha(1f).setDuration(250).start();

    }
    private void zoomOut(Animator.AnimatorListener l){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(255, 0).setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                mParentView.getBackgroundView().setImageAlpha(value);
            }
        });
        valueAnimator.setStartDelay(mBackAnimDelay);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setRepeatCount(0);

        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorSet.Builder animatorSetBuilder = animatorSet.play(valueAnimator);
        animatorSet.addListener(l);
        animatorSet.start();

        final View v = getParentView().getContentView();
        v.setPivotX(0.5f * v.getMeasuredWidth());
        v.setPivotY(0.5f * v.getMeasuredHeight());
        v.animate().scaleX(0.5f).scaleY(0.5f).setDuration(300).start();
//        v.animate().alpha(0f).setDuration(200).start();
    }
    private void startBottomTranslationIn(Animator.AnimatorListener l){

        mParentView.getBackgroundView().setImageAlpha(0);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 255).setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                mParentView.getBackgroundView().setImageAlpha(value);
            }
        });
        valueAnimator.setInterpolator(new FastOutLinearInInterpolator());
        valueAnimator.setRepeatCount(0);
        valueAnimator.start();
        final View v = getParentView().getContentView();
        v.setPivotX(0.5f * v.getMeasuredWidth());
        v.setPivotY(0.5f * v.getMeasuredHeight());
        v.setTranslationY(v.getMeasuredHeight());
        v.animate().translationY(0).setDuration(250).setListener(l).start();
//        v.animate().alpha(1f).setDuration(250).start();


    }
    private void startBottomTranslationOut(Animator.AnimatorListener l){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(255, 0).setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                mParentView.getBackgroundView().setImageAlpha(value);
            }
        });
        valueAnimator.setStartDelay(100);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setRepeatCount(0);

        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorSet.Builder animatorSetBuilder = animatorSet.play(valueAnimator);
        animatorSet.addListener(l);
        animatorSet.start();

        final View v = getParentView().getContentView();
        v.setPivotX(0.5f * v.getMeasuredWidth());
        v.setPivotY(0.5f * v.getMeasuredHeight());
        v.animate().translationY(v.getMeasuredHeight()).setDuration(300).start();
        v.animate().alpha(0f).setDuration(200).start();
    }

    protected boolean onCreateShowAnim(Animator.AnimatorListener l) {
        return false;
    }

    protected boolean onCreateDismissAnim(Animator.AnimatorListener l) {
        return false;
    }


    public void setContentView(View view) {
        getParentView();
        if (mParentView.getContentView() != null) {
            mParentView.removeView(mParentView.getContentView());

        }
        mParentView.addView(view);
        mParentView.setContentView(view);
        mContentView = view;
        mContentView.setClickable(true);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mContentView.getLayoutParams();
        layoutParams.gravity = windowBuilder.getGravity();


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            view.setElevation(Utils.dp2px(6));
//            view.setTranslationZ(Utils.dp2px(6));
            ViewOutlineProvider viewOutlineProvider = null;
            viewOutlineProvider = new ViewOutlineProvider() {
                @SuppressLint("NewApi")
                public void getOutline(View view, Outline outline) {
                    // 可以指定圆形，矩形，圆角矩形，path
                    outline.setRoundRect(new Rect(0, 0, view.getWidth(), view.getHeight()), 10);
                }
            };
            mContentView.setOutlineProvider(viewOutlineProvider);
        }
        onViewCreated();


    }

    public ScreenLayout getParentView() {
        if (mParentView == null) {
            mParentView = new ScreenLayout(mContext);
            mParentView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                        startDismissAnim();
                        return true;
                    }
                    return false;
                }
            });
        }
        return mParentView;
    }

    private ArrayList<OnAnimEndListner> mShowAnimEndListeners = new ArrayList<>();
    private ArrayList<OnAnimEndListner> mDismissAnimEndListeners = new ArrayList<>();

    public void addShowAnimEndListener(OnAnimEndListner l) {
        mShowAnimEndListeners.add(l);
    }

    public void addDismissAnimEndListener(OnAnimEndListner l) {
        mDismissAnimEndListeners.add(l);
    }

    public static interface OnAnimEndListner {
        void onEnd();

        void onStart();
    }

    private AnimationGenerater mShowAnimationGenerator;
    private AnimationGenerater mDIsmissAnimationGenerator;

    public void setShowAnimationGenerator(AnimationGenerater showAnimConfig) {
        this.mShowAnimationGenerator = showAnimConfig;
    }

    public void setDismissAnimationGenerator(AnimationGenerater dismissAnimConfig) {
        this.mDIsmissAnimationGenerator = dismissAnimConfig;
    }

    public interface AnimationGenerater {
        void onStartAnim(AnimDelegate animDelegate, WindowBuilder dialogBuilder, Animator.AnimatorListener l);
    }
}
