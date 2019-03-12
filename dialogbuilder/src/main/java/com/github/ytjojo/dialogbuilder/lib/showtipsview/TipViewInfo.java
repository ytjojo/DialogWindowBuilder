package com.github.ytjojo.dialogbuilder.lib.showtipsview;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.ViewCompat;

/**
 * @文件 fileName
 * @作者 jiulongteng
 * @日期 2019/3/11
 * @时间 14:01
 * @描述 description
 */
public class TipViewInfo {


    /**
     * 高亮区域 起始X坐标
     */
    private int locationX;
    /**
     * 高亮区域 起始Y坐标
     */
    private int locationY;

    private int insetDx;

    private int insetDy;

    private int offsetTipViewX;
    private int offsetTipViewY;

    private View hightLightedView;
    private View tipView;

    private int mHorizontalAnchorFlag;
    private int mVerticalAnchorFlag;

    private  LighterShape shape;

    private int[] locationInScreen;

    private Rect drawingRect = new Rect();


    public int getLocationX() {
        return locationX;
    }

    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    public int getInsetDx() {
        return insetDx;
    }


    public TipViewInfo setInsetDx(int insetDx) {
        this.insetDx = insetDx;
        return this;
    }

    public int getInsetDy() {
        return insetDy;
    }

    public TipViewInfo setInsetDy(int insetDy) {
        this.insetDy = insetDy;
        return this;
    }

    public TipViewInfo setInset(int dx,int dy){
        this.insetDx=dx;
        this.insetDy = dy;
        return this;
    }
    public int getOffsetTipViewX() {
        return offsetTipViewX;
    }

    public TipViewInfo setOffsetTipViewX(int offsetTipViewX) {
        this.offsetTipViewX = offsetTipViewX;
        return this;
    }

    public int getOffsetTipViewY() {
        return offsetTipViewY;
    }

    public TipViewInfo setOffsetTipViewY(int offsetTipViewY) {
        this.offsetTipViewY = offsetTipViewY;
        return this;
    }
    public TipViewInfo setOffsetTipView(int offsetTipViewX,int offsetTipViewY) {
        this.offsetTipViewX = offsetTipViewX;
        this.offsetTipViewY = offsetTipViewY;
        return this;
    }
    public View getHightLightedView() {
        return hightLightedView;
    }

    public TipViewInfo setHightLightedView(View hightLightedView) {
        this.hightLightedView = hightLightedView;
        if (ViewCompat.isLaidOut(hightLightedView)) {

        }
        return this;
    }

    public View getTipView() {
        return tipView;
    }

    public TipViewInfo setTipView(View tipView) {
        this.tipView = tipView;
        return this;
    }


    public void updateLocation( int[] parentLocation) {
        if (locationInScreen == null) {
            locationInScreen = new int[2];
            hightLightedView.getLocationOnScreen(locationInScreen);
        }
        locationX = locationInScreen[0] - parentLocation[0];
        locationY = locationInScreen[1] - parentLocation[1];

        drawingRect.set(locationX,locationY,locationX + hightLightedView.getWidth(),
                locationY + hightLightedView.getHeight());

        drawingRect.inset(insetDx,insetDy);
        getShape().setViewRect(drawingRect);

    }

    public LighterShape getShape() {
        return shape;
    }

    public TipViewInfo setShape(LighterShape shape) {
        this.shape = shape;
        return this;
    }

    protected void addToParentView(ViewGroup parent){
        parent.addView(tipView);
    }

    public int getHorizontalAnchorFlag() {
        return mHorizontalAnchorFlag;
    }

    public TipViewInfo setHorizontalAnchorFlag(int horizontalAnchorFlag) {
        this.mHorizontalAnchorFlag = horizontalAnchorFlag;
        return this;
    }

    public int getVerticalAnchorFlag() {
        return mVerticalAnchorFlag;
    }

    public TipViewInfo setVerticalAnchorFlag(int verticalAnchorFlag) {
        this.mVerticalAnchorFlag = verticalAnchorFlag;
        return this;
    }

    public Rect getDrawingRect() {
        return drawingRect;
    }


    public TipViewInfo create(View hightLightedView,View tipView){
       return create().setHightLightedView(hightLightedView)
                .setTipView(tipView);
    }
    public static TipViewInfo create(){
        return new TipViewInfo();
    }


}
