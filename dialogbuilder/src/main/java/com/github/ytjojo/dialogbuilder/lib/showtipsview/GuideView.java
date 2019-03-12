package com.github.ytjojo.dialogbuilder.lib.showtipsview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.ytjojo.dialogbuilder.lib.WindowBuilder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by zhaozhibo on 2017/10/31.
 */

public class GuideView extends FrameLayout implements View.OnClickListener {

    public static final int DEFAULT_HIGHLIGHT_VIEW_BG_COLOR = 0xcc000000;
    private int maskColor = DEFAULT_HIGHLIGHT_VIEW_BG_COLOR;
    private boolean isAutoNext;
    private ArrayList<TipViewInfo> mTipViewInfos;
    private int currentPos;

    public GuideView(@NonNull Context context) {
        this(context, null);
    }

    public GuideView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClickable(true);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setWillNotDraw(false);
        setOnClickListener(this);
    }

    public void setAlpha(int alpha) {
        maskColor = Color.argb(alpha, 0, 0, 0);
    }

    public void setMaskColor(int color) {
        this.maskColor = color;
    }


    public void setAutoNext(boolean autoNext) {
        isAutoNext = autoNext;
    }


    public void setTipViewInfos(ArrayList<TipViewInfo> mTipViewInfos) {
        this.mTipViewInfos = mTipViewInfos;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        blurMaskFilter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.OUTER);
