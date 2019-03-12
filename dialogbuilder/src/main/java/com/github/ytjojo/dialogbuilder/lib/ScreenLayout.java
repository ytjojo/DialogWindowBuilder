package com.github.ytjojo.dialogbuilder.lib;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ScreenLayout extends FrameLayout {

    View mContentView;
    ImageView mBackgroundView;

    private int mMinTopMargin;
    private int mMinBottomMargin;
    private int mLeftMargin;
    private int mRightMargin;
    private float mLayoutVerticalBias = -1f;
    private int mParentHeight;
    private int mBackgroundColor;

    private ColorStateList backgroundColor;
    private float radius;
    private float elevation;
    private float maxElevation;
    private Rect shadowPadding;
    private RoundRectDrawableWithShadow shadowDrawable;

    public ScreenLayout(@NonNull Context context) {
        this(context, null);
    }

    public ScreenLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScreenLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public ScreenLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {

        backgroundColor = ColorStateList.valueOf(getResources().getColor(R.color.dialog_light_background));
        radius = getResources().getDimensionPixelOffset(R.dimen.dialog_default_radius);
        elevation = getResources().getDimensionPixelOffset(R.dimen.dialog_default_elevation);
        maxElevation = getResources().getDimensionPixelOffset(R.dimen.dialog_max_elevation);
        shadowDrawable = RoundRectDrawableWithShadow.initialize(getContext(), backgroundColor, radius, elevation, maxElevation);
        shadowPadding = new Rect();
        ensureBackgroudView();
        setBackground(null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    private Path mPath = new Path();
    private RectF mClipRect = new RectF();
    private Matrix matrix = new Matrix();

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {

        if (child == mContentView) {

            try {
                canvas.save();
                clipChild(canvas, child);
                return super.drawChild(canvas, child, drawingTime);
//                return true;
            } finally {
                canvas.restore();
            }
        } else {
            return super.drawChild(canvas, child, drawingTime);
        }


    }

    private void clipChild(Canvas canvas, View child) {
        if (radius <= 0) {
            return;
        }
        canvas.save();

        float cx = child.getLeft() + child.getPivotX();
        float cy = child.getTop() + child.getPivotY();
        matrix.reset();
//        canvas.scale(child.getScaleX(),child.getScaleY(),cx,cy);
        matrix.postScale(child.getScaleX(), child.getScaleY(), cx, cy);
        matrix.preTranslate(child.getTranslationX(), child.getTranslationY());
        canvas.setMatrix(matrix);
        canvas.translate(child.getLeft() - shadowPadding.left, child.getTop() - shadowPadding.top);

        int alpha = (int) (child.getAlpha() * 255);
        if (shadowDrawable.getAlpha() != alpha) {
            shadowDrawable.setAlpha(alpha);
        }
        shadowDrawable.draw(canvas);
        canvas.restore();
//        mClipRect.set(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());

        float left = child.getPivotX() + child.getLeft() + child.getScaleX() * (-child.getPivotX());
        float top = child.getTop() + child.getPivotY() + child.getScaleY() * (-child.getPivotY());

        mClipRect.set(left,
                top, left + child.getMeasuredWidth() * child.getScaleX(), top + child.getMeasuredHeight() * child.getScaleY());
        mClipRect.offset(child.getTranslationX(), child.getTranslationY());
        mPath.reset();
        mPath.addRoundRect(mClipRect, radius * child.getScaleX(), radius * child.getScaleX(), Path.Direction.CW);
        canvas.clipPath(mPath, Region.Op.REPLACE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mContentView != null) {
            LayoutParams params = (LayoutParams) mContentView.getLayoutParams();
            params.leftMargin = mLeftMargin;
            params.rightMargin = mRightMargin;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int contentViewHeight = mContentView.getMeasuredHeight();
        mParentHeight = this.getMeasuredHeight();
        if (mContentView != null) {
            if (contentViewHeight + mMinTopMargin + mMinBottomMargin > mParentHeight) {
                contentViewHeight = mParentHeight - mMinBottomMargin - mMinTopMargin;
                if (contentViewHeight != mContentView.getMeasuredHeight()) {
                    mContentView.measure(MeasureSpec.makeMeasureSpec(mContentView.getMeasuredWidth(), MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(contentViewHeight, MeasureSpec.EXACTLY));

                }
            }

        }
        int measureHeight = mContentView.getMeasuredHeight();
        if (measureHeight != 0) {

            mContentViewHeight = mContentView.getMeasuredHeight();
        }
    }

    private int mContentViewHeight;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mContentView != null && mLayoutVerticalBias >= 0 && mLayoutVerticalBias <= 1) {
            int contentViewTop = (int) ((mLayoutVerticalBias) * (mParentHeight - mContentView.getMeasuredHeight()));
            mContentView.layout(mContentView.getLeft(), contentViewTop, mContentView.getLeft() + mContentView.getMeasuredWidth()
                    , contentViewTop + mContentView.getMeasuredHeight());

        } else if (mContentAnchorView != null && mContentView != null) {
            if (auchorViewlocation == null) {
                auchorViewlocation = new int[2];
                mContentAnchorView.getLocationOnScreen(auchorViewlocation);
            }
            int[] parentLocation = new int[2];
            getLocationOnScreen(parentLocation);
            int localX = auchorViewlocation[0] - parentLocation[0] + mOffsetAnchorX;
            int localY = auchorViewlocation[1] - parentLocation[1] + mOffsetAnchorY;
            int childLeft = 0;
            int childTop = 0;
            int childWidth = mContentView.getMeasuredWidth();
            int childHeight = mContentView.getMeasuredHeight();
            if ((mHorizontalAnchorFlag & WindowBuilder.RELATIVE_LEFT_TO_LEFTOF) !=0
                    && (mHorizontalAnchorFlag & WindowBuilder.RELATIVE_RIGHT_TO_LEFTOF) !=0) {
                childLeft = localX - childWidth / 2;
            } else if ((mHorizontalAnchorFlag & WindowBuilder.RELATIVE_RIGHT_TO_RIGHTOF) !=0
                    && (mHorizontalAnchorFlag & WindowBuilder.RELATIVE_LEFT_TO_RIGHTOF) !=0) {
                childLeft = localX + mContentAnchorView.getMeasuredWidth() - childWidth / 2;
            } else if ((mHorizontalAnchorFlag & WindowBuilder.RELATIVE_LEFT_TO_LEFTOF) !=0
                    && (mHorizontalAnchorFlag & WindowBuilder.RELATIVE_RIGHT_TO_RIGHTOF) !=0) {
                childLeft = localX + (mContentAnchorView.getMeasuredWidth() - childWidth) / 2;
            } else if ((mHorizontalAnchorFlag & WindowBuilder.RELATIVE_LEFT_TO_LEFTOF) !=0) {
                childLeft = localX;
            } else if ((mHorizontalAnchorFlag & WindowBuilder.RELATIVE_RIGHT_TO_LEFTOF) !=0) {
                childLeft = localX - childWidth;
            } else if ((mHorizontalAnchorFlag & WindowBuilder.RELATIVE_RIGHT_TO_RIGHTOF) !=0) {
                childLeft = localX + mContentAnchorView.getMeasuredWidth() - childWidth;

            } else if ((mHorizontalAnchorFlag & WindowBuilder.RELATIVE_LEFT_TO_RIGHTOF) !=0) {
                childLeft = localX + mContentAnchorView.getMeasuredWidth();

            } else {
                childLeft = localX;
            }

            if ((mVerticalAnchorFlag & WindowBuilder.RELATIVE_TOP_TO_TOPOF) !=0
                    && (mVerticalAnchorFlag & WindowBuilder.RELATIVE_BOTTOM_TO_TOPOF) !=0) {
                childTop = localY - childHeight / 2;
            } else if ((mVerticalAnchorFlag & WindowBuilder.RELATIVE_BOTTOM_TO_BOTTOMOF) !=0
                    && (mVerticalAnchorFlag & WindowBuilder.RELATIVE_TOP_TO_BOTTOMOF) !=0) {
                childTop = localY + mContentAnchorView.getMeasuredHeight() - childHeight / 2;
            } else if ((mVerticalAnchorFlag & WindowBuilder.RELATIVE_TOP_TO_TOPOF) !=0
                    && (mVerticalAnchorFlag & WindowBuilder.RELATIVE_BOTTOM_TO_BOTTOMOF) !=0) {
                childTop = localY + (mContentAnchorView.getMeasuredHeight() - childHeight) / 2;
            } else if ((mVerticalAnchorFlag & WindowBuilder.RELATIVE_TOP_TO_TOPOF) !=0) {
                childTop = localY;
            } else if ((mVerticalAnchorFlag & WindowBuilder.RELATIVE_BOTTOM_TO_TOPOF) !=0) {
                childTop = localY - childHeight;
            } else if ((mVerticalAnchorFlag & WindowBuilder.RELATIVE_BOTTOM_TO_BOTTOMOF) !=0) {
                childTop = localY + mContentAnchorView.getMeasuredHeight() - childHeight;

            } else if ((mVerticalAnchorFlag & WindowBuilder.RELATIVE_TOP_TO_BOTTOMOF) !=0) {
                childTop = localY + mContentAnchorView.getMeasuredHeight();

            } else {
                childTop = localY;
            }
            mContentView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

        }

        if (shadowDrawable != null) {
            shadowDrawable.getMaxShadowAndCornerPadding(shadowPadding);
            shadowDrawable.setBounds(0, 0,
                    mContentView.getMeasuredWidth() + shadowPadding.left + shadowPadding.right,
                    mContentView.getMeasuredHeight() + shadowPadding.top + shadowPadding.bottom);
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return super.generateLayoutParams(lp);
    }

    public View getContentView() {
        return mContentView;
    }

    public void setContentView(View mContentView) {
        this.mContentView = mContentView;
    }

    public ImageView getBackgroundView() {
        return mBackgroundView;
    }

    public void setBackgroundView(ImageView mBackgroundView) {
        this.mBackgroundView = mBackgroundView;
    }

    public int getMinTopMargin() {
        return mMinTopMargin;
    }

    public void setMinTopMargin(int mMinTopMargin) {
        this.mMinTopMargin = mMinTopMargin;
    }

    public int getMinBottomMargin() {
        return mMinBottomMargin;
    }

    public void setMinBottomMargin(int mMinBottomMargin) {
        this.mMinBottomMargin = mMinBottomMargin;
    }

    public int getLeftMargin() {
        return mLeftMargin;
    }

    public void setLeftMargin(int mLeftMargin) {
        this.mLeftMargin = mLeftMargin;
    }

    public int getRightMargin() {
        return mRightMargin;
    }

    public void setRightMargin(int mRightMargin) {
        this.mRightMargin = mRightMargin;
    }

    public float getLayoutVerticalBias() {
        return mLayoutVerticalBias;
    }

    public void setLayoutVerticalBias(float layoutVerticalBias) {
        this.mLayoutVerticalBias = layoutVerticalBias;
    }

    public int getParentHeight() {
        return mParentHeight;
    }

    public void setParentHeight(int mParentHeight) {
        this.mParentHeight = mParentHeight;
    }

    public int getContentViewHeight() {
        return mContentViewHeight;
    }

    public void setWindowBackgroudColor(@ColorInt int color) {

        ensureBackgroudView();
        this.mBackgroundColor = color;
        mBackgroundView.setImageDrawable(new ColorDrawable(color));

    }

    public void setWindowBackgroud(Drawable drawable) {
        ensureBackgroudView();
        mBackgroundView.setImageDrawable(drawable);
    }

    private void ensureBackgroudView() {
        if (mBackgroundView == null) {
            mBackgroundView = new ImageView(getContext());
            this.addView(mBackgroundView);
        }
    }

    View mContentAnchorView;
    int mOffsetAnchorX;
    int mOffsetAnchorY;
    int mHorizontalAnchorFlag;
    int mVerticalAnchorFlag;
    int[] auchorViewlocation;

    public void setRelativePosition(View anchor, int horizontalFlag, int verticalFlag) {
        setRelativePosition(anchor, horizontalFlag, verticalFlag, 0, 0);
    }

    public void setRelativePosition(View anchor, int horizontalFlag, int verticalFlag, int offsetX, int offsetY) {
        this.mContentAnchorView = anchor;
        this.mHorizontalAnchorFlag = horizontalFlag;
        this.mVerticalAnchorFlag = verticalFlag;
        this.mOffsetAnchorX = offsetX;
        this.mOffsetAnchorY = offsetY;
    }

    public RoundRectDrawableWithShadow getShadowDrawable() {
        return shadowDrawable;
    }

    public void setCornerRadius(float radius){
        this.radius = radius;
    }
}