//        int saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
//        canvas.drawColor(maskColor);
//        maskPaint.setXfermode(porterDuffXfermode);
//        canvas.drawRect(rect,maskPaint);
//        maskPaint.setXfermode(null);
//        canvas.restoreToCount(saved);

        if (mTipViewInfos != null && !mTipViewInfos.isEmpty()) {
            for (TipViewInfo tipViewInfo : mTipViewInfos) {

                if (tipViewInfo.getHightLightedView() == null
                        || tipViewInfo.getShape() == null
                        || tipViewInfo.getShape().getViewRect() == null
                        || tipViewInfo.getShape().getViewRect().isEmpty()) {
                    continue;
                }
                canvas.clipPath(tipViewInfo.getShape().getShapePath(), Region.Op.DIFFERENCE);
            }
        }

        //then, draw the bg color
        canvas.drawColor(maskColor);

        //finally, draw the rects of all the highlighted views.
        if (mTipViewInfos != null && !mTipViewInfos.isEmpty()) {
            for (TipViewInfo tipViewInfo : mTipViewInfos) {

                if (tipViewInfo.getShape() == null
                        || tipViewInfo.getShape().getViewRect() == null
                        || tipViewInfo.getShape().getViewRect().isEmpty()) {
                    continue;
                }

                tipViewInfo.getShape().onDraw(canvas);
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = MeasureSpec.getSize(widthMeasureSpec);
//        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY));
        if (getLayoutParams().width != ViewGroup.LayoutParams.MATCH_PARENT || getLayoutParams().height != ViewGroup.LayoutParams.MATCH_PARENT) {
            getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (canLayout) {
            super.onLayout(changed, left, top, right, bottom);
            reLayout();
        }

    }

    int[] parentLocation = new int[2];

    public void reLayout() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            for (TipViewInfo info : mTipViewInfos) {
                if (childView == info.getTipView()) {
                    getLocationOnScreen(parentLocation);
                    info.updateLocation(parentLocation);

                    int localX = info.getLocationX() + info.getOffsetTipViewX();
                    int localY = info.getLocationY() + info.getOffsetTipViewY();
                    int childLeft = 0;
                    int childTop = 0;
                    int childWidth = childView.getMeasuredWidth();
                    int childHeight = childView.getMeasuredHeight();
                    int mHorizontalAnchorFlag = info.getHorizontalAnchorFlag();
                    int mVerticalAnchorFlag = info.getVerticalAnchorFlag();
                    if ((mHorizontalAnchorFlag & WindowBuilder.RELATIVE_LEFT_TO_LEFTOF) != 0
                            && (mHorizontalAnchorFlag & WindowBuilder.RELATIVE_RIGHT_TO_LEFTOF) != 0) {
                        childLeft = localX - childWidth / 2;
                    } else if ((mHorizontalAnchorFlag & WindowBuilder.RELATIVE_RIGHT_TO_RIGHTOF) != 0
                            && (mHorizontalAnchorFlag & WindowBuilder.RELATIVE_LEFT_TO_RIGHTOF) != 0) {
                        childLeft = localX + childView.getMeasuredWidth() - childWidth / 2;
                    } else if ((mHorizontalAnchorFlag & WindowBuilder.RELATIVE_LEFT_TO_LEFTOF) != 0
                            && (mHorizontalAnchorFlag & WindowBuilder.RELATIVE_RIGHT_TO_RIGHTOF) != 0) {
                        childLeft = localX + (childView.getMeasuredWidth() - childWidth) / 2;
                    } else if ((mHorizontalAnchorFlag & WindowBuilder.RELATIVE_LEFT_TO_LEFTOF) != 0) {
                        childLeft = localX;
                    } else if ((mHorizontalAnchorFlag & WindowBuilder.RELATIVE_RIGHT_TO_LEFTOF) != 0) {
                        childLeft = localX - childWidth;
                    } else if ((mHorizontalAnchorFlag & WindowBuilder.RELATIVE_RIGHT_TO_RIGHTOF) != 0) {
                        childLeft = localX + childView.getMeasuredWidth() - childWidth;

                    } else if ((mHorizontalAnchorFlag & WindowBuilder.RELATIVE_LEFT_TO_RIGHTOF) != 0) {
                        childLeft = localX + childView.getMeasuredWidth();

                    } else {
                        childLeft = localX;
                    }

                    if ((mVerticalAnchorFlag & WindowBuilder.RELATIVE_TOP_TO_TOPOF) != 0
                            && (mVerticalAnchorFlag & WindowBuilder.RELATIVE_BOTTOM_TO_TOPOF) != 0) {
                        childTop = localY - childHeight / 2;
                    } else if ((mVerticalAnchorFlag & WindowBuilder.RELATIVE_BOTTOM_TO_BOTTOMOF) != 0
                            && (mVerticalAnchorFlag & WindowBuilder.RELATIVE_TOP_TO_BOTTOMOF) != 0) {
                        childTop = localY + childView.getMeasuredHeight() - childHeight / 2;
                    } else if ((mVerticalAnchorFlag & WindowBuilder.RELATIVE_TOP_TO_TOPOF) != 0
                            && (mVerticalAnchorFlag & WindowBuilder.RELATIVE_BOTTOM_TO_BOTTOMOF) != 0) {
                        childTop = localY + (childView.getMeasuredHeight() - childHeight) / 2;
                    } else if ((mVerticalAnchorFlag & WindowBuilder.RELATIVE_TOP_TO_TOPOF) != 0) {
                        childTop = localY;
                    } else if ((mVerticalAnchorFlag & WindowBuilder.RELATIVE_BOTTOM_TO_TOPOF) != 0) {
                        childTop = localY - childHeight;
                    } else if ((mVerticalAnchorFlag & WindowBuilder.RELATIVE_BOTTOM_TO_BOTTOMOF) != 0) {
                        childTop = localY + childView.getMeasuredHeight() - childHeight;

                    } else if ((mVerticalAnchorFlag & WindowBuilder.RELATIVE_TOP_TO_BOTTOMOF) != 0) {
                        childTop = localY + childView.getMeasuredHeight();

                    } else {
                        childTop = localY;
                    }
                    childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
                }
            }
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isAutoNext) {
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    boolean canLayout;

    public void setCanLayout() {
        canLayout = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        currentPos = 0;
        canLayout = false;
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    public interface OnClickListener {
        void onClick(int index, TipViewInfo info, ArrayList<TipViewInfo> allTipViewInfos);
    }

    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener l) {
        this.onClickListener = l;
    }

    public void setOnDismissListener(OnDismissListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        this.listeners.add(listener);
    }

    private ArrayList<OnDismissListener> listeners;

    @Override
    public void onClick(View v) {
        showHighLight();
    }

    public void showHighLight() {
        if (currentPos >= mTipViewInfos.size() - 1 || getChildCount() >= mTipViewInfos.size()) {
            if (listeners != null) {
                for (OnDismissListener l : listeners) {
                    l.onDismiss();
                }
            }
            if (onClickListener != null) {
                onClickListener.onClick(currentPos, mTipViewInfos.get(currentPos), mTipViewInfos);
            }

        } else {
            this.removeAllViews();
            if (onClickListener != null) {
                onClickListener.onClick(currentPos, mTipViewInfos.get(currentPos), mTipViewInfos);
            }
            currentPos++;
            if (currentPos <= mTipViewInfos.size() - 1) {
                mTipViewInfos.get(currentPos).addToParentView(this);
            }


        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }
}